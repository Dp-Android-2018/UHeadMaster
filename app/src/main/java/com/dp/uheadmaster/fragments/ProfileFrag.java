package com.dp.uheadmaster.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.ProfileSpinnerAdapter;
import com.dp.uheadmaster.interfaces.CheckOutDialogInterface;

import java.util.ArrayList;

/**
 * Created by DELL on 27/08/2017.
 */

public class ProfileFrag extends Fragment implements AdapterView.OnItemSelectedListener{
    private Spinner profileData;
    private ArrayList<String>data;
    private Fragment nextFrag=null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profile_layout,container,false);
        initializeUi(v);
        return v;
    }
    public void initializeUi(View v)
    {
        profileData=(Spinner) v.findViewById(R.id.sp_profile);
        profileData.setOnItemSelectedListener(this);
        data=new ArrayList<>();
        data.add(getString(R.string.basic_profile));
        data.add(getString(R.string.account));
        data.add(getString(R.string.privacy));
        data.add(getString(R.string.api_client));
        data.add(getString(R.string.notification));
        data.add(getString(R.string.delete_account));
        data.add(getString(R.string.subscribe));
        data.add(getString(R.string.credit_card));
        profileData.setAdapter(new ProfileSpinnerAdapter(getActivity().getApplicationContext(),data));
        if(nextFrag==null)
        {

            BasicProfileFrag basicProfileFrag = new BasicProfileFrag();
            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame1, basicProfileFrag);
            fragmentTransaction.commit();


        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem=data.get(position);
        if(selectedItem.equalsIgnoreCase(getString(R.string.basic_profile))){
            BasicProfileFrag basicProfileFrag = new BasicProfileFrag();
            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame1, basicProfileFrag);
            fragmentTransaction.commit();
        }
        else if(selectedItem.equalsIgnoreCase(getString(R.string.account))){
            AccountFrag basicProfileFrag = new AccountFrag();
            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame1, basicProfileFrag);
            fragmentTransaction.commit();

        }  else if(selectedItem.equalsIgnoreCase(getString(R.string.privacy))){
            PrivacyFrag privacyFrag = new PrivacyFrag();
            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame1, privacyFrag);
            fragmentTransaction.commit();

        }

        else if(selectedItem.equalsIgnoreCase(getString(R.string.api_client))){
            ApiClientFrag apiClientFrag = new ApiClientFrag();
            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame1, apiClientFrag);
            fragmentTransaction.commit();

        }
        else if(selectedItem.equalsIgnoreCase(getString(R.string.notification))){
            NotificationFrag notificationFrag = new NotificationFrag();
            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame1, notificationFrag);
            fragmentTransaction.commit();

        }

        else if(selectedItem.equalsIgnoreCase(getString(R.string.delete_account))){
            DeleteAccount deleteAccount = new DeleteAccount();
            deleteAccount.verify=(CheckOutDialogInterface) getActivity();
            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame1, deleteAccount);
            fragmentTransaction.commit();

        }

        else if(selectedItem.equalsIgnoreCase(getString(R.string.subscribe))){
            SubScribeFrag subScribeFrag = new SubScribeFrag();
            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame1, subScribeFrag);
            fragmentTransaction.commit();

        }

        else if(selectedItem.equalsIgnoreCase(getString(R.string.credit_card))){
            CreditCardFrag creditCardFrag = new CreditCardFrag();
            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame1, creditCardFrag);
            fragmentTransaction.commit();

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
