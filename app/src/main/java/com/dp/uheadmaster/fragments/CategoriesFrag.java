package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.CategoriesAdapter;
import com.dp.uheadmaster.models.CategoryModel;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.response.CategoriesResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CategoriesFrag extends Fragment {


    View view;
    private GridView gVCategories;
   // private ProgressDialog progressDialog;
    private  static List<CategoryModel> categoriesArray ;
    private SharedPrefManager sharedPrefManager;
    CategoriesAdapter categoriesAdapter;
    public static int categoryId=0;
    int myLastVisiblePos;// global variable of activity
    private String next_page="";
    private int pageId = 0;
    private boolean isLoading;
    private  int position=0;
    private ImageView mIvEmptyView;
    private FontChangeCrawler fontChanger;
    private Activity mActivity;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_categories, container, false);
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        initView();
        if(categoriesArray.size() ==0){
            getCategories();
        }else{
            notifyAdapter();
        }


        return view;
    }

    public void getCategories() {
        if (NetWorkConnection.isConnectingToInternet(mActivity.getApplicationContext(),mActivity.findViewById(R.id.content))) {
         //   Toast.makeText(getActivity().getApplicationContext(), "Starting Dialog", Snackbar.LENGTH_LONG).show();
           // progressDialog = ConfigurationFile.showDialog(getActivity());

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            System.out.println("App Language :"+ConfigurationFile.GlobalVariables.APP_LANGAUGE);
            Call<CategoriesResponse> call = apiService.getCategories(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),pageId);
            System.out.println("Category / id : " + sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            System.out.println("Category / authro : " + sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN));
            call.enqueue(new Callback<CategoriesResponse>() {
                @Override
                public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                 //   ConfigurationFile.hideDialog(progressDialog);
                  //  Toast.makeText(getActivity().getApplicationContext(), "Closing Dialog", Snackbar.LENGTH_LONG).show();
                    try {

                        System.out.println("Category Body : "+response.body().getCategoriesList().size());

                    if (response.body().getStatus() == 200) {
                        // use response data and do some fancy stuff :)
                        CategoriesResponse categoriesResponse = response.body();
                       // Snackbar.success(getContext(), "Done! cat.Num = " + categoriesResponse.getCategoriesList().size(), Snackbar.LENGTH_LONG, true).show();
                        categoriesArray = categoriesResponse.getCategoriesList();
                      //  categoriesArray.clear();
                        notifyAdapter();
                        gVCategories.setSelection(position);
                        isLoading = false;
                        if(categoriesArray.size()==0){
                            gVCategories.setVisibility(View.GONE);
                            mIvEmptyView.setVisibility(View.VISIBLE);

                        }else {
                            gVCategories.setVisibility(View.VISIBLE);
                            mIvEmptyView.setVisibility(View.GONE);
                        }
                     //   Toast.makeText(getActivity().getApplicationContext(), " Page Id : "+categoriesResponse.getNextPage(), Snackbar.LENGTH_LONG).show();
                        if (!categoriesResponse.getNextPage().equals("")) {
                            next_page = categoriesResponse.getNextPage();
                            pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
                          //  Toast.makeText(getActivity().getApplicationContext(), " Page Id :"+pageId, Snackbar.LENGTH_LONG).show();
                        } else {
                            next_page = "";
                        }
                       // Toast.makeText(getApplicationContext(), "Next :"+next_page, Snackbar.LENGTH_LONG).show();


                    } else {
                        // parse the response body …
                        System.out.println("Category /error Code message :" + response.body().getMessage());
                        Snackbar.make(mActivity.findViewById(R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG).show();


                    }
                    }catch (NullPointerException ex){
                        ex.printStackTrace();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                  //  Toast.makeText(getActivity().getApplicationContext(), "Failiure Closing Dialog", Snackbar.LENGTH_LONG).show();
                   // ConfigurationFile.hideDialog(progressDialog);

                  //  Snackbar.error(getContext(), "Failure:"+t.getMessage(), Snackbar.LENGTH_LONG, true).show();
                    System.out.println("Category / Fialer :" + t.getMessage());
                }
            });

        } else {
           // Toast.makeText(getActivity().getApplicationContext(), "Network Closing Dialog", Snackbar.LENGTH_LONG).show();
          //  ConfigurationFile.hideDialog(progressDialog);
        }
    }

    private void notifyAdapter() {
        System.out.println("Category / Num = " + categoriesArray.size());
        categoriesAdapter = new CategoriesAdapter(getContext(), categoriesArray , true);
        gVCategories.setAdapter(categoriesAdapter);

    }


    private void initView() {
        categoriesArray = new ArrayList<>();
        gVCategories = (GridView) view.findViewById(R.id.gridview_categories);
        mIvEmptyView=(ImageView) view.findViewById(R.id.iv_category_empty);
        myLastVisiblePos=gVCategories.getFirstVisiblePosition();
        gVCategories.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int currentFirstVisPos = view.getFirstVisiblePosition();
                if(currentFirstVisPos > myLastVisiblePos  ) {
                    if(firstVisibleItem + visibleItemCount >= totalItemCount-1) {
                        if (!isLoading && !next_page.equals("")) {
                            isLoading = true;

                                position=totalItemCount;
                                getCategories();
                        }

                    }
                }
                if(currentFirstVisPos < myLastVisiblePos) {
                    //scroll up
                    //Toast.makeText(getActivity().getApplicationContext(), "Scroll Up:"+totalItemCount, Snackbar.LENGTH_LONG).show();
                }
                myLastVisiblePos = currentFirstVisPos;
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        View view;
        gVCategories=null;
      //  categoriesArray.clear();
        categoriesAdapter=null;
         fontChanger=null;
        mActivity=null;
    }
}
