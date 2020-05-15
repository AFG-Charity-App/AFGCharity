package com.example.afgcharity;

import com.google.android.gms.maps.model.LatLng;

public class Address {
    private String address;
    private String id;
    private LatLng latLng;



    public Address(String address, String id, LatLng latLng){
        this.latLng=latLng;
        this.address=address;
        this.id=id;
    }
    public String getId() {
        return id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getAddress() {
        return address;
    }

}
