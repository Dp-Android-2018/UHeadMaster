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
import com.dp.uheadmaster.activites.CourseLearn;
import com.dp.uheadmaster.adapters.AnnouncementAdapter;
import com.dp.uheadmaster.adapters.QuestionAnswerAdapter;
import com.dp.uheadmaster.models.AnnouncementData;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.response.AnnounceMentResponse;
import com.dp.uheadmaster.models.response.QuestionsResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 24/09/2017.
 */

public class AnnouncementsFragment extends Fragment {

    private RecyclerView recyclerQustions;
    private LinearLayoutManager mLayoutManager;
    private ProgressDialog progressDialog;
    private SharedPrefManager sharedPrefManager;
     private AnnouncementAdapter announcementAdapter;
    private ArrayList<AnnouncementData>announcementDatas;
    private TextView emptyView;
    private String next_page=null;
    private int pageId=0;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int position=0;
    private ImageView ivEmptyView;
    View v;
    private FontChangeCrawler fontChanger;
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
        v=inflater.inflate(R.layout.frag_questions_answers_layout,container,false);
        initializeUi(v);

        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        if(CourseLearn.enrolled) {
            getAnnounceMents();
        }else {
            recyclerQustions.setVisibility(View.GONE);

            ivEmptyView.setImageResource(R.drawable.ic_not_enrolled);
            ivEmptyView.setVisibility(View.VISIBLE);
        }
    }

    public void initializeUi(View v){
         emptyView=(TextView)v.findViewById(R.id.empty_view) ;
        announcementDatas=new ArrayList<>();
        sharedPrefManager=new SharedPrefManager(getActivity().getApplicationContext());
        recyclerQustions = (RecyclerView) v.findViewById(R.id.recycler1);
        ivEmptyView = (ImageView)v.findViewById(R.id.iv_resources_empty);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerQustions.setLayoutManager(mLayoutManager);
        recyclerQustions.setItemAnimator(new DefaultItemAnimator());



        recyclerQustions.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            getAnnounceMents();
                        }
                    }
                }
            }
        });
    }


    public void getAnnounceMents(){
     //   Toast.makeText(getActivity().getApplicationContext(), "Course Id:"+CourseLearn.courseID, Toast.LENGTH_SHORT).show();
        if (NetWorkConnection.isConnectingToInternet(mActivity.getApplicationContext(),mActivity.findViewById(R.id.content))) {
            progressDialog = ConfigurationFile.showDialog(getActivity());

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<AnnounceMentResponse> call = apiService.getAnnouncements(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), CourseLearn.courseID,pageId);
            call.enqueue(new Callback<AnnounceMentResponse>() {
                @Override
                public void onResponse(Call<AnnounceMentResponse> call, Response<AnnounceMentResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    AnnounceMentResponse announceMentResponse=response.body();
                    try {

                        if (announceMentResponse.getStatus() == 200) {

                            announcementDatas.clear();
                          //  Toast.makeText(getActivity().getApplicationContext(), "sUCCESS", Toast.LENGTH_LONG).show();
                            if(!announceMentResponse.getAnnouncements().getAnnouncementData().isEmpty()) {
                            //    Toast.makeText(getActivity().getApplicationContext(), "Fill", Toast.LENGTH_LONG).show();
                                for (int i = 0; i < announceMentResponse.getAnnouncements().getAnnouncementData().size(); i++) {
                                  //  Toast.makeText(getActivity().getApplicationContext(), "sIZE :"+announceMentResponse.getAnnouncements().getAnnouncementData().size(), Toast.LENGTH_LONG).show();
                                    announcementDatas.add(announceMentResponse.getAnnouncements().getAnnouncementData().get(i));
                                }

                                loading = true;
                                if (announceMentResponse.getAnnouncements().getNextPage()!= null && !announceMentResponse.getAnnouncements().getNextPage().equals("")) {
                                    next_page = announceMentResponse.getAnnouncements().getNextPage();
                                    pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
                                } else {
                                    next_page = null;

                                }

                                announcementAdapter=new AnnouncementAdapter(getActivity().getApplicationContext(),announcementDatas);
                                recyclerQustions.setAdapter(announcementAdapter);
                               // announcementAdapter.notifyDataSetChanged();
                            }else {
                              //  Toast.makeText(getActivity().getApplicationContext(), "Empty", Toast.LENGTH_LONG).show();
                             /*   emptyView.setVisibility(View.VISIBLE);
                                emptyView.setText(R.string.no_responses);
                                recyclerQustions.setVisibility(View.GONE);*/
                                recyclerQustions.setVisibility(View.GONE);

                                ivEmptyView.setImageResource(R.drawable.ic_announcement_empty);
                                ivEmptyView.setVisibility(View.VISIBLE);
                            }

                        }else {
                         //   Toast.makeText(getActivity().getApplicationContext(), " "+announceMentResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }catch (NullPointerException ex){
                        System.out.println("Exception :"+ex.getMessage());
                        ex.printStackTrace();
                    }catch (Exception ex){
                        System.out.println("Exception :"+ex.getMessage());
                        ex.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<AnnounceMentResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);
                    Snackbar.make(mActivity.findViewById(R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });




        } else {
            ConfigurationFile.hideDialog(progressDialog);
            Snackbar.make(mActivity.findViewById(R.id.content),getActivity().getResources().getString(R.string.internet_message),Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }
}
