package com.elecCoen_390_projectgroup_3.g_track.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.elecCoen_390_projectgroup_3.g_track.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class EditBinActivity extends AppCompatActivity implements View.OnClickListener{

    private String binCode;
    private EditText binNameEditText, binLocationEditText;
    private Button saveNewInformationButton;
    public String binNewNameString, binNewLocationString;
    DatabaseReference BinCodeRef;
    ProgressBar progressBar;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(EditBinActivity.this, InfoActivity.class));
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bin);

        Bundle b = getIntent().getExtras();
        binCode = "-1";
        if(b != null)
            binCode = b.getString("code");
        //makeText("bincode imported: " + binCode);

        binNameEditText = findViewById(R.id.editTextChangeBinName);
        binLocationEditText = findViewById(R.id.editTextChangeLocation);

        saveNewInformationButton = findViewById(R.id.saveNewBinInformationButton);
        saveNewInformationButton.setOnClickListener(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = user.getUid();
        BinCodeRef= FirebaseDatabase.getInstance().getReference()
                .child("Users").child(currentUid).child("User Bins").child(binCode);

        progressBar = findViewById(R.id.progressBarEditBin);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveNewBinInformationButton:
                modifyBinInfo();
                break;
        }
    }

    public void modifyBinInfo(){
        binNewNameString = binNameEditText.getText().toString().trim();
        binNewLocationString = binLocationEditText.getText().toString().trim();

        //set constraint for no null values
        if(binNewNameString.isEmpty()){binNameEditText.setError(getString(R.string.FITSP));binNameEditText.requestFocus(); return;}
        if(binNewLocationString.isEmpty()){binLocationEditText.setError(getString(R.string.FITSP));binLocationEditText.requestFocus(); return;}

        BinCodeRef.child("Bin Name").setValue(binNewNameString);
        BinCodeRef.child("Bin Location").setValue(binNewLocationString);
        startActivity(new Intent(EditBinActivity.this, InfoActivity.class ));
        finish();
        makeText(getString(R.string.thewordbin) + " " + binCode + " "
                + getString(R.string.MSString));
    }

    private void makeText(String s){
        Toast toast = Toast.makeText(this,s,Toast.LENGTH_LONG);
        toast.show();
    }

}