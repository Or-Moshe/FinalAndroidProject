package com.example.finalproject.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Models.Customer;
import com.example.finalproject.Models.WorkItem;
import com.example.finalproject.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Map;

public class WorkItemAdapter extends RecyclerView.Adapter<WorkItemAdapter.WorkViewHolder> {

    private Context context;
    private Map<Integer, WorkItem> workItemsMap;

    public WorkItemAdapter(Context context, Map<Integer, WorkItem> workItemsMap){
        this.context = context;
        this.workItemsMap = workItemsMap;
    }

    @NonNull
    @Override
    public WorkItemAdapter.WorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("passed VT:", "" + viewType);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_item, parent, false);
        return new WorkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkViewHolder holder, int position) {
        WorkItem workItem = getItem(position);
        Customer customer = workItem.getCustomer();
        if(customer != null){
            holder.customer_name.setText(customer.getName());
            holder.phone.setText(customer.getPhone());
        }
        holder.address.setText(workItem.getAddress() + "");
        //holder.duration_estimated.setText(workItem.getDuration_estimated() + "");
    }

    public void updateWorkItemsMap(/*ArrayList<WorkItem> workItems*/ Map<Integer, WorkItem> workItemsMap) {
        //this.workItems = workItems;
        this.workItemsMap = workItemsMap;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        //return this.workItems == null ? 0 : workItems.size();
        return this.workItemsMap == null ? 0 : workItemsMap.size();
    }

    private WorkItem getItem(int position) {
        //return this.workItems.get(position);
        return this.workItemsMap.get(position);
    }

    public class WorkViewHolder extends RecyclerView.ViewHolder {

        private MaterialTextView customer_name;
        private MaterialTextView address;
        private MaterialTextView phone;
        private MaterialTextView duration_estimated;

        public WorkViewHolder(@NonNull View itemView) {
            super(itemView);
            customer_name = itemView.findViewById(R.id.customer_name);
            address = itemView.findViewById(R.id.address);
            phone = itemView.findViewById(R.id.phone);
            duration_estimated = itemView.findViewById(R.id.duration_estimated);
        }
    }
}
