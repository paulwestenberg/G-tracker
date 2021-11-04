package com.elecCoen_390_projectgroup_3.g_track;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class InsertBinDialogFragment extends DialogFragment {
    protected EditText binCode, binName, binLocation;
    protected double binValue;
    protected Button saveBin,cancelBin;
    //ProgressBar progressBarRegister2;
    String bincodestring, binnumberstring, binlocationstring;
    double binvaluedouble=5;
    DatabaseReference ref;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_insert_bin,container,false);
        binCode= view.findViewById(R.id.editTextBinId);
        binName=view.findViewById(R.id.editTextBinName);
        binLocation=view.findViewById(R.id.editTextLocation);
        saveBin=view.findViewById(R.id.binSaveButton);
        cancelBin=view.findViewById(R.id.binCancelButton);
        binvaluedouble = 5;
        ref= FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        //progressBarRegister2=(ProgressBar) findViewById(R.id.progressBarRegister);

        Sensor sensor = new Sensor();

        //to save a bin:
        saveBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensor.setbincode(binCode.getText().toString().trim());
                sensor.setbinname(binName.getText().toString().trim());
                sensor.setbinlocation(binLocation.getText().toString().trim());
                sensor.setValue(binValue);

                writeNewBinWithListeners(sensor);

                    //need to set some constraints making sure the user has enterred accurate information
                //ref.setValue(sensor);
                //FirebaseDatabase.getInstance().getReference("Bin").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(sensor);
                makeText("Bin has been saved to database");

                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        cancelBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
//        return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    private void getAllSensorValues(){

    }

    public void writeNewBinWithListeners(Sensor s) {
        //set bin name:
        ref.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User Bins").child("Bin Code: " + s.getBinCode()).child("Bin Name").setValue(s.getName())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                    }
                });
        //set bin location:
        ref.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User Bins").child("Bin Code: " + s.getBinCode()).child("Bin Location").setValue(s.getBinLocation())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                    }
                });

    }
/*
    private void registerBinMethod() {

        bincodestring = binCode.getText().toString().trim();
        binlocationstring = binLocation.getText().toString().trim();
        binnumberstring = binNumber.getText().toString().trim();
        binvaluedouble = 5;

        //need to set constraints such as:
        //if(!confirmPasswordString.equals(passwordString)){confirmPassword.setError("The password should be the same ");confirmPassword.requestFocus(); return;}

        progressBarRegister2.setVisibility(View.VISIBLE);

        Sensor sensor2 = new Sensor(bincodestring, binlocationstring, binnumberstring, binvaluedouble);
        FirebaseDatabase.getInstance().getReference("Bins").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(sensor2).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast toast = Toast.makeText(getActivity(), "Success, bin added", Toast.LENGTH_LONG);
                    toast.show();
                    progressBarRegister2.setVisibility(View.GONE);
                    //startActivity(new Intent(RegisterActivity.this, InfoActivity.class));
                    // redirection to the login lauoyt
                }
                else {
                    progressBarRegister2.setVisibility(View.GONE);
                }

            }
                    });
    }

 */


    private void makeText(String s){
        Toast toast = Toast.makeText(getActivity(),s,Toast.LENGTH_LONG);
        toast.show();
    }
}
