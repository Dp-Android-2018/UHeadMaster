package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.dp.uheadmaster.adapters.WishListAdapter;
import com.dp.uheadmaster.interfaces.RefershWishList;
import com.dp.uheadmaster.models.CategoryModel;
import com.dp.uheadmaster.models.CourseModel;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.response.WishListResponse;
import com.dp.uheadmaster.models.response.WishListResponse;
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

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by DELL on 22/08/2017.
 */

public class WishListFrag extends Fragment implements RefershWishList{

    View view;
    private static GridView gvWishList;
    private ProgressDialog progressDialog;
    private static List<CourseModel> wishCoursesList;
    private SharedPrefManager sharedPrefManager;
    WishListAdapter wishListAdapter;
    int myLastVisiblePos;// global variable of activity
    private String next_page="";
    private int pageId = 0;
    private boolean isLoading;
    private  int position=0;
    private FontChangeCrawler fontChanger;
    private ImageView ivEmptyView;
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
        view = inflater.inflate(R.layout.fragment_wish_list_layout, container, false);


        initializeUi(view);

        return view;
    }
    public void initializeUi(View v){
        wishCoursesList = new ArrayList<>();
        gvWishList=(GridView) v.findViewById(R.id.gridview_wish_list);
        ivEmptyView=(ImageView) v.findViewById(R.id.iv_courses_empty);
        myLastVisiblePos=gvWishList.getFirstVisiblePosition();
        gvWishList.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                            getWishList(getContext());
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

    public void getWishList(final Context context) {
        if (NetWorkConnection.isConnectingToInternet(context,mActivity.findViewById(R.id.content))) {
            progressDialog = ConfigurationFile.showDialog((Activity) context);
            sharedPrefManager = new SharedPrefManager(context);
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<WishListResponse> call = apiService.getWishList(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),pageId);
            System.out.println("Category / id : " + sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            System.out.println("Category / authro : " + sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN));
            call.enqueue(new Callback<WishListResponse>() {
                @Override
                public void onResponse(Call<WishListResponse> call, Response<WishListResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    try {

                    System.out.println("Category Body : "+response.body().getCoursesList());
                    if (response.body().getStatus() == 200) {
                        // use response data and do some fancy stuff :)
                        WishListResponse wishListResponse = response.body();
                        // Snackbar.success(getContext(), "Done! cat.Num = " + WishListResponse.getCategoriesList().size(), Toast.LENGTH_LONG, true).show();
                        wishCoursesList = wishListResponse.getCoursesList();
                        if(wishCoursesList.size()==0){
                            gvWishList.setVisibility(View.GONE);
                            ivEmptyView.setVisibility(View.VISIBLE);
                            //ivEmptyView.setText(getActivity().getResources().getString(R.string.no_wish_list_courses));
                        }else {
                            gvWishList.setVisibility(View.VISIBLE);
                            ivEmptyView.setVisibility(View.GONE);
                        }
                        notifyAdapter(context);
                        gvWishList.setSelection(position);
                        isLoading = false;

                        if (!wishListResponse.getNextPagePath().equals("")) {
                            next_page = wishListResponse.getNextPagePath();
                            pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
                        } else {
                            next_page = "";
                        }

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
                public void onFailure(Call<WishListResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                   // Snackbar.error(context, t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println("Category / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }

    private void notifyAdapter(Context context) {
        wishListAdapter = new WishListAdapter(context, wishCoursesList);
        gvWishList.setAdapter(wishListAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getWishList(getContext());

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }

    @Override
    public void refershList(Context context) {
        getWishList(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity=null;
        gvWishList=null;
        progressDialog=null;
        wishCoursesList.clear();
         sharedPrefManager=null;
         wishListAdapter=null;
        fontChanger=null;
        mActivity=null;
    }
}
