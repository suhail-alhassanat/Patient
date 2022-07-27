package com.suhail.patient.models;

public class UserCategory {
    private String email;
    //1.admin
    //2.doctor
    //3.receptor
    //4.patient
    private int type;

    public UserCategory(String email, int type) {
        this.email = email;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
