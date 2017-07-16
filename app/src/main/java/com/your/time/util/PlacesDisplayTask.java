package com.your.time.util;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.your.time.bean.Places;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {

    JSONObject googlePlacesJson;
    GoogleMap googleMap;
    Map<String,Object> param;

    public PlacesDisplayTask(Map<String,Object> param){
        this.param = param;
    }

    public PlacesDisplayTask() {
    }

    @Override
    protected List<HashMap<String, String>> doInBackground(Object... inputObj) {

        List<HashMap<String, String>> googlePlacesList = null;
        Places placeJsonParser = new Places();

        try {
            googleMap = (GoogleMap) inputObj[0];
            googlePlacesJson = new JSONObject((String) inputObj[1]);
            googlePlacesList = placeJsonParser.parse(googlePlacesJson);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return googlePlacesList;
    }

    @Override
    protected void onPostExecute(List<HashMap<String, String>> list) {
        googleMap.clear();
        for (int i = 0; i < list.size(); i++) {
            HashMap<String, String> googlePlace = list.get(i);
            //for (String key : googlePlace.keySet()) {
                //String val = (String)param.get(key);
                //if (googlePlace.get(key) != null && val != null && googlePlace.get(key).contains(val.toLowerCase())){
                    MarkerOptions markerOptions = new MarkerOptions();
                    double lat = Double.parseDouble(googlePlace.get("lat"));
                    double lng = Double.parseDouble(googlePlace.get("lng"));
                    String placeName = googlePlace.get("name");
                    String vicinity = googlePlace.get("address");
                    LatLng latLng = new LatLng(lat, lng);
                    markerOptions.position(latLng);
                    markerOptions.title(placeName + " : " + vicinity);
                    markerOptions.snippet("9m");
                    //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                    googleMap.addMarker(markerOptions);
                //}
            //}
        }
    }
}