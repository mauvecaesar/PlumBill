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

import java.util.HashMap;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    DocumentReference documentReference;

    EditText itemName;
    EditText itemPrice;
    EditText itemID;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_item);

        itemName = findViewById(R.id.editText_additemname);
        itemID = findViewById(R.id.editText_additemid);
        itemPrice = findViewById(R.id.editText_additemprice);
        submitButton = findViewById(R.id.submitButton);

        firestore = FirebaseFirestore.getInstance();

        submitButton.setOnClickListener(view -> {

            String item = itemName.getText().toString().toUpperCase();
            String pid = itemID.getText().toString().toUpperCase();

            documentReference = firestore.collection("Products").document(item);
            Map<String, Object> map = new HashMap<>();

            Float price = Float.parseFloat(itemPrice.getText().toString());

            map.put("PName", item);
            map.put("Price", price);
            map.put("PID", pid);

            documentReference.set(map)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getApplicationContext(), "Successfully added product", Toast.LENGTH_SHORT).show();
                        Log.d("PlumBill", "Added product");
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Couldn't add product", Toast.LENGTH_SHORT).show();
                        Log.d("PlumBill", "Couldn't add product");
                    });

        });
    }
}
