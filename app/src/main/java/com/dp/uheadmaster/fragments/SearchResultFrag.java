package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.SubCategoriesAct;
import com.dp.uheadmaster.adapters.CoursesSearchResultAdapter;
import com.dp.uheadmaster.models.CourseModel;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.response.SearchCoursesResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;
import com.google.gson.Gson;

import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 06/09/2017.
 */

public class SearchResultFrag extends Fragment {
    private String keyWord="";
    private GridView gvSearchResult;
    private ImageView ivEmptySearch;
    private int pageId=0;
    private CoursesSearchResultAdapter searchResultAdapter;
    private ArrayList<CourseModel> coursesSearchResult;
    private SharedPrefManager sharedPrefManager;
    int myLastVisiblePos;// global variable of activity
    private String next_page="";
    private boolean isLoading;
    private  int position=0;
    private FontChangeCrawler fontChanger;
    private View v;
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
         v=inflater.inflate(R.layout.fragment_search_result_layout,container,false);
        if(getArguments()!=null){
            keyWord=getArguments().getString("keyword");
        }
        initializeUi(v);
        searchForCourses();
        return v;
    }
    public void initializeUi(View v){
        sharedPrefManager =new SharedPrefManager(getActivity().getApplicationContext());
        gvSearchResult=(GridView)v.findViewById(R.id.gv_search_result);

        ivEmptySearch=(ImageView) v.findViewById(R.id.iv_search_empty);
        coursesSearchResult=new ArrayList<>();
        searchResultAdapter = new CoursesSearchResultAdapter(getContext(), coursesSearchResult);
        gvSearchResult.setAdapter(searchResultAdapter);
        myLastVisiblePos=gvSearchResult.getFirstVisiblePosition();
        gvSearchResult.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                            searchForCourses();
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

    public void searchForCourses()
    {
       // Toasty.success(getActivity().getApplicationContext(),keyWord+"\n"+SearchCategoriesFrag.search_category_id+"\n"+SearchSubCategoriesFrag.search_sub_category_id, Toast.LENGTH_LONG).show();

        gvSearchResult.setVisibility(View.VISIBLE);
        ivEmptySearch.setVisibility(View.GONE);
        if (NetWorkConnection.isConnectingToInternet(getActivity().getApplicationContext(),mActivity.findViewById(R.id.content))) {
            EndPointInterfaces apiServices = ApiClient.getClient().create(EndPointInterfaces.class);
            Call<SearchCoursesResponse> call = apiServices.coursesSearch(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), keyWord, SearchCategoriesFrag.search_category_id, SearchSubCategoriesFrag.search_sub_category_id, pageId);
            call.enqueue(new Callback<SearchCoursesResponse>() {
                @Override
                public void onResponse(Call<SearchCoursesResponse> call, Response<SearchCoursesResponse> response) {

                    SearchCoursesResponse searchCoursesResponse=response.body();
                    System.out.println("Data Json:"+ new Gson().toJson(response));
                    if( searchCoursesResponse.getStatus()==200)
                    {
                        if(searchCoursesResponse.getCourses().size()>0) {
                            for (int i = 0; i < searchCoursesResponse.getCourses().size(); i++) {
                                coursesSearchResult.add(searchCoursesResponse.getCourses().get(i));
                            }
                            searchResultAdapter.notifyDataSetChanged();
                            gvSearchResult.setSelection(position);
                            isLoading = false;

                            if (!searchCoursesResponse.getNextPageUrl() .equals("")) {
                                next_page = searchCoursesResponse.getNextPageUrl();
                                pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
                            } else {
                                next_page = "";
                            }
                        }else {
                            gvSearchResult.setVisibility(View.GONE);
                            ivEmptySearch.setVisibility(View.VISIBLE);
                        }
                    }

                }

                @Override
                public void onFailure(Call<SearchCoursesResponse> call, Throwable t) {

                }
            });

        }
    }
}
