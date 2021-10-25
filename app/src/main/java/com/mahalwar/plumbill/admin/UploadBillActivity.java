package com.mahalwar.plumbill.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mahalwar.plumbill.R;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UploadBillActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 100;
    Button uploadBillButton;
    Button submitButton;
    EditText billamt;
    EditText aadhaared;
    private Uri filePath;
    Task<Uri> billpath;
    String filename;
    String aadhaar;
    float billamount;

    FirebaseFirestore firestore;
    DocumentReference documentReference;
    private StorageReference storageReference;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_upload_bill);

        uploadBillButton = findViewById(R.id.uploadBillButton);
        submitButton = findViewById(R.id.submitButton);
        billamt = findViewById(R.id.editText_Amt);
        aadhaared = findViewById(R.id.editText_UID);

        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        uploadBillButton.setOnClickListener(view -> upload());

        submitButton.setOnClickListener(view -> submit());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
        }
    }

    private void upload(){
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FILE_REQUEST);
        Log.d("PlumBill", "picked file");

        if (filePath != null) {

            Log.d("PlumBill", "uploading file");
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            filename = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("Bills/" + filename);

            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        //if the upload is successful
                        //hiding the progress dialog
                        progressDialog.dismiss();
                        //and displaying a success toast
                        billpath = ref.getDownloadUrl();
                        Log.d("PlumBill", "File uploaded");
                        Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(exception -> {
                        //if the upload is not successful
                        //hiding the progress dialog
                        progressDialog.dismiss();
                        //and displaying error message
                        Log.d("PlumBill", "Couldn't upload file");
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        //calculating progress percentage
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        //displaying percentage in progress dialog
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    });
        }
    }

    private void submit(){

        aadhaar = aadhaared.getText().toString();
        billamount = Float.parseFloat(billamt.getText().toString());

        documentReference = firestore.collection("Bills").document(aadhaar);
        Map<String, Object> userInfo = new HashMap<>();

        userInfo.put("Bill"+filename, billpath);
        documentReference.set(userInfo)
                .addOnSuccessListener(unused -> {
                    Log.d("PlumBill", "Uploaded bill");
                    Toast.makeText(getApplicationContext(), "Successfully uploaded bill", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.d("PlumBill", "Couldn't upload bill");
                    Toast.makeText(getApplicationContext(), "Couldn't upload bill", Toast.LENGTH_SHORT).show();
                });

        Task<DocumentSnapshot> uid;
        documentReference = firestore.collection("AadhaarUsers").document(aadhaar);
        documentReference.update(
                "Cash", billamount/10,
                "Supercash", billamount/100
        );

        uid = documentReference.get(Source.valueOf("UID"));

        documentReference = firestore.collection("UIDUsers").document(String.valueOf(uid));
        documentReference.update(
                "Cash", billamount/10,
                "Supercash", billamount/100
        );
    }
}
