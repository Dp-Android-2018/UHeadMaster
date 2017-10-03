package com.dp.uheadmaster.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

/**
 * Created by DELL on 06/09/2017.
 */

public class SearchSubCategoriesFrag extends Fragment implements SwitchFragmentInterface{

    public static SwitchFragmentInterface delegate=null;
    public static int search_sub_category_id=0;
    private ProgressDialog progressDialog;
    private SharedPrefManager sharedPrefManager;
    private RecyclerView recyclerCategories;
    private android.support.v7.widget.LinearLayoutManager mLayoutManager;
    private CategorySearchAdapter categorySearchAdapter;
    private List<CategoryModel> subCategories;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private String next_page=null;
    private int pageId=0;
    private int position=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_category_search_layout,container,false);
        sharedPrefManager=new SharedPrefManager(getActivity().getApplicationContext());
        initializeUi(v);
        if(SearchCategoriesFrag.search_category_id!=0)
        getSubCategories();
        else
            Toasty.warning(getActivity().getApplicationContext(),getString(R.string.category_not_choosed),Toast.LENGTH_LONG).show();
        return v;
    }
    public void initializeUi(View v){
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

                    if (loading&&next_page!=null)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            position=totalItemCount;
                            getSubCategories();
                        }
                    }
                }
            }
        });
    }

    public void getSubCategories() {
        if (NetWorkConnection.isConnectingToInternet(getActivity())) {

            progressDialog = ConfigurationFile.showDialog(getActivity());
            SubCategoryRequest subCategoryRequest = new SubCategoryRequest(SearchCategoriesFrag.search_category_id);
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
                             //Toasty.success(getContext(), "Done! cat.Num = " + categoriesResponse.getCategoriesList().size(), Toast.LENGTH_LONG, true).show();
                            subCategories = categoriesResponse.getCategoriesList();
                            notifyAdapter();
                            recyclerCategories.getLayoutManager().scrollToPosition(position);
                            loading = true;

                            if (categoriesResponse.getNextPage()!= null) {
                                next_page = categoriesResponse.getNextPage();
                                pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
                            } else {
                                next_page = null;

                            }

                        } else {
                            // parse the response body …
                            System.out.println("Sub Category /error Code message :" + response.body().getMessage());
                            Toasty.error(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG, true).show();


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

                    Toasty.error(getActivity(), t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println("Sub Category / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }
    public void notifyAdapter()
    {
        categorySearchAdapter=new CategorySearchAdapter(getActivity().getApplicationContext(),subCategories,false);
        recyclerCategories.setAdapter(categorySearchAdapter);
    }

    @Override
    public void moveFragment(int checker) {
        delegate.moveFragment(2);
    }
}
