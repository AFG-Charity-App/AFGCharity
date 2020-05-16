
package com.example.afgcharity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserViewLocationsAdapter extends RecyclerView.Adapter<UserViewLocationsAdapter.MyViewHolder> {
    private ArrayList<com.example.afgcharity.Address> mDataset;
    public Context context;
    private File localFile;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView textView;
        public CardView cardView;
        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.locationAddress);
            cardView=v.findViewById(R.id.clickLocation);
        }

        @Override
        public void onClick(View v) {

        }
    }
    private GoogleMap mMap;
    // Provide a suitable constructor (depends on the kind of dataset)
    public UserViewLocationsAdapter(ArrayList<com.example.afgcharity.Address> myDataset, Context context, GoogleMap map) {
        mDataset = (ArrayList<com.example.afgcharity.Address>) myDataset.clone();
        this.context = context;
        mMap=map;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public UserViewLocationsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_view_locations, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        com.example.afgcharity.Address a = mDataset.get(position);

        holder.textView.setText(a.getAddress());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(a.getLatLng(), 15f));
            }
        });


    }


}

