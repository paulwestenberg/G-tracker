package com.elecCoen_390_projectgroup_3.g_track;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BinListAdapter extends ArrayAdapter<Bin> {

    final Context context;
    int resource;
    DatabaseReference root;

    public BinListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Bin> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //get the bin information:
//        String binCode = getItem(position).getBinCode();
        String binName = getItem(position).getName();
        String binLocation = getItem(position).getBinLocation();
        String binEC = getItem(position).getValue();


        //create the bin object:


        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        ImageView binImageIV = (ImageView) convertView.findViewById(R.id.bin_image_id);
        TextView binNameTV = (TextView) convertView.findViewById(R.id.bin_name_id);
        TextView binLocationTV = (TextView) convertView.findViewById(R.id.bin_location_id);
        TextView binECTV = (TextView) convertView.findViewById(R.id.bin_estimatedcapacity_id);
        String binNameTVString="Name: " + binName;
        String binLocationTVString="Location: " + binLocation;
        String binEstimatedTVString="Estimated Capacity: " + binEC + "%";
        binNameTV.setText(binNameTVString);
        binLocationTV.setText(binLocationTVString);
        binECTV.setText(binEstimatedTVString);

        //double EC_double = Double.parseDouble(binEC);
        //now set the image:
        root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reference = root.child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User Bins")
                .child("sensors").child("estimatedcapacity");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (binEC.equals("N/A")){
                    binImageIV.setImageResource(R.drawable.bin_x);
                }
                else{
                    double EC_double = Double.parseDouble(binEC);
                    if (EC_double>=0 && EC_double <=20)
                        binImageIV.setImageResource(R.drawable.bin);
                    else if (EC_double>20 && EC_double <=40){
                        binImageIV.setImageResource(R.drawable.bin1);
                    }
                    else if (EC_double>40 && EC_double <=60){
                        binImageIV.setImageResource(R.drawable.bin2);
                    }
                    else if (EC_double>60 && EC_double <=80){
                        binImageIV.setImageResource(R.drawable.bin3);
                    }
                    else if (EC_double>80 && EC_double <=100){
                        binImageIV.setImageResource(R.drawable.bin4);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //nothing
            }
        });

        return convertView;

    }
}
