package com.elecCoen_390_projectgroup_3.g_track;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InfoActivity extends AppCompatActivity {
    protected FloatingActionButton addBinFloatingButton;

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
}