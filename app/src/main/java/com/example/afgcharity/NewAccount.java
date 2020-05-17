package com.example.afgcharity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * creates new charity account
 */
public class NewAccount extends AppCompatActivity {
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private String displayname;
    private String email;
    private String description;
    private String website;
    private Uri imageURI;
    private boolean test;

    /**
     * Creating new account activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mStorageRef = FirebaseStorage.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_chairty_profile);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();


    }

    /**
     * getting details from user input for new account
     * @param v
     */
    public void makeAccount(View v) {
        test = false;
        EditText emailET = findViewById(R.id.enterUsernameCreateProfile);
        EditText passwordET = findViewById(R.id.enterPasswordCreateProfile);
        EditText passwordV = findViewById(R.id.enterConfirmPassword);
        EditText name = findViewById(R.id.enterCharityName);
        EditText descriptionET = findViewById(R.id.enterDescription);
        EditText websiteLink = findViewById(R.id.enterWebsiteLink);

        if (emailET.getText().toString() != null)
            email = emailET.getText().toString();
        else
            email = "";
        String password = new String("");
        if (passwordET.getText().toString() != null)
            password = passwordET.getText().toString();
        else {
            Toast.makeText(getBaseContext(), "No password entered",
                    Toast.LENGTH_SHORT).show();
        }
        String passwordConf = new String("");
        if (passwordV.getText().toString() != null)
            passwordConf = passwordV.getText().toString();
        if (name.getText().toString() != null)
            displayname = name.getText().toString();
        else
            displayname = "";
        if (descriptionET.getText().toString() != null)
            displayname = name.getText().toString();
        else
            displayname = "";
        if (descriptionET.getText().toString() != null)
            description = descriptionET.getText().toString();
        else
            description = "";
        if (websiteLink.getText().toString() != null)
            website = websiteLink.getText().toString();
        else
            website = "";
        passwordConfirmation(password, passwordConf);
        /*
        if(test=true){
            Intent intent = new Intent(this, CharityLogin.class);
            startActivity(intent);
            test = false;
        }
         */
    }

    /**
     * confirms if password is correct when user logins
     * @param password
     * @param passwordConf
     */
    private void passwordConfirmation(String password, String passwordConf) {
        if (password.equals(passwordConf) && password != null) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        /**
                         * creates new profile
                         * @param task
                         */
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(getBaseContext(), "Account successfully created",
                                        Toast.LENGTH_SHORT).show();

                                FirebaseUser user = mAuth.getCurrentUser();
                                test = true;

                                newAccount(user);

                                transfer();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getBaseContext(), "Account creation failed",
                                        Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                            }

                            // ...
                        }
                    });
        } else {
            Toast.makeText(getBaseContext(), "Confirmation Password Does Not Match",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * registers user on firebase
     * @param user
     */
    private void newAccount(FirebaseUser user) {

        UserProfileChangeRequest profileUpdates;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Info");
        reference.child("email").setValue(email);
        reference.child("name").setValue(displayname);
        reference.child("website").setValue(website);
        reference.child("description").setValue(description);

        if (imageURI != null) {
            Uri file = imageURI;
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayname)
                    .setPhotoUri(imageURI)
                    .build();
            StorageReference riversRef = mStorageRef.child("logos/" + user.getUid());

            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Toast.makeText(getBaseContext(), "Logo Upload Success",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            Toast.makeText(getBaseContext(), "Logo Upload Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } else
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayname)
                    .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getBaseContext(), "Profile successfully updated",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Info");
        reference.child("email").setValue(email);
        reference.child("name").setValue(displayname);
        reference.child("description").setValue(description);

    }

    /**
     * brings charity user back to the login page
     */
    private void transfer() {
        Intent intent = new Intent(this, CharityLogin.class);

        startActivity(intent);
    }

    private static final int FILE_SELECT_CODE = 0;

    /**
     * choosing file for logo
     * @param v
     */
    public void chooseFile(View v) {
        if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.M) && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            try {
                startActivityForResult(
                        Intent.createChooser(intent, "Select a File to Upload"),
                        FILE_SELECT_CODE);
            } catch (android.content.ActivityNotFoundException ex) {
                // Potentially direct the user to the Market with a Dialog
                Toast.makeText(this, "Please install a File Manager.",
                        Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    imageURI = data.getData();
                    ImageView logo = findViewById(R.id.logoImage);
                    logo.setImageURI(imageURI);
                    Toast.makeText(getBaseContext(), "File Uri: " + imageURI.getPath(),
                            Toast.LENGTH_SHORT).show();
                    // Get the file instance
                    // File file = new File(path);

                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getBaseContext(), "Permission Granted",
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getBaseContext(), "Permission Denied",
                            Toast.LENGTH_SHORT).show();
            }

        }
    }

}
