package com.example.chatwithbestie;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ContactViewHolder extends RecyclerView.ViewHolder {

    public TextView nickName;
    public TextView mymsg,frndmsg,mymsgtime,frndmsgtime,mypictime,frndpictime;
    public LinearLayout linearLayout;
    public ConstraintLayout frndlay,mylay,frndpiclay,mypiclay;
    public ImageView prfimg,mypic,frndpic;

    public ContactViewHolder(@NonNull View itemView) {
        super(itemView);

        nickName=itemView.findViewById(R.id.name1);
        linearLayout=itemView.findViewById(R.id.linlayout);
        prfimg=itemView.findViewById(R.id.img1);
        mypic=itemView.findViewById(R.id.mypic);
        frndpic=itemView.findViewById(R.id.frndpic);
        mypiclay=itemView.findViewById(R.id.mypiclayout);
        frndpiclay=itemView.findViewById(R.id.frndpiclayout);
        mypictime=itemView.findViewById(R.id.mypictime);
        frndpictime=itemView.findViewById(R.id.frnd_pic_time);

        mymsg=itemView.findViewById(R.id.my_msg);
        mymsgtime=itemView.findViewById(R.id.my_msg_time);
        frndmsg=itemView.findViewById(R.id.frnd_msg);
        frndmsgtime=itemView.findViewById(R.id.frnd_msg_time);
        frndlay=itemView.findViewById(R.id.frndlayout);
        mylay=itemView.findViewById(R.id.mylayout);

    }

}
