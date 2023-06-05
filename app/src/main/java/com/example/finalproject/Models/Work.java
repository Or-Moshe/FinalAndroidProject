package com.example.finalproject.Models;

import android.location.Address;

import java.util.Date;

public class Work {

    private Date date;
    private Customer customer;
    private long duration_estimated;
    private Address address;
    private String comment;

    public Work(Date date, Customer customer, long duration_estimated, Address address, String comment) {
        this.date = date;
        this.customer = customer;
        this.duration_estimated = duration_estimated;
        this.address = address;
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public long getDuration_estimated() {
        return duration_estimated;
    }

    public void setDuration_estimated(long duration_estimated) {
        this.duration_estimated = duration_estimated;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Work{" +
                "date=" + date +
                ", customer=" + customer +
                ", duration_estimated=" + duration_estimated +
                ", address=" + address +
                ", comment='" + comment + '\'' +
                '}';
    }
}
