package com.example.finalproject;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.finalproject.Models.Customer;
import com.example.finalproject.Models.WorkItem;
import com.example.finalproject.Utility.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {

    private static DataManager INSTANCE;
    private FirebaseFirestore db;

    private List<Customer> customerList;
    private Map<String, Customer> customerMap;
    private Map<Integer, WorkItem> workItemsMap;
    public static DataManager getInstance() {
        if(INSTANCE == null){
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    private DataManager(){
        this.db = FirebaseFirestore.getInstance();
        this.workItemsMap = new HashMap<>();
        this.customerMap = new HashMap<>();
        this.customerList = new ArrayList<>();
        getDataFromFirebase();
    }

    public Map<String, Customer> getCustomerMap(){
        if(!workItemsMap.isEmpty()){
            for (WorkItem wo : workItemsMap.values()) {
                Customer customer = wo.getCustomer();
                customerMap.put(customer.getPhone(), wo.getCustomer());
            }
        }
        return customerMap;
    }
    public List<Customer> getCustomerList(){
        if(!workItemsMap.isEmpty()){
            for (WorkItem wo : workItemsMap.values()) {
                customerList.add(wo.getCustomer());
            }
        }
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    public void setWorkItemsMap(Map<Integer, WorkItem> workItemsMap) {
        this.workItemsMap = workItemsMap;
    }

    public Map<Integer, WorkItem> getWorkItemsMap(){
        return workItemsMap;
    }

    private Map<Integer, WorkItem> getDataFromFirebase() {
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();

        db.collection(Constants.DBKeys.USERS)
                .document("gYkdDsk6c7Wk8ADQ3gZYs4Ovujx2")
                .collection(Constants.DBKeys.WORK_ITEMS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null) {
                                for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                                    if (documentSnapshot.exists()) {
                                        // Document exists, retrieve the desired fields
                                        fillWorkItemMap(documentSnapshot);
                                        fillCustomerList(documentSnapshot);
                                    }
                                }
                            }
                        } else {
                            Log.e("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return workItemsMap;
    }

    private void fillCustomerList(QueryDocumentSnapshot documentSnapshot) {
        WorkItem workItem = documentSnapshot.toObject(WorkItem.class);
        if(workItem != null){
            Customer customer = workItem.getCustomer();
            customerList.add(customer);
        }
    }

    private void fillWorkItemMap(QueryDocumentSnapshot documentSnapshot){
        WorkItem workItem = documentSnapshot.toObject(WorkItem.class);
        if(workItem != null){
            Log.d("TAG", "fillWorkItem: " + workItem);
            workItemsMap.put(workItem.getId(), workItem);
        }
    }

}
