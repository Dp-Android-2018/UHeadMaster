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
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.AnnouncementAdapter;
import com.dp.uheadmaster.adapters.QuestionAnswerAdapter;
import com.dp.uheadmaster.models.AnnouncementData;
import com.dp.uheadmaster.models.response.AnnounceMentResponse;
import com.dp.uheadmaster.models.response.QuestionsResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
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
    private int courseId;
    private AnnouncementAdapter announcementAdapter;
    private ArrayList<AnnouncementData>announcementDatas;
    private TextView emptyView;
    private String next_page=null;
    private int pageId=0;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int position=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag_questions_answers_layout,container,false);
        initializeUi(v);
        getAnnounceMents();
        return v;

    }

    public void initializeUi(View v){
        courseId=getActivity().getIntent().getIntExtra("CourseId",0);
        emptyView=(TextView)v.findViewById(R.id.empty_view) ;
        announcementDatas=new ArrayList<>();
        sharedPrefManager=new SharedPrefManager(getActivity().getApplicationContext());
        recyclerQustions = (RecyclerView) v.findViewById(R.id.recycler1);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerQustions.setLayoutManager(mLayoutManager);
        recyclerQustions.setItemAnimator(new DefaultItemAnimator());
        announcementAdapter=new AnnouncementAdapter(getActivity().getApplicationContext(),announcementDatas);
        recyclerQustions.setAdapter(announcementAdapter);


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
        if (NetWorkConnection.isConnectingToInternet(getContext())) {
            progressDialog = ConfigurationFile.showDialog(getActivity());

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<AnnounceMentResponse> call = apiService.getAnnouncements(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),36,pageId);
            call.enqueue(new Callback<AnnounceMentResponse>() {
                @Override
                public void onResponse(Call<AnnounceMentResponse> call, Response<AnnounceMentResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    AnnounceMentResponse announceMentResponse=response.body();
                    try {

                        if (announceMentResponse.getStatus() == 200) {

                            if(!announceMentResponse.getAnnouncements().getAnnouncementData().isEmpty()) {
                                for (int i = 0; i < announceMentResponse.getAnnouncements().getAnnouncementData().size(); i++) {
                                    announcementDatas.add(announceMentResponse.getAnnouncements().getAnnouncementData().get(i));
                                }

                                loading = true;
                                if (announceMentResponse.getAnnouncements().getNextPage()!= null) {
                                    next_page = announceMentResponse.getAnnouncements().getNextPage();
                                    pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
                                } else {
                                    next_page = null;

                                }
                                announcementAdapter.notifyDataSetChanged();
                            }else {
                                emptyView.setVisibility(View.VISIBLE);
                                emptyView.setText(R.string.no_responses);
                                recyclerQustions.setVisibility(View.GONE);
                            }

                        }
                    }catch (NullPointerException ex){
                        ex.printStackTrace();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<AnnounceMentResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);
                    Toasty.error(getContext(), t.getMessage(), Toast.LENGTH_LONG, true).show();
                }
            });




        } else {
            ConfigurationFile.hideDialog(progressDialog);
            Toasty.error(getActivity().getApplicationContext(),getActivity().getResources().getString(R.string.internet_message),Toast.LENGTH_LONG).show();
        }
    }
}
