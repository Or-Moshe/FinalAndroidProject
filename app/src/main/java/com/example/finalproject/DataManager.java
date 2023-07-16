package com.example.finalproject;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finalproject.Fragments.WorksFormFragment;
import com.example.finalproject.Interfaces.DataRetrievedListener;
import com.example.finalproject.Interfaces.DocumentCreatedListener;
import com.example.finalproject.Interfaces.DocumentDeletedListener;
import com.example.finalproject.Models.Customer;
import com.example.finalproject.Models.WorkItem;
import com.example.finalproject.Utility.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataManager {

    private static DataManager INSTANCE;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private DocumentReference userDocumentRef;
    private CollectionReference workItemsCollectionRef;
    private Map<String, Customer> customerMap;
    private Map<String, WorkItem> workItemsMap;
    public static DataManager getInstance() {
        if(INSTANCE == null){
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public FirebaseUser getUser() {
        return this.user;
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

    public Map<String, WorkItem> getWorkItemsMap(){
        Log.d("TAG", "getWorkItemsMap: " + workItemsMap.size());
        return workItemsMap;
        //return retrieveDataFromFirestore();
    }

    public void deleteDocuments(final DocumentDeletedListener listener){
        Set<String> docIdSet = new HashSet<>();
        Query query = workItemsCollectionRef.whereEqualTo("isDone", true);
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        // Iterate through the documents returned by the query
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            // Delete each document
                            String documentId = documentSnapshot.getId();
                            docIdSet.add(documentId);
                            deleteWorkItem(documentSnapshot);
                            listener.onDocumentDeleted(documentId);
                            Log.d("TAG", "deleteDocuments: ");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error
                    }
                });
    }

    public String addNewDocument(WorkItem workItem, final DocumentCreatedListener listener){
        DocumentReference newDocumentRef = workItemsCollectionRef.document();
        String documentId = newDocumentRef.getId(); // Get the auto-generated document ID
        workItem.setId(documentId);
        newDocumentRef.set(workItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onDocumentCreated(documentId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onDocumentCreationFailed(e);
                    }
                });
        return documentId;
    }


    public Map<String, WorkItem> retrieveDataFromFirestore(FirebaseUser user, DataRetrievedListener listener) {
        this.user = user;
        this.userDocumentRef = db.collection(Constants.DBKeys.USERS).document(user.getUid());
        this.workItemsCollectionRef = userDocumentRef.collection(Constants.DBKeys.WORK_ITEMS);

        workItemsCollectionRef.get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    List<DocumentSnapshot> documentList = task.getResult().getDocuments();
                    for (DocumentSnapshot documentSnapshot : documentList) {
                        fillWorkItemMap(documentSnapshot);
                        fillCustomerMap(documentSnapshot);
                    }
                    listener.onDataRetrieved(documentList);
                } else {

                    listener.onDataRetrievedFailed(task.getException());
                }
            });
        userDocumentRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", "Listen failed", error);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                } else {
                    Log.d("Firestore", "Current data: null");
                }
            }
        });
        return workItemsMap;
    }

    /*private Map<String, WorkItem> retrieveDataFromFirestore() {
        workItemsCollectionRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        List<DocumentSnapshot> documentList = task.getResult().getDocuments();
                        for (DocumentSnapshot documentSnapshot : documentList) {
                            fillWorkItemMap(documentSnapshot);
                            fillCustomerMap(documentSnapshot);
                        }

                    } else {
                        Log.e("Firestore", "Error retrieving data", task.getException());
                    }
                });
        userDocumentRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
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
        return workItemsMap;
    }*/

    public void updateWorkOrder(WorkItem workItem) {
        if (workItem != null) {
            String documentId = workItem.getId()+"";
            workItemsCollectionRef.document(documentId).set(workItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Update successful
                        Log.d("TAG", "updateWorkOrder onSuccess: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Update failed
                        Log.d("TAG", "updateWorkOrder onFailure: ");
                    }
                });
        }
    }

    private void fillCustomerMap(DocumentSnapshot documentSnapshot) {
        WorkItem workItem = documentSnapshot.toObject(WorkItem.class);
        if(workItem != null){
            Customer customer = workItem.getCustomer();
            if(customer != null){
                customerMap.put(customer.getPhone(), customer);
            }
        }
    }

    private void fillWorkItemMap(DocumentSnapshot documentSnapshot){
        WorkItem workItem = documentSnapshot.toObject(WorkItem.class);
        if(workItem != null){
            workItemsMap.put(documentSnapshot.getId(), workItem);
        }
    }

    private void deleteWorkItem(DocumentSnapshot documentSnapshot){
        WorkItem workItem = documentSnapshot.toObject(WorkItem.class);
        if(workItem != null){
            workItemsMap.remove(documentSnapshot.getId());
            documentSnapshot.getReference().delete();
        }
    }
}
