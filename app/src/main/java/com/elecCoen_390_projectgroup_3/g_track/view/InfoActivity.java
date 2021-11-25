package com.elecCoen_390_projectgroup_3.g_track.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.elecCoen_390_projectgroup_3.g_track.model.Bin;
import com.elecCoen_390_projectgroup_3.g_track.R;
import com.elecCoen_390_projectgroup_3.g_track.controller.BinListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.sql.ResultSet;
import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {
    protected FloatingActionButton addBinFloatingButton;
    private ListView allbinlistview;
    //private ListView binlistview;
    private SwipeMenuListView binlistview;
    public TextView welcomeTextView;
    public int thisposition = 0;
    public String bincode="-1";

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


    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutInInfoPage:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));

                makeText(getString(R.string.YHBLO));
                return true;
            case R.id.editProfilePage:
                startActivity(new Intent(this, EditProfileActivity.class));
                makeText(getString(R.string.editprofiletext));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        welcomeTextView = findViewById(R.id.welcomeMessageTextView);
        addBinFloatingButton=findViewById(R.id.floatingActionButtonAddBin);
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
        /*
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
                    + "\nDistance1: " + snapshot.child("sensors").child("distance1").getValue() + " cm"
                    + "\nDistance2: " + snapshot.child("sensors").child("distance2").getValue() + " cm"
                    + "\nDistance3: " + snapshot.child("sensors").child("distance3").getValue() + " cm"
                    + "\nAverage distance: " + snapshot.child("sensors").child("averagedistance").getValue() + " cm"
                    + "\nEstimated Capacity: " + snapshot.child("sensors").child("estimatedcapacity").getValue() + "%");

                }
                adapter_allbin.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

         */


        //list that will be displayed to the user:
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
                       if (isInteger(RoundedValueofEC)) {
                           if (Integer.parseInt(RoundedValueofEC) >= 80) {
                               notifyUser(InfoActivity.this,snapshot.child("Bin Name").getValue().toString(),
                                       snapshot.child("Bin Location").getValue().toString(),
                                       RoundedValueofEC);
                           }
                       }
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

                    //reset value of sensor display
                    RoundedValueofEC = "N/A";
                }
                new_adapter_bin.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(300);
                // set item title
                openItem.setTitle(getString(R.string.editBinString));
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(300);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        binlistview.setMenuCreator(creator);

        binlistview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        //for editing the bin:
                        //makeText("case 0");
                        thisposition = position;
                        Query specialQuery = reference3.orderByKey().limitToFirst(position+1);
                        specialQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot Snapshot: dataSnapshot.getChildren()) {
                                    if (thisposition!=0){
                                        thisposition--;
                                    }
                                    else {
                                        bincode = Snapshot.getKey();
                                        Intent intent = new Intent(InfoActivity.this, EditBinActivity.class);
                                        Bundle b = new Bundle();
                                        b.putString("code", bincode); //Your id
                                        intent.putExtras(b); //Put your id to your next Intent
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG, "onCancelled", databaseError.toException());
                            }
                        });
                        break;
                    case 1:
                        //for deleting the bin:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(InfoActivity.this);
                        dialog.setTitle(getString(R.string.AYS));
                        dialog.setMessage(getString(R.string.deleteBinFragmentTitle));
                        dialog.setPositiveButton(getString(R.string.Delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                thisposition = position;
                                Query specialQuery = reference3.orderByKey().limitToFirst(position+1);
                                specialQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot Snapshot: dataSnapshot.getChildren()) {
                                            if (thisposition!=0){
                                                thisposition--;
                                            }
                                            else {
                                                String bincode = Snapshot.getKey();
                                                Snapshot.getRef().removeValue();
                                                makeText((getString(R.string.thewordbin)) + " "
                                                        + bincode + " " + (getString(R.string.removedfromaccount)));
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e(TAG, "onCancelled", databaseError.toException());
                                    }
                                });
                            }
                        });
                        dialog.setNegativeButton(getString(R.string.Dismiss), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog alertDialog = dialog.create();
                        alertDialog.show();
                        //makeText("case 1");
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

    }

    //getting the name and surname and displaying a welcome message to the user
    private void setWelcomeMessage(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();

        //get name of the user in a string:
        ref.child(currentuid).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    //Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    welcomeTextView.setText(getString(R.string.Welcome) +" "+ task.getResult().getValue());


                }
            }
        });

        //add the user's surname:
        ref.child(currentuid).child("surname").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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

    private void notifyUser(Context context,String name, String location, String cap){

        Intent intent=new Intent(context,InfoActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,100,intent,PendingIntent.FLAG_IMMUTABLE );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(InfoActivity.this, "My Notification");
        builder.setContentTitle("Full Bin");
        builder.setContentText("Your bin " + name + " at " + location + " is at " + cap + "%"
        + "\n Consider changing it!");
        builder.setSmallIcon(R.drawable.logo);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(InfoActivity.this);
        managerCompat.notify(1, builder.build());

    }
    private boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }

private void makeText(String s){
    Toast toast = Toast.makeText(this,s,Toast.LENGTH_LONG);
    toast.show();
}
}