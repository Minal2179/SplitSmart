package com.example.minalshettigar.splashscreen.helper;

/**
 * Created by Shailesh
 */

public class UsersDataModel {
    private String pic;
    private String name;
    private String email;

    public UsersDataModel(String pic, String name, String email) {
        this.pic = pic;
        this.name = name;
        this.email = email;
    }

    public String getPic() {
        return pic;
    }

    public String getname() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
