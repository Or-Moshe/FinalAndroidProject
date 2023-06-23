package com.example.finalproject.Fragments;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.res.Resources;
import android.location.Address;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.example.finalproject.Adapters.CustomerAdapter;
import com.example.finalproject.DataManager;
import com.example.finalproject.Models.Customer;
import com.example.finalproject.Models.WorkItem;
import com.example.finalproject.R;
import com.example.finalproject.Utility.Constants;
import com.example.finalproject.Utility.Helper;
import com.example.finalproject.ViewModels.NewWorkFormViewModel;
import com.example.finalproject.ViewModels.WorksFormViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.search.SearchBar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class NewWorkFormFragment extends Fragment {

    private NewWorkFormViewModel mViewModel;
    private FrameLayout workTypeDropdownFrameLayout;
    private Helper helper;

    private MaterialCheckBox newCusCheckbox;
    private LinearLayout llNewCustomer, llSearchCustomer;
    private Spinner workTypeDropdown, phoneDropdown;
    private MaterialButton submitBtn;

    private SearchView searchView;
    private CustomerAdapter customerAdapter;
    private RecyclerView customerRecyclerView;

    // form values
    private TextInputEditText newCustomerNameEditText, streetEditText,
            input_street_number, priceEditText, commentEditText, phoneEditText;
    private String typeOfWork, phonePrefix;
    private NumberPicker hoursOfWorkPicker, minutesOfWorkPicker;
    private Boolean isNewCustomer = false;

    public NewWorkFormFragment(){
        helper = new Helper();
    }

    public static NewWorkFormFragment newInstance() {
        return new NewWorkFormFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_new_work_form, container, false);
        Log.d("TAG", "onCreateView: ");
        findViews(root);

        helper.setDropDown(getResources(), R.array.work_types_array, getContext(), workTypeDropdown, android.R.layout.simple_spinner_dropdown_item);
        helper.setDropDown(getResources(), R.array.work_types_array, getContext(), phoneDropdown, android.R.layout.simple_spinner_dropdown_item);

        Map<String, Customer> customerMap = DataManager.getInstance().getCustomerMap();

        // Create the adapter for the customer list
        customerAdapter = new CustomerAdapter(searchView, customerRecyclerView, customerMap);
        customerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        customerRecyclerView.setAdapter(customerAdapter);

        Customer customer = customerAdapter.getSelectedCustomer();
        // Set a listener for the search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                customerRecyclerView.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the customer list based on the user's input
                customerRecyclerView.setVisibility(View.VISIBLE);
                customerAdapter.getFilter().filter(newText);
                return true;
            }
        });

        setSpinnersListeners();
        setCheckboxesListeners();
        setBtnsListeners();

        return root;
    }

    private void setBtnsListeners() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //typeOfWork
                int hoursOfWorkVal = hoursOfWorkPicker.getValue();
                int minutesOfWorkVal = minutesOfWorkPicker.getValue();
                double timeOfWork = hoursOfWorkVal * 10.0 + minutesOfWorkVal;
                String priceVal = priceEditText.getText().toString();
                Customer customer;
                Address address;
                if(isNewCustomer){
                    String phone = phonePrefix + phoneEditText.getText().toString();
                    String street = streetEditText.getText().toString();
                    String streetNumber = input_street_number.getText().toString();
                    customer = new Customer(newCustomerNameEditText.getText().toString(), phone, 0);
                }
                else{
                    customer = customerAdapter.getSelectedCustomer();
                }

                String comment = commentEditText.getText().toString();

                WorkItem workItem = new WorkItem(0, null, typeOfWork, timeOfWork, 0, customer, null, comment);
                    // Assume we have a reference to the parent document
                DocumentReference parentDocumentRef = db.collection(Constants.DBKeys.USERS).document("gYkdDsk6c7Wk8ADQ3gZYs4Ovujx2");

                // Create a reference to the collection
                CollectionReference collectionRef = parentDocumentRef.collection(Constants.DBKeys.WORK_ITEMS);

                // Set data to the collection

                // Create a new document in the collection and set data
                collectionRef.document(workItem.getId()+"").set(workItem)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Data set to the subcollection successfully
                                Log.d("TAG", "saved to document success : " + workItem);
                                requireActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.frame_layout, new WorksFormFragment())
                                        .commit();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Failed to set data to the subcollection
                                Log.e("TAG", "saved to document failed : " + workItem);
                            }
                        });
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NewWorkFormViewModel.class);
        // TODO: Use the ViewModel

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "onCreate: ");
    }

    private void setSpinnersListeners() {
        workTypeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeOfWork = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when no item is selected
            }
        });

        phoneDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                phonePrefix = (String) parent.getItemAtPosition(position);
                // Handle the selected item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when no item is selected
            }
        });
    }

    private void setCheckboxesListeners() {
        newCusCheckbox.addOnCheckedStateChangedListener(new MaterialCheckBox.OnCheckedStateChangedListener() {
            @Override
            public void onCheckedStateChangedListener(@NonNull MaterialCheckBox checkBox, int state) {
                if(state == 1){
                    llNewCustomer.setVisibility(View.VISIBLE);
                    llSearchCustomer.setVisibility(View.GONE);
                    isNewCustomer = true;
                }
                else{
                    llNewCustomer.setVisibility(View.GONE);
                    llSearchCustomer.setVisibility(View.GONE);
                    isNewCustomer = false;
                }
            }
        });
    }

    /*@Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.search, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("type here");

        listView.setAdapter(arrayAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                arrayAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }*/



    private void findViews(View view){
        //workTypeDropdownFrameLayout = view.findViewById(R.id.work_type_dropdown_frame_layout);
        /*search = (SearchView) view.findViewById(R.id.search);
        listView = (ListView) view.findViewById(R.id.list_view);*/
        hoursOfWorkPicker = view.findViewById(R.id.hours_picker);
        minutesOfWorkPicker = view.findViewById(R.id.minutes_picker);
        priceEditText = view.findViewById(R.id.input_price);
        newCusCheckbox = view.findViewById(R.id.new_customer_checkbox);
        newCustomerNameEditText = view.findViewById(R.id.input_customer_name);
        streetEditText = view.findViewById(R.id.input_street);
        input_street_number = view.findViewById(R.id.input_street);
        llNewCustomer = view.findViewById(R.id.ll_new_customer);
        workTypeDropdown = view.findViewById(R.id.work_type_dropdown);
        phoneDropdown = view.findViewById(R.id.phone_dropdown);
        phoneEditText = view.findViewById(R.id.input_phone);
        submitBtn = view.findViewById(R.id.submit);
        llSearchCustomer = view.findViewById(R.id.ll_search_customer);
        customerRecyclerView = view.findViewById(R.id.customerRecyclerView);
        searchView = view.findViewById(R.id.searchView);
        commentEditText = view.findViewById(R.id.input_comment);
    }

}