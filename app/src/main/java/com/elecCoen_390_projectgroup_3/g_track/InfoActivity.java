package com.elecCoen_390_projectgroup_3.g_track;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
    private ListView sensorlistview;
    private ListView binlistview;
    public TextView welcomeTextView, estimatedCapactityTextView, sumOfDistancesTextView;

    //need to set this to the size of the bin
    public float BinMaxSize = 58;
    public float sumOfDistances;
    public float estimatedcapacity;

    DatabaseReference ref;
    private FirebaseAuth mAuth;

    public String currentprofilesurname;
    public String currentprofilename;


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
        sensorlistview = findViewById(R.id.SensorListViewid);
        binlistview = findViewById(R.id.BinListViewid);
        sumOfDistancesTextView = findViewById(R.id.sumOfDistancesTextView);
        estimatedCapactityTextView = findViewById(R.id.estimatedCapacityTextView);

        mAuth = FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReference().child("Users");

//here
        setWelcomeMessage();

        addBinFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertBinDialogFragment dialog = new InsertBinDialogFragment();

                dialog.show(getSupportFragmentManager(),"InsertBinDialogFragment");
            }
        });


        //make sensor list
        ArrayList<String> list_sensor = new ArrayList<>();
        ArrayAdapter adapter_sensor = new ArrayAdapter<String>(this , R.layout.list_item , list_sensor);

        sensorlistview.setAdapter(adapter_sensor);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("sensor");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_sensor.clear();
                sumOfDistances=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    list_sensor.add(String.valueOf((Integer.parseInt(snapshot.getValue().toString()))-5));

                    sumOfDistances=sumOfDistances+Integer.parseInt(
                            String.valueOf((Integer.parseInt(snapshot.getValue().toString()))-5));
                }
                adapter_sensor.notifyDataSetChanged();
                sumOfDistancesTextView.setText("The sum of sensor distances is " + sumOfDistances);
                estimatedcapacity = ((BinMaxSize-(sumOfDistances/3))/(BinMaxSize))*100;
                        //(BinMaxSize-(sumOfDistances/3))/BinMaxSize;
                estimatedCapactityTextView.setText("Estimated Capacity is " + estimatedcapacity + "%");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*
        for(int i=1;i<sensorlistview.getCount();i++){
            sumOfDistances = sumOfDistances + Integer.parseInt(list_sensor.get(i));
            //makeText(String.valueOf(sumOfDistances));
        }

         */

        //display estimated % capacity:
        /*
        sumOfDistancesTextView.setText("The sum of sensor distances is " + sumOfDistances);
        estimatedcapacity = (BinMaxSize-sumOfDistances)/BinMaxSize;
        estimatedCapactityTextView.setText("Estimated Capacity is " + estimatedcapacity);

         */


        //make bin list
        ArrayList<String> list_bin = new ArrayList<>();
        ArrayAdapter adapter_bin = new ArrayAdapter<String>(this , R.layout.list_item , list_bin);

        binlistview.setAdapter(adapter_bin);

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("User Bins");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_bin.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    list_bin.add(snapshot.child("Bin Name").getValue()
                            + ", " + snapshot.child("Bin Location").getValue()
                            );
                }
                adapter_bin.notifyDataSetChanged();
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