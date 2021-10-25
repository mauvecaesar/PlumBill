package com.mahalwar.plumbill;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        FirebaseAuth.getInstance().signOut();

        Toast.makeText(getApplicationContext(), "Logging out...", Toast.LENGTH_SHORT).show();
        Log.d("PlumBill", "Successfully Logged Out");

        finish();
    }
}
