package com.mahalwar.plumbill.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mahalwar.plumbill.R;
import com.mahalwar.plumbill.admin.product.AddProductActivity;
import com.mahalwar.plumbill.admin.product.GetProductID;
import com.mahalwar.plumbill.admin.product.RemoveItemActivity;
import com.mahalwar.plumbill.admin.product.UpdateItemActivity;
import com.mahalwar.plumbill.admin.product.ViewAllItemsActivity;

public class ProductActivity extends AppCompatActivity {

    Button addItemButton;
    Button removeItemButton;
    Button updateItemButton;
    Button viewItemButton;
    Button viewAllItemsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product);

        addItemButton = findViewById(R.id.add_item_button);
        removeItemButton = findViewById(R.id.remove_item_button);
        updateItemButton = findViewById(R.id.update_item_button);
        viewItemButton = findViewById(R.id.view_item_button);
        viewAllItemsButton = findViewById(R.id.view_all_items_button);

        addItemButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), AddProductActivity.class)));
        removeItemButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), RemoveItemActivity.class)));
        updateItemButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), UpdateItemActivity.class)));
        viewItemButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), GetProductID.class)));
        viewAllItemsButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ViewAllItemsActivity.class)));
    }
}

