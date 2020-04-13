package com.example.afgcharity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class CharityAccount extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_charity_profile);
        TextView name=findViewById(R.id.charity_name);
        name.setText(MainActivity.user.getEmail());
        reference.child("users").child(MainActivity.user.getUid());

    }
    public void test(View v){
        /*
        Random r = new Random();
        reference.child("users").child(MainActivity.user.getUid()).child("Test").setValue("Test");
        DatabaseReference ref=reference.child("apparel").push();
        ref.child("Charity").setValue(MainActivity.user.getUid());
        ref.child("Clothes").setValue("T-Shirt");
        ref.child("Number").setValue(r.nextInt(1000));
        */
        reference.child("apparel").child("test").
    }
}
