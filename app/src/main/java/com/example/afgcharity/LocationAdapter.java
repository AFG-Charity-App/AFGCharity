
package com.example.afgcharity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationAdapter  extends RecyclerView.Adapter<CharityAdapter.MyViewHolder> {
    private ArrayList<LatLng> mDataset;
    public Context context;
    private File localFile;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView textView;
        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.locationAddress);
        }
        @Override
        public void onClick(View v) {

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public LocationAdapter(ArrayList<LatLng> myDataset, Context context) {
        mDataset = (ArrayList<LatLng>) myDataset.clone();
        this.context=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public LocationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.charity_view_locations, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) throws IOException {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final LatLng a=mDataset.get(position);
        holder.textView.setText(getAddress(a));
    }

    private String getAddress(LatLng coordinates) throws IOException {
        Geocoder geocoder= new Geocoder(this, Locale.getDefault());
        String strAddress="";
        List<Address> address= geocoder.getFromLocation(coordinates.latitude, coordinates.longitude, 1);
        try {
            if (address != null) {
                strAddress=address.get(0).getAddressLine(0);
            }
        } catch (Exception exception){
            exception.printStackTrace();
        }

            return (strAddress);

    }
}
