package com.dp.uheadmaster.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.SubCategoriesAct;
import com.dp.uheadmaster.adapters.ViewPagerAdapter;
import com.dp.uheadmaster.holders.SearchCategoryHolder;
import com.dp.uheadmaster.interfaces.CheckOutDialogInterface;
import com.dp.uheadmaster.interfaces.SwitchFragmentInterface;
import com.dp.uheadmaster.models.response.SearchCoursesResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;
import com.google.gson.Gson;

import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 06/09/2017.
 */

public class BasicSearchFrag extends Fragment implements SwitchFragmentInterface{

   private Button btnCategories,btnSubCategories;
    private EditText etSearchKeyWord;
    private String keyWord=null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_basic_search_layout,container,false);
        initializeUi(v);
        return v;
    }

    public void initializeUi(View v)
    {
        etSearchKeyWord=(EditText)v.findViewById(R.id.et_search_keyword);
        etSearchKeyWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    Bundle b=new Bundle();
                    b.putString("keyword",etSearchKeyWord.getText().toString().trim());
                    SearchResultFrag fragment = new SearchResultFrag();
                    fragment.setArguments(b);
                    SearchCategoriesFrag.delegate=(SwitchFragmentInterface) BasicSearchFrag.this;
                    android.support.v4.app.FragmentManager fragmentManager = getChildFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });
        btnCategories=(Button)v.findViewById(R.id.btn_categories);
        btnSubCategories=(Button)v.findViewById(R.id.btn_sub_categories);
        btnCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCategories.setTextColor(getActivity().getResources().getColor(R.color.dot_active_screen));
                btnSubCategories.setTextColor(getActivity().getResources().getColor(R.color.black));
                SearchCategoriesFrag fragment = new SearchCategoriesFrag();
                SearchCategoriesFrag.delegate=(SwitchFragmentInterface) BasicSearchFrag.this;
                android.support.v4.app.FragmentManager fragmentManager = getChildFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commit();
            }
        });

        btnSubCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCategories.setTextColor(getActivity().getResources().getColor(R.color.black));
                btnSubCategories.setTextColor(getActivity().getResources().getColor(R.color.dot_active_screen));
                SearchSubCategoriesFrag fragment = new SearchSubCategoriesFrag();
                SearchSubCategoriesFrag.delegate=(SwitchFragmentInterface) BasicSearchFrag.this;
                android.support.v4.app.FragmentManager fragmentManager = getChildFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public void moveFragment(int checker) {

        if(isAdded()) {
            Bundle b = new Bundle();
            b.putString("keyword", etSearchKeyWord.getText().toString().trim());
            SearchResultFrag fragment = new SearchResultFrag();
            fragment.setArguments(b);
            SearchCategoriesFrag.delegate = (SwitchFragmentInterface) BasicSearchFrag.this;
            android.support.v4.app.FragmentManager fragmentManager = getChildFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
        }

    }





}
