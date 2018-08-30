package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.MoreCoursesAdapter;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.InstructorCourse;
import com.dp.uheadmaster.models.response.InstructorCoursesResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 01/11/2017.
 */

public class InstructorDraftCoursesFrag extends Fragment {
    @Nullable
    @BindView(R.id.recycler_courses)
    RecyclerView recyclerCourse;
    private FontChangeCrawler fontChanger;
    @BindView(R.id.iv_courses_empty)
    ImageView ivEmptyView;
    private ProgressDialog progressDialog;
    private SharedPrefManager sharedPrefManager;
    private int  instructorId=1;
    private ArrayList<InstructorCourse> courses2;
    private int pageId=0;
    private String next_page="";
    private MoreCoursesAdapter adapter;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager mLayoutManager;
    private boolean loading = true;
    private int position=0;
    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }

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
        v=inflater.inflate(R.layout.frag_instructor_courses_layout,container,false);
        ButterKnife.bind(this,v);
        initalizeUi();
        return v;
    }
    public void initalizeUi(){
        sharedPrefManager=new SharedPrefManager(getActivity());
        courses2 = new ArrayList<>();
        mLayoutManager=new LinearLayoutManager(getActivity());
        recyclerCourse.setLayoutManager(mLayoutManager);
        recyclerCourse.setItemAnimator(new DefaultItemAnimator());
        adapter=new MoreCoursesAdapter(getActivity().getApplicationContext(),courses2);
        recyclerCourse.setAdapter(adapter);
        recyclerCourse.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            position=totalItemCount;
                            getInstructorCourses();
                        }
                    }
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getInstructorCourses();
    }

    public void getInstructorCourses() {
        if (NetWorkConnection.isConnectingToInternet(getActivity().getApplicationContext(),mActivity.findViewById(R.id.content))) {
            progressDialog = ConfigurationFile.showDialog(getActivity());


            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);

            Call<InstructorCoursesResponse> call = apiService.getInstructorCourses(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),"draft", pageId);
            call.enqueue(new Callback<InstructorCoursesResponse>() {
                @Override
                public void onResponse(Call<InstructorCoursesResponse> call, Response<InstructorCoursesResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    InstructorCoursesResponse instructorCoursesResponse = response.body();
                    try {


                        // Toast.makeText(getActivity(), "Status Dashboard :"+instructorCoursesResponse.getStatusCode(), Toast.LENGTH_SHORT).show();
                        if (instructorCoursesResponse.getStatusCode() == 200) {

                            for (int i = 0; i < instructorCoursesResponse.getCourses().size(); i++) {

                                if(instructorCoursesResponse.getCourses().get(i).getStatus().equals("draft"))

                                    courses2.add(instructorCoursesResponse.getCourses().get(i));
                            }
                            if(courses2.size()==0){
                                recyclerCourse.setVisibility(View.GONE);
                                ivEmptyView.setVisibility(View.VISIBLE);

                            }else {
                                recyclerCourse.setVisibility(View.VISIBLE);
                                ivEmptyView.setVisibility(View.GONE);
                            }

                            loading = true;
                            if (!instructorCoursesResponse.getNextPageUrl().equals("")) {
                                next_page = instructorCoursesResponse.getNextPageUrl();
                                pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
                            } else {
                                next_page = "";

                            }

                            if(courses2.isEmpty()&& !next_page.equals("")){
                                getInstructorCourses();
                            }

                           /* final Collator collator = Collator.getInstance(Locale.US);
                            if (!courses.isEmpty()) {
                                Collections.sort(courses, new Comparator<InstructorCourse>() {
                                    @Override
                                    public int compare(InstructorCourse o1, InstructorCourse o2) {
                                        return collator.compare(o1.getTitle(), o2.getTitle());
                                    }

                                });
                            }*/

                            adapter.notifyDataSetChanged();

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
         //   Snackbar.make(mActivity.findViewById(R.id.content), getString(R.string.check_internet_connection), Snackbar.LENGTH_LONG).show();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        courses2.clear();
        loading = true;
        next_page="";
        pageId=0;
    }
}
