package com.finalproject21.chooseyourfavoriteplacetotravel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                }
            }
        }
    }
    public void displayMapLocation(Location location,String title){
        if(location != null) {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        Intent intent = getIntent();
        Log.i("NumberIdPlace passed through Activity(Process 1) :", "" + Integer.toString(intent.getIntExtra("locationId",0)));
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (intent.getIntExtra("locationId",0) == 0) {
            mMap.setOnMapLongClickListener(this);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                displayMapLocation(lastKnownLocation, "Your location updated");

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }else {
            Location placeLocation = new Location(LocationManager.GPS_PROVIDER);
            placeLocation.setLatitude(MainActivity.locations.get(intent.getIntExtra("locationId",0)).latitude);
            placeLocation.setLongitude(MainActivity.locations.get(intent.getIntExtra("locationId",0)).longitude);
            displayMapLocation(placeLocation, MainActivity.addedLocations.get(intent.getIntExtra("locationId",0)));
        }

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        String address = "";
        /*
        D/Addresses getAdminArea()﹕ Florida
        D/Addresses getCountryCode()﹕ US
        D/Addresses getCountryName()﹕ Estados Unidos
        D/Addresses getFeatureName()﹕ 7500
        D/Addresses getLocality()﹕ Miami
        D/Addresses getPostalCode()﹕ 33156
        D/Addresses getPremises()﹕ null
        D/Addresses getSubAdminArea()﹕ null
        D/Addresses getSubLocality()﹕ null
        D/Addresses getSubThoroughfare()﹕ 7500
        D/Addresses getThoroughfare()﹕ SW 120th St
         */
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try{
            List<Address> listAddresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);

            if(listAddresses != null && listAddresses.size() >0 ){
                if(listAddresses.get(0).getThoroughfare() != null){
                    if(listAddresses.get(0).getSubThoroughfare() != null){
                        address += listAddresses.get(0).getSubThoroughfare() + "-" ;
                    }
                    address += listAddresses.get(0).getThoroughfare()+" - " + listAddresses.get(0).getAdminArea() ;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        if (address.equals("")){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm yyyy-MM-dd");
            address += simpleDateFormat.format(new Date());
        }
        mMap.addMarker(new MarkerOptions().position(latLng).title(address));
        MainActivity.addedLocations.add(address);
        MainActivity.locations.add(latLng);
        MainActivity.arrayAdapter.notifyDataSetChanged();
    }
}
