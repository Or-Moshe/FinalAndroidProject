package com.example.finalproject.Models;

import android.location.Address;

public class Customer {

    private String name;
    private String phone;
    private long time;

    private String addressStr;

    public Customer(){}
    public Customer(String name, String phone, String addressStr, long time) {
        this.name = name;
        this.phone = phone;
        this.time = time;
        this.addressStr = addressStr;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return addressStr;
    }

    public void setAddress(String addressStr) {
        this.addressStr = addressStr;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", time=" + time +
                ", addressStr=" + addressStr +
                '}';
    }
}
