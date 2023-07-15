package com.example.finalproject.Models;

import android.content.Context;

import com.example.finalproject.Utility.Helper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class PlaceApi {

    private Helper helper = new Helper();
    public ArrayList<String> autoComplete(String input){
        ArrayList<String> arrayList = new ArrayList();
        HttpURLConnection connection = null;
        StringBuilder jsonResult = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?");
            sb.append("input=" + input);
            String key = /*helper.getValueByKeyFromLocalProperties("PLACES_API_KEY");*/ "AIzaSyB5L_pketmzC7_WK67nWH32-tglqOmVCYA";
            sb.append("&key=" + key);
            URL url = new URL(sb.toString());
            connection = (HttpURLConnection)url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader((connection.getInputStream()));

            int read;

            char[] buff = new char[1024];
            while ((read=inputStreamReader.read(buff)) != -1){
                jsonResult.append(buff, 0, read);
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(connection != null){
                connection.disconnect();
            }
        }
        try{
            JSONObject jsonObject = new JSONObject(jsonResult.toString());
            JSONArray prediction = jsonObject.getJSONArray("predictions");

            for(int i=0; i<prediction.length(); i++){
                arrayList.add(prediction.getJSONObject(i).getString("description"));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public void addMarkerByPlaceId(Context context, GoogleMap googleMap, String placeId){
        PlacesClient placesClient = Places.createClient(context);
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, Arrays.asList(Place.Field.LAT_LNG));
        LatLng latLng2;
        // Fetch the place details
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            LatLng latLng = place.getLatLng();
            // Add a marker to the GoogleMap object
            googleMap.addMarker(new MarkerOptions().position(latLng));
        }).addOnFailureListener((exception) -> {
            // Handle the error
            exception.printStackTrace();
        });
    }
}
