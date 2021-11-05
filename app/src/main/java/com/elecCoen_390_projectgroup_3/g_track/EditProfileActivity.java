package com.elecCoen_390_projectgroup_3.g_track;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText surnameEditText, nameEditText;
    private EditText deleteemailEditText, deletepasswordEditText;
    private Button saveChangesButton, changePasswordButton, deleteProfileButton;
    DatabaseReference ref;
    private FirebaseAuth mAuth;
    public String surNameEditString, nameEditString;
    ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReference().child("Users");

        surnameEditText = findViewById(R.id.editTextEditSurname);
        nameEditText = findViewById(R.id.editTextEditName);
        saveChangesButton = findViewById(R.id.saveEditsButton);
        changePasswordButton = findViewById(R.id.changepasswordbutton);
        deleteProfileButton = findViewById(R.id.deleteprofilebutton);
        deleteemailEditText = findViewById(R.id.deleteEmailEditText);
        deletepasswordEditText = findViewById(R.id.deletePasswordEditText);
        progressBar = findViewById(R.id.progressBarEditProfile);

        saveChangesButton.setOnClickListener(this);
        changePasswordButton.setOnClickListener(this);
        deleteProfileButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.saveEditsButton:
                editUserMethod();
                startActivity(new Intent(this, InfoActivity.class ));
                break;
            case R.id.changepasswordbutton:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
            case R.id.deleteprofilebutton:
                deleteProfileMethod(deleteemailEditText.getText().toString(), deletepasswordEditText.getText().toString());
                break;
        }
    }

    private void editUserMethod(){
        surNameEditString=surnameEditText.getText().toString().trim();
        nameEditString=nameEditText.getText().toString().trim();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();
        ref.child(currentuid).child("surname").setValue(surNameEditString);
        ref.child(currentuid).child("name").setValue(nameEditString);

        makeText("Profile Changes Saved");
    }

    private void deleteProfileMethod(String email,String password) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        AlertDialog.Builder dialog = new AlertDialog.Builder(EditProfileActivity.this);
        dialog.setTitle("Are you sure?");
        dialog.setMessage("Deleting this account will result in completely removing your " +
                "account from the system and you won't be able to access the app.");
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progressBar.setVisibility(View.VISIBLE);
                // Prompt the user to re-provide their sign-in credentials
                if (user != null) {
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("TAG", "User account deleted.");
                                                startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                                                makeText("Deleted User Successfully,");
                                            }
                                        }
                                    });
                                }

                            });
                }
            }
        });

        dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

    }

    private void makeText(String s){
        Toast toast = Toast.makeText(this,s,Toast.LENGTH_LONG);
        toast.show();
    }

}

