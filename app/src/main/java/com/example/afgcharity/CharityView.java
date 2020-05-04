package com.example.afgcharity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class CharityView extends AppCompatActivity {
    private ArrayList<Apparel> Userlist;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private StorageReference mStorageRef;
    private ImageView profilepic;
    private File localFile;
    private DatabaseReference reference=FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charity_list);
        Userlist=new ArrayList<Apparel>();
        boolean test=true;
        while (test){

            getList();
            test=false;
        }
    }

    public void moveToProfile(View v){
        Intent intent = new Intent(this, CharityAccount.class);
        startActivity(intent);
    }

    private void getList(){
        reference.child("users").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Userlist = new ArrayList<Apparel>();
                        // Result will be held Here
                        for(DataSnapshot dsp: dataSnapshot.getChildren()){
                           for(DataSnapshot dsp2: dsp.child("Items").getChildren()){
                              Userlist.add(new Apparel(dsp.getKey(),String.valueOf(dsp2.child("Clothing").getValue()), Integer.parseInt(String.valueOf(dsp2.child("Number").getValue())), dsp2.toString(),String.valueOf(dsp.child("name").getValue()))); //add result into array list

                           }
                        }

                        mAdapter = new MyAdapter(Userlist, getBaseContext());
                        recyclerView= findViewById(R.id.charity_profile_needs_list);
                        recyclerView.setHasFixedSize(true);
                        layoutManager = new LinearLayoutManager(getBaseContext());
                        recyclerView.setLayoutManager(layoutManager);

                        recyclerView.setAdapter(mAdapter);
                        Toast.makeText(getBaseContext(), "Amount: "+mAdapter.getItemCount(),
                                Toast.LENGTH_SHORT).show();
                        recyclerView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }


        );


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.charitylistmenu, menu);
        return true;
    }

    @Nullable
    @Override
    public View onCreatePanelView(int featureId) {
        return super.onCreatePanelView(featureId);
    }

}
