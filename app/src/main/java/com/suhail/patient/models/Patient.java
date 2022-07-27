package com.suhail.patient.models;

import java.io.Serializable;

public class Patient implements Serializable {
    private String name;
    private String email;
    private String phoneNo;
    private String address;
    private String gender;
    private String dateOfBirth;

    //constructor for all failed
    public Patient(String name, String email, String phoneNo, String address, String gender, String dateOfBirth) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.address = address;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    //empty constructor
    public Patient() {
    }

    //setter & getter methods

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}

