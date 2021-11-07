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

    private EditText surnameEditText, nameEditText,currentPasswordEditProfile,newPasswordEditProfile;
    //private EditText deleteemailEditText, deletepasswordEditText;
    private Button saveChangesButton, changePasswordButton, deleteProfileButton;
    DatabaseReference ref;
    private FirebaseAuth mAuth;
    public String surNameEditString, nameEditString,currentPasswordEditProfileString,newPasswordEditProfileString;
    ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReference().child("Users");

        surnameEditText = findViewById(R.id.editTextEditSurname);
        nameEditText = findViewById(R.id.editTextEditName);

        //deleteemailEditText = findViewById(R.id.deleteEmailEditText);
        //deletepasswordEditText = findViewById(R.id.deletePasswordEditText);
        progressBar = findViewById(R.id.progressBarEditProfile);

        saveChangesButton = findViewById(R.id.saveEditsButton);
        saveChangesButton.setOnClickListener(this);

        changePasswordButton = findViewById(R.id.changepasswordbutton);
        changePasswordButton.setOnClickListener(this);

        deleteProfileButton = findViewById(R.id.deleteprofilebutton);
//        deleteProfileButton.setEnabled(false);
        deleteProfileButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.saveEditsButton:
                editUserMethod();
                break;
            case R.id.changepasswordbutton:
                changePassword();
                break;
            case R.id.deleteprofilebutton:
                deleteProfileMethod();
                break;
        }
    }

    private void editUserMethod(){
        surNameEditString=surnameEditText.getText().toString().trim();
        nameEditString=nameEditText.getText().toString().trim();

        if(surNameEditString.isEmpty()){surnameEditText.setError("Fill the Surname Please!");surnameEditText.requestFocus(); return;}
        if(nameEditString.isEmpty()){nameEditText.setError("Fill the Name Please!");nameEditText.requestFocus(); return;}
        if(!surNameEditString.isEmpty()&& !nameEditString.isEmpty()){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = user.getUid();
        ref.child(currentUid).child("surname").setValue(surNameEditString);
        ref.child(currentUid).child("name").setValue(nameEditString);
        startActivity(new Intent(this, InfoActivity.class ));
        finish();
        makeText("Profile Changes Saved");
        }
    }
    private void changePassword(){
        currentPasswordEditProfile=findViewById(R.id.currentPasswordEditProfile);
        newPasswordEditProfile=findViewById(R.id.newPasswordEditProfile);

        currentPasswordEditProfileString=currentPasswordEditProfile.getText().toString().trim();
        newPasswordEditProfileString=newPasswordEditProfile.getText().toString().trim();
        if(currentPasswordEditProfileString.isEmpty()&& currentPasswordEditProfileString.length()<6){currentPasswordEditProfile.setError("Please Enter your current password");currentPasswordEditProfile.requestFocus() ;return;}
        if(newPasswordEditProfileString.isEmpty()){newPasswordEditProfile.setError("Please Enter your current password");newPasswordEditProfile.requestFocus() ;return;}
        if(currentPasswordEditProfileString.equals(newPasswordEditProfileString)){makeText("the new password is similar ");return;}

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPasswordEditProfileString);
        progressBar.setVisibility(View.VISIBLE);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                            user.updatePassword(newPasswordEditProfileString);
                            startActivity(new Intent(EditProfileActivity.this, InfoActivity.class ));
                            finish();
                            makeText("New password has been set");

                        }
                        else{
                            makeText("Reauthentication failed");
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


//        startActivity(new Intent(this, InfoActivity.class ));
//        finish();
//        makeText("Profile Changes Saved");
    }
    private void deleteProfileMethod(/*String email,String password*/) {


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        
        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        String email = "yo";
        String password = "eyo";
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
                                    // Get User Id
                                    // Compare User id to Deleted User Id
                                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                //Log.d("TAG", "User account deleted.");
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
    private void deleteButtonVisibility(){
        if(currentPasswordEditProfileString.isEmpty()&& currentPasswordEditProfileString.length()<6){currentPasswordEditProfile.setError("Please Enter your current password");currentPasswordEditProfile.requestFocus();return;}
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPasswordEditProfileString);
        progressBar.setVisibility(View.VISIBLE);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            makeText("the password is good you can dellet");

                        }
                        else{
                            makeText("make sure that you know the passwrod");
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void makeText(String s){
        Toast toast = Toast.makeText(this,s,Toast.LENGTH_LONG);
        toast.show();
    }

}

