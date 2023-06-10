package com.example.finalproject;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.finalproject.Models.WorkItem;
import com.example.finalproject.Utility.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class DataManager {

    private static DataManager INSTANCE;
    private FirebaseFirestore db;

    public static DataManager getInstance() {
        return INSTANCE;
    }

    /*public void aa(){
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
    }

    private void fillWorkItem(QueryDocumentSnapshot documentSnapshot, Map<Integer, WorkItem> workItemsMap){
        WorkItem workItem = documentSnapshot.toObject(WorkItem.class);
        if(workItem != null){
            Log.d("TAG", "fillWorkItem: " + workItem);
            workItemsMap.put(workItem.getId(), workItem);
            mWorkItemsMap.setValue(workItemsMap);
        }
    }*/

    private DataManager(){
        this.db = FirebaseFirestore.getInstance();
    }

}
