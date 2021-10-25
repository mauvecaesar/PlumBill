package com.mahalwar.plumbill.admin.product;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mahalwar.plumbill.R;

public class RemoveItemActivity extends AppCompatActivity {

    EditText editText;
    Button submitButton;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_remove_item);

        editText = findViewById(R.id.editText_removeitemname);
        submitButton = findViewById(R.id.submitButton);

        firestore = FirebaseFirestore.getInstance();

        submitButton.setOnClickListener(view -> firestore.collection("Products").document(editText.getText().toString().toUpperCase()).delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getApplicationContext(), "Successfully deleted product", Toast.LENGTH_SHORT).show();
                    Log.d("PlumBill", "deleted product");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Couldn't delete product", Toast.LENGTH_SHORT).show();
                    Log.d("PlumBill", "Couldn't delete product");
                }));

    }
}
