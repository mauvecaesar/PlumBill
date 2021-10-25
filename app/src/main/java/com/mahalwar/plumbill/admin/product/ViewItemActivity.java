package com.mahalwar.plumbill.admin.product;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mahalwar.plumbill.R;

public class ViewItemActivity extends AppCompatActivity {

    TextView nameView;
    TextView idView;
    TextView priceView;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        Intent intent = getIntent();
        String pid = intent.getStringExtra("PID");

        setContentView(R.layout.activity_view_item);

        nameView = findViewById(R.id.viewitem_name);
        idView = findViewById(R.id.viewitem_pid);
        priceView = findViewById(R.id.viewitem_price);

        displayProductData(pid);
    }

    @SuppressLint("SetTextI18n")
    private void displayProductData(String pid) {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = firestore.collection("Products").document(pid);
        documentReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        Log.d("PlumBill", "Successfully fetched product data: " + pid);

                        nameView.setText(documentSnapshot.getString("PName"));
                        idView.setText(documentSnapshot.getString("PID"));
                        priceView.setText(documentSnapshot.getDouble("Price").toString());
                        //profileImageView.setImageURI(Uri.parse(documentSnapshot.getString("imgURL")));
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("PlumBill", "Couldn't get product data");
                    Toast.makeText(getApplicationContext(), "Couldn't get product data", Toast.LENGTH_SHORT).show();
                });
    }
}
