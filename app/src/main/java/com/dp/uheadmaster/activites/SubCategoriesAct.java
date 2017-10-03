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
import com.dp.uheadmaster.adapters.CategoriesAdapter;
import com.dp.uheadmaster.fragments.MainFrag;
import com.dp.uheadmaster.fragments.SearchFrag;
import com.dp.uheadmaster.fragments.SubCategoriesFrag;
import com.dp.uheadmaster.models.CategoryModel;
import com.dp.uheadmaster.models.request.SubCategoryRequest;
import com.dp.uheadmaster.models.response.CategoriesResponse;
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

import static com.facebook.FacebookSdk.getApplicationContext;

public class SubCategoriesAct extends AppCompatActivity {


    private TextView tvActionBarTitle ;
    public static int subCategoryId=0;
    private String categoryTitle = "";
    private int categoryID = -1;
    private boolean isSearchOpened=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categories);

        if (getIntent().getExtras() != null) {
            categoryID = getIntent().getExtras().getInt("category_id" , -1);
            categoryTitle = getIntent().getExtras().getString("category_title" , "");
            System.out.println("Sub Category / categoryID = " + categoryID);
        }

        initView();


    }


    private void initView() {

        tvActionBarTitle = (TextView) findViewById(R.id.toolbar_title);
        if(!categoryTitle.equals("")){
            tvActionBarTitle.setText(categoryTitle);
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
                    b.putString("category_title",categoryTitle);
                    b.putInt("category_id",categoryID);
                    SubCategoriesFrag fragment = new SubCategoriesFrag();
                    fragment.setArguments(b);
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.commit();
                }
            }
        });

        SubCategoriesFrag fragment = new SubCategoriesFrag();
        Bundle b=new Bundle();
        b.putString("category_title",categoryTitle);
        b.putInt("category_id",categoryID);
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
