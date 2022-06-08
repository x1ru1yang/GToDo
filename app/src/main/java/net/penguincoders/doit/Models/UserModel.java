package net.penguincoders.doit.Models;

public class UserModel {
    private int userid;
    private String password;

    public UserModel(int userid, String password){
        this.userid = userid;
        this.password = password;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
