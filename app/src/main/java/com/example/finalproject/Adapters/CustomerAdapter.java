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

import com.example.finalproject.Models.Customer;

import java.util.ArrayList;
import java.util.Map;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> implements Filterable {

    private Map<String, Customer> customerMap;
    private ArrayList<Customer> filteredList;
    private SearchView searchView;
    private RecyclerView customerRecyclerView;
    private Customer selectedCustomer;

    public CustomerAdapter(SearchView searchView, RecyclerView customerRecyclerView, Map<String, Customer> customerMap) {
        this.customerMap = customerMap;
        this.filteredList = new ArrayList<>(customerMap.values());
        this.searchView = searchView;
        this.customerRecyclerView = customerRecyclerView;
    }

    public Customer getSelectedCustomer(){
        return selectedCustomer;
    }

    @NonNull
    @Override
    public CustomerAdapter.CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new CustomerViewHolder(view, searchView, customerRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = filteredList.get(position);
        holder.bind(customer);
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
                ArrayList<Customer> filteredResults = new ArrayList<>();

                for (Customer customer : customerMap.values()) {
                    if (customer.getName().toLowerCase().startsWith(query)) {
                        filteredResults.add(customer);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredResults;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<Customer>) results.values;
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

        public void bind(Customer customer) {
            customerNameTextView.setText(customer.getName());
        }

        @Override
        public void onClick(View v) {
            // Handle item click here
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                selectedCustomer = filteredList.get(position);
                // Perform any action on the selected customer
                searchView.setQuery(selectedCustomer.getName(), true);
                customerRecyclerView.setVisibility(View.GONE);
            }
        }
    }
}
