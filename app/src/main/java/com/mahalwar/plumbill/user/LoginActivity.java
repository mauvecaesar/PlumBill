package com.mahalwar.plumbill.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.mahalwar.plumbill.R;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

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
        mAuth = FirebaseAuth.getInstance();
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

        // TODO: Use FirebaseAuth to sign in with email & password
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            Log.d("FlashChat", "signInWithEmail() onComplete : " +task.isSuccessful());

            if(!task.isSuccessful())
            {
                Log.d("FlashChat", "Problem signing in : " +task.getException());
                showErrorDialog();
            }
            else
            {
                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }

    // TODO: Show error on screen with an alert dialog
    private void showErrorDialog()
    {
        new AlertDialog.Builder(this, R.style.alertDialogTheme)
                .setTitle("Oops!")
                .setPositiveButton(android.R.string.ok, null)
                .setMessage("There was a problem signing in!")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        MediaPlayer.create(this, R.raw.alert).start();
    }

}
