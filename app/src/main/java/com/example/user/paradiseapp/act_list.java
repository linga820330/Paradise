package com.example.user.paradiseapp;

import android.media.Image;

public class act_list {
    private int position;
    private String title;
    private int image;

    public act_list(){
        super();
    }
    public act_list(int position,String title,int image){
        super();
        this.position=position;
        this.title=title;
        this.image=image;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }




}
