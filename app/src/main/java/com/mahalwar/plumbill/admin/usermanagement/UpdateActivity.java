package com.mahalwar.plumbill.admin.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.mahalwar.plumbill.R;

public class UpdateActivity extends AppCompatActivity {

    EditText editText;
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mUsernameView;
    private AutoCompleteTextView mPhoneView;
    private AutoCompleteTextView mAadhaarView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private ImageView imageView;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(getApplicationContext(), GetUserId.class);
        startActivity(intent);
        String uid = intent.getStringExtra("UID");

        setContentView(R.layout.activity_update_user);

        editText = findViewById(R.id.editText_UID);
        mEmailView = findViewById(R.id.register_email);
        mPasswordView = findViewById(R.id.register_password);
        mConfirmPasswordView = findViewById(R.id.register_confirm_password);
        mUsernameView = findViewById(R.id.register_username);
        mPhoneView = findViewById(R.id.register_phone);
        mAadhaarView = findViewById(R.id.register_aadhaar);
        updateButton = findViewById(R.id.update_button);

        updateButton.setOnClickListener(view -> updateUser(editText.getText().toString()));
    }

    private void updateUser(String uid){

        /*UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid)
                .setEmail(mEmailView.getText().toString())
                .setPhoneNumber(mPhoneView.getText().toString())
                .setEmailVerified(true)
                .setPassword(mPasswordView.getText().toString())
                .setDisplayName(mUsernameView.getText().toString());
                //.setPhotoUrl();

        UserRecord userRecord = null;
        try {
            userRecord = FirebaseAuth.getInstance().updateUser(request);
            Log.d("PlumBill", "Successfully updated user: " + userRecord.getUid());

        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }*/

    }
}
