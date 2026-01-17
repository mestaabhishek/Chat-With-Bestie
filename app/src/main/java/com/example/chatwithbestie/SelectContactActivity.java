package com.example.chatwithbestie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.security.PrivateKey;

public class SelectContactActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<UserReg, ContactViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    private FirebaseAuth fAuth;
    private FirebaseUser user;
    private StorageReference fstore;
    public Uri imageUri;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("USERS");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_contacts);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        fAuth = FirebaseAuth.getInstance();
        fstore= FirebaseStorage.getInstance().getReference().child("ProfileImage");

        if (fAuth.getCurrentUser() != null)
        {
            user = fAuth.getCurrentUser();
            uid=user.getUid();
            //Toast.makeText(SelectContactActivity.this,"hi"+uid,Toast.LENGTH_LONG).show();
        }
        else {
            fAuth.signOut();
            startActivity(new Intent(SelectContactActivity.this, MainActivity.class));
        }
        showList();
    }

    private void showList() {

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<UserReg>()
                .setQuery(reference,UserReg.class)
                .build();

        adapter= new FirebaseRecyclerAdapter<UserReg,ContactViewHolder>(options) {
            @NonNull
            @Override
            public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.contact_lists,viewGroup,false);
                return new ContactViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull final ContactViewHolder holder, int i, @NonNull final UserReg itemCategory) {

                holder.nickName.setText(itemCategory.getNickName());
                if(itemCategory.getProfilePic()!=null)
                  Picasso.get().load(itemCategory.getProfilePic()).into(holder.prfimg);
                else {
                    Picasso.get().load(R.drawable.ic_baseline_face_24).into(holder.prfimg);
                }
                //holder.prfimg.setImageURI(Uri.parse(itemCategory.getProfilePic()));
                final String frndid=itemCategory.getUserId();

                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(SelectContactActivity.this,ChatActivity.class);
                        i.putExtra("frndid",frndid);
                        i.putExtra("frndName",itemCategory.getNickName());
                        i.putExtra("frndImg",itemCategory.getProfilePic());
                        startActivity(i);
                        //Toast.makeText(SelectContactActivity.this,"hi "+frndid,Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        if (item.getItemId() == R.id.logout) {
            fAuth.signOut();
            intent = new Intent(SelectContactActivity.this, MainActivity.class);
            this.startActivity(intent);
            finish();
        }
        else if(item.getItemId()==R.id.profile){
            intent = new Intent(SelectContactActivity.this, ProfileActivity.class);
            intent.putExtra("myid",fAuth.getCurrentUser().getUid());
            startActivity(intent);
            finish();
        }
        return (super.onOptionsItemSelected(item));
    }
}