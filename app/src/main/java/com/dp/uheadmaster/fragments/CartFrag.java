package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.MainAct;
import com.dp.uheadmaster.adapters.CartCoursesAdapter;
import com.dp.uheadmaster.adapters.WishListAdapter;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.interfaces.CartPositionInterface;
import com.dp.uheadmaster.interfaces.CheckOutDialogInterface;
import com.dp.uheadmaster.models.CourseIDRequest;
import com.dp.uheadmaster.models.CourseModel;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.response.CartListResponse;
import com.dp.uheadmaster.models.response.CartListResponse;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 21/08/2017.
 */

public class CartFrag extends Fragment implements CartPositionInterface{

    private RecyclerView recyclerCourses;
    private android.support.v7.widget.LinearLayoutManager mLayoutManager;
    public static CartCoursesAdapter cartCoursesAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button btnCartCheck;
    public CheckOutDialogInterface verify = null;
    public static TextView tvTotalPrice, tvCourseCount;
    private ProgressDialog progressDialog;
    public static List<CourseModel> cartCoursesList ;
    private SharedPrefManager sharedPrefManager;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private String next_page="";
    private int pageId=0;
    private int position=0;
    public Context mContext;
    private FontChangeCrawler fontChanger;
    private Activity mactivity;
    private ImageView ivEmptyCart;
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
        v = inflater.inflate(R.layout.fragment_cart_layout, container, false);
        initializeUi(v);

        initEventDriven();
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        getCartList();
    }

    private void initEventDriven() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                next_page="";
                pageId = 0;
                getCartList();
            }
        });

        btnCartCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  verify.verifyDialog(1);
                com.dp.uheadmaster.dialogs.CheckOutDialog checkOutDialog = new com.dp.uheadmaster.dialogs.CheckOutDialog(mactivity);
                checkOutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                checkOutDialog.show();
            }
        });
    }

    public void getCartList() {
        cartCoursesList.clear();
        if (NetWorkConnection.isConnectingToInternet(mactivity.getApplicationContext(),mactivity.findViewById(R.id.content))) {
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
                            //Snackbar.success(getgetContext()(), "Done! cat.Num = " + CartListResponse.getCategoriesList().size(), Snackbar.LENGTH_LONG, true).show();
                            cartCoursesList = cartListResponse.getCoursesList();

                 //           cartCoursesList.clear();
                            if(cartCoursesList.size()==0){
                                recyclerCourses.setVisibility(View.GONE);
                                ivEmptyCart.setVisibility(View.VISIBLE);
                                btnCartCheck.setVisibility(View.GONE);
                            }else {
                                recyclerCourses.setVisibility(View.VISIBLE);
                                ivEmptyCart.setVisibility(View.GONE);
                            }
                            notifyAdapter();
                            recyclerCourses.getLayoutManager().scrollToPosition(position);
                            loading = true;

                            if (!cartListResponse.getNextPagePath().equals("")) {
                                next_page = cartListResponse.getNextPagePath();
                                pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
                            } else {
                                next_page = "";

                            }

                        } else {
                            // parse the response body …
                            System.out.println("Cart /error Code message :" + response.body().getMessage());
                           Snackbar.make(mactivity.findViewById(R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG).show();
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

                  // Snackbar.error(getContext(), t.getMessage(), Snackbar.LENGTH_LONG, true).show();
                    System.out.println("Cart / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    private void notifyAdapter() {
        cartCoursesAdapter = new CartCoursesAdapter(getContext(), cartCoursesList);
        recyclerCourses.setAdapter(cartCoursesAdapter);
        tvCourseCount.setText(cartCoursesList.size() + getString(R.string.course_in_cart));


    }


    public void initializeUi(View v) {
        cartCoursesList=new ArrayList<>();
        ivEmptyCart=(ImageView) v.findViewById(R.id.iv_cart_empty) ;
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

                    if (loading&& !next_page.equals(""))
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

    @Override
    public void getCartPosition(int id) {
            removefromCart(id);
    }





    private void removefromCart(final int courseID) {
        if (NetWorkConnection.isConnectingToInternet(mContext,mactivity.findViewById(R.id.content))) {

            progressDialog = ConfigurationFile.showDialog(getActivity());
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            CourseIDRequest request = new CourseIDRequest(courseID);
            Call<DefaultResponse> call = apiService.addOrRemoveFromCartList(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), request);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    try {
                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)

                            removeFromList(courseID);
                           Snackbar.make(mactivity.findViewById(R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG).show();


                        } else {
                            // parse the response body …
                            System.out.println("cart adapter      /error Code message :" + response.body().getMessage());
                           Snackbar.make(mactivity.findViewById(R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG).show();


                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                   Snackbar.make(mactivity.findViewById(R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println("cart adapter / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }

    private void removeFromList(int courseID) {
        for (int i = 0; i < cartCoursesList.size(); i++) {

            if (courseID == cartCoursesList.get(i).getId()) {
                cartCoursesList.remove(i);
                calculateTotalPrice();
                cartCoursesAdapter.notifyDataSetChanged();
                return;
            }
        }



    }





    private void calculateTotalPrice() {
        double totalPrice = 0.0;
        String currency = "";
        for (int i = 0; i < cartCoursesList.size(); i++) {
            if(cartCoursesList.get(i).getPrice() !=null && !cartCoursesList.get(i).getPrice().equals("")){
                totalPrice +=Double.parseDouble(cartCoursesList.get(i).getPrice());
            }
            currency = cartCoursesList.get(0).getCurrency();
        }
        CartFrag.tvCourseCount.setText(cartCoursesList.size()+getActivity().getResources().getString(R.string.course_in_cart));
        CartFrag.tvTotalPrice.setText(getActivity().getResources().getString(R.string.total_payment)+totalPrice+" "+currency);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mactivity=activity;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext=null;
        mactivity=null;
        tvTotalPrice=null;
        tvCourseCount=null;
        cartCoursesAdapter=null;
        cartCoursesList.clear();
    }
}
