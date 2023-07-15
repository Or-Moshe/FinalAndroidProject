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
import com.example.finalproject.Interfaces.DataRetrievedListener;
import com.example.finalproject.Interfaces.DocumentDeletedListener;
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
import java.util.List;
import java.util.Map;

public class WorksFormViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private final MutableLiveData<Map<String, WorkItem>> mWorkItemsMap;

    public WorksFormViewModel() {
        this.mWorkItemsMap = new MutableLiveData<>();
        this.mWorkItemsMap.setValue(DataManager.getInstance().getWorkItemsMap());

    }

    public void onDocumentDeleted(String documentId) {
        Map<String, WorkItem> workItemMap = mWorkItemsMap.getValue();
        workItemMap.remove(documentId);
        mWorkItemsMap.setValue(workItemMap);
        Log.d("TAG", "onDocumentDeleted"+ mWorkItemsMap);
    }

    public LiveData<Map<String, WorkItem>> getWorkItemsMap() {
        DataManager manager = DataManager.getInstance();

        DataManager.getInstance().retrieveDataFromFirestore(manager.getUser(), new DataRetrievedListener() {
            @Override
            public void onDataRetrieved(List<DocumentSnapshot> documentList) {
                Map<String, WorkItem> workItemMap = new HashMap<>();
                for (DocumentSnapshot documentSnapshot : documentList) {
                    WorkItem workItem = documentSnapshot.toObject(WorkItem.class);
                    workItemMap.put(workItem.getId(), workItem);
                    Log.d("TAG", "onDataRetrieved: "+ workItemMap);
                }
                mWorkItemsMap.setValue(workItemMap);
            }
        });
        Log.d("TAG", "onDataRetrieved:mWorkItemsMap"+ mWorkItemsMap);
        return mWorkItemsMap;
    }
}