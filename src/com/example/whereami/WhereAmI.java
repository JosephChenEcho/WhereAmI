package com.example.whereami;

import java.io.IOException;
import java.util.*;

import com.google.android.maps.*;

import android.location.*;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.*;

@SuppressLint("NewApi")
public class WhereAmI extends MapActivity {
	
	private final LocationListener locationListener = new LocationListener(){

		@Override
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);			
		}

		@Override
		public void onProviderDisabled(String provider) {
			updateWithNewLocation(null);
		}

		@Override
		public void onProviderEnabled(String provider) {
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		
	};
	
	MapController mapController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); //For API
		
		// Get a reference to the MapView
		MapView myMapView = (MapView)findViewById(R.id.myMapView);
		// Get the MapView's controller
		mapController = myMapView.getController();
		
		// Configure the map display options
		myMapView.setSatellite(true);
		myMapView.setStreetView(true);
		myMapView.displayZoomControls(false);
		
		// Zoom in
		mapController.setZoom(17);
		
		LocationManager locationManager;
		String context= Context.LOCATION_SERVICE;
		locationManager = (LocationManager)getSystemService(context);
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		
		String provider = locationManager.getBestProvider(criteria,true);
		Location location = locationManager.getLastKnownLocation(provider);
		
		updateWithNewLocation(location);
		
		locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.where_am_i, menu);
		return true;
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	private void updateWithNewLocation(Location location){
		String latLongString;
		TextView myLocationText;
		myLocationText = (TextView)findViewById(R.id.myLocationText);
		
		String addressString = "No address found";
		
		if (location != null){
			
			// Update the map location
			
			Double geoLat = location.getLatitude()*1E6;
			Double geoLng = location.getLongitude()*1E6;
			GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue());
			
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			latLongString = "Lat: " + lat + " \nLong : " + lng ;
			
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			
			Geocoder gc = new Geocoder(this, Locale.getDefault());
			try{
				List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
				StringBuilder sb = new StringBuilder();
				if (addresses.size() > 0){
					Address address = addresses.get(0);
					
					for (int i = 0; i <address.getMaxAddressLineIndex();i++){
						sb.append(address.getAddressLine(i)).append("\n");
						sb.append(address.getLocality()).append("\n");
						sb.append(address.getPostalCode()).append("\n");
						sb.append(address.getCountryName()).append("\n");
					}
					addressString = sb.toString();
				}
			}
			catch (IOException e){}
		}
		else
		{
			latLongString = "No location found ";
		}
		myLocationText.setText("Your Current Position is : \n " + latLongString + "\n" + addressString);
	}





}
