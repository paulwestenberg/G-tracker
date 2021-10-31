package com.elecCoen_390_projectgroup_3.g_track;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {
    protected FloatingActionButton addBinFloatingButton;
    private ListView listview;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater editSetting = getMenuInflater();
        editSetting.inflate(R.menu.logout_in_login_page, menu);
        return true;
    }
    //end
    //create the toggle button to toggle between by Id mode or by surname mode
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, MainActivity.class));
        makeText("logout");
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        addBinFloatingButton=findViewById(R.id.floatingActionButtonAddBin);
        listview = findViewById(R.id.BinListViewid);

        addBinFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertBinDialogFragment dialog = new InsertBinDialogFragment();

                dialog.show(getSupportFragmentManager(),"InsertBinDialogFragment");
            }
        });

        ArrayList<String> list = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this , R.layout.list_item , list);

        listview.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("sensor");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    list.add(snapshot.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


//    private void go(){
//        Intent intent= new Intent(this,InfoActivity.class);
//        startActivity(intent);
//    }
private void makeText(String s){
    Toast toast = Toast.makeText(this,s,Toast.LENGTH_LONG);
    toast.show();
}
}