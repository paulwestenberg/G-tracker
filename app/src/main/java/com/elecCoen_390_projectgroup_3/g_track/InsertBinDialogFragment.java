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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InsertBinDialogFragment extends DialogFragment {
    protected EditText binCode, binName, binLocation;
    protected double binValue;
    protected Button saveBin,cancelBin;
    //ProgressBar progressBarRegister2;
    String bincodestring, binnumberstring, binlocationstring;
    double binvaluedouble=0;
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

        Bin bin = new Bin();

        //to save a bin:
        saveBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bin.setbincode(binCode.getText().toString().trim());
                bin.setbinname(binName.getText().toString().trim());
                bin.setbinlocation(binLocation.getText().toString().trim());
                bin.setValue(binValue);


                writeNewBinWithListeners(bin);

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

    public void writeNewBinWithListeners(Bin bin) {

        //set bin name:
        ref.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User Bins")
                .child(bin.getBinCode()).child("Bin Name").setValue(bin.getName())
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
        ref.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User Bins")
                .child(bin.getBinCode()).child("Bin Location").setValue(bin.getBinLocation())
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

        //associate bin with available bins in database:
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UnusedBins");
        if ((ref.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User Bins")
                .child(bin.getBinCode()).getKey() == reference.child(bin.getBinCode()).getKey())
                && (ref.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User Bins")
                .child(bin.getBinCode()).getKey() != null)
                && (reference.child(bin.getBinCode()).getKey() != null)) {

            String key = reference.child(bin.getBinCode()).getKey();
            Map<String, Object> childUpdates = new HashMap<>();

            DatabaseReference furtherreference = reference.child(bin.getBinCode());

            furtherreference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //makeText(snapshot.child("distance1").toString());
                        childUpdates.put("/sensors/distance1", snapshot.child("distance1").getValue());
                        childUpdates.put("/sensors/distance2", snapshot.child("distance2").getValue());
                        childUpdates.put("/sensors/distance3", snapshot.child("distance3").getValue());
                        childUpdates.put("/sensors/averagedistance", snapshot.child("averagedistance").getValue());
                        childUpdates.put("/sensors/estimatedcapacity", snapshot.child("estimatedcapacity").getValue());

                        ref.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User Bins")
                                .child(key).updateChildren(childUpdates);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //canceled
                }
            });

        } else {
            makeText("It seems this bin code is incorrect or not in the system yet");
        }
    }


    private void makeText(String s){
        Toast toast = Toast.makeText(getActivity(),s,Toast.LENGTH_LONG);
        toast.show();
    }
}

