package com.example.chatwithbestie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {
    private EditText name, phone, email, passwd;
    private Button reg,login,editpic;
    private DatabaseReference reff,reff1;
    private FirebaseAuth fAuth;
    UserReg user;
    String userID,prfPicUrl;
    private ImageView prfpic;
    public Uri imageUri,image;
    private StorageReference fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.nameEnter);
        phone = findViewById(R.id.phoneNumEnter);
        email = findViewById(R.id.emailEnter);
        passwd = findViewById(R.id.passwdEnter);
        reg = findViewById(R.id.register);
        login = findViewById(R.id.login);
        editpic=findViewById(R.id.editicon);
        prfpic=findViewById(R.id.prfimg);

        fstore= FirebaseStorage.getInstance().getReference();
        reff = FirebaseDatabase.getInstance().getReference().child("USERS");
        reff1 = FirebaseDatabase.getInstance().getReference().child("ProfilePic");
        fAuth = FirebaseAuth.getInstance();
        user = new UserReg();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, SelectContactActivity.class));
            finish();
        }

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name1 = name.getText().toString().trim();
                final String phno1 = phone.getText().toString().trim();
                final String email1 = email.getText().toString().trim();
                final String passwd1 = passwd.getText().toString().trim();

                if (TextUtils.isEmpty(name1) || TextUtils.isEmpty(phno1) || TextUtils.isEmpty(email1)
                        || TextUtils.isEmpty(passwd1)) {
                    Toast.makeText(MainActivity.this, "Enter All the details..", Toast.LENGTH_SHORT).show();
                } else if(!TextUtils.isEmpty(name1) && !TextUtils.isEmpty(phno1) && !TextUtils.isEmpty(email1)
                        && !TextUtils.isEmpty(passwd1)){

                    fAuth.createUserWithEmailAndPassword(email1, passwd1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                userID = fAuth.getCurrentUser().getUid();
                                user.setUserId(userID);
                                user.setNickName(name1);
                                user.setPhoneNum(phno1);
                                user.setEmail(email1);
                                user.setPassword(passwd1);
                                user.setStatus("null");
                                if(imageUri!=null) {
                                    final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                                    progressDialog.setTitle("Uploading...");
                                    progressDialog.show();
                                    final StorageReference filepath = fstore.child("ProfileImage").child(userID);
                                    filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            progressDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_LONG).show();

                                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    UserReg u = new UserReg();
                                                    u.setProfilePic(uri.toString());
                                                    reff1.child(userID).setValue(u);
                                                }
                                            });
                                        }
                                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                        }
                                    });
                                }
                                    user.setProfilePic("null");
                                    reff.child(userID).setValue(user);
                                    updateProfilePic();
                                Toast.makeText(MainActivity.this,"Successfully Registered..! ",Toast.LENGTH_LONG).show();
                                Intent i=new Intent(MainActivity.this,SelectContactActivity.class);
                                startActivity(i);
                            }else {
                                Toast.makeText(MainActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        editpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
        prfpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
    }

    private void updateProfilePic() {
       reff1.child(userID).child("profilePic").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()) {
                   prfPicUrl = snapshot.getValue().toString();
                   reff.child(userID).child("profilePic").setValue(prfPicUrl);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }

    private void choosePicture() {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri=data.getData();
            prfpic.setImageURI(imageUri);
        }
    }
}