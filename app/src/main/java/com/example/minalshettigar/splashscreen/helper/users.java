package com.example.minalshettigar.splashscreen.helper;

public class users {

    private String contact;
    private String email;
    private String name;


    public users()
    {

    }
    public users(String con,String email,String name)
    {
        this.contact=con;
        this.email=email;
        this.name=name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
