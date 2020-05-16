package com.example.afgcharity;

import com.google.android.gms.maps.model.LatLng;

/**
 * location of charity
 */
public class Address {
    //Data
    private String address;
    private String id;
    private LatLng latLng;


    private String user;

    /**
     * Creates a new Address Object
     * @param address the address of the charity location
     * @param id the firebase id
     * @param latLng the latitude/longitude location of the charity
     */
    public Address(String address, String id, LatLng latLng, String user){
        this.latLng=latLng;
        this.address=address;
        this.id=id;
        this.user=user;
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
    /**
     * Gets the id of the charity the location is for
     * @return string value of the UID
     */
    public String getUser() {
        return user;
    }
}
