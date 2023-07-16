package com.example.finalproject.Interfaces;

public interface DocumentCreatedListener {

    void onDocumentCreated(String documentId);
    void onDocumentCreationFailed(Exception e);
}
