package com.example.afgcharity;

import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class CharityAccount extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<Apparel> Userlist;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private StorageReference mStorageRef;
    private ImageView profilepic;
    private TextView description;
    private TextView website;
    TextView name;
    private File localFile;
    private boolean testing=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mStorageRef = FirebaseStorage.getInstance().getReference().child("logos/"+MainActivity.user.getUid());
        setContentView(R.layout.nav_charity_menu);
        name=findViewById(R.id.charity_name);
        description=findViewById(R.id.charityDescription);

        //Toast.makeText(getBaseContext(), description, Toast.LENGTH_SHORT).show();

        profilepic=findViewById((R.id.charity_logo));
        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
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
        name.setText(MainActivity.user.getDisplayName());
        description.setText(MainActivity.user.getDescription);
        website.setText(MainActivity.user.getWebsite);
        Userlist = new ArrayList<Apparel>();
        getList();
       ActionBar actionBar=getSupportActionBar();
       actionBar.setDisplayHomeAsUpEnabled(true);
       actionBar.setHomeAsUpIndicator(getDrawable(R.drawable.ic_dehaze_white_24dp));
        DrawerLayout drawer = findViewById(R.id.charitymenu);
        drawer.closeDrawer(GravityCompat.START);
        drawer.setVisibility(View.VISIBLE);
    }
    public void test(View v){
        Random r = new Random();
        DatabaseReference  ref=reference.child("users").child(MainActivity.user.getUid()).child("Items").push();

        ref.child("Clothing").setValue("T-Shirt");
        ref.child("Number").setValue(r.nextInt(1000));

         getList();
    }
    private void getList(){
        reference.child("users").child(MainActivity.user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Userlist = new ArrayList<Apparel>();
                        // Result will be holded Here
                        TextView name=findViewById(R.id.charityDescription);
                        description=String.valueOf(dataSnapshot.child("description").getValue());
                        name.setText(description);
                        for (DataSnapshot dsp : dataSnapshot.child("Items").getChildren()) {
                            Userlist.add(new Apparel(MainActivity.user.getUid(),String.valueOf(dsp.child("Clothing").getValue()), Integer.parseInt(String.valueOf(dsp.child("Number").getValue()))));//add result into array list
                        }
                        mAdapter = new MyAdapter(Userlist, getBaseContext());
                        recyclerView= findViewById(R.id.charity_profile_locations_list);
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
        DrawerLayout drawer = findViewById(R.id.charitymenu);
        NavigationView menu     =findViewById(R.id.menu_navigation);
        //drawer.isDrawerOpen(); MAKE THIS THE THING
        //ViewGroup.LayoutParams params =  drawer.getLayoutParams();
        if(item.getItemId()==android.R.id.home)
            if(testing){

               // params.width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1000, getResources().getDisplayMetrics());
                //drawer.setLayoutParams(params);
                drawer.setVisibility(View.VISIBLE);
                drawer.openDrawer(GravityCompat.START);
            }

            else {
                //params.width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
                //drawer.setLayoutParams(params);

                drawer.closeDrawer(GravityCompat.START);
                drawer.setVisibility(View.VISIBLE);
            }
            testing=!testing;
        return true;
    }


}
