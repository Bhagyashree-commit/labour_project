package com.example.labourmangement.Contractor;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.example.labourmangement.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.android.volley.VolleyLog.TAG;
import static com.example.labourmangement.Contractor.TrackLabor.idList;
import static com.example.labourmangement.Contractor.TrackLabor.lat;
import static com.example.labourmangement.Contractor.TrackLabor.lng;
import static com.example.labourmangement.Contractor.TrackLabor.locationList;
import static com.example.labourmangement.Contractor.TrackLabor.mArrayList;

public class MapsActivityTrackLabor extends FragmentActivity implements OnMapReadyCallback{
    private static final String TAG = MapsActivityTrackLabor.class.getSimpleName();
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    String straddress;
    LatLngBounds.Builder builder;
    CameraUpdate cu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_track_labor);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
       // String labor_id = intent.getStringExtra("labor_id");
        straddress = intent.getStringExtra("lastaddress");
        //Log.d(TAG, "labor_id " + labor_id);
        Log.d(TAG, "Ttrack Address " + mArrayList.toString());
        //convertAddress();




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mSetUpMap();

         for(int i = 0 ; i < mArrayList.size() ; i++) {

           //createMarker(Double.parseDouble(mArrayList.get(i).get("lat")),Double.parseDouble( mArrayList.get(i).get("lng")));
        }

        try {
            mMap.setMyLocationEnabled(true);

        } catch (SecurityException se) {

        }

        //Edit the following as per you needs
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);



        }


    public void mSetUpMap() {
        /**clear the map before redraw to them*/
        mMap.clear();
        /**Create dummy Markers List*/
        List<Marker> markersList = new ArrayList<Marker>();

        Log.d("Rashmiiiiii",""+mArrayList);

        for(int i = 0 ; i < mArrayList.size() ; i++) {
            Marker location = mMap.addMarker(new MarkerOptions().position(new LatLng(
                    Double.parseDouble(mArrayList.get(i).get("lat")), Double.parseDouble(mArrayList.get(i).get("lng")))).title(mArrayList.get(i).get("name")));
            markersList.add(location);

        }


        builder = new LatLngBounds.Builder();
        for (Marker m : markersList) {
            builder.include(m.getPosition());
        }
        /**initialize the padding for map boundary*/
        int padding = 50;
        /**create the bounds from latlngBuilder to set into map camera*/
        LatLngBounds bounds = builder.build();
        /**create the camera with bounds and padding to set into map*/
        cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        /**call the map call back to know map is loaded or not*/
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                /**set animated zoom camera into map*/
                mMap.animateCamera(cu);

            }
        });
    }
}
