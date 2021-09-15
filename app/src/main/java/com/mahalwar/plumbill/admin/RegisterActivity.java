package com.mahalwar.plumbill.admin;

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

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mahalwar.plumbill.R;

import java.io.FileInputStream;
import java.io.IOException;
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
                try {
                    attemptRegistration();
                } catch (FirebaseAuthException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        buttonChooseImage.setOnClickListener(v -> SelectImage());

        buttonUploadImage.setOnClickListener(v -> uploadImage());

        registerButton.setOnClickListener(v -> {
            try {
                signUp(v);
            } catch (FirebaseAuthException | IOException e) {
                e.printStackTrace();
            }
        });

        // TODO: Get hold of an instance of FirebaseAuth
        // Firebase instance variables
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

    // Executed when Sign Up button is pressed.
    public void signUp(View v) throws FirebaseAuthException, IOException {
        attemptRegistration();
    }

    private void attemptRegistration() throws FirebaseAuthException, IOException {

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
    public void createFirebaseUser() throws FirebaseAuthException, IOException {
        String password = mPasswordView.getText().toString();
        String email = mEmailView.getText().toString();
        String aadhaar = mAadhaarView.getText().toString();
        String phone = mPhoneView.getText().toString();
        String name = mUsernameView.getText().toString();
        final String[] imageUrl = new String[1];

        // Handle any errors
        storageReference.child(filePath.toString()).getDownloadUrl().addOnSuccessListener(uri -> imageUrl[0] = uri.toString()).addOnFailureListener(Throwable::printStackTrace);


        FileInputStream refreshToken = new FileInputStream("C:\\Users\\hp\\AndroidStudioProjects\\PlumBill\\plumbill-firebase-adminsdk-1lohe-dc9d1117e8.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(refreshToken))
                .build();

        FirebaseApp.initializeApp(options);

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPhoneNumber(phone)
                .setDisplayName(name)
                .setPassword(password)
                .setUid(aadhaar)
                .setPhotoUrl(imageUrl[0]);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

        if(userRecord.isDisabled())
            showErrorDialog();
        else
            Log.d("PlumBill", "Successfully created new user: " + userRecord.getUid());
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
