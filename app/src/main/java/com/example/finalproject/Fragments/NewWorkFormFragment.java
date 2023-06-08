package com.example.finalproject.Fragments;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import android.widget.ListView;
import android.widget.Spinner;

import com.example.finalproject.R;
import com.example.finalproject.ViewModels.NewWorkFormViewModel;
import com.google.android.material.search.SearchBar;

public class NewWorkFormFragment extends Fragment {

    private NewWorkFormViewModel mViewModel;
    private FrameLayout workTypeDropdownFrameLayout;

    /*SearchView search;
    private ListView listView;
    private String[] names = {"aaa", "vvv", "xxx"};
    private ArrayAdapter<String> arrayAdapter;*/

    public static NewWorkFormFragment newInstance() {
        return new NewWorkFormFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_new_work_form, container, false);
        Log.d("TAG", "onCreateView: ");
        findViews(root);

        //arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, names);

        DropDownFragment dropDownFragment = new DropDownFragment();
        Bundle args = new Bundle();
        args.putInt("array_res_id", R.array.work_types_array);
        args.putInt("key_argument2", R.layout.fragment_drop_down);
        dropDownFragment.setArguments(args);

       // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.work_type_dropdown_frame_layout, dropDownFragment).commit();
//
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.work_types_array, android.R.layout.simple_spinner_item);
//
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//
//        // Apply the adapter to the spinner
//        workTypeDropdown.setAdapter(adapter);
//        workTypeDropdown.setOnItemSelectedListener(this);
        //search.setOnQueryTextListener(get);
        return root;
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

    private void findViews(View view){
        //workTypeDropdownFrameLayout = view.findViewById(R.id.work_type_dropdown_frame_layout);
        /*search = (SearchView) view.findViewById(R.id.search);
        listView = (ListView) view.findViewById(R.id.list_view);*/
    }

}