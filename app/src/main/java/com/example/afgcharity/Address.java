package com.example.afgcharity;

import com.google.android.gms.maps.model.LatLng;

public class Address {
    //Data
    private String address;
    private String id;
    private LatLng latLng;


    /**
     * Creates a new Address Object
     * @param address the address of the charity location
     * @param id the firebase id
     * @param latLng the latitude/longitude location of the charity
     */
    public Address(String address, String id, LatLng latLng){
        this.latLng=latLng;
        this.address=address;
        this.id=id;
    }

    /**
     * Gets the firebase id
     * @return string value of the firebase id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the latitude/longitude location of the charity
     * @return LatLng object that contains the latitude/longitude location
     */
    public LatLng getLatLng() {
        return latLng;
    }

    /**
     * Gets the address of the charity location
     * @return string value of the address
     */
    public String getAddress() {
        return address;
    }

}
