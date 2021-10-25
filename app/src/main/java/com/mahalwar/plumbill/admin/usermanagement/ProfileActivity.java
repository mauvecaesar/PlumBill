package com.mahalwar.plumbill.admin.usermanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.mahalwar.plumbill.R;
import com.mahalwar.plumbill.user.LoginActivity;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImageView;
    TextView nameView;
    TextView phoneView;
    TextView emailView;
    TextView aadhaarView;
    TextView cashView;
    TextView supercashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        String uid = intent.getStringExtra("UID");
        String activity = intent.getStringExtra("Activity");

        setContentView(R.layout.activity_profile);

        nameView = findViewById(R.id.profile_username);
        phoneView = findViewById(R.id.profile_phone);
        emailView = findViewById(R.id.profile_email);
        aadhaarView = findViewById(R.id.profile_aadhaar);
        profileImageView = findViewById(R.id.profileImageView);
        cashView = findViewById(R.id.profile_cash);
        supercashView = findViewById(R.id.profile_supercash);

        displayProfileData(uid, activity);
    }

    @SuppressLint("SetTextI18n")
    private void displayProfileData(String uid, String activity) {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference;

        if(activity.equals("Login"))
            documentReference = firestore.collection("UIDUsers").document(uid);
        else
            documentReference = firestore.collection("AadhaarUsers").document(uid);

        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                Log.d("PlumBill", "Successfully fetched user data: " + uid);

                nameView.setText(documentSnapshot.getString("Name"));
                phoneView.setText(documentSnapshot.getString("Phone"));
                emailView.setText(documentSnapshot.getString("Email"));
                aadhaarView.setText(documentSnapshot.getString("Aadhaar"));
                cashView.setText(documentSnapshot.getDouble("Cash").toString());
                supercashView.setText(documentSnapshot.getDouble("Supercash").toString());
                //profileImageView.setImageURI(Uri.parse(documentSnapshot.getString("imgURL")));
            }
        })
        .addOnFailureListener(e -> {
            Log.d("PlumBill", "Couldn't get user data");
            Toast.makeText(getApplicationContext(), "Couldn't get user data", Toast.LENGTH_SHORT).show();
        });

        //profileImageView.setImageURI(Uri.parse(Objects.requireNonNull(userRecord.getPhotoUrl()).toString()));
    }
}
