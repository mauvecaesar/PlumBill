package com.mahalwar.plumbill.admin.usermanagement;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mahalwar.plumbill.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    //public static final String CHAT_PREFS = "ChatPrefs";
    //public static final String DISPLAY_NAME_KEY = "username";

    // TODO: Add member variables here:
    // UI references.
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mUsernameView;
    private AutoCompleteTextView mPhoneView;
    private AutoCompleteTextView mAadhaarView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private ImageView imageView;

    private StorageReference storageReference;
    private FirebaseFirestore firestore;

    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = findViewById(R.id.register_email);
        mPasswordView = findViewById(R.id.register_password);
        mConfirmPasswordView = findViewById(R.id.register_confirm_password);
        mUsernameView = findViewById(R.id.register_username);
        mPhoneView = findViewById(R.id.register_phone);
        mAadhaarView = findViewById(R.id.register_aadhaar);

        Button buttonChooseImage = findViewById(R.id.buttonChoosePicture);
        Button buttonUploadImage = findViewById(R.id.buttonUploadPicture);
        imageView = findViewById(R.id.personimageView);
        Button registerButton = findViewById(R.id.register_sign_up_button);

        // Keyboard sign in action
        mConfirmPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.integer.register_form_finished || id == EditorInfo.IME_NULL) {
                attemptRegistration();
                return true;
            }
            return false;
        });

        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        buttonChooseImage.setOnClickListener(v -> SelectImage());

        buttonUploadImage.setOnClickListener(v -> uploadImage());

        registerButton.setOnClickListener(v -> attemptRegistration());

    }

    private void SelectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

            ref.putFile(filePath)

                    .addOnSuccessListener(taskSnapshot -> {

                // Image uploaded successfully
                // Dismiss dialog
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
            })
                    .addOnFailureListener(e -> {

                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    })

                    .addOnProgressListener(taskSnapshot -> {
                                double progress
                                        = (100.0
                                        * taskSnapshot.getBytesTransferred()
                                        / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int)progress + "%");
                            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();

            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void attemptRegistration() {

        //Reset errors displayed in the form.
        /*mEmailView.setError(null);
        mPasswordView.setError(null);
        mAadhaarView.setError(null);
        mPhoneView.setError(null);*/

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        Log.d("PlumBill", "TextUtils.isEmpty(password): " + TextUtils.isEmpty(password));
        Log.d("PlumBill", "TextUtils.isEmpty(password) && !isPasswordValid(password): " + (TextUtils.isEmpty(password) && isPasswordValid(password)));


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            Log.d("PlumBill", "Password Invalid");
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // TODO: Call create FirebaseUser() here
            createFirebaseUser();
        }
    }

    private boolean isEmailValid(String email) {
        // You can add more checking logic here.
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Add own logic to check for a valid password (minimum 6 characters)
        String confirmPassword = mConfirmPasswordView.getText().toString();
        return confirmPassword.equals(password) && password.length() >= 6;
    }

    // TODO: Create a Firebase user
    public void createFirebaseUser() {
        String password = mPasswordView.getText().toString();
        String email = mEmailView.getText().toString();
        String aadhaar = mAadhaarView.getText().toString();
        String phone = mPhoneView.getText().toString();
        String name = mUsernameView.getText().toString();

        // Handle any errors
        storageReference.child(aadhaar).putFile(filePath).addOnSuccessListener(taskSnapshot -> makeToast()).addOnFailureListener(e -> showErrorDialog());

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {

            DocumentReference documentReference = firestore.collection("Users").document(aadhaar);
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("Name", name);
            userInfo.put("Email", email);
            userInfo.put("Phone", phone);
            userInfo.put("Aadhaar", aadhaar);
            userInfo.put("UID", aadhaar);
            userInfo.put("isUser", "1");
            userInfo.put("imgURL", filePath.toString());

            documentReference.set(userInfo);
        }).addOnFailureListener(e -> showErrorDialog());

    }

    private void makeToast(){
        Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show();
    }
    /*
    // TODO: Save the display name to Shared Preferences
    private void saveDisplayName()
    {
        String displayName = mUsernameView.getText().toString();
        SharedPreferences prefs = getSharedPreferences(CHAT_PREFS, 0);
        prefs.edit().putString(DISPLAY_NAME_KEY, displayName).apply();
        FirebaseUser user = FirebaseAuth.getInstance().getUser();

        if(user!=null)
        {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("PlumBill", "User profile updated.");
                        }
                    });
        }

    }*/

    // TODO: Create an alert dialog to show in case registration failed
    private void showErrorDialog()
    {
        new AlertDialog.Builder(this, R.style.alertDialogTheme)
                .setTitle("Oops!")
                .setMessage("Registration attempt failed!")
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        MediaPlayer.create(this, R.raw.alert).start();
    }


}
