package com.example.finalproject.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DropDownFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DropDownFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Integer array_res_id;
    private Spinner dropDown;
    public DropDownFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            array_res_id = getArguments().getInt("array_res_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_drop_down, container, false);
        findViews(root);
        // Inflate the layout for this fragment
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), array_res_id, android.R.layout.simple_spinner_dropdown_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // Apply the adapter to the spinner
        dropDown.setAdapter(adapter);
        dropDown.setOnItemSelectedListener(this);


        return root;
    }

    private void findViews(View view){
        dropDown = view.findViewById(R.id.drop_down);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        Toast.makeText(getContext(), "Selected item: " + selectedItem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}