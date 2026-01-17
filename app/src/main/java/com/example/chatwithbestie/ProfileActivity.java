package com.example.chatwithbestie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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
import com.squareup.picasso.Picasso;

import static com.example.chatwithbestie.R.drawable.ic_baseline_face_24;

public class ProfileActivity extends AppCompatActivity {

    private EditText edname,ednum,edstatus;
    public Uri imageUri;
    private TextView name,num,status;
    private Button namebtn,numbtn,statusbtn,donename,donenum,donestatus,addimage;
    String frndid,frndimg,myid;
    private ImageView frndimage;
    DatabaseReference reff,reff1;
    StorageReference fstore;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name=findViewById(R.id.name2);
        num=findViewById(R.id.phonenum);
        status=findViewById(R.id.status);
        namebtn=findViewById(R.id.namebtn);
        numbtn=findViewById(R.id.phbtn);
        statusbtn=findViewById(R.id.stbtn);
        edname=findViewById(R.id.editname2);
        ednum=findViewById(R.id.editphonenum);
        edstatus=findViewById(R.id.editstatus);
        donename=findViewById(R.id.donenamebtn);
        donenum=findViewById(R.id.donephbtn);
        donestatus=findViewById(R.id.donestbtn);
        addimage=findViewById(R.id.addimage);

        frndimage=findViewById(R.id.frndimage);
        fAuth=FirebaseAuth.getInstance();
        fstore= FirebaseStorage.getInstance().getReference();
        reff1 = FirebaseDatabase.getInstance().getReference().child("ProfilePic");

        frndid=getIntent().getStringExtra("frndid");
        frndimg=getIntent().getStringExtra("frndimg");
        myid=getIntent().getStringExtra("myid");
        reff=FirebaseDatabase.getInstance().getReference().child("USERS");

        if(frndid!=null) {
            addimage.setVisibility(View.GONE);
            if(frndimg!=null)
                Picasso.get().load(frndimg).into(frndimage);
            showFrndDetails(frndid);
        }
        else if(myid!=null) {
            showMyDetails();
        }

    }

    private void showMyDetails() {
        donename.setVisibility(View.GONE);
        donenum.setVisibility(View.GONE);
        donestatus.setVisibility(View.GONE);
        edname.setVisibility(View.GONE);
        ednum.setVisibility(View.GONE);
        edstatus.setVisibility(View.GONE);
        namebtn.setVisibility(View.VISIBLE);
        statusbtn.setVisibility(View.VISIBLE);
        numbtn.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        status.setVisibility(View.VISIBLE);
        num.setVisibility(View.VISIBLE);

        reff.child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("profilePic").getValue().toString()!=null) {
                        Picasso.get().load(snapshot.child("profilePic").getValue().toString()).into(frndimage);
                    }
                    name.setText(snapshot.child("nickName").getValue().toString());
                    num.setText(snapshot.child("phoneNum").getValue().toString());
                    if (snapshot.child("status").getValue().toString().equals("null")) {
                        status.setText("Hey there..! I am " + snapshot.child("nickName").getValue().toString());
                    } else
                        status.setText(snapshot.child("status").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        namebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setVisibility(View.INVISIBLE);
                edname.setVisibility(View.VISIBLE);
                namebtn.setVisibility(View.INVISIBLE);
                donename.setVisibility(View.VISIBLE);

                donename.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!edname.getText().toString().equals("null")){
                            reff.child(fAuth.getCurrentUser().getUid()).child("nickName").setValue(edname.getText().toString());
                            showMyDetails();
                        }
                        else{
                            Toast.makeText(ProfileActivity.this,"Enter Your Name Correctly..!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        numbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num.setVisibility(View.INVISIBLE);
                ednum.setVisibility(View.VISIBLE);
                numbtn.setVisibility(View.INVISIBLE);
                donenum.setVisibility(View.VISIBLE);

                donenum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!ednum.getText().toString().equals("null")){
                            reff.child(fAuth.getCurrentUser().getUid()).child("phoneNum").setValue(ednum.getText().toString());
                            showMyDetails();
                        }
                        else if(ednum.getText().toString().equals("null") || ednum.getText().toString().length()!=10){
                            Toast.makeText(ProfileActivity.this,"Enter Your Phone Number Correctly..!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        statusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status.setVisibility(View.INVISIBLE);
                edstatus.setVisibility(View.VISIBLE);
                statusbtn.setVisibility(View.INVISIBLE);
                donestatus.setVisibility(View.VISIBLE);

                donestatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!edstatus.getText().toString().equals("null") && edstatus.getText().toString().length()<=200){
                            reff.child(fAuth.getCurrentUser().getUid()).child("status").setValue(edstatus.getText().toString());
                            showMyDetails();
                        }
                        else if(edstatus.getText().toString().length()>200){
                            Toast.makeText(ProfileActivity.this,"Keep Status less than 200 words..!",Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(ProfileActivity.this,"Enter Your Status Correctly..!",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
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
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            imageUri = data.getData();
            frndimage.setImageURI(imageUri);
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog=new ProgressDialog(ProfileActivity.this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        final StorageReference filepath = fstore.child("ProfileImage").child(myid);
        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "Uploaded", Toast.LENGTH_LONG).show();

                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserReg u=new UserReg();
                        u.setProfilePic(uri.toString());
                        reff1.child(myid).setValue(u);
                        updateToDatabase();
                        //Toast.makeText(ProfileActivity.this,"mmm "+filepath,Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress=(100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded "+(int)progress+"%");
            }
        });
    }

    private void updateToDatabase() {
        reff1.child(myid).child("profilePic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    reff.child(myid).child("ProfilePic").setValue(snapshot.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showFrndDetails(String frndid) {

        namebtn.setVisibility(View.GONE);
        statusbtn.setVisibility(View.GONE);
        numbtn.setVisibility(View.GONE);
        edname.setVisibility(View.GONE);
        ednum.setVisibility(View.GONE);
        edstatus.setVisibility(View.GONE);
        donename.setVisibility(View.GONE);
        donenum.setVisibility(View.GONE);
        donestatus.setVisibility(View.GONE);

        reff.child(frndid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    name.setText(snapshot.child("nickName").getValue().toString());
                    num.setText(snapshot.child("phoneNum").getValue().toString());
                    if (snapshot.child("status").getValue().toString().equals("null")) {
                        status.setText("Hey there..! I am " + snapshot.child("nickName").getValue().toString());
                    } else
                        status.setText(snapshot.child("status").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}