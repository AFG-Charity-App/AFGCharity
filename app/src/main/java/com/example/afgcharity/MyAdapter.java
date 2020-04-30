package com.example.afgcharity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Apparel> mDataset;
    private Context context;
    private File localFile;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView charityName;
        public TextView clothingItem;
        public TextView numItems;

        public ImageView imageView
        public MyViewHolder(View v) {
            super(v);
            charityName= v.findViewById(R.id.charityNameDisplay);
            clothingItem = v.findViewById(R.id.clothingType);
            numItems = v.findViewById(R.id.numberNeeded);

            imageView=v.findViewById(R.id.logoForCharity);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Apparel> myDataset, Context context) {
        mDataset = (ArrayList<Apparel>) myDataset.clone();
        this.context=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.charitylayout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    public void moveToProfile(){
        Intent intent = new Intent(this, CharityAccount.class);
        startActivity(intent);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
       Apparel a=mDataset.get(position);
       holder.charityName.setText(a.getUser());
        holder.numItems.setText(""+a.getAmount());
        holder.clothingItem.setText(a.getClothing());

        FirebaseStorage.getInstance().getReference().child("logos/"+a.getUser()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                Glide.with(context)
                        .load(downloadUrl.toString())
                        .placeholder(R.drawable.default_logo)
                        .error(R.drawable.default_logo)
                        .into(holder.imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.imageView.setImageResource(R.drawable.default_logo);
    }
});
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
