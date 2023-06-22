package com.example.finalproject.ViewModels;

import android.location.Address;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.finalproject.DataManager;
import com.example.finalproject.Models.Customer;
import com.example.finalproject.Models.WorkItem;
import com.example.finalproject.Utility.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WorksFormViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private final MutableLiveData<Map<Integer, WorkItem>> mWorkItemsMap;
    private FirebaseFirestore db;

    public WorksFormViewModel() {
        this.mWorkItemsMap = new MutableLiveData<>();
        this.mWorkItemsMap.setValue(getWorkItemsFromFirebase());

    }

    private Map<Integer, WorkItem> getWorkItemsFromFirebase() {
        /*Map<Integer, WorkItem> workItemsMap = DataManager.getInstance().getWorkItemsMap();
        mWorkItemsMap.setValue(workItemsMap);
        return workItemsMap;*/

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();

        Map<Integer, WorkItem> workItemsMap = new HashMap<>();

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
                                        fillWorkItem(documentSnapshot, workItemsMap);
                                    }
                                }
                            }
                        } else {
                            Log.e("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
        DataManager.getInstance().setWorkItemsMap(workItemsMap);
        return workItemsMap;
    }

    private void fillWorkItem(QueryDocumentSnapshot documentSnapshot, Map<Integer, WorkItem> workItemsMap){
        WorkItem workItem = documentSnapshot.toObject(WorkItem.class);
        if(workItem != null){
            Log.d("TAG", "fillWorkItem: " + workItem);
            workItemsMap.put(workItem.getId(), workItem);
            mWorkItemsMap.setValue(workItemsMap);
        }
    }

    public void addWorkItemToDB(String documentPath, WorkItem workItem){
        // Assume we have a reference to the parent document
        DocumentReference parentDocumentRef = db.collection(Constants.DBKeys.USERS).document(documentPath);

        // Create a reference to the collection
        CollectionReference collectionRef = parentDocumentRef.collection(Constants.DBKeys.WORK_ITEMS);

        // Set data to the collection

        // Create a new document in the collection and set data
        collectionRef.document(workItem.getId()+"").set(workItem)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Data set to the subcollection successfully
                    Log.d("TAG", "saved to document success : " + workItem);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Failed to set data to the subcollection
                    Log.e("TAG", "saved to document failed : " + workItem);
                }
        });
    }

    public LiveData<Map<Integer, WorkItem>> getWorkItemsMap() {

        return mWorkItemsMap;
    }
}