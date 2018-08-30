package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.CategorySearchAdapter;
import com.dp.uheadmaster.interfaces.SwitchFragmentInterface;
import com.dp.uheadmaster.models.CategoryModel;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.response.CategoriesResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 06/09/2017.
 */

public class SearchCategoriesFrag extends Fragment implements SwitchFragmentInterface{
    public static int search_category_id=0;
    private ProgressDialog progressDialog;
    private SharedPrefManager sharedPrefManager;
    private List<CategoryModel> categoriesData;
    private RecyclerView recyclerCategories;
    private android.support.v7.widget.LinearLayoutManager mLayoutManager;
    private CategorySearchAdapter categorySearchAdapter;
    private Activity mHostActivity;
    public static SwitchFragmentInterface delegate=null;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private String next_page="";
    private int pageId=0;
    private int position=0;

    private FontChangeCrawler fontChanger;
    View v;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup) this.getView());
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup) this.getView());
        }

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v=inflater.inflate(R.layout.fragment_category_search_layout,container,false);
        SearchSubCategoriesFrag.search_sub_category_id=0;
        sharedPrefManager=new SharedPrefManager(getActivity().getApplicationContext());
        initializeUi(v);
        getCategories();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        SearchSubCategoriesFrag.search_sub_category_id=0;
    }

    public void initializeUi(View v){
        categoriesData=new ArrayList<>();
        recyclerCategories = (RecyclerView) v.findViewById(R.id.rv_categories);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerCategories.setLayoutManager(mLayoutManager);
        recyclerCategories.setItemAnimator(new DefaultItemAnimator());
        recyclerCategories.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {


                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading&& !next_page.equals(""))
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            position=totalItemCount;
                            getCategories();
                        }
                    }
                }
            }
        });
    }

    public void getCategories() {
        if (NetWorkConnection.isConnectingToInternet(mHostActivity.getApplicationContext(),mHostActivity.findViewById(R.id.content))) {
            progressDialog = ConfigurationFile.showDialog(getActivity());

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<CategoriesResponse> call = apiService.getCategories(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),pageId);
            System.out.println("Category / id : " + sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            System.out.println("Category / authro : " + sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN));
            call.enqueue(new Callback<CategoriesResponse>() {
                @Override
                public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                   // System.out.println("Category Body : "+response.body().getCategoriesList().size());
                    try {

                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            CategoriesResponse categoriesResponse = response.body();

                            categoriesData = categoriesResponse.getCategoriesList();
                            //Snackbarsuccess(getContext(), "Done! cat.Num = " + categoriesResponse.getCategoriesList().size(), Toast.LENGTH_LONG, true).show();
                            notifyAdapter();

                            recyclerCategories.getLayoutManager().scrollToPosition(position);
                            loading = true;

                            if (categoriesResponse.getNextPage()!= null  && !categoriesResponse.getNextPage().equals("")) {
                                next_page = categoriesResponse.getNextPage();
                                pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
                            } else {
                                next_page = "";

                            }


                        } else {
                            // parse the response body …
                            System.out.println("Category /error Code message :" + response.body().getMessage());
                            Snackbar.make(mHostActivity.findViewById(R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG).show();


                        }
                    }catch (NullPointerException ex){
                        ex.printStackTrace();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(mHostActivity.findViewById(R.id.content), t.getMessage(), Toast.LENGTH_LONG).show();
                    System.out.println("Category / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }
    public void notifyAdapter()
    {
        categorySearchAdapter=new CategorySearchAdapter(getActivity().getApplicationContext(),categoriesData,true);
        CategorySearchAdapter.delegate=(SwitchFragmentInterface)this;
        recyclerCategories.setAdapter(categorySearchAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHostActivity=activity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHostActivity=null;


    }

    @Override
    public void moveFragment(int checker) {
      delegate.moveFragment(1);
    }
}
