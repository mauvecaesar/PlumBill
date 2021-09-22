package com.mahalwar.plumbill.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mahalwar.plumbill.R;

public class ProductActivity extends AppCompatActivity {

    Button addItemButton;
    Button removeItemButton;
    Button updateItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product);

        addItemButton = findViewById(R.id.add_item_button);
        removeItemButton = findViewById(R.id.remove_item_button);
        updateItemButton = findViewById(R.id.update_item_button);

        addItemButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), com.mahalwar.plumbill.admin.product.AddItemActivity.class)));

        removeItemButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), com.mahalwar.plumbill.admin.product.RemoveItemActivity.class)));

        updateItemButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), com.mahalwar.plumbill.admin.product.UpdateItemActivity.class)));
    }
}

