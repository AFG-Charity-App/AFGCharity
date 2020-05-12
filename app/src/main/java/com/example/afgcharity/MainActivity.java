package com.example.afgcharity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


    }

    public void userlogin(View v){
        Intent intent = new Intent(this, CharityView.class);
        startActivity(intent);
    }
    public void charitylogin(View v){
        Intent intent = new Intent(this, CharityLogin.class);
        startActivity(intent);
    }
}
