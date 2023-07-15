package com.example.finalproject.Utility;


import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.finalproject.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Helper  {

    public void setDropDown(Resources resources, Integer arrayId, Context context, Spinner dropDown, Integer dropDownViewResource){
        String[] items = resources.getStringArray(arrayId);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(dropDownViewResource);
        dropDown.setAdapter(adapter);
    }

    public Boolean validateEmail(TextInputEditText email, String value, String errorMsg){
        if (!RegexUtils.isValidEmail(value)) {
            email.setError(errorMsg);
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }
    public Boolean validatePassword(TextInputEditText password, String value, Integer minLen, String errorMsg){
        if (value.length() < minLen) {
            password.setError(errorMsg);
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public String getValueByKeyFromLocalProperties(String key){
        Properties properties = new Properties();
        try {
            Log.d("TAG", "getValueByKeyFromLocalProperties: " + System.getProperty("PLACES_API_KEY"));
            FileInputStream fileInputStream = new FileInputStream("local.properties");
            properties.load(fileInputStream);
            if(properties != null){
                return properties.getProperty(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
