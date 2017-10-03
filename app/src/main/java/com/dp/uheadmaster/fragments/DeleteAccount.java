package com.dp.uheadmaster.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.interfaces.CheckOutDialogInterface;

/**
 * Created by DELL on 27/08/2017.
 */

public class DeleteAccount extends Fragment {

    private Button deleteCourse;
    CheckOutDialogInterface verify=null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.delete_account_layout,container,false);
        initializeUi(v);
        return v;
    }

    public void initializeUi(View v)
    {
        deleteCourse=(Button)v.findViewById(R.id.btn_delete_course);
        deleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify.verifyDialog(2);

            }
        });
    }
}
