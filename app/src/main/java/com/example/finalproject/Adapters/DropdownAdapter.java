package com.example.finalproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.R;

import java.util.List;

public class DropdownAdapter /*extends ArrayAdapter<String>*/ {

   /* private Context context;
    private List<String> items;

    public DropdownAdapter(Context context, List<String> items) {
        super(context, R.layout.dropdown_item, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.dropdown_item, null);
        }

        ImageView icon = view.findViewById(R.id.icon);
        TextView itemText = view.findViewById(R.id.item_text);

        // Set the appropriate icon and text based on the position
        if (position == 0) {
            icon.setImageResource(R.drawable.search_icon);
            itemText.setText("Search");
        } else if (position == 1) {
            icon.setImageResource(R.drawable.arrow_down_icon);
            itemText.setText("Arrow Down");
        }

        return view;
    }*/
}
