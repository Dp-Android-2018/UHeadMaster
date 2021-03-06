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
import com.dp.uheadmaster.activites.SubCategoriesAct;
import com.dp.uheadmaster.adapters.CategoriesAdapter;
import com.dp.uheadmaster.models.CategoryModel;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.request.SubCategoryRequest;
import com.dp.uheadmaster.models.response.CategoriesResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 06/09/2017.
 */

public class SubCategoriesFrag extends Fragment {

    private GridView gVCategories;
    private ProgressDialog progressDialog;
    private static List<CategoryModel> categoriesArray = new ArrayList<>();
    private SharedPrefManager sharedPrefManager;
    CategoriesAdapter categoriesAdapter;
    private int categoryID = -1;
    private String categoryTitle = "";
    int myLastVisiblePos;// global variable of activity
    private String next_page;
    private int pageId = 0;
    private boolean isLoading;
    private  int position=0;
    private FontChangeCrawler fontChanger;
    private ImageView mIvEmptyView;
    View v;
    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }

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
        v=inflater.inflate(R.layout.frag_sub_categories_layout,container,false);
        sharedPrefManager = new SharedPrefManager(getActivity());
        if(getArguments()!=null) {
            categoryTitle = getArguments().getString("category_title");
            categoryID = getArguments().getInt("category_id");
        }
        initializeUi(v);
        return v;
    }




    public void initializeUi(View v){

        gVCategories = (GridView) v.findViewById(R.id.gridview_categories);
        mIvEmptyView=(ImageView) v.findViewById(R.id.iv_category_empty);
        myLastVisiblePos = gVCategories.getFirstVisiblePosition();
        gVCategories.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int currentFirstVisPos = view.getFirstVisiblePosition();
                if(currentFirstVisPos > myLastVisiblePos  ) {
                    if(firstVisibleItem + visibleItemCount >= totalItemCount-1) {
                        if (!isLoading && next_page != null) {
                            isLoading = true;

                                position=totalItemCount;
                                   getSubCategories();

                        }

                    }
                }
                if(currentFirstVisPos < myLastVisiblePos) {
                    //scroll up
                    //Toast.makeText(getActivity().getApplicationContext(), "Scroll Up:"+totalItemCount, Toast.LENGTH_SHORT).show();
                }
                myLastVisiblePos = currentFirstVisPos;
            }
        });
        if (categoryID != -1) {
            getSubCategories();
        } else {
            Snackbar.make(mActivity.findViewById(R.id.content), getString(R.string.no_category_has_id), Snackbar.LENGTH_LONG).show();

        }
    }


    public void getSubCategories() {
        if (NetWorkConnection.isConnectingToInternet(getActivity().getApplicationContext(),mActivity.findViewById(R.id.content))) {

            progressDialog = ConfigurationFile.showDialog(getActivity());
            SubCategoryRequest subCategoryRequest = new SubCategoryRequest(categoryID);
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<CategoriesResponse> call = apiService.getSubCategories(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), subCategoryRequest,pageId);
            call.enqueue(new Callback<CategoriesResponse>() {
                @Override
                public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    System.out.println("Sub Category Body : " + response.body().getCategoriesList().size());
                    try {

                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            CategoriesResponse categoriesResponse = response.body();
                            // Snackbar.success(getContext(), "Done! cat.Num = " + categoriesResponse.getCategoriesList().size(), Toast.LENGTH_LONG, true).show();
                            categoriesArray = categoriesResponse.getCategoriesList();
                            if(categoriesArray.size()==0){
                                gVCategories.setVisibility(View.GONE);
                                mIvEmptyView.setVisibility(View.VISIBLE);
                            }else {
                                gVCategories.setVisibility(View.VISIBLE);
                                mIvEmptyView.setVisibility(View.GONE);
                            }
                            notifyAdapter();
                            gVCategories.setSelection(position);
                            isLoading = false;

                            if (categoriesResponse.getNextPage() != null && !categoriesResponse.getNextPage().equals("")) {
                                next_page = categoriesResponse.getNextPage();
                                pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
                            } else {
                                next_page = null;
                            }
                          //  Toast.makeText(getActivity().getApplicationContext(), "Next:"+next_page, Toast.LENGTH_SHORT).show();
                        } else {
                            // parse the response body …
                            System.out.println("Sub Category /error Code message :" + response.body().getMessage());
                            Snackbar.make(mActivity.findViewById(R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG).show();


                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(mActivity.findViewById(R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println("Sub Category / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }

    private void notifyAdapter() {
        System.out.println("Sub Category / Num = " + categoriesArray.size());
        categoriesAdapter = new CategoriesAdapter(getActivity(), categoriesArray, false);
        gVCategories.setAdapter(categoriesAdapter);

    }

}
