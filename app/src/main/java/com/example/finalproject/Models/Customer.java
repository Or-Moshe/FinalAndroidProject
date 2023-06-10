package com.example.finalproject.Models;

public class Customer {

    private String name;
    private String phone;
    private long time;

    public Customer(){}
    public Customer(String name, String phone, long time) {
        this.name = name;
        this.phone = phone;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
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
                '}';
    }
}
