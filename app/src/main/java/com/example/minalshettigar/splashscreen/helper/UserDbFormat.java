package com.example.minalshettigar.splashscreen.helper;

import android.net.Uri;

import java.net.URI;

public class UserDbFormat {
//    private Uri pic;
    private String name;
    private String email;
    private String contact;
    private String uid;

    public UserDbFormat(String uid, String name, String email, String contact) {
//        this.pic = pic;
        this.name = name;
        this.email = email;
        this.contact = contact;
    }

//    public void setPic(Uri pic) {
//        this.pic = pic;
//    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

//    public Uri getPic() {
//        return pic;
//    }

    public String getname() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}

