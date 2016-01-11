package com.dzepna_apoteka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaFragment extends Fragment {
MapView mMapView;
GoogleMap googleMap;
View v;
ArrayList<HashMap<String, String>> arraylist;
ArrayList<HashMap<String, String>> al;
JSONArray jsonArray;
JSONObject jsonObject;

@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	v = inflater.inflate(R.layout.fragment_location, container,
            false);
    mMapView = (MapView) v.findViewById(R.id.mapView);
    mMapView.onCreate(savedInstanceState);

    mMapView.onResume();

    try {
        MapsInitializer.initialize(getActivity().getApplicationContext());
    } catch (Exception e) {
        e.printStackTrace();
    }

    googleMap = mMapView.getMap();
    googleMap.setMyLocationEnabled(true);
    
    LocationManager locationManager = (LocationManager) v.getContext().getSystemService(Context.LOCATION_SERVICE);
    Criteria criteria = new Criteria();

    Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
    if (location != null)
    {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 13));
        

        CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
        .zoom(17)                   // Sets the zoom
        //.bearing(90)                // Sets the orientation of the camera to east
       // .tilt(40)                   // Sets the tilt of the camera to 30 degrees
        .build();                   // Creates a CameraPosition from the builder
    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
   
    addPharmacymarkers();
    // latitude and longitude
 /*   double latitude = 17.385044;
    double longitude = 78.486671;*/

/*    // create marker
    MarkerOptions marker = new MarkerOptions().position(
            new LatLng(latitude, longitude)).title("Hello Maps");

    // Changing marker icon
    marker.icon(BitmapDescriptorFactory
            .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

    // adding marker
    googleMap.addMarker(marker);
    CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(new LatLng(17.385044, 78.486671)).zoom(12).build();
    googleMap.animateCamera(CameraUpdateFactory
            .newCameraPosition(cameraPosition));*/

    // Perform any camera updates here
   

    return v;
}

private void addPharmacymarkers() {
	try {
		al= new DownloadMarkers().execute().get();
		HashMap<String,String> pom;
		for(int i=0;i<al.size();i++){
			pom=al.get(i);
	    googleMap.addMarker(new MarkerOptions()
	        .title(pom.get("name"))
	        .snippet(pom.get("address")+"\n"+pom.get("workingTime"))
	        .position(new LatLng(
	               Double.parseDouble(pom.get("xcoords")),
	               Double.parseDouble(pom.get("ycoords"))
	         ))
	         .icon(BitmapDescriptorFactory
	                 .defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
	    );
		}
	} catch (InterruptedException e) {
		
		e.printStackTrace();
	} catch (ExecutionException e) {
		
		e.printStackTrace();
	}
}

@Override
public void onResume() {
    super.onResume();
    mMapView.onResume();
    addPharmacymarkers();
}

@Override
public void onPause() {
    super.onPause();
    mMapView.onPause();
}

@Override
public void onDestroy() {
    super.onDestroy();
    mMapView.onDestroy();
}

@Override
public void onLowMemory() {
    super.onLowMemory();
    mMapView.onLowMemory();
}

private class DownloadMarkers extends AsyncTask<Void, Void, ArrayList<HashMap<String,String>>>{

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// Create a progressdialog
		/*mProgressDialog = new ProgressDialog(rootView.getContext());
		// Set progressdialog title
		mProgressDialog.setTitle("Učitavanje");
		// Set progressdialog message
		mProgressDialog.setMessage("Sadržaj se učitava");
		mProgressDialog.setIndeterminate(false);
		// Show progressdialog
		mProgressDialog.show();*/
	}

	@Override
	protected ArrayList<HashMap<String,String>> doInBackground(Void... params) {
		// Create an array
		arraylist = new ArrayList<HashMap<String, String>>();
		// Retrieve JSON Objects from the given URL address
		jsonArray = com.dzepna_apoteka.logic.JSONfunctions
				.getJSONfromURL("http://boiling-ravine-2299.herokuapp.com/pharmacies");
		//Log.e("Darko", "jsonarray pre for petlje: " + jsonarray.toString());
		

		try {
			for(int i=0;i<jsonArray.length();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				jsonObject = jsonArray.getJSONObject(i);
				
				map.put("name", jsonObject.getString("name"));
				map.put("address", jsonObject.getString("address"));
				/*map.put("county", jsonObject.getString("county"));
				map.put("phones", jsonObject.getString("phones"));*/
				//map.put("county", jsonobject.getString("county"));
				map.put("workingTime", jsonObject.getString("workingTime"));
				map.put("xcoords", jsonObject.getString("xcoords"));
				map.put("ycoords", jsonObject.getString("ycoords"));
				//map.put("takesPerscriptions", String.valueOf(jsonobject.getBoolean("takesPerscriptions")));
				
				
				arraylist.add(map);
			}
				
			//Collections.sort(arraylist, new MapComparator("name"));

		} catch (JSONException e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return arraylist;
	}

	
}
}
