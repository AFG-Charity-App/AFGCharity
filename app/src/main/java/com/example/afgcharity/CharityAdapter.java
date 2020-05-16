
package com.example.afgcharity;

        import android.content.Context;
        import android.content.Intent;
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
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.storage.FirebaseStorage;

        import java.io.File;
        import java.util.ArrayList;

/**
 * recycler view of data under each charity
 */
public class CharityAdapter  extends RecyclerView.Adapter<CharityAdapter.MyViewHolder> {
    private ArrayList<Apparel> mDataset;
    public Context context;
    private File localFile;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    /**
     * Placeholders for apparel images
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder  {
        // each data item is just a string in this case
        public TextView textView;
        public TextView textView2;

        public ImageView imageView;
        public ImageButton buttonView;
        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.clothingType);
            textView2 = v.findViewById(R.id.numberNeeded);
            imageView=v.findViewById(R.id.logoForCharity);
            buttonView=v.findViewById(R.id.editListButton);


        }

    }

    /**
     * Provide a suitable constructor (depends on the kind of dataset)
      */
    public CharityAdapter(ArrayList<Apparel> myDataset, Context context) {
        mDataset = (ArrayList<Apparel>) myDataset.clone();
        this.context=context;
    }

    /**
     * Create new views (invoked by the layout manager)
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public CharityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.charity_layout_user_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;


    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Apparel a=mDataset.get(position);
        holder.textView2.setText(""+a.getAmount());
        holder.textView.setText(a.getClothing());

        FirebaseStorage.getInstance().getReference().child("logos/"+a.getUser()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            /**
             * downloads url of charity page
             * @param downloadUrl
             */
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
            /**
             * failure exceptions
             * @param e
             */
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.imageView.setImageResource(R.drawable.default_logo);
            }
        });
        holder.buttonView.setOnClickListener(new View.OnClickListener(){

            /**
             * view on click
             * @param v
             */
            @Override
            public void onClick(View v) {
                Apparel a=mDataset.get(position);
                Intent intent=new Intent(context, EditItem.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("Clothing", a.getClothing())
                        .putExtra("Amount", a.getAmount())
                        .putExtra("Id", a.getId())
                        .putExtra("User", a.getUser())
                        .putExtra("Name", a.getName());
                context.startActivity(intent);
            }
        });
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     * @return size of data set
     */
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
