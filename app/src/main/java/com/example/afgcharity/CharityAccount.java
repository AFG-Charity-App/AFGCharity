package com.example.afgcharity;

import android.content.Intent;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.internal.firebase_auth.zzcz;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CharityAccount extends AppCompatActivity {
    //Data
    private FirebaseAuth mAuth;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<Apparel> Userlist;
    private ArrayList<LatLng> Locations;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private StorageReference mStorageRef;
    private ImageView profilepic;
    private TextView description;
    private TextView website;
    private TextView name;
    private GeoApiContext geoApiContext;
    private File localFile;
    private boolean testing = true;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        geoApiContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyAmgVnIMQGig2SgDBm8GOXLKfId6tJHzHY")
                .build();





        mStorageRef = FirebaseStorage.getInstance().getReference().child("logos/" +  FirebaseAuth.getInstance().getCurrentUser().getUid());
        setContentView(R.layout.nav_charity_menu);
        name = findViewById(R.id.charity_name);
        description = findViewById(R.id.charityDescription);
        website = findViewById(R.id.website_link_placeholder);
        Linkify.addLinks(website, Linkify.WEB_URLS);
        //Toast.makeText(getBaseContext(), description, Toast.LENGTH_SHORT).show();
        ImageButton addItem = findViewById(R.id.addNewItem);
        addItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), NewItem.class));
            }
        });

        profilepic = findViewById((R.id.charity_logo));
        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                Glide.with(getBaseContext())
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

        Locations= new ArrayList<LatLng>();
        Userlist = new ArrayList<Apparel>();
        getUserList();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(getDrawable(R.drawable.ic_dehaze_white_24dp));
        DrawerLayout drawer = findViewById(R.id.charity_menu);
        drawer.closeDrawer(GravityCompat.START);
        drawer.setVisibility(View.VISIBLE);




    }


    /**
     * updates recycler view
     */
    private void getUserList() {
        reference.child("users").child( FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Userlist = new ArrayList<Apparel>();
                         FirebaseAuth.getInstance().getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(
                                String.valueOf(dataSnapshot.child("name").getValue())
                        ).build());
                        name.setText( FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                        // Result will be holded Here
                        description.setText(String.valueOf(dataSnapshot.child("description").getValue()));
                        website.setText((String.valueOf(dataSnapshot.child("website").getValue())));
                        for (DataSnapshot dsp : dataSnapshot.child("Items").getChildren()) {

                                Userlist.add(new Apparel(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                        String.valueOf(dsp.child("Clothing").getValue()),
                                        Integer.parseInt(""+
                                                String.valueOf(dsp.child("Number")
                                                        .getValue())),
                                        dsp.getKey(),
                                        FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));

                        }
                        mAdapter = new CharityAdapter(Userlist, getBaseContext());
                        recyclerView = findViewById(R.id.charity_profile_locations_list);
                        recyclerView.setHasFixedSize(true);
                        layoutManager = new LinearLayoutManager(getBaseContext());
                        recyclerView.setLayoutManager(layoutManager);

                        recyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.charity_menu);
        NavigationView navView= findViewById(R.id.menu_navigation);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.change_info:
                        break;
                    case R.id.change_locations:

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LocationEditor()).commit();
                        drawer.closeDrawer(GravityCompat.START);

                        break;
                    case R.id.change_logo:
                        break;

                }
                return true;
            }

        });
        //drawer.isDrawerOpen(); MAKE THIS THE THING
        //ViewGroup.LayoutParams params =  drawer.getLayoutParams();
        if (item.getItemId() == android.R.id.home)
            if (!drawer.isDrawerOpen(GravityCompat.START)) {

                // params.width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1000, getResources().getDisplayMetrics());
                //drawer.setLayoutParams(params);
                drawer.setVisibility(View.VISIBLE);
                drawer.openDrawer(GravityCompat.START);
            } else {
                //params.width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
                //drawer.setLayoutParams(params);

                drawer.closeDrawer(GravityCompat.START);
                drawer.setVisibility(View.VISIBLE);
            }
        testing = !testing;
        return true;
    }
    public void logOut(View v){
       
        FirebaseAuth.getInstance().signOut();
        finish();
     
    }

    /**
     * Edits Logo for charity profile
     * @param v the view the user presses
     */
    public void editLogo(View v){

    }



}
