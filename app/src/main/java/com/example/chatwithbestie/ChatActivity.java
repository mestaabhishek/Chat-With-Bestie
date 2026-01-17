package com.example.chatwithbestie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    ImageView sendImage,frndImg;
    TextView frndName;
    Button sendpic,cam,gal;
    EditText userMessage;
    RelativeLayout relay;
    DatabaseReference mChatData, mUserData;
    String username = "null",mAuth1,frndid,myid,frname,frimg;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<ModelChat, ContactViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    StorageReference fstore;
    private Uri imageUri;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        fAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("USERS");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_chats);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        fAuth = FirebaseAuth.getInstance();
        relay=findViewById(R.id.imageselection);

        fstore= FirebaseStorage.getInstance().getReference().child("MessagePic").child(fAuth.getCurrentUser().getUid());

        sendImage = findViewById(R.id.send_image);
        userMessage = findViewById(R.id.user_message);
        cam = findViewById(R.id.cam);
        gal = findViewById(R.id.gal);
        frndImg = findViewById(R.id.frndimg);
        frndName = findViewById(R.id.frndname);
        sendpic=findViewById(R.id.sendpic);
        myid=fAuth.getUid();

        frndid=getIntent().getStringExtra("frndid");
        frname=getIntent().getStringExtra("frndName");
        frimg=getIntent().getStringExtra("frndImg");

        mChatData = FirebaseDatabase.getInstance().getReference().child("Chats");
        mUserData = FirebaseDatabase.getInstance().getReference().child("USERS").child(frndid);
        mUserData.keepSynced(true);
        mChatData.keepSynced(true);

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(adapter.getItemCount()-1);
            }
        },500);

        displayChatMessage();

        frndName.setText(frname);
        if(frimg!=null)
            Picasso.get().load(frimg).into(frndImg);

        userMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendpic.setVisibility(View.INVISIBLE);
            }
        });

        sendpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relay.setVisibility(View.VISIBLE);
                cam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TakePicture();
                        relay.setVisibility(View.GONE);
                    }
                });
                gal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SendPicture();
                        relay.setVisibility(View.GONE);
                    }
                });
            }
        });

        mAuth1 = fAuth.getCurrentUser().getUid();
        //Toast.makeText(ChatActivity.this,"hi"+mAuth1,Toast.LENGTH_LONG).show();
        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userMessage.getText() != null) {
                    if (!userMessage.getText().toString().equals("") && username != null) {
                        mChatData.push().setValue(new ModelChat(userMessage.getText().toString(),  mAuth1,frndid, System.currentTimeMillis()));
                        userMessage.setText("");
                        sendpic.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void TakePicture() {
    }

    private void SendPicture() {
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
            final ProgressDialog progressDialog=new ProgressDialog(ChatActivity.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference filepath = fstore.child(String.valueOf(System.currentTimeMillis()));
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(ChatActivity.this, "Uploaded", Toast.LENGTH_LONG).show();

                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            ModelChat u=new ModelChat();
                            u.setPicture(uri.toString());
                            u.setMyid(fAuth.getCurrentUser().getUid());
                            u.setFrndid(frndid);
                            u.setMytimestamp(System.currentTimeMillis());
                            mChatData.push().setValue(u);
                            Toast.makeText(ChatActivity.this, "Sent", Toast.LENGTH_LONG).show();
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
    }

    private void displayChatMessage() {
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ModelChat>()
                .setQuery(mChatData, ModelChat.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<ModelChat, ContactViewHolder>(options) {
            @NonNull
            @Override
            public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.list_msg, viewGroup, false);
                return new ContactViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ContactViewHolder holder, int i, @NonNull final ModelChat model) {

                if(myid.equals(model.getMyid()) && frndid.equals(model.getFrndid())){
                    holder.frndlay.setVisibility(View.GONE);
                    holder.frndpiclay.setVisibility(View.GONE);
                    holder.mypiclay.setVisibility(View.GONE);

                if (model.getMymessage() != null) {
                    holder.mymsg.setText(model.getMymessage());
                    holder.mymsgtime.setText((DateFormat.format("hh:mmaa", model.getMytimestamp())));
                }
                if(model.getPicture()!=null){
                    holder.mypiclay.setVisibility(View.VISIBLE);
                    Picasso.get().load(model.getPicture()).into(holder.mypic);
                    holder.mypictime.setText((DateFormat.format("hh:mmaa", model.getMytimestamp())));
                }
                } else if (frndid.equals(model.getMyid()) && myid.equals(model.getFrndid())) {
                    holder.mylay.setVisibility(View.GONE);
                    holder.frndpiclay.setVisibility(View.GONE);
                    holder.mypiclay.setVisibility(View.GONE);

                    if(model.getMymessage() != null) {
                        holder.frndmsg.setText(model.getMymessage());
                        holder.frndmsgtime.setText((DateFormat.format("hh:mmaa", model.getMytimestamp())));
                    }
                    if(model.getPicture()!=null){
                        holder.frndpiclay.setVisibility(View.VISIBLE);
                        Picasso.get().load(model.getPicture()).into(holder.frndpic);
                        holder.frndpictime.setText((DateFormat.format("hh:mmaa", model.getMytimestamp())));
                    }
                } else {
                    holder.mylay.setVisibility(View.GONE);;
                    holder.frndlay.setVisibility(View.GONE);
                }
                //final String frndid=model.getFrndid();
            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    public void openNewActivity(View view) {
        Intent intent=new Intent(ChatActivity.this,ProfileActivity.class);
        intent.putExtra("frndid",frndid);
        intent.putExtra("frndimg",frimg);
        startActivity(intent);
    }

    private void addNotification(){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Chat With Bestie")
                .setContentText("");
    }



    /*private void displayChatMessage() {
        ListView listOfMessage = findViewById(R.id.list_message);
        FirebaseListAdapter<ModelChat> adapter = new FirebaseListAdapter<ModelChat>(this, ModelChat.class, R.layout.list_msg, FirebaseDatabase.getInstance().getReference().child("chats").limitToLast(100)) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void populateView(View v, ModelChat model, int position) {

                TextView myMessageText, frndMessageText, myMessageTime,frndMessageTime;
                myMessageText = v.findViewById(R.id.my_msg);
                frndMessageText = v.findViewById(R.id.frnd_msg);
                myMessageTime = v.findViewById(R.id.my_msg_time);
                frndMessageTime = v.findViewById(R.id.frnd_msg_time);

                if(mAuth1.equals(model.getMyid())) {
                    myMessageText.setText(model.getMymessage());
                    myMessageTime.setText(DateFormat.format("dd:MMaa", model.getMytimestamp()));
                    frndMessageText.setVisibility(View.GONE);
                    frndMessageTime.setVisibility(View.GONE);
                    myMessageText.setVisibility(View.VISIBLE);
                    myMessageTime.setVisibility(View.VISIBLE);
                }
               // else {
                    //frndMessageText.setText(model.getFrndmessage());
                    //frndMessageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getFrndtimestamp()));
                //}
            }
        };
        listOfMessage.setAdapter(adapter);
    }*/
}