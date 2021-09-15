package com.mahalwar.plumbill.user;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.mahalwar.plumbill.R;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = findViewById(R.id.login_email);
        mPasswordView = findViewById(R.id.login_password);

        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.integer.login || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });

        Button registerButton = findViewById(R.id.login_register_button);
        registerButton.setOnClickListener(this::registerNewUser);

        Button loginButton = findViewById(R.id.login_sign_in_button);
        loginButton.setOnClickListener(this::signInExistingUser);

        // TODO: Grab an instance of FirebaseAuth
    }

    // Executed when Sign in button pressed
    public void signInExistingUser(View v)   {
        // TODO: Call attemptLogin() here
        attemptLogin();
    }

    // Executed when Register button pressed
    public void registerNewUser(View v) {
        Intent intent = new Intent(this, com.mahalwar.plumbill.admin.RegisterActivity.class);
        finish();
        startActivity(intent);
    }

    // TODO: Complete the attemptLogin() method
    private void attemptLogin() {
        String password = mPasswordView.getText().toString();
        String email = mEmailView.getText().toString();

        if(password.equals("") || email.equals(""))
        {
            return;
        }
        else
        {
            Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show();
        }

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build());
        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);

    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            finish();
            startActivity(intent);
        } else {
            showErrorDialog();
        }
    }

    // TODO: Show error on screen with an alert dialog
    private void showErrorDialog()
    {
        new AlertDialog.Builder(this, R.style.alertDialogTheme)
                .setTitle("Regret")
                .setPositiveButton(android.R.string.ok, null)
                .setMessage("There was a problem signing in!")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        MediaPlayer.create(this, R.raw.alert).start();
    }

}
