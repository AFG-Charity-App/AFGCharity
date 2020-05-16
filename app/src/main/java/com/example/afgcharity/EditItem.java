package com.example.afgcharity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditItem extends Activity {
    private Apparel a;
    private DatabaseReference reference;
    private EditText amount;
    private EditText clothing;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_item);
        Bundle extras = getIntent().getExtras();
        a=new Apparel(extras.getString("User"), extras.getString("Clothing"), extras.getInt("Amount"), extras.getString("Id"), extras.getString("Name"));
        reference =  FirebaseDatabase.getInstance().getReference().child("users").child(a.getUser()).child("Items");
        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getActionBar().hide();
        amount=findViewById(R.id.editAmount);
        clothing=findViewById(R.id.editClothing);
        clothing.setText(a.getClothing());
        amount.setText(""+a.getAmount());
        getWindow().setLayout((int) (width*.8), height/2);
        Button saveChanges=findViewById(R.id.saveChanges);
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!clothing.getText().toString().equals(""))
                    reference.child(a.getId()).child("Clothing").setValue(clothing.getText().toString());
                if(!amount.getText().toString().equals(""))
                    reference.child(a.getId()).child("Number").setValue(amount.getText().toString());
                finish();
            }
        });
        Button deleteItem=findViewById(R.id.deleteItem);
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(a.getId()).removeValue();
                finish();
            }
        });
    }
}
