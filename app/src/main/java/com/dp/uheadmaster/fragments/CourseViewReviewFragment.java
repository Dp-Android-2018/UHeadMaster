package com.dp.uheadmaster.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.ViewReviewAdapter;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.ReviewModel;
import com.dp.uheadmaster.models.response.CourseReviewChartResponse;
import com.dp.uheadmaster.models.response.GetAllReviews;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 26/08/2017.
 */

@SuppressLint("ValidFragment")
public class CourseViewReviewFragment extends Fragment {
    private HorizontalBarChart chart;
    private BarDataSet set1;
    ArrayList<IBarDataSet> dataSets;
    BarData data;
    private RecyclerView recyclerReviews;
    private ViewReviewAdapter viewReviewAdapter;
    private LinearLayoutManager mLayoutManager;
    private TextView tvReview;
    private Context mcontext;
    private Activity mHostActivity;
    private ProgressDialog progressDialog,progressDialog2;
    private SharedPrefManager sharedPrefManager;
    private ArrayList<ReviewModel> reviews;
    private ArrayList<BarEntry> valueSet1 = new ArrayList<>();
    private int courseID = -1;
    public View v;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private String next_page="";
    private int pageId=0;
    private TextView mTvEmptyView;
    @SuppressLint("ValidFragment")
    public CourseViewReviewFragment(int courseID) {
        this.courseID = courseID;
        System.out.println("CourseViewReviewFragment / courseID : " + courseID);
    }

    private FontChangeCrawler fontChanger;

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
        v = inflater.inflate(R.layout.fragment_view_review_layout, container, false);
        initializeUi(v);
        if (courseID != 0 && courseID != -1) {
            getReviews(courseID);
            System.out.println("Initialization :"+courseID);

        }

        return v;
    }

    public void initializeUi(View v) {

        sharedPrefManager = new SharedPrefManager(mcontext);
        mTvEmptyView=(TextView) v.findViewById(R.id.tv_empty_view);
        tvReview = (TextView) v.findViewById(R.id.tv_review_text);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN)) {
            // tvRateValue.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            tvReview.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));


        } else if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR)) {
            // tvRateValue.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            tvReview.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));

        }
        recyclerReviews = (RecyclerView) v.findViewById(R.id.recyler_reviews);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerReviews.setLayoutManager(mLayoutManager);
        recyclerReviews.setItemAnimator(new DefaultItemAnimator());

        chart = (HorizontalBarChart) v.findViewById(R.id.chart);
        recyclerReviews.setOnScrollListener(new RecyclerView.OnScrollListener() {
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

                    if (loading&&!next_page.equals(""))
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            getReviews(courseID);
                        }
                    }
                }
            }
        });

    }

    public void getReviews(int courseId) {

        if (NetWorkConnection.isConnectingToInternet(mcontext,mHostActivity.findViewById(R.id.content))) {
            progressDialog = ConfigurationFile.showDialog(mHostActivity);


            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);

            System.out.println("Reviews : " + courseId);
            Call<GetAllReviews> call = apiService.getAllReviews(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), courseId,pageId);
            call.enqueue(new Callback<GetAllReviews>() {
                @Override
                public void onResponse(Call<GetAllReviews> call, Response<GetAllReviews> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    GetAllReviews getAllReviews = response.body();
                    try {
                        //Snackbar.error(getActivity().getApplicationContext(),"Success :"+response.body().getStatus(), Toast.LENGTH_LONG, true).show();
                        System.out.println("Response Reviews:" + new Gson().toJson(response));

                        if (getAllReviews.getStatusCode() == 200) {
                            reviews = new ArrayList<>();
                            for (int i = 0; i < getAllReviews.getRates().size(); i++) {
                                //   Snackbar.success(mcontext,"Title Data:"+reviews.size(),Toast.LENGTH_LONG).show();
                                reviews.add(getAllReviews.getRates().get(i));
                                System.out.println("Review Status :"+getAllReviews.getRates().get(i));
                            }

                            if(reviews.size()==0){
                                recyclerReviews.setVisibility(View.GONE);
                                mTvEmptyView.setVisibility(View.VISIBLE);
                            }else {
                                recyclerReviews.setVisibility(View.VISIBLE);
                                mTvEmptyView.setVisibility(View.GONE);
                            }

                            notifyAdapter();
                            loading = true;

                            if (!getAllReviews.getNextPageUrl().equals("")) {
                                next_page = getAllReviews.getNextPageUrl();
                                pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
                            } else {
                                next_page = "";

                            }

                        }
                    } catch (NullPointerException ex) {
                        System.out.println("Catch Exception1:" + ex.getMessage());


                    } catch (Exception ex) {
                        System.out.println("Catch Exception2:" + ex.getMessage());
                    }

                    getChartData(courseID);
                }

                @Override
                public void onFailure(Call<GetAllReviews> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);
                    getChartData(courseID);
                    Snackbar.make(mHostActivity.findViewById(R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });

        } else {
            Snackbar.make(mHostActivity.findViewById(R.id.content), getString(R.string.check_internet_connection), Snackbar.LENGTH_LONG).show();
        }
    }

    public void getChartData(int courseId) {
        if (NetWorkConnection.isConnectingToInternet(mcontext,mHostActivity.findViewById(R.id.content))) {
          //  progressDialog2 = ConfigurationFile.showDialog(mHostActivity);


            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);

            Call<CourseReviewChartResponse> call = apiService.getReviewChart(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), courseId);
            call.enqueue(new Callback<CourseReviewChartResponse>() {
                @Override
                public void onResponse(Call<CourseReviewChartResponse> call, Response<CourseReviewChartResponse> response) {
            //        ConfigurationFile.hideDialog(progressDialog2);

                    CourseReviewChartResponse getChartObj = response.body();
                    try {
                        //Snackbar.error(getActivity().getApplicationContext(),"Success :"+response.body().getStatus(), Toast.LENGTH_LONG, true).show();
                        System.out.println("Response Reviews:" + new Gson().toJson(response));

                        if (getChartObj.getStatus() == 200) {
                            setChartData(getChartObj);
                        }
                    } catch (NullPointerException ex) {
                        System.out.println("Catch Exception1:" + ex.getMessage());


                    } catch (Exception ex) {
                        System.out.println("Catch Exception2:" + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<CourseReviewChartResponse> call, Throwable t) {
               //     ConfigurationFile.hideDialog(progressDialog2);

                    Snackbar.make(mHostActivity.findViewById(R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });

        } else {
            Snackbar.make(mHostActivity.findViewById(R.id.content), getString(R.string.check_internet_connection), Snackbar.LENGTH_LONG).show();
        }
    }

    private void setChartData(CourseReviewChartResponse obj) {
        try {
            BarEntry v1e1 = new BarEntry(1, Float.parseFloat(obj.getContentRate()));
            valueSet1.add(v1e1);
            System.out.println("Chart : content" + Float.parseFloat(obj.getContentRate()));
            BarEntry v1e2 = new BarEntry(2, Float.parseFloat(obj.getProviderRate()));
            valueSet1.add(v1e2);
            System.out.println("Chart :provider " + Float.parseFloat(obj.getProviderRate()));

            BarEntry v1e3 = new BarEntry(3, Float.parseFloat(obj.getInstructorRate()));
            valueSet1.add(v1e3);
            System.out.println("Chart : instructor" + Float.parseFloat(obj.getInstructorRate()));

            ///////////////////
            set1 = new BarDataSet(valueSet1, "The year 2017");

            set1.setColors(Color.parseColor("#00887A"), Color.parseColor("#00887A"), Color.parseColor("#00887A"));
            if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN)) {
                set1.setValueTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font2"));

            } else if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR)) {
                set1.setValueTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            }
            dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            data = new BarData(dataSets);
            data.setValueTextColor(R.color.black);

            data.setBarWidth(0.2f);
            // hide Y-axis
            YAxis left = chart.getAxisLeft();
            left.setDrawLabels(false);

            // custom X-axis labels
            String[] values = new String[]{" ", "content rate", " provider rate", "instructor rate"};
            XAxis xAxis = chart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormatter(values));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawAxisLine(true);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f);

            YAxis yl = chart.getAxisLeft();
            yl.setDrawAxisLine(true);
            yl.setDrawGridLines(true);
            yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
            yl.setAxisMaximum(5);
//        yl.setInverted(true);

            YAxis yr = chart.getAxisRight();
            yr.setDrawAxisLine(true);
            yr.setDrawGridLines(false);
            yr.setAxisMinimum(0f);
            yr.setAxisMaximum(5);// this replaces setStartAtZero(true)

//        yr.setInverted(true);
            //chart.getXAxis().setEnabled(false);
            chart.getXAxis().setDrawGridLines(false);
            yl.setEnabled(false);
            yr.setEnabled(true);
            chart.setData(data);

            // custom description
            Description description = new Description();
            description.setText("Rating");
            chart.setDescription(description);

            // hide legend
            chart.getLegend().setEnabled(false);

            chart.animateY(1000);
            chart.invalidate();

        } catch (Exception e) {
            System.out.println("Chart Data :"+e.getMessage());
        }

    }

    private void notifyAdapter() {
        // Snackbar.success(mcontext,"size Data ZZZZZ:"+reviews.size(),Toast.LENGTH_LONG).show();
        viewReviewAdapter = new ViewReviewAdapter(mcontext, reviews);
        recyclerReviews.setAdapter(viewReviewAdapter);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcontext = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHostActivity = activity;
    }

    class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }

    }


}
