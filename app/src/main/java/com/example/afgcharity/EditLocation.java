package com.example.afgcharity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * edits location
 */
public class EditLocation extends Activity {
    private Address a;
    private DatabaseReference reference;
    private EditText address;

    /**
     * creates an edit location popup
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_location);
        Bundle extras = getIntent().getExtras();
    a= new Address(extras.getString("Address"), extras.getString("Id"),(LatLng) extras.get("LatLng"), extras.getString("User"));
        reference =  FirebaseDatabase.getInstance().getReference().child("users").child(a.getUser()).child("Locations");
        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getActionBar().hide();
        address=findViewById(R.id.editAddressItem);
        address.setText(a.getAddress());
        getWindow().setLayout((int) (width*.8), height/2);
        Button saveChanges=findViewById(R.id.editLocationItem);
        saveChanges.setOnClickListener(new View.OnClickListener() {
            /**
             * edits the location
             * @param v view when clicked
             */
            @Override
            public void onClick(View v) {
                if(!address.getText().toString().equals(""))
                    reference.child(a.getId()).child("Address").setValue(address.getText().toString());
                finish();
            }
        });
        Button deleteItem=findViewById(R.id.deleteLocation);
        deleteItem.setOnClickListener(new View.OnClickListener() {
            /**
             * deletes item
             * @param v view when clicked
             */
            @Override
            public void onClick(View v) {
                reference.child(a.getId()).removeValue();
                finish();
            }
        });
    }
}
