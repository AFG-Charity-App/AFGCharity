package com.example.afgcharity;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class CharityView extends AppCompatActivity {
    private ArrayList<Apparel> Userlist;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private StorageReference mStorageRef;
    private ImageView profilepic;
    private File localFile;
    private MenuItem charitySort;
    private MenuItem itemSort;
    private MenuItem ammountSort;
    private String query;

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charity_list);
        Userlist = new ArrayList<Apparel>();
        boolean test = true;
        query = "";
        getSupportActionBar().setTitle("");

    }


    private void getList() {

        reference.child("users").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Userlist = new ArrayList<Apparel>();
                        // Result will be held Here
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            for (DataSnapshot dsp2 : dsp.child("Items").getChildren()) {


                                if (query.equals("") || String.valueOf(dsp2.child("Clothing").getValue()).toLowerCase().contains(query) || String.valueOf(dsp.child("name").getValue()).toLowerCase().contains(query))
                                    Userlist.add(new Apparel(dsp.getKey(), String.valueOf(dsp2.child("Clothing").getValue()), Integer.parseInt(String.valueOf(dsp2.child("Number").getValue())), dsp2.toString(), String.valueOf(dsp.child("name").getValue()))); //add result into array list

                            }
                        }
                        if (charitySort.isChecked())
                            Collections.sort(Userlist, new Comparator<Apparel>() {
                                public int compare(Apparel o1, Apparel o2) {
                                    return o1.getName().compareTo(o2.getName());
                                }
                            });
                        else if (itemSort.isChecked())
                            Collections.sort(Userlist, new Comparator<Apparel>() {
                                public int compare(Apparel o1, Apparel o2) {
                                    return o1.getClothing().compareTo(o2.getClothing());
                                }
                            });
                        else if (ammountSort.isChecked())
                            Collections.sort(Userlist, new Comparator<Apparel>() {
                                public int compare(Apparel o1, Apparel o2) {
                                    return new Integer(o1.getAmount()).compareTo(new Integer(o2.getAmount()));
                                }
                            });
                        mAdapter = new MyAdapter(Userlist, getBaseContext());
                        recyclerView = findViewById(R.id.charity_profile_needs_list);
                        recyclerView.setHasFixedSize(true);
                        layoutManager = new LinearLayoutManager(getBaseContext());
                        recyclerView.setLayoutManager(layoutManager);

                        recyclerView.setAdapter(mAdapter);
                        Toast.makeText(getBaseContext(), "Amount: " + mAdapter.getItemCount(),
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.charitylistmenu, menu);
        charitySort = menu.findItem(R.id.nav_charity);
        itemSort = menu.findItem(R.id.nav_item);
        ammountSort = menu.findItem(R.id.nav_amount);
        charitySort.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                charitySort.setChecked(true);
                itemSort.setChecked(false);
                ammountSort.setChecked(false);
                getList();
                return true;
            }
        });
        itemSort.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                charitySort.setChecked(false);
                itemSort.setChecked(true);
                ammountSort.setChecked(false);
                getList();
                return true;
            }
        });
        ammountSort.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                charitySort.setChecked(!true);
                itemSort.setChecked(false);
                ammountSort.setChecked(!false);
                getList();
                return true;
            }
        });

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String q) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String q) {
                query = q.toLowerCase();
                getList();
                return false;
            }
        });
        getList();
        return true;
    }

    @Nullable
    @Override
    public View onCreatePanelView(int featureId) {
        return super.onCreatePanelView(featureId);
    }

}
