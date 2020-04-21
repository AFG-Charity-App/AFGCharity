package com.example.afgcharity;

import android.os.Bundle;
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
       getList();
    }


    private void getList(){
        reference.child("users").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Userlist = new ArrayList<Apparel>();
                        // Result will be holded Here
                        for(DataSnapshot dsp: dataSnapshot.getChildren()){
                           for(DataSnapshot dsp2: dsp.child("Items").getChildren()){
                               String a = String.valueOf(dsp2.getValue());
                              Userlist.add(new Apparel(dsp.getKey(),a.substring(a.indexOf("Clothing=")+9, a.lastIndexOf("}")),Integer.parseInt(a.substring(a.indexOf("Number=")+7, a.indexOf(","))))); //add result into array list

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

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }


        );


    }

}
