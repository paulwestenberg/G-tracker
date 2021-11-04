package com.elecCoen_390_projectgroup_3.g_track;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText surnameEditText, nameEditText;
    private Button saveChangesButton, changePasswordButton, deleteProfileButton;
    DatabaseReference ref;
    private FirebaseAuth mAuth;
    String surNameEditString, nameEditString;



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

                break;
            case R.id.deleteprofilebutton:

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

    private void makeText(String s){
        Toast toast = Toast.makeText(this,s,Toast.LENGTH_LONG);
        toast.show();
    }

}

