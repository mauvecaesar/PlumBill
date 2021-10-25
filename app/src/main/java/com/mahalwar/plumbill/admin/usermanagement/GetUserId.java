package com.mahalwar.plumbill.admin.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.mahalwar.plumbill.R;

public class GetUserId extends AppCompatActivity {

    EditText uidView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.get_user_uid);
        uidView = findViewById(R.id.editText_UID);
        button = findViewById(R.id.useridsubmitButton);

        button.setOnClickListener(view -> {
            Log.d("PlumBill", "UserID recieved");
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            intent.putExtra("UID", uidView.getText().toString());
            intent.putExtra("Activity", "UserID");
            startActivity(intent);
        });

    }
}
