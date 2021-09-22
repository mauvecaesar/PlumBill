package com.mahalwar.plumbill.admin.usermanagement;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mahalwar.plumbill.R;
import androidx.appcompat.app.AppCompatActivity;

public class RemoveActivity extends AppCompatActivity {

    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.get_user_uid);
        editText = findViewById(R.id.editText_UID);
        button = findViewById(R.id.submitButton);

        button.setOnClickListener(view -> {
            //FirebaseAuth.getInstance().deleteUser(editText.getText().toString());
            Log.d("PlumBill", "Successfully deleted user.");
            Toast.makeText(getApplicationContext(), "Successfully deleted user", Toast.LENGTH_SHORT).show();

        });

    }
}
