package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.AnnounceMentAct;
import com.dp.uheadmaster.activites.MoreCoursesInstructorAct;
import com.dp.uheadmaster.adapters.AnnouncementAdapter;
import com.dp.uheadmaster.adapters.MoreCoursesAdapter;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.InstructorCourse;
import com.dp.uheadmaster.models.response.InstructorCoursesResponse;
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
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.dp.uheadmaster.R.id.btn_load_more;
import static com.dp.uheadmaster.R.id.courses;

/**
 * Created by DELL on 17/10/2017.
 */

public class InstructorDashboard extends Fragment{


    private ArrayList<String>keys;
    private ArrayList<String>values;
    private TextView tvStudentsnum;
    private TextView tvRateValue;
    private HorizontalBarChart chart;
    private BarDataSet set1;
    ArrayList<IBarDataSet> dataSets;
    BarData data;
    private ArrayList<BarEntry> valueSet1 = new ArrayList<>();
    @BindView(R.id.btn_load_more)
    Button loadMorebtn;

    @BindView(R.id.recycler_courses)
    RecyclerView recyclerCourse;

    private ImageView ivEmptyView;

    FontChangeCrawler fontChanger;
    private ProgressDialog progressDialog,progressDialog2;
    private SharedPrefManager sharedPrefManager;
    private int  instructorId=1;
    private ArrayList<InstructorCourse> courses;
    private int pageId=0;
    private String next_page="";
    private MoreCoursesAdapter adapter;
    private Activity mActivity;
    View v;
    @Override
    public void onAttach(Context context) {
        super.onAttach(CalligraphyContextWrapper.wrap(context));
        // super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
        v=inflater.inflate(R.layout.instructor_dashboard_fragment_layout,container,false);
        ButterKnife.bind(this,v);
        keys=new ArrayList<>();
        values=new ArrayList<>();
        tvStudentsnum=(TextView)v.findViewById(R.id.tv_total_students_num);
        ivEmptyView = (ImageView) v.findViewById(R.id.iv_courses_empty);
        tvRateValue=(TextView)v.findViewById(R.id.tv_rating_value);
        chart = (HorizontalBarChart) v.findViewById(R.id.chart);
        sharedPrefManager=new SharedPrefManager(getActivity());
        courses = new ArrayList<>();
        recyclerCourse.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerCourse.setItemAnimator(new DefaultItemAnimator());

        adapter=new MoreCoursesAdapter(getActivity().getApplicationContext(),courses);
        recyclerCourse.setAdapter(adapter);
        getInstructorDashboard();
        return v;
    }

    @OnClick(R.id.btn_load_more)
    public void loadMoreCourses(){
        Intent i=new Intent(getActivity(), MoreCoursesInstructorAct.class);
        startActivity(i);
    }



    public void getInstructorCourses() {
        if (NetWorkConnection.isConnectingToInternet(mActivity.getApplicationContext(),mActivity.findViewById(R.id.content))) {
            progressDialog = ConfigurationFile.showDialog(getActivity());


            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            // Toast.makeText(getActivity(), " Iddddddddd:"+sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), Toast.LENGTH_SHORT).show();

            Call<InstructorCoursesResponse> call = apiService.getInstructorCourses(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),"else", pageId);
            call.enqueue(new Callback<InstructorCoursesResponse>() {
                @Override
                public void onResponse(Call<InstructorCoursesResponse> call, Response<InstructorCoursesResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    InstructorCoursesResponse instructorCoursesResponse = response.body();
                    try {


                        //  Toast.makeText(getActivity(), "Status Dashboard :"+instructorCoursesResponse.getStatusCode(), Toast.LENGTH_SHORT).show();
                        if (instructorCoursesResponse.getStatusCode() == 200) {

                            for (int i = 0; i < instructorCoursesResponse.getCourses().size(); i++) {

                                courses.add(instructorCoursesResponse.getCourses().get(i));
                            }
                          //  Toast.makeText(getActivity().getApplicationContext(), " "+courses.size()s, Toast.LENGTH_SHORT).show();


                            if(courses.size()>0){
                                recyclerCourse.setVisibility(View.VISIBLE);
                                ivEmptyView.setVisibility(View.GONE);
                            }else {
                                recyclerCourse.setVisibility(View.GONE);
                                ivEmptyView.setVisibility(View.VISIBLE);
                                loadMorebtn.setVisibility(View.GONE);

                            }
                            adapter.notifyDataSetChanged();
                           // updateUi();

                            if (!instructorCoursesResponse.getNextPageUrl().equals("")) {
                                next_page = instructorCoursesResponse.getNextPageUrl();
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


                }

                @Override
                public void onFailure(Call<InstructorCoursesResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(mActivity.findViewById(R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });

        } else {
         //   Snackbar.make(v, getString(R.string.check_internet_connection), Snackbar.LENGTH_LONG).show();
        }
    }




    public void  getInstructorDashboard(){
        if (NetWorkConnection.isConnectingToInternet(mActivity.getApplicationContext(),mActivity.findViewById(R.id.content))) {
            progressDialog2 = ConfigurationFile.showDialog(getActivity());

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);


            Call<JsonElement> call = apiService.getInstructorDashboard(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    ConfigurationFile.hideDialog(progressDialog2);
                    JsonElement element=response.body();

                    try {

                        JSONObject object=new JSONObject(element.toString());
                        int status=object.getInt("status");
                        if (status == 200) {
                            tvStudentsnum.setText(String.valueOf(object.getInt("students")));
                            tvRateValue.setText(String.valueOf(object.getDouble("rates")));
                            JSONObject json=object.getJSONObject("chart");
                            Iterator<String> iter = json.keys();
                            int i=0;
                            while (iter.hasNext()) {
                                String key = iter.next();
                                keys.add(key);
                                try {
                                    String value = json.getString(key);
                                    values.add(value);
                                } catch (JSONException e) {
                                    // Something went wrong!
                                }
                                i++;
                            }


                            setChartData(values);
                            getInstructorCourses();

                        }else {
                            Snackbar.make(mActivity.findViewById(R.id.content),object.getString("message"),Snackbar.LENGTH_LONG).show();
                            getInstructorCourses();
                        }
                    }catch (NullPointerException ex){
                        ex.printStackTrace();
                        getInstructorCourses();
                    }catch (Exception ex){
                        ex.printStackTrace();
                        getInstructorCourses();

                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog2);

                    Snackbar.make(mActivity.findViewById(R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println(" Fialer :" + t.getMessage());
                }
            });

        } else {
           // Snackbar.make(v,getString(R.string.check_internet_connection),Snackbar.LENGTH_LONG).show();
        }
    }



    private void setChartData(ArrayList<String>valuesData) {
        try {

            for (int i=0;i<valuesData.size();i++){
                BarEntry v1e1 = new BarEntry(i+1, Float.parseFloat(valuesData.get(i)));
                valueSet1.add(v1e1);
            }

            set1 = new BarDataSet(valueSet1, "The year 2017");
            int[]colors=new int[valuesData.size()];
            for (int i=0;i<colors.length;i++){
                colors[i]= Color.parseColor("#00887A");
            }
            set1.setColors(colors);
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
            String[] values = new String[keys.size()];
            values=keys.toArray(values);
            XAxis xAxis = chart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormatter(values));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


            YAxis yl = chart.getAxisLeft();
            yl.setDrawAxisLine(true);
            yl.setDrawGridLines(true);
            yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
            yl.setAxisMaximum(5000);
//        yl.setInverted(true);

            YAxis yr = chart.getAxisRight();
            yr.setDrawAxisLine(true);
            yr.setDrawGridLines(false);
            yr.setAxisMinimum(0f);
            yr.setAxisMaximum(5000);// this replaces setStartAtZero(true)

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




    class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            try {

                return mValues[(int) value];
            }catch (ArrayIndexOutOfBoundsException ex){

                ex.printStackTrace();
            }
            return "";
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }
}
