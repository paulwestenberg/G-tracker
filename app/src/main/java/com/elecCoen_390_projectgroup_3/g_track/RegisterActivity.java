package com.elecCoen_390_projectgroup_3.g_track;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText surName,name,eMail,password,confirmPassword;
    Button saveRegisterBtn,cancelRegisterBtn;
    ProgressBar progressBarRegister;
    String surNameString,nameString,eMailString,passwordString,confirmPasswordString;
    DatabaseReference ref;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        surName=(EditText)findViewById(R.id.editTextSurname);
        name=(EditText)findViewById(R.id.editTextName);

        eMail  =(EditText)findViewById(R.id.editTextEmail);
        password=(EditText) findViewById(R.id.editTextPassword);
        confirmPassword=(EditText) findViewById(R.id.editTextConfirmPassword);
        saveRegisterBtn=(Button) findViewById(R.id.saveRegisterButton);
        cancelRegisterBtn=(Button) findViewById(R.id.cancelRegisterButton);
        progressBarRegister=(ProgressBar) findViewById(R.id.progressBarRegister);
        ref= FirebaseDatabase.getInstance().getReference().child("User");

        cancelRegisterBtn.setOnClickListener(this);
        saveRegisterBtn.setOnClickListener(this);



    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.cancelRegisterButton:
                startActivity(new Intent(this,MainActivity.class ));
                break;
            case R.id.saveRegisterButton:
                registerUserMethod();
                break;
        }
    }

    private void registerUserMethod() {

        surNameString=surName.getText().toString().trim();
        nameString=name.getText().toString().trim();

        eMailString=eMail.getText().toString().trim();
        passwordString=password.getText().toString().trim();
        confirmPasswordString=confirmPassword.getText().toString().trim();
        if(surNameString.isEmpty()){surName.setError("Fill the Surname Please!");surName.requestFocus(); return;}
        if(nameString.isEmpty()){name.setError("Fill the Name Please!");name.requestFocus(); return;}

        if(eMailString.isEmpty()){eMail.setError("Fill the E-mail Please!");eMail.requestFocus(); return;}
        if(!Patterns.EMAIL_ADDRESS.matcher(eMailString).matches()){eMail.setError("Please Enter a valid E-mail!");eMail.requestFocus(); return;}
        if(passwordString.isEmpty()){password.setError("Fill the Password Please!");password.requestFocus(); return;}
        if(passwordString.length()<6){password.setError("The password should be more then 6 characters");password.requestFocus(); return;}
        if(confirmPasswordString.isEmpty()){confirmPassword.setError("Confirm your Password please!");confirmPassword.requestFocus(); return;}
        if(!confirmPasswordString.equals(passwordString)){confirmPassword.setError("The password should be the same ");confirmPassword.requestFocus(); return;}
        progressBarRegister.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(eMailString,passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){User user = new User(surNameString,nameString,eMailString);
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){makeText("The user has been registered!");
                        progressBarRegister.setVisibility(View.GONE);
                            startActivity(new Intent(RegisterActivity.this, InfoActivity.class));
                        // redirection to the login lauoyt
                        }
                        else
                        makeText("Failed to register!");
                        progressBarRegister.setVisibility(View.GONE);
                    }

                });
                }
                else
                    makeText("Failed to register!");
                progressBarRegister.setVisibility(View.GONE);

            }
        });

    }
    private void makeText(String s){
        Toast toast = Toast.makeText(this,s,Toast.LENGTH_LONG);
        toast.show();
    }
}