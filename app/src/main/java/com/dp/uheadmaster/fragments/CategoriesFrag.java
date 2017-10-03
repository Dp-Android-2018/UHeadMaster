package com.dp.uheadmaster.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.CategoriesAdapter;
import com.dp.uheadmaster.models.CategoryModel;
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

public class CategoriesFrag extends Fragment {


    View view;
    private GridView gVCategories;
    private ProgressDialog progressDialog;
    private static List<CategoryModel> categoriesArray = new ArrayList<>();
    private SharedPrefManager sharedPrefManager;
    CategoriesAdapter categoriesAdapter;
    public static int categoryId=0;
    int myLastVisiblePos;// global variable of activity
    private String next_page;
    private int pageId = 0;
    private boolean isLoading;
    private  int position=0;

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
        if (NetWorkConnection.isConnectingToInternet(getContext())) {
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
                    try {

                        System.out.println("Category Body : "+response.body().getCategoriesList().size());

                    if (response.body().getStatus() == 200) {
                        // use response data and do some fancy stuff :)
                        CategoriesResponse categoriesResponse = response.body();
                       // Toasty.success(getContext(), "Done! cat.Num = " + categoriesResponse.getCategoriesList().size(), Toast.LENGTH_LONG, true).show();
                        categoriesArray = categoriesResponse.getCategoriesList();
                        notifyAdapter();
                        gVCategories.setSelection(position);
                        isLoading = false;

                        if (categoriesResponse.getNextPage() != null) {
                            next_page = categoriesResponse.getNextPage();
                            pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
                        } else {
                            next_page = null;
                        }
                       // Toast.makeText(getApplicationContext(), "Next :"+next_page, Toast.LENGTH_SHORT).show();


                    } else {
                        // parse the response body …
                        System.out.println("Category /error Code message :" + response.body().getMessage());
                        Toasty.error(getContext(), response.body().getMessage(), Toast.LENGTH_LONG, true).show();


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

                    Toasty.error(getContext(), t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println("Category / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }

    private void notifyAdapter() {
        System.out.println("Category / Num = " + categoriesArray.size());
        categoriesAdapter = new CategoriesAdapter(getContext(), categoriesArray , true);
        gVCategories.setAdapter(categoriesAdapter);

    }


    private void initView() {
        gVCategories = (GridView) view.findViewById(R.id.gridview_categories);
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
                        if (!isLoading && next_page != null) {
                            isLoading = true;

                                position=totalItemCount;
                                getCategories();
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
    }
}
