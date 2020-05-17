package com.example.afgcharity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseUser;

/**
 * Main page in app open
 */
public class MainActivity extends AppCompatActivity {
    /**
     * creates layout for page when first opening
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


    }

    /**
     * creates user login button
     * @param v
     */
    public void userlogin(View v){
        Intent intent = new Intent(this, CharityView.class);
        startActivity(intent);
    }

    /**
     * creates charity login button
     * @param v
     */
    public void charitylogin(View v){
        Intent intent = new Intent(this, CharityLogin.class);
        startActivity(intent);
    }
}
