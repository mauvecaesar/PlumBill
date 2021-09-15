package com.mahalwar.plumbill.user;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.mahalwar.plumbill.R;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImageView;
    TextView nameView;
    TextView phoneView;
    TextView emailView;
    TextView aadhaarView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        nameView = findViewById(R.id.profile_username);
        phoneView = findViewById(R.id.profile_phone);
        emailView = findViewById(R.id.profile_email);
        aadhaarView = findViewById(R.id.profile_aadhaar);
        profileImageView = findViewById(R.id.profileImageView);

        try {
            displayProfileData();
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }
    }

    private void displayProfileData() throws FirebaseAuthException {
        UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(LoginActivity.mEmailView.getText().toString());

        nameView.setText(userRecord.getDisplayName());
        phoneView.setText(userRecord.getPhoneNumber());
        emailView.setText(userRecord.getEmail());
        aadhaarView.setText(userRecord.getUid());
        profileImageView.setImageURI(Uri.parse(userRecord.getPhotoUrl()));
    }
}
