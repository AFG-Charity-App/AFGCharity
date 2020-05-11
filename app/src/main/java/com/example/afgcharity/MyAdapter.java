package com.example.afgcharity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Apparel> mDataset;
    public static Context context;
    private File localFile;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView clothingType;
        public TextView clothingNum;
        public TextView name;
        public CardView cardView;
        public ImageView imageView;

        public MyViewHolder(View v) {
            super(v);
            clothingType = v.findViewById(R.id.clothingType);
            clothingNum = v.findViewById(R.id.numberNeeded);
            name = v.findViewById(R.id.locationAddress);
            imageView = v.findViewById(R.id.logoForCharity);
            cardView = v.findViewById(R.id.UserCardView);
        }


        @Override
        public void onClick(View v) {
            /*
            Intent intent=new Intent(context, UserViewsCharityProfile.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra("Clothing", clothingType.getText())
                    .putExtra("Amount", Integer.parseInt(clothingNum.getText().toString()))
                    .putExtra("Id", a.getId())
                    .putExtra("User", a.getUser())
                    .putExtra("Name", a.getName());
            context.startActivity(intent);

             */
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Apparel> myDataset, Context context) {
        mDataset = (ArrayList<Apparel>) myDataset.clone();
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.charitylayout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Apparel a = mDataset.get(position);
        holder.name.setText(a.getName());
        holder.clothingType.setText("" + a.getAmount());
        holder.clothingNum.setText(a.getClothing());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UserViewsCharityProfile.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("Clothing", a.getClothing())
                        .putExtra("Amount", a.getAmount())
                        .putExtra("Id", a.getId())
                        .putExtra("User", a.getUser())
                        .putExtra("Name", a.getName());
                context.startActivity(intent);


            }
        });
        FirebaseStorage.getInstance().getReference().child("logos/" + a.getUser()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
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
