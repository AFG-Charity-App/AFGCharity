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
    private ArrayList<String> Userlist;
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
        Userlist=new ArrayList<>();
       updateList();
        Toast.makeText(getBaseContext(), "Amount: "+Userlist.toString(),
                Toast.LENGTH_SHORT).show();
    }

    private void updateList(){
        reference.child("users").addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        getList();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        getList();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        getList();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        getList();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }
    private void getList(){
        reference.child("users").child(MainActivity.user.getUid()).child("Items").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Userlist = new ArrayList<String>();
                        // Result will be holded Here
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            Userlist.add(String.valueOf(dsp.getValue())); //add result into array list
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
