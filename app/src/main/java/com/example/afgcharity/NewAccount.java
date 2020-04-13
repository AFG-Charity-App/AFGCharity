package com.example.afgcharity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewAccount extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private boolean test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charity_entrance);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }
    public void makeAccount(View v){
    test = false;
        EditText emailET=findViewById(R.id.charity_new_email);
        EditText passwordET=findViewById(R.id.charity_new_password);
        String email= emailET.getText().toString();
        String password=passwordET.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getBaseContext(), "createUserWithEmail:success",
                                    Toast.LENGTH_SHORT).show();

                            FirebaseUser user = mAuth.getCurrentUser();
                            test=true;
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            reference.child("users").child(user.getUid()).setValue(user.getEmail());
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getBaseContext(), "createUserWithEmail:failure",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // ...
                    }
                });
        /*
        if(test=true){
            Intent intent = new Intent(this, CharityLogin.class);
            startActivity(intent);
            test = false;
        }
         */
    }
}
