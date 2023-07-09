package com.example.finalproject;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.finalproject.Interfaces.DataRetrievedListener;
import com.example.finalproject.Models.Customer;
import com.example.finalproject.Models.WorkItem;
import com.example.finalproject.Utility.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {

    private static DataManager INSTANCE;
    private FirebaseFirestore db;
    private DocumentReference documentRef;
    private CollectionReference collectionRef;
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

    public void setWorkItemsMap(Map<Integer, WorkItem> workItemsMap) {
        this.workItemsMap = workItemsMap;
    }

    public Map<Integer, WorkItem> getWorkItemsMap(){
        return workItemsMap;
    }

    public void retrieveDataFromFirestore(FirebaseUser user, DataRetrievedListener listener) {
        // Retrieve the collection from Firestore
        db = FirebaseFirestore.getInstance();
        documentRef = db.collection(Constants.DBKeys.USERS).document("gYkdDsk6c7Wk8ADQ3gZYs4Ovujx2");
        collectionRef = documentRef.collection(Constants.DBKeys.WORK_ITEMS);

        collectionRef.get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
                        for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                            if (documentSnapshot.exists()) {
                                // Document exists, retrieve the desired fields
                                fillWorkItemMap(documentSnapshot);
                                fillCustomerMap(documentSnapshot);
                            }
                        }
                    }
                    List<DocumentSnapshot> documentList = task.getResult().getDocuments();
                    // Pass the retrieved data to the listener
                    Log.d("TAG", "Pass the retrieved data to the listener: ");
                    listener.onDataRetrieved(documentList);
                } else {
                    Log.e("Firestore", "Error retrieving data", task.getException());
                }
            });
        documentRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", "Listen failed", error);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    // Document data is available in the snapshot
                    // Extract the updated data and perform necessary actions
                    String updatedField = snapshot.getString("field_name");
                    // Update your UI or perform other operations based on the updated data
                } else {
                    Log.d("Firestore", "Current data: null");
                }
            }
        });
    }

    public void updateWorkOrder(WorkItem workItem) {
        if (workItem != null) {
            String documentId = workItem.getId()+"";
            collectionRef.document(documentId).set(workItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Update successful
                        Log.d("TAG", "onSuccess: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Update failed
                        Log.d("TAG", "onFailure: ");
                    }
                });
        }
    }
    /*public Map<Integer, WorkItem> getDataFromFirebase() {
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
                                        fillCustomerMap(documentSnapshot);
                                    }
                                }
                            }
                        } else {
                            Log.e("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return workItemsMap;
    }*/

    private void fillCustomerMap(QueryDocumentSnapshot documentSnapshot) {
        WorkItem workItem = documentSnapshot.toObject(WorkItem.class);
        if(workItem != null){
            Customer customer = workItem.getCustomer();
            if(customer != null){
                customerMap.put(customer.getPhone(), customer);
            }
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
