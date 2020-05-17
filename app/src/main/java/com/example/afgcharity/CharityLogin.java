package com.example.afgcharity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.FirebaseDatabase;

/**
 * logs into charity
 */
public class CharityLogin extends AppCompatActivity {
    //data
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charity_login);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();    }

    /**
     * When user presses login button, either allows passage to account or not
     *
     * @param v the view the user presses
     */
    public void enter(View v) {
        //Get text
        EditText emailET = findViewById(R.id.charity_email);
        EditText passwordET = findViewById(R.id.charity_password);
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        if (!email.equals("") || !password.equals(""))
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        /**
                         * if successful login, then presents charity account, else, shows error message
                         * @param task
                         */
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                Log.d("signin", "signInWithEmail:success");
                                Toast.makeText(getBaseContext(), "Authentication successful",
                                        Toast.LENGTH_SHORT).show();

                                profile();
                                //updateUI(user);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("signin", "signInWithEmail:failure", task.getException());
                                Toast.makeText(getBaseContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                            // ...
                        }
                    });
        else Toast.makeText(getBaseContext(), "Please enter a username and password",
                Toast.LENGTH_SHORT).show();
    }


    /**
     * Brings to the new account page
     *
     * @param v the view the user presses
     */
    public void newAccount(View v) {
        Intent intent = new Intent(this, NewAccount.class);
        startActivity(intent);
    }

    /**
     * Brings user to the actual account after logging in
     */
    private void profile() {
        Intent intent = new Intent(this, CharityAccount.class);

        startActivity(intent);


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
