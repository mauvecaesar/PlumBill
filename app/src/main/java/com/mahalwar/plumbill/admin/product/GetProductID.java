package com.mahalwar.plumbill.admin.product;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mahalwar.plumbill.R;


public class GetProductID extends AppCompatActivity {

    Button button;
    EditText editText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.get_product_id);
        button = findViewById(R.id.submitButton);
        editText = findViewById(R.id.editText_PID);

        button.setOnClickListener(view -> {
            Log.d("PlumBill", "Got product name");
            Intent intent = new Intent(getApplicationContext(), ViewItemActivity.class);
            intent.putExtra("PID", editText.getText().toString().toUpperCase());
            startActivity(intent);
        });
    }
}
