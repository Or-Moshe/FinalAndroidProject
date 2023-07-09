package com.example.finalproject.Interfaces;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public interface DataRetrievedListener {

    void onDataRetrieved(List<DocumentSnapshot> documentList);

}
