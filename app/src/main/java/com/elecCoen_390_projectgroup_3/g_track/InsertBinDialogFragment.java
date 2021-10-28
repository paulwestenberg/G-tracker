package com.elecCoen_390_projectgroup_3.g_track;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class InsertBinDialogFragment extends DialogFragment {
    protected EditText binCode, binName,binLocation;
    protected Button saveBin,cancelBin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_insert_bin,container,false);
        binCode= view.findViewById(R.id.editTextBinId);
        binName=view.findViewById(R.id.editTextBinName);
        binLocation=view.findViewById(R.id.editTextLocation);
        saveBin=view.findViewById(R.id.binSaveButton);
        cancelBin=view.findViewById(R.id.binCancelButton);


        saveBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeText("welcoklnl to save butonlnoi");
            }
        });


        cancelBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });


//        return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }
    private void makeText(String s){
        Toast toast = Toast.makeText(getActivity(),s,Toast.LENGTH_LONG);
        toast.show();
    }
}
