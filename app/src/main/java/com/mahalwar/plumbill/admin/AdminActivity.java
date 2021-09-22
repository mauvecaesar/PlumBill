package com.mahalwar.plumbill.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mahalwar.plumbill.R;

public class AdminActivity extends AppCompatActivity {

    Button manage_user_button;
    Button manage_product_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin);
        manage_user_button = findViewById(R.id.user_management_button);
        manage_product_button = findViewById(R.id.product_management_button);

        manage_user_button.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), com.mahalwar.plumbill.admin.UserActivity.class)));
        manage_product_button.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), com.mahalwar.plumbill.admin.ProductActivity.class)));

    }
}