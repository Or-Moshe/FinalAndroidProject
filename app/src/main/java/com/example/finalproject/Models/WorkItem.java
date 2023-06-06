package com.example.finalproject.Models;

import android.location.Address;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WorkItem {

    private Integer id;
    private Date date;
    private Customer customer;
    private long duration_estimated;
    private Address address;
    private String comment;

    public WorkItem(Integer id, Date date, Customer customer, long duration_estimated, Address address, String comment) {
        this.id = id;
        this.date = date;
        this.customer = customer;
        this.duration_estimated = duration_estimated;
        this.address = address;
        this.comment = comment;
    }

    public WorkItem(){
        this.id = 0;
        this.date = Calendar.getInstance().getTime();
        this.customer = new Customer("Yossi", "0542576111", 2);
        this.duration_estimated = 2;
        //this.address = new Address(new Locale());
        this.comment = "bla bla bla";
    }

    public Integer getId() {
        return id;
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
                "id=" + id +
                "date=" + date +
                ", customer=" + customer +
                ", duration_estimated=" + duration_estimated +
                ", address=" + address +
                ", comment='" + comment + '\'' +
                '}';
    }
}
