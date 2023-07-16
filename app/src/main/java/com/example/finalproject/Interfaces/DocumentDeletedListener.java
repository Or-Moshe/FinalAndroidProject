package com.example.finalproject.Interfaces;

public interface DocumentDeletedListener {

    void onDocumentDeleted(String documentId);
    void onDocumentDeletionFailed(Exception e);
}
