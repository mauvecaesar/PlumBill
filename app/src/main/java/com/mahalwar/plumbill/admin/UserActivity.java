package com.mahalwar.plumbill.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.mahalwar.plumbill.R;

import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {

    Button addUserButton;
    Button removeUserButton;
    Button updateUserButton;
    Button viewUserButton;
    Button viewAllUsersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

        addUserButton = findViewById(R.id.add_user_button);
        removeUserButton = findViewById(R.id.remove_user_button);
        updateUserButton = findViewById(R.id.update_user_button);
        viewUserButton = findViewById(R.id.view_user_button);
        viewAllUsersButton = findViewById(R.id.view_all_users_button);

        addUserButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), com.mahalwar.plumbill.admin.usermanagement.RegisterActivity.class)));
        removeUserButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), com.mahalwar.plumbill.admin.usermanagement.RemoveActivity.class)));
        updateUserButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), com.mahalwar.plumbill.admin.usermanagement.UpdateActivity.class)));
        viewUserButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), com.mahalwar.plumbill.admin.usermanagement.ProfileActivity.class)));
        viewAllUsersButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), com.mahalwar.plumbill.admin.usermanagement.ViewUsersActivity.class)));

    }
}
