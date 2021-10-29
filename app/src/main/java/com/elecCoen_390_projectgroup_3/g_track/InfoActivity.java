package com.elecCoen_390_projectgroup_3.g_track;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class InfoActivity extends AppCompatActivity {
    protected FloatingActionButton addBinFloatingButton;

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
        addBinFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertBinDialogFragment dialog = new InsertBinDialogFragment();

                dialog.show(getSupportFragmentManager(),"InsertBinDialogFragment");
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