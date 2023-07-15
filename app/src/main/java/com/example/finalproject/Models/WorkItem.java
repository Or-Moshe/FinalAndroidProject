package com.example.finalproject.Models;

import android.location.Address;
import android.net.wifi.WpsInfo;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WorkItem {

    private String id;
    private Date date;
    private Customer customer;
    private Address address;
    private String typeOfWork, comment;
    private double timeOfWork, price;
    private Boolean isDone;
    public WorkItem(){}

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean done) {
        isDone = done;
    }

    public WorkItem(/*Integer id, */Date date, String typeOfWork, double timeOfWork, double price, Customer customer, Address address, String comment) {
        this.id = id;
        this.date = date;
        this.typeOfWork = typeOfWork;
        this.timeOfWork = timeOfWork;
        this.price = price;
        this.customer = customer;
        this.address = address;
        this.comment = comment;
        this.isDone  = false;
    }


    public void setId(String id){
        this.id = id;
    }
    public double getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getTypeOfWork() {
        return typeOfWork;
    }

    public void setTypeOfWork(String typeOfWork) {
        this.typeOfWork = typeOfWork;
    }

    public double getTimeOfWork() {
        return timeOfWork;
    }

    public void setTimeOfWork(double timeOfWork) {
        this.timeOfWork = timeOfWork;
    }

    public String getId() {
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
        return "WorkItem{" +
                "id=" + id +
                ", price=" + price +
                ", date=" + date +
                ", customer=" + customer +
                ", address=" + address +
                ", typeOfWork='" + typeOfWork + '\'' +
                ", comment='" + comment + '\'' +
                ", timeOfWork=" + timeOfWork +
                '}';
    }
}
