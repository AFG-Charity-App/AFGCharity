package com.example.afgcharity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

import java.io.IOException;
import java.util.ArrayList;

/**
 * view locations
 */
public class ViewLocations extends Fragment {
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<Address> Locations;
    private GoogleMap map;
    private View view;
    private String user;

    /**
     * create location view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_locations, container, false);

        user=getArguments().getString("User");


        Locations = new ArrayList<>();
        MapView mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            /**
             * updates the map
             * @param googleMap
             */
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                getLocations();

                mapView.onResume();
            }
        });

        return view;

    }
    /**
     * gets locations
     */
    public void getLocations() {
        reference.child("users").child(user).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Locations = new ArrayList<Address>();
                        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                                .apiKey("AIzaSyAmgVnIMQGig2SgDBm8GOXLKfId6tJHzHY")
                                .build();
                        for (DataSnapshot dsp : dataSnapshot.child("Locations").getChildren()) {
                            GeocodingResult[] results = GeocodingApi
                                    .geocode(geoApiContext, String.valueOf(dsp.child("Address").getValue())).awaitIgnoreError();


                            LatLng latLng = new LatLng(results[0].geometry.location.lat, results[0].geometry.location.lng);
                            Locations.add(new Address(String.valueOf(dsp.child("Address").getValue()), dsp.getKey(), latLng, user));
                        }
                        RecyclerView.Adapter mAdapter = new UserViewLocationsAdapter(Locations, getContext(), map);
                        RecyclerView recyclerView = view.findViewById(R.id.view_locations);
                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);

                        recyclerView.setAdapter(mAdapter);
                        map.clear();
                        LatLngBounds.Builder builder=new LatLngBounds.Builder();
                        for (Address a : Locations) {
                            builder.include(a.getLatLng());
                            map.addMarker(new MarkerOptions().position(a.getLatLng())
                                    .title(a.getAddress())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        }
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(builder.build().getCenter(), 10f));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }




}
