package com.example.afgcharity;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.DisplayMetrics;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;

        import androidx.annotation.Nullable;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import java.util.HashMap;
        import java.util.Map;

public class NewItem extends Activity {
    private DatabaseReference reference;
    private EditText amount;
    private EditText clothing;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_item);
        reference =  FirebaseDatabase.getInstance().getReference().child("users").child( FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Items");
        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getActionBar().hide();
        amount=findViewById(R.id.amountNeeded);
        clothing=findViewById(R.id.clothingType);
        getWindow().setLayout((int) (width*.8), height/2);
        Button saveChanges=findViewById(R.id.addItem);
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!clothing.getText().toString().equals("")&& !amount.getText().toString().equals("")) {
                    DatabaseReference ref=reference.push();
                    Map<String, String> item= new HashMap<String, String>();
                    item.put("Clothing", clothing.getText().toString());
                    item.put("Number", amount.getText().toString());
                    ref.setValue(item);
                    finish();
                }

            }
        });
        Button deleteItem=findViewById(R.id.cancelItem);
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
