package com.elecCoen_390_projectgroup_3.g_track;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
EditText textUsername,textPassword;
Button btn;
DatabaseReference ref;
User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textUsername= (EditText) findViewById(R.id.userNameId);
        textPassword = (EditText) findViewById(R.id.passwordId);
        btn= (Button) findViewById(R.id.logInButton);
        user= new User();
        ref=FirebaseDatabase.getInstance().getReference().child("User");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeText("data inserted successfully");
                String username= textUsername.getText().toString();
                String password= textPassword.getText().toString();
                user.setUsername(username);
                user.setPassword(password);
                ref.push().setValue(user);
                goToInfoActivity();

            }
        });

    }
    private void goToInfoActivity(){
        Intent intent= new Intent(this,InfoActivity.class);
        startActivity(intent);
    }

    private void makeText(String s){
        Toast toast = Toast.makeText(this,s,Toast.LENGTH_LONG);
        toast.show();
    }

}