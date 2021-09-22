package com.mahalwar.plumbill.admin.usermanagement;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mahalwar.plumbill.R;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImageView;
    TextView nameView;
    TextView phoneView;
    TextView emailView;
    TextView aadhaarView;
    EditText uidView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.get_user_uid);

        nameView = findViewById(R.id.profile_username);
        phoneView = findViewById(R.id.profile_phone);
        emailView = findViewById(R.id.profile_email);
        aadhaarView = findViewById(R.id.profile_aadhaar);
        profileImageView = findViewById(R.id.profileImageView);
        uidView = findViewById(R.id.editText_UID);
        button = findViewById(R.id.submitButton);

        button.setOnClickListener(view -> displayProfileData(uidView.getText().toString()));
    }

    private void displayProfileData(String uid) {

        FirebaseUser userRecord = FirebaseAuth.getInstance().getCurrentUser();
        // See the UserRecord reference doc for the contents of userRecord.
        assert userRecord != null;
        Log.d("PlumBill", "Successfully fetched user data: " + uid);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = firestore.collection("Users").document(userRecord.getUid());
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                setContentView(R.layout.activity_profile);

                nameView.setText(documentSnapshot.getString("Name"));
                phoneView.setText(documentSnapshot.getString("Phone"));
                emailView.setText(documentSnapshot.getString("Email"));
                aadhaarView.setText(documentSnapshot.getString("Aadhaar"));
            }
        });

        //profileImageView.setImageURI(Uri.parse(Objects.requireNonNull(userRecord.getPhotoUrl()).toString()));
    }
}
