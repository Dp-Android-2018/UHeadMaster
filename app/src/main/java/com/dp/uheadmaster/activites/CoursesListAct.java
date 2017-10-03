package com.dp.uheadmaster.activites;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.CoursesListAdapter;
import com.dp.uheadmaster.fragments.CoursesListFrag;
import com.dp.uheadmaster.fragments.SearchFrag;
import com.dp.uheadmaster.fragments.SubCategoriesFrag;
import com.dp.uheadmaster.models.CourseModel;
import com.dp.uheadmaster.models.response.CourseListResponse;
import com.dp.uheadmaster.models.response.CourseResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoursesListAct extends AppCompatActivity {



    private String subCategoryTitle = "";
    private TextView tvActionBarTitle;
    private int subCategoryID = -1;
    private int reqestType = -1;
    private boolean isSearchOpened=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);


        if (getIntent().getExtras() != null) {
            subCategoryID = getIntent().getExtras().getInt("sub_category_id", -1);
            reqestType = getIntent().getExtras().getInt("reqest_type", -1);
            subCategoryTitle = getIntent().getExtras().getString("sub_category_title", "");
            System.out.println("Sub CategoryID = " + subCategoryID);
        }
        initializeUi();


    }

    public void initializeUi() {

        tvActionBarTitle = (TextView) findViewById(R.id.toolbar_title);
        if (!subCategoryTitle.equals("")) {
            tvActionBarTitle.setText(subCategoryTitle);
        }

        ImageView search=(ImageView)findViewById(R.id.iv_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSearchOpened==false) {
                    isSearchOpened=true;
                    SearchFrag fragment = new SearchFrag();
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.commit();
                }
                else if(isSearchOpened) {
                    isSearchOpened=false;
                    Bundle b=new Bundle();
                    b.putString("sub_category_title",subCategoryTitle);
                    b.putInt("sub_category_id",subCategoryID);
                    b.putInt("reqest_type",reqestType);
                    CoursesListFrag fragment = new CoursesListFrag();
                    fragment.setArguments(b);
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.commit();
                }
            }
        });

        CoursesListFrag fragment = new CoursesListFrag();
        Bundle b=new Bundle();
        b.putString("sub_category_title",subCategoryTitle);
        b.putInt("sub_category_id",subCategoryID);
        b.putInt("reqest_type",reqestType);
        fragment.setArguments(b);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
