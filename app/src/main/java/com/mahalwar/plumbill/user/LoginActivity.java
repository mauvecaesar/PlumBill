package com.mahalwar.plumbill.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mahalwar.plumbill.LogoutActivity;
import com.mahalwar.plumbill.R;
import com.mahalwar.plumbill.admin.AdminActivity;
import com.mahalwar.plumbill.admin.usermanagement.ProfileActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = findViewById(R.id.login_email);
        mPasswordView = findViewById(R.id.login_password);

        Button loginButton = findViewById(R.id.login_sign_in_button);
        loginButton.setOnClickListener(this::attemptLogin);

        // TODO: Grab an instance of FirebaseAuth
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    // TODO: Complete the attemptLogin() method
    private void attemptLogin(View v) {
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
            Log.d("PlumBill", "signInWithEmail() onComplete : " +task.isSuccessful());

            if(!task.isSuccessful())
            {
                Log.d("PlumBill", "Problem signing in : " +task.getException());
                showErrorDialog();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Signed in successfully", Toast.LENGTH_SHORT).show();
                /*NavigationView navigationView = findViewById(R.id.nav_view);
                Menu menu = Objects.requireNonNull(navigationView.getMenu());
                MenuItem menuItem = menu.findItem(R.id.nav_login);
                menuItem.setTitle("Logout");

                navigationView.setNavigationItemSelectedListener(item -> {
                    startActivity(new Intent(getApplicationContext(), LogoutActivity.class));
                    return true;
                });*/

                checkAccessLevel(mAuth.getCurrentUser().getUid());
            }
        });
    }

    private void checkAccessLevel(String uid){
        DocumentReference documentReference = firestore.collection("UIDUsers").document(uid);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                if(documentSnapshot.getBoolean("isAdmin")!=null){
                        Log.d("PlumBill", "Logged in Admin");
                        startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                }
                else
                {
                    Log.d("PlumBill", "Logged in User1");
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.putExtra("UID", uid);
                    intent.putExtra("Activity", "Login");
                    startActivity(intent);
                }
            }

            else {
                Log.d("PlumBill", "Logged in User2");
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("UID", uid);
                intent.putExtra("Activity", "Login");
                startActivity(intent);
            }
            finish();
        });
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
