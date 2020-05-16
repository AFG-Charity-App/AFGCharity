package com.example.afgcharity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Class extends Activity class
 * Adding Location to Charity
 */
public class AddLocation extends Activity {
    private DatabaseReference reference;
    private EditText address;

    /**
     * Initializes layout to different functions
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_location);
        reference =  FirebaseDatabase.getInstance().getReference().child("users").child( FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Locations");
        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getActionBar().hide();
        address=findViewById(R.id.newAddress);
        getWindow().setLayout((int) (width*.8), height/2);
        Button saveChanges=findViewById(R.id.addLocationItem);
        saveChanges.setOnClickListener(new View.OnClickListener() {
            /**
             *  Adds location to Firebase
             * @param v the view the user presses
             */
            @Override
            public void onClick(View v) {

                if(!address.getText().toString().equals("")) {
                    DatabaseReference ref=reference.push();
                    Map<String, String> item= new HashMap<String, String>();
                    item.put("Address", address.getText().toString());
                    ref.setValue(item);
                    finish();
                }

            }
        });
        Button deleteItem=findViewById(R.id.cancelLocation);
        deleteItem.setOnClickListener(new View.OnClickListener() {
            /**
             * Ends the activity
             * @param v the view the user presses
             */
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
