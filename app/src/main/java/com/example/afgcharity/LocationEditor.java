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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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

public class LocationEditor extends Fragment {
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<Address> Locations;
    private GoogleMap map;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.location_editor, container, false);
        ImageButton addItem = view.findViewById(R.id.addLocationItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddLocation.class));
            }
        });
        Locations = new ArrayList<>();
        MapView mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                getLocations();

                mapView.onResume();
            }
        });

        return view;

    }

    public void getLocations() {
        reference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(
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
                            Locations.add(new Address(String.valueOf(dsp.child("Address").getValue()), dsp.getKey(), latLng, FirebaseAuth.getInstance().getCurrentUser().getUid()));


                        }
                        RecyclerView.Adapter mAdapter = new LocationAdapter(Locations, getContext());
                        RecyclerView recyclerView = view.findViewById(R.id.location_items);
                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);

                        recyclerView.setAdapter(mAdapter);
                        map.clear();
                        for (Address a : Locations)
                            map.addMarker(new MarkerOptions().position(a.getLatLng())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }


}
