package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.ChangePasswordAct;
import com.dp.uheadmaster.activites.SubCategoriesAct;
import com.dp.uheadmaster.adapters.CoursesAdapter;
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

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 21/08/2017.
 */

public class SearchFrag extends Fragment{
    private SharedPrefManager sharedPrefManager;
    private String keyWord=null;
    private int pageId=0;
    private EditText etSearchKeyWord;
    private GridView gvSearchResult;
    private CoursesSearchResultAdapter searchResultAdapter;
    private ArrayList<CourseModel> coursesSearchResult;
    private FontChangeCrawler fontChanger;
    private  View v;
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_search_layout,container,false);
        initializeUi(v);
        etSearchKeyWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    searchForCourses();
                    return true;
                }
                return false;
            }
        });

        return v;
    }
    public void initializeUi(View v){
        sharedPrefManager =new SharedPrefManager(getActivity().getApplicationContext());
        etSearchKeyWord=(EditText)v.findViewById(R.id.et_search_keyword);
        gvSearchResult=(GridView)v.findViewById(R.id.gv_search_result);
        coursesSearchResult=new ArrayList<>();
        searchResultAdapter = new CoursesSearchResultAdapter(getContext(), coursesSearchResult);
        gvSearchResult.setAdapter(searchResultAdapter);
    }



    public void searchForCourses()
    {

        keyWord = etSearchKeyWord.getText().toString().trim();
       // Toast.makeText(getActivity().getApplicationContext(), " "+CategoriesFrag.categoryId+"\n"+SubCategoriesAct.subCategoryId, Toast.LENGTH_LONG).show();
        if (NetWorkConnection.isConnectingToInternet(getActivity().getApplicationContext(),mActivity.findViewById(R.id.content))) {
            EndPointInterfaces apiServices = ApiClient.getClient().create(EndPointInterfaces.class);
            Call<SearchCoursesResponse> call = apiServices.coursesSearch(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), keyWord, CategoriesFrag.categoryId, SubCategoriesAct.subCategoryId, pageId);
            call.enqueue(new Callback<SearchCoursesResponse>() {
                @Override
                public void onResponse(Call<SearchCoursesResponse> call, Response<SearchCoursesResponse> response) {

                    SearchCoursesResponse searchCoursesResponse=response.body();
                    System.out.println("Data Json:"+ new Gson().toJson(response));
                    if( searchCoursesResponse.getStatus()==200)
                    {
                        for (int i=0;i<searchCoursesResponse.getCourses().size();i++){
                            coursesSearchResult.add(searchCoursesResponse.getCourses().get(i));
                        }
                        searchResultAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onFailure(Call<SearchCoursesResponse> call, Throwable t) {

                }
            });

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }
}
