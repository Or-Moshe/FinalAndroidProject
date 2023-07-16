package com.example.finalproject.Fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalproject.Adapters.WorkItemAdapter;
import com.example.finalproject.DataManager;
import com.example.finalproject.Interfaces.DocumentDeletedListener;
import com.example.finalproject.Models.WorkItem;
import com.example.finalproject.R;
import com.example.finalproject.ViewModels.WorksFormViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.Map;

public class WorksFormFragment extends Fragment {

    private WorksFormViewModel viewModel;
    private WorkItemAdapter adapter;
    private MaterialButton save_btn;

    private RecyclerView mainLSTWorks;
    private Observer<Map<String, WorkItem>> observer = new Observer<Map<String, WorkItem>>() {
        @Override
        public void onChanged(Map<String, WorkItem> workNumToWorkItemMap) {
            adapter.updateWorkItemsMap(workNumToWorkItemMap);
        }
    } ;

    public static WorksFormFragment newInstance() {
        return new WorksFormFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = (View) inflater.inflate(R.layout.fragment_works_form, container, false);

        findViews(root);
        setBtnsListeners();
        viewModel = new ViewModelProvider(this).get(WorksFormViewModel.class);

        createLinearLayout();

        adapter = new WorkItemAdapter(getContext(), viewModel.getWorkItemsMap().getValue());
        mainLSTWorks.setAdapter(adapter);
        viewModel.getWorkItemsMap().observe(getViewLifecycleOwner(), observer);
        return root;
    }

    private void setBtnsListeners() {
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.getInstance().deleteDocuments(new DocumentDeletedListener() {
                    @Override
                    public void onDocumentDeleted(String documentId) {
                        Log.d("TAG", "onDocumentDeleted: "+ documentId);
                        int index = viewModel.onDocumentDeleted(documentId);
                        adapter.notifyItemRemoved(index);
                    }

                    @Override
                    public void onDocumentDeletionFailed(Exception e) {

                    }
                });
            }
        });
    }
    private void createLinearLayout(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mainLSTWorks.setLayoutManager(linearLayoutManager);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(WorksFormViewModel.class);
        // TODO: Use the ViewModel
    }

    private void findViews(View view){
        mainLSTWorks = view.findViewById(R.id.main_LST_works);
        save_btn = view.findViewById(R.id.save_btn);
    }
}