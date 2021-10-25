package com.mahalwar.plumbill.admin.product;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mahalwar.plumbill.R;

public class UpdateItemActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    DocumentReference documentReference;

    EditText itemName;
    EditText itemID;
    EditText itemPrice;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_item);

        itemName = findViewById(R.id.editText_updateitemname);
        itemID = findViewById(R.id.editText_updateitemid);
        itemPrice = findViewById(R.id.editText_updateitemprice);
        submitButton = findViewById(R.id.submitButton);

        firestore = FirebaseFirestore.getInstance();

        submitButton.setOnClickListener(view -> {
            documentReference = firestore.collection("Products").document(itemName.getText().toString().toUpperCase());

            documentReference.update(
                    "Price", itemPrice.getText().toString(),
                    "PID", itemID.getText().toString().toUpperCase())
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getApplicationContext(), "Successfully updated product", Toast.LENGTH_SHORT).show();
                        Log.d("PlumBill", "Updated product");
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Couldn't update product", Toast.LENGTH_SHORT).show();
                        Log.d("PlumBill", "Couldn't update product");
                    });

        });

    }
}
