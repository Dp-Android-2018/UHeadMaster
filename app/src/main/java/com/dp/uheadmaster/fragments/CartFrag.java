package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.CartCoursesAdapter;
import com.dp.uheadmaster.adapters.WishListAdapter;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.interfaces.CheckOutDialogInterface;
import com.dp.uheadmaster.models.CourseModel;
import com.dp.uheadmaster.models.response.CartListResponse;
import com.dp.uheadmaster.models.response.CartListResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 21/08/2017.
 */

public class CartFrag extends Fragment {

    private RecyclerView recyclerCourses;
    private android.support.v7.widget.LinearLayoutManager mLayoutManager;
    private CartCoursesAdapter cartCoursesAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button btnCartCheck;
    public CheckOutDialogInterface verify = null;
    public static TextView tvTotalPrice, tvCourseCount;
    private ProgressDialog progressDialog;
    private static List<CourseModel> cartCoursesList = new ArrayList<>();
    private SharedPrefManager sharedPrefManager;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private String next_page=null;
    private int pageId=0;
    private int position=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart_layout, container, false);
        initializeUi(v);
        getCartList();
        initEventDriven();
        return v;
    }

    private void initEventDriven() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                next_page=null;
                pageId = 0;
                getCartList();
            }
        });

        btnCartCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify.verifyDialog(1);
            }
        });
    }

    public void getCartList() {
        cartCoursesList.clear();
        if (NetWorkConnection.isConnectingToInternet(getContext())) {
            progressDialog = ConfigurationFile.showDialog(getActivity());
            sharedPrefManager = new SharedPrefManager(getContext());
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<CartListResponse> call = apiService.getCartist(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),pageId);
            System.out.println("Category / id : " + sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            System.out.println("Category / authro : " + sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN));
            call.enqueue(new Callback<CartListResponse>() {
                @Override
                public void onResponse(Call<CartListResponse> call, Response<CartListResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    try {


                        System.out.println("Cart Body : " + response.body().getCoursesList());
                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            CartListResponse cartListResponse = response.body();
                            // Toasty.success(getgetContext()(), "Done! cat.Num = " + CartListResponse.getCategoriesList().size(), Toast.LENGTH_LONG, true).show();
                            cartCoursesList = cartListResponse.getCoursesList();
                            notifyAdapter();
                            recyclerCourses.getLayoutManager().scrollToPosition(position);
                            loading = true;

                            if (cartListResponse.getNextPagePath()!= null) {
                                next_page = cartListResponse.getNextPagePath();
                                pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
                            } else {
                                next_page = null;

                            }

                        } else {
                            // parse the response body …
                            System.out.println("Cart /error Code message :" + response.body().getMessage());
                            Toasty.error(getContext(), response.body().getMessage(), Toast.LENGTH_LONG, true).show();
                        }
                    } catch (NullPointerException ex) {
                        System.out.println("NullPointerException :" + ex.getMessage());
                    } catch (Exception ex) {
                        System.out.println("Exception :" + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<CartListResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Toasty.error(getContext(), t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println("Cart / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    private void notifyAdapter() {
        cartCoursesAdapter = new CartCoursesAdapter(getActivity(), cartCoursesList);
        recyclerCourses.setAdapter(cartCoursesAdapter);
        tvCourseCount.setText(cartCoursesList.size() + getString(R.string.course_in_cart));


    }


    public void initializeUi(View v) {
        btnCartCheck = (Button) v.findViewById(R.id.btn_cart_check);
        tvCourseCount = (TextView) v.findViewById(R.id.tv_courses_count);
        tvTotalPrice = (TextView) v.findViewById(R.id.tv_courses_total_price);
        recyclerCourses = (RecyclerView) v.findViewById(R.id.rv_courses);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.activity_main_swipe_refresh_layout);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerCourses.setLayoutManager(mLayoutManager);
        recyclerCourses.setItemAnimator(new DefaultItemAnimator());
        if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) ){
            tvCourseCount.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            tvTotalPrice.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            btnCartCheck.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
        }else if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ){
            tvCourseCount.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            tvTotalPrice.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            btnCartCheck.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
        }
        CartFrag.tvTotalPrice.setText(getString(R.string.total_payment) + "0.0");
        tvCourseCount.setText(cartCoursesList.size() + getString(R.string.course_in_cart));
        recyclerCourses.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            getCartList();
                        }
                    }
                }
            }
        });


    }
}
