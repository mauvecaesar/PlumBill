package com.mahalwar.plumbill;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddBillActivity extends android.app.Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bill_activity);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddBillActivity.this, AddItemActivity.class);
            AddBillActivity.this.startActivity(intent);
        });
    }
}
