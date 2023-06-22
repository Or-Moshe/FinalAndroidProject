package com.example.finalproject.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> implements Filterable {

    private ArrayList<String> customerList;
    private ArrayList<String> filteredList;
    private SearchView searchView;
    private RecyclerView customerRecyclerView;

    public CustomerAdapter(SearchView searchView, RecyclerView customerRecyclerView, ArrayList<String> customerList) {
        this.customerList = customerList;
        this.filteredList = new ArrayList<>(customerList);
        this.searchView = searchView;
        this.customerRecyclerView = customerRecyclerView;
    }

    @NonNull
    @Override
    public CustomerAdapter.CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new CustomerViewHolder(view, searchView, customerRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        String customerName = filteredList.get(position);
        holder.bind(customerName);
    }


    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase();
                ArrayList<String> filteredResults = new ArrayList<>();

                for (String customer : customerList) {
                    if (customer.toLowerCase().startsWith(query)) {
                        filteredResults.add(customer);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredResults;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private SearchView searchView;
        private RecyclerView customerRecyclerView;

        private TextView customerNameTextView;
        public CustomerViewHolder(@NonNull View itemView, SearchView searchView, RecyclerView customerRecyclerView) {
            super(itemView);
            customerNameTextView = itemView.findViewById(android.R.id.text1);
            itemView.setOnClickListener(this);
            this.searchView = searchView;
            this.customerRecyclerView = customerRecyclerView;
        }

        public void bind(String customerName) {
            customerNameTextView.setText(customerName);
        }

        @Override
        public void onClick(View v) {
            // Handle item click here
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                String selectedCustomer = filteredList.get(position);
                // Perform any action on the selected customer
                searchView.setQuery(selectedCustomer, true);
                customerRecyclerView.setVisibility(View.GONE);
            }
        }
    }
}
