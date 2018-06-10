package com.example.minalshettigar.splashscreen.helper;

import android.net.Uri;

import java.net.URI;

public class UserDbFormat {
    private String pic;
    private String name;
    private String email;
    private String contact;
    private String uid;

    public UserDbFormat(String uid, String name, String email, String contact, String pic) {
        this.pic = pic;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.uid = uid;
    }

    public UserDbFormat() {
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getPic() {
        return pic;
    }

    public String getName() {
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

