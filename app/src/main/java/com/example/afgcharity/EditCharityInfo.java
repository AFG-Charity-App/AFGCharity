package com.example.afgcharity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class EditCharityInfo extends Fragment {
    private View view;
    private DatabaseReference ref;
    private Uri imageURI;
    private ImageView profilepic;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.edit_charity_profile, container, false);
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("logos/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        ref=FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid());
        EditText name=view.findViewById(R.id.editName);
        EditText website=view.findViewById(R.id.editWebsite);
        EditText description=view.findViewById(R.id.editDescription);
         profilepic=view.findViewById(R.id.LogoCharity);
        Button editPic=view.findViewById(R.id.editLogo);
        editPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((Build.VERSION.SDK_INT>Build.VERSION_CODES.M) && checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                        PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                }else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);

                    try {
                        startActivityForResult(
                                Intent.createChooser(intent, "Select a File to Upload"),
                                FILE_SELECT_CODE);
                    } catch (android.content.ActivityNotFoundException ex) {
                        // Potentially direct the user to the Market with a Dialog
                        Toast.makeText(getContext(), "Please install a File Manager.",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                Glide.with(getContext())
                        .load(downloadUrl.toString())
                        .placeholder(R.drawable.default_logo)
                        .error(R.drawable.default_logo)
                        .into(profilepic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                profilepic.setImageResource(R.drawable.default_logo);
            }
        });
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(String.valueOf(dataSnapshot.child("Info").child("name").getValue()));
                website.setText(String.valueOf(dataSnapshot.child("Info").child("website").getValue()));
                description.setText(String.valueOf(dataSnapshot.child("Info").child("description").getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

        Button saveChanges=view.findViewById(R.id.saveEdits);
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> info=new HashMap<>();

                info.put("name",name.getText().toString());
                info.put("website",website.getText().toString());
                info.put("description",description.getText().toString());
                ref.child("Info").setValue(info);
                if(imageURI!=null) {
                    FirebaseAuth.getInstance().getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder()
                            .setDisplayName(name.getText().toString())
                            .setPhotoUri(imageURI)
                            .build());

                    mStorageRef.putFile(imageURI)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    // ...
                                    Toast.makeText(getContext(), "Logo Upload Failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                    FirebaseAuth.getInstance().getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder()
                            .setDisplayName(name.getText().toString()).build());

            getFragmentManager().popBackStack();
            }
        });
        return view;
    }

    private static final int FILE_SELECT_CODE = 0;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    imageURI = data.getData();
                    profilepic.setImageURI(imageURI);
                    Toast.makeText(getContext(), "File Uri: " + imageURI.getPath(),
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 2:{
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getContext(), "Permission Granted",
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Permission Denied",
                            Toast.LENGTH_SHORT).show();
            }

        }
    }
}
