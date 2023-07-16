package com.example.finalproject.Fragments;

import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.finalproject.Adapters.CustomerAdapter;
import com.example.finalproject.Adapters.PlaceAutoSuggestAdapter;
import com.example.finalproject.DataManager;
import com.example.finalproject.Interfaces.DocumentCreatedListener;
import com.example.finalproject.Models.Customer;
import com.example.finalproject.Models.WorkItem;
import com.example.finalproject.R;
import com.example.finalproject.Utility.Constants;
import com.example.finalproject.Utility.Helper;
import com.example.finalproject.Utility.RegexUtils;
import com.example.finalproject.ViewModels.NewWorkFormViewModel;
import com.example.finalproject.ViewModels.WorksFormViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.search.SearchBar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class NewWorkFormFragment extends Fragment{

    private NewWorkFormViewModel mViewModel;
    private Helper helper;

    private LinearLayout llNewCustomer, llSearchCustomer;
    private Spinner workTypeDropdown, phoneDropdown;
    private MaterialButton submitBtn;

    private SearchView searchView;
    private CardView customerSearchCard;
    private CustomerAdapter customerAdapter;
    private RecyclerView customerRecyclerView;
    private SwitchMaterial sectionSwitch;

    private AutoCompleteTextView autoCompleteLocation;
    // form values
    private TextInputEditText newCustomerNameEditText, priceEditText, commentEditText, phoneEditText, hoursEditText;
    private String typeOfWork, phonePrefix;
    private NumberPicker hoursOfWorkPicker, minutesOfWorkPicker;
    private Boolean isNewCustomer = false;

    private WorkItem workItem;

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
        helper.setDropDown(getResources(), R.array.phone_array, getContext(), phoneDropdown, android.R.layout.simple_spinner_dropdown_item);

        Map<String, Customer> customerMap = DataManager.getInstance().getCustomerMap();
        Log.d("TAG", "customerMap: " + customerMap);
        // Create the adapter for the customer list
        customerAdapter = new CustomerAdapter(searchView, customerRecyclerView, customerMap);
        customerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        customerRecyclerView.setAdapter(customerAdapter);

        autoCompleteLocation.setAdapter(new PlaceAutoSuggestAdapter(getActivity(), android.R.layout.simple_list_item_1));

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customerSearchCard.getVisibility() == View.GONE) {
                    customerSearchCard.setVisibility(View.VISIBLE);
                } else {
                    customerSearchCard.setVisibility(View.GONE);
                }
            }
        });
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

        sectionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llNewCustomer.setVisibility(View.VISIBLE);
                    llSearchCustomer.setVisibility(View.GONE);
                    isNewCustomer = true;
                }
                else{
                    llNewCustomer.setVisibility(View.GONE);
                    llSearchCustomer.setVisibility(View.VISIBLE);
                    isNewCustomer = false;
                }
            }
        });

        setSpinnersListeners();
        setBtnsListeners();

        return root;
    }

    private void setBtnsListeners() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateForm()){
                    Toast.makeText(getContext(), getString(R.string.form_not_valid), Toast.LENGTH_SHORT).show();
                    return;
                }
                createWoFromForm();
                DataManager.getInstance().addNewDocument(workItem, new DocumentCreatedListener() {
                    @Override
                    public void onDocumentCreated(String documentId) {
                        Log.d("TAG", "saved to document success : " + workItem);
                        requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout, new WorksFormFragment())
                            .commit();
                    }

                    @Override
                    public void onDocumentCreationFailed(Exception e) {
                        Log.e("TAG", "onDocumentCreationFailed: "+ e.getMessage());
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

    private Boolean validateForm(){
        if(hoursEditText.getText().toString().isEmpty()){
            return false;
        }
        if(priceEditText.getText().toString().isEmpty()){
            return false;
        }
        if(isNewCustomer){
            if(phoneEditText.getText().toString().isEmpty() || newCustomerNameEditText.getText().toString().isEmpty() ||autoCompleteLocation.getText().toString().isEmpty()){
                return false;
            }
        }
        else{
            if(customerAdapter.getSelectedCustomer() == null){
                return false;
            }
        }
        return true;
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

    private void createWoFromForm(){
        //int hoursOfWorkVal = hoursOfWorkPicker.getValue();
        //int minutesOfWorkVal = minutesOfWorkPicker.getValue();
        String hoursStr = hoursEditText.getText().toString();
        //double timeOfWork = hoursOfWorkVal * 10.0 + minutesOfWorkVal;
        String priceStr = priceEditText.getText().toString();
        double priceVal = !priceStr.isEmpty() ? Double.parseDouble(priceStr): 0;
        Customer customer;
        Address address;
        if(isNewCustomer){
            String phone = phonePrefix + phoneEditText.getText().toString();
            String customerName = newCustomerNameEditText.getText().toString();
            String addressStr = autoCompleteLocation.getText().toString();
            customer = new Customer(customerName, phone, addressStr, 0);

        }
        else{
            customer = customerAdapter.getSelectedCustomer();
        }

        String comment = commentEditText.getText().toString();
        workItem = new WorkItem(/*DataManager.getInstance().getWorkItemsMap().size(),*/ null, typeOfWork, hoursStr, priceVal, customer, null, comment);
    }

    private void findViews(View view){
        //hoursOfWorkPicker = view.findViewById(R.id.hours_picker);
        //minutesOfWorkPicker = view.findViewById(R.id.minutes_picker);
        hoursEditText = view.findViewById(R.id.hours_input);
        priceEditText = view.findViewById(R.id.input_price);
        newCustomerNameEditText = view.findViewById(R.id.input_customer_name);
        llNewCustomer = view.findViewById(R.id.ll_new_customer);
        workTypeDropdown = view.findViewById(R.id.work_type_dropdown);
        phoneDropdown = view.findViewById(R.id.phone_dropdown);
        phoneEditText = view.findViewById(R.id.input_phone);
        submitBtn = view.findViewById(R.id.submit);
        llSearchCustomer = view.findViewById(R.id.ll_search_customer);
        customerRecyclerView = view.findViewById(R.id.customerRecyclerView);
        searchView = view.findViewById(R.id.searchView);
        customerSearchCard = view.findViewById(R.id.customer_search_card);
        commentEditText = view.findViewById(R.id.input_comment);
        sectionSwitch = view.findViewById(R.id.sectionSwitch);
        autoCompleteLocation = view.findViewById(R.id.autoCompleteLocation);
    }

}