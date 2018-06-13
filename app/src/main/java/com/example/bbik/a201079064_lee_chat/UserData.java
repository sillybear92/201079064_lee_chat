package com.example.bbik.a201079064_lee_chat;

public class UserData{
    public String id;
    public String pw;

    public UserData(){
        //
    }

    public UserData(String id, String pw){
        this.id = id;
        this.pw = pw;
    }

    public String getId() {
        return id;
    }

    public String getPw() {
        return pw;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
}
