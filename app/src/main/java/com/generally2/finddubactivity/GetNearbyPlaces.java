package com.generally2.finddubactivity;


import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlaces extends AsyncTask<Object, String, String> {
    private String googlePlaceData, url;
    private GoogleMap mMap;


    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadURL downloadURL = new DownloadURL();

        try {
            googlePlaceData = downloadURL.ReadURL(url);
        }
        catch (IOException e){
            e.printStackTrace();

        }
        return googlePlaceData;
    }

    protected void onPostExecute(String s){
        List<HashMap<String, String>> nearbyPlacesList;
        DataParser dataParser = new DataParser();
        nearbyPlacesList = dataParser.parse(s);

        DisplayNearbyPlaces(nearbyPlacesList);

    }

    private void DisplayNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList){

        for (int i = 0; i < nearbyPlaceList.size(); i++){
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googleNearbyPlaces = nearbyPlaceList.get(i);
            String nameOfPlace = googleNearbyPlaces.get("place_name");
            String vicinity = googleNearbyPlaces.get("vicinity");

            Double lat = Double.parseDouble(googleNearbyPlaces.get("lat"));
            Double lng = Double.parseDouble(googleNearbyPlaces.get("lng"));

            LatLng latLng = new LatLng(lat, lng);

            markerOptions.position(latLng);
            markerOptions.title(nameOfPlace + " " + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker));

            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));






        }

    }
}
