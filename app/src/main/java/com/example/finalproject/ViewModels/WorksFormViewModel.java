package com.example.finalproject.ViewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalproject.Models.Customer;
import com.example.finalproject.Models.WorkItem;
import com.example.finalproject.Utility.Constants;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WorksFormViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private final MutableLiveData<Map<Integer, WorkItem>> mWorkItemsMap;

    public WorksFormViewModel() {
        this.mWorkItemsMap = new MutableLiveData<>();
        this.mWorkItemsMap.setValue(getWorkItemsFromFirebase());
    }

    private Map<Integer, WorkItem> getWorkItemsFromFirebase() {
        Map<Integer, WorkItem> workItemsMap = new HashMap<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.DBKeys.WORK_ITEMS);
        if(workItemsMap.size() == 0){
            WorkItem workItem = new WorkItem();
            workItemsMap.put(workItemsMap.size(), workItem);
            mWorkItemsMap.setValue(workItemsMap);
        }
        else {

            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    WorkItem workItem = snapshot.getValue(WorkItem.class);
                    workItemsMap.put(workItemsMap.size(), workItem);
                    mWorkItemsMap.setValue(workItemsMap);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    WorkItem workItem = snapshot.getValue(WorkItem.class);
                    Log.d("TAG", "onChildChanged: " + workItem);
                    workItemsMap.put(workItem.getId(), workItem);
                    Log.d("TAG_All", "onChildChanged: " + workItemsMap);
                    mWorkItemsMap.setValue(workItemsMap);
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return workItemsMap;
    }

    public LiveData<Map<Integer, WorkItem>> getWorkItemsMap() {
        return mWorkItemsMap;
    }
}