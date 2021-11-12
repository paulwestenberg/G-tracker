package com.elecCoen_390_projectgroup_3.g_track;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {
    protected FloatingActionButton addBinFloatingButton;
    private ListView allbinlistview;
    private ListView binlistview;
    public TextView welcomeTextView;


    private static final String TAG = "InfoActivity";


    //Be able to round the estimated capacity to 2 digits after period:
    public String RoundedValueofEC = "N/A";

    //need to set this to the size of the bin
    public float BinMaxSize = 58;
    public float estimatedcapacity;

    DatabaseReference ref;
    private FirebaseAuth mAuth;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater editSetting = getMenuInflater();
        editSetting.inflate(R.menu.logout_in_login_page, menu);
        return true;
    }
    //end

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutInInfoPage:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
                makeText("You have been logged out");
                return true;
            case R.id.editProfilePage:
                startActivity(new Intent(this, EditProfileActivity.class));
                makeText("Edit Profile");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        welcomeTextView = findViewById(R.id.welcomeMessageTextView);
        addBinFloatingButton=findViewById(R.id.floatingActionButtonAddBin);
        allbinlistview = findViewById(R.id.SensorListViewid);
        binlistview = findViewById(R.id.BinListViewid);


        mAuth = FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReference().child("Users");


        setWelcomeMessage();

        addBinFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertBinDialogFragment dialog = new InsertBinDialogFragment();

                dialog.show(getSupportFragmentManager(),"InsertBinDialogFragment");
            }
        });


        //make all available bins list -> will be removed later
        ArrayList<String> list_allbin = new ArrayList<>();
        ArrayAdapter adapter_allbin = new ArrayAdapter<String>(this , R.layout.list_item , list_allbin);

        allbinlistview.setAdapter(adapter_allbin);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UnusedBins");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_allbin.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    list_allbin.add("Bin code: " + snapshot.getKey()
                    + "\nDistance1: " + snapshot.child("sensors").child("distance1").getValue()
                    + "\nDistance2: " + snapshot.child("sensors").child("distance2").getValue()
                    + "\nDistance3: " + snapshot.child("sensors").child("distance3").getValue()
                    + "\nAverage distance: " + snapshot.child("sensors").child("averagedistance").getValue()
                    + "\nEstimated Capacity: " + snapshot.child("sensors").child("estimatedcapacity").getValue());

                }
                adapter_allbin.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //display estimated % capacity:
/*
        sumOfDistancesTextView.setText("The sum of sensor distances is " + sumOfDistances);
                estimatedcapacity = ((BinMaxSize-(sumOfDistances/3))/(BinMaxSize))*100;
                        //(BinMaxSize-(sumOfDistances/3))/BinMaxSize;
                estimatedCapactityTextView.setText("Estimated Capacity is " + estimatedcapacity + "%");
 */

        //make bin list of the user
        /*
        ArrayList<String> list_bin = new ArrayList<>();
        ArrayAdapter adapter_bin = new ArrayAdapter<String>(this , R.layout.list_item , list_bin);

        binlistview.setAdapter(adapter_bin);

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User Bins");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_bin.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.child("sensors").child("estimatedcapacity").getValue()!=null){
                        RoundedValueofEC = String.valueOf(Math.round(
                                Float.parseFloat(snapshot.child("sensors").child("estimatedcapacity").getValue().toString())
                        ));
                    }
                    list_bin.add(
                            "Name: " + snapshot.child("Bin Name").getValue()
                            + "\nLocation: " + snapshot.child("Bin Location").getValue()
                            + "\nEstimated Capacity: " + RoundedValueofEC
                            + "%"
                            );
                    //reset value of sensor display
                    RoundedValueofEC = "N/A";
                }
                adapter_bin.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

         */

        //attempt to make new list view:
        //with list_bin:
        ArrayList<Bin> new_list_bin = new ArrayList<>();
        BinListAdapter new_adapter_bin = new BinListAdapter(this, R.layout.bin_item, new_list_bin);

        binlistview.setAdapter(new_adapter_bin);

        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User Bins");
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                new_list_bin.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    if (snapshot.child("sensors").child("estimatedcapacity").getValue()!=null) {
                        RoundedValueofEC = String.valueOf(Math.round(
                                Double.parseDouble(snapshot.child("sensors").child("estimatedcapacity").getValue().toString()))
                        );
                    }
                        //makeText(snapshot.child("Bin Code").getValue().toString());
                    if ((snapshot.child("Bin Name").getValue()!=null) &&
                            (snapshot.child("Bin Location").getValue()!=null) &&
                            (snapshot.getKey()!=null)){

                        Bin new_bin = new Bin(snapshot.getKey(),
                                snapshot.child("Bin Name").getValue().toString(),
                                snapshot.child("Bin Location").getValue().toString(),
                                RoundedValueofEC);
                        new_list_bin.add(new_bin);
                    }
                    //there is a slight timing problem in the app
                    //when the user inputs a bin it takes a millisecond but the table
                    //tries to load it automatically, resulting in an error
                    //thus, setting these temp value below is key to obtaining
                    //that 0.0001 second delay
                    else {
                        Bin new_bin = new Bin("W", "A", "I", "T");
                        new_list_bin.add(new_bin);
                    }



                            /*
                            "Name: " + snapshot.child("Bin Name").getValue()
                                    + "\nLocation: " + snapshot.child("Bin Location").getValue()
                                    + "\nEstimated Capacity: " + RoundedValueofEC
                                    + "%"

                             */
                    //reset value of sensor display
                    RoundedValueofEC = "N/A";
                }
                new_adapter_bin.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //getting the name and surname and displaying a welcome message to the user
    private void setWelcomeMessage(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();
        //get surname and name in a string:
        ref.child(currentuid).child("surname").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    //Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    welcomeTextView.setText("Welcome " + task.getResult().getValue());


                }
            }
        });

        ref.child(currentuid).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    //Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    welcomeTextView.setText(welcomeTextView.getText() + " " +
                            task.getResult().getValue());

                }
            }
        });
    }

private void makeText(String s){
    Toast toast = Toast.makeText(this,s,Toast.LENGTH_LONG);
    toast.show();
}
}