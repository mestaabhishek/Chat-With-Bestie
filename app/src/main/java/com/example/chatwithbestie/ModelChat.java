package com.example.chatwithbestie;

public class ModelChat {

    private String frndmessage, mymessage,myid,frndid,Picture;
    private Long frndtimestamp, mytimestamp;

    public ModelChat() {
    }

    ModelChat(String mymessage, String myid, String frndid, Long timestamp) {
        this.mymessage = mymessage;
        this.myid = myid;
        this.frndid=frndid;
        this.mytimestamp = timestamp;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public String getFrndid() {
        return frndid;
    }

    public void setFrndid(String frndid) {
        this.frndid = frndid;
    }

    public String getMyid() {
        return myid;
    }

    public void setMyid(String myid) {
        this.myid = myid;
    }

    public String getFrndmessage() {
        return frndmessage;
    }

    public void setFrndmessage(String frndmessage) {
        this.frndmessage = frndmessage;
    }

    public String getMymessage() {
        return mymessage;
    }

    public void setMymessage(String mymessage) {
        this.mymessage = mymessage;
    }

    public Long getFrndtimestamp() {
        return frndtimestamp;
    }

    public void setFrndtimestamp(Long frndtimestamp) {
        this.frndtimestamp = frndtimestamp;
    }

    public Long getMytimestamp() {
        return mytimestamp;
    }

    public void setMytimestamp(Long mytimestamp) {
        this.mytimestamp = mytimestamp;
    }
}

