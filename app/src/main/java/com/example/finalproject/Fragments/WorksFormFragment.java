package com.example.finalproject.Fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.finalproject.Adapters.WorkItemAdapter;
import com.example.finalproject.Models.WorkItem;
import com.example.finalproject.R;
import com.example.finalproject.ViewModels.WorksFormViewModel;
import java.util.Map;


public class WorksFormFragment extends Fragment {

    private WorksFormViewModel viewModel;
    private WorkItemAdapter adapter;

    private RecyclerView mainLSTWorks;
    private Observer<Map<Integer, WorkItem>> observer = new Observer<Map<Integer, WorkItem>>() {
        @Override
        public void onChanged(Map<Integer, WorkItem> workNumToWorkItemMap) {
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
        viewModel = new ViewModelProvider(this).get(WorksFormViewModel.class);

        createLinearLayout();

        adapter = new WorkItemAdapter(getContext(), viewModel.getWorkItemsMap().getValue());
        mainLSTWorks.setAdapter(adapter);
        viewModel.getWorkItemsMap().observe(getViewLifecycleOwner(), observer);

        return root;
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
    }
}