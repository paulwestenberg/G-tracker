package com.elecCoen_390_projectgroup_3.g_track.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elecCoen_390_projectgroup_3.g_track.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText textEmail,textPassword;
    private Button loginBtn;
    private TextView forgotPasswordTextView,registerTextView,aboutTextView,contactUsTextView;
    DatabaseReference ref;
    private FirebaseAuth mAuth;
    private ProgressBar progressBarLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideTopScreen();
        setContentView(R.layout.activity_main);

        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));

        progressBarLogin=(ProgressBar)findViewById(R.id.progressBarMain);
        textEmail= (EditText) findViewById(R.id.userNameId);
        textPassword = (EditText) findViewById(R.id.passwordId);
        loginBtn= (Button) findViewById(R.id.logInButton);
        loginBtn.setOnClickListener(this);
        forgotPasswordTextView=(TextView)findViewById(R.id.forgotPasswordTextView);
        forgotPasswordTextView.setOnClickListener(this);
        registerTextView= (TextView) findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(this);
        aboutTextView = (TextView) findViewById(R.id.aboutTextView);
        aboutTextView.setOnClickListener(this);
        contactUsTextView = (TextView) findViewById(R.id.contactUsTextView);
        contactUsTextView.setOnClickListener(this);

        mAuth= FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.registerTextView:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.logInButton:
                userLogin();
                break;
            case R.id.forgotPasswordTextView:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
            case R.id.aboutTextView:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.contactUsTextView:
                startActivity(new Intent(this, ContactUsActivity.class));

        }
    }

    private void userLogin() {

        String emailString= textEmail.getText().toString().trim();
        String passwordString= textPassword.getText().toString().trim();

        if(emailString.isEmpty()){textEmail.setError(getString(R.string.PETUN));textEmail.requestFocus();return;}
        if(!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()){textEmail.setError(getResources().getString(R.string.PEAVEM));textEmail.requestFocus();return;}
        if(passwordString.isEmpty()){textPassword.setError(getString(R.string.PETP));textPassword.requestFocus();return;}
        if(passwordString.length() < 6){textPassword.setError(getString(R.string.TPSB6C));textPassword.requestFocus();return;}
        progressBarLogin.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    //if you want to make a veyfing email uncomment the next code
//                    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
//                    if(user.isEmailVerified()){ startActivity(new Intent(MainActivity.this,InfoActivity.class));}
//                    else {user.sendEmailVerification(); makeText("Please verify your Email!");}

                    //redirect to user profile

                    startActivity(new Intent(MainActivity.this, InfoActivity.class));

                }
                else
                    makeText(getString(R.string.PCYEMAP));
                progressBarLogin.setVisibility(View.GONE);
            }
        });
    }

    private void hideTopScreen(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    private void makeText(String s){
        Toast toast = Toast.makeText(this,s,Toast.LENGTH_LONG);
        toast.show();
    }
}