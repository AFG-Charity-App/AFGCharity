package com.example.afgcharity;

import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class NewAccount extends AppCompatActivity {
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private String displayname;
    private String email;
    private boolean test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mStorageRef= FirebaseStorage.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_chairty_profile);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }
    public void makeAccount(View v){
    test = false;
        EditText emailET=findViewById(R.id.enterUsernameCreateProfile);
        EditText passwordET=findViewById(R.id.enterPasswordCreateProfile);
        EditText passwordV=findViewById(R.id.enterConfirmPassword);
        EditText name=findViewById(R.id.enterCharityName);

        email= emailET.getText().toString();
        String password=passwordET.getText().toString();
        String passwordConf=passwordV.getText().toString();
        displayname = name.getText().toString();
        if(password.equals(passwordConf)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(getBaseContext(), "createUserWithEmail:success",
                                        Toast.LENGTH_SHORT).show();

                                FirebaseUser user = mAuth.getCurrentUser();
                                test = true;

                                newAccount(user);

                                transfer();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getBaseContext(), "createUserWithEmail:failure",
                                        Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                            }

                            // ...
                        }
                    });
        }else{
            Toast.makeText(getBaseContext(), "Confirmation Password Does Not Match",
                    Toast.LENGTH_SHORT).show();
        }
        /*
        if(test=true){
            Intent intent = new Intent(this, CharityLogin.class);
            startActivity(intent);
            test = false;
        }
         */
    }
    private void newAccount(FirebaseUser user){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayname)
                //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getBaseContext(), "ProfileUpdate:success",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        reference.child("email").setValue(email);
        reference.child("name").setValue(displayname);

    }

    private void transfer(){
        Intent intent = new Intent(this, CharityLogin.class);

        startActivity(intent);
    }
}
