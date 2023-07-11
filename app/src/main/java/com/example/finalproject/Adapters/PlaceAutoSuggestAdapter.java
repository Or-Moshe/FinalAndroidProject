package com.example.finalproject.Adapters;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;

import com.example.finalproject.Models.PlaceApi;
import com.example.finalproject.Utility.Constants;

import java.util.ArrayList;
import java.util.List;

public class PlaceAutoSuggestAdapter extends ArrayAdapter implements Filterable {

    private ArrayList<String> results;
    private Context context;
    private int resId;
    private PlaceApi placeApi = new PlaceApi();


    public PlaceAutoSuggestAdapter(@NonNull Context context, int resId) {
        super(context, resId);
        this.context = context;
        this.resId = resId;
    }

    @Override
    public int getCount(){
       return results.size();
    }

    @Override
    public String getItem(int pos){
        return results.get(pos);
    }

    @Override
    public Filter getFilter(){
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null){
                    results = placeApi.autoComplete(constraint.toString());

                    filterResults.values = results;
                    filterResults.count = results.size();
                    Log.d("TAG", "performFiltering: " + filterResults.values);
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results != null && results.count > 0){
                    notifyDataSetChanged();
                }
                else{
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}
