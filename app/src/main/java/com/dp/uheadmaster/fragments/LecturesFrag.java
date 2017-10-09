package com.dp.uheadmaster.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.ChangePasswordAct;
import com.dp.uheadmaster.activites.MainAct;
import com.dp.uheadmaster.adapters.ExpandedAdapter;
import com.dp.uheadmaster.interfaces.ParentPositionChecker;
import com.dp.uheadmaster.interfaces.VideoPathChecker;
import com.dp.uheadmaster.models.Content;
import com.dp.uheadmaster.models.CourseContentModel;
import com.dp.uheadmaster.models.CourseData;
import com.dp.uheadmaster.models.QuizAnswer;
import com.dp.uheadmaster.models.Resource;
import com.dp.uheadmaster.models.Section;
import com.dp.uheadmaster.models.TitleChild;
import com.dp.uheadmaster.models.TitleParent;
import com.dp.uheadmaster.models.request.ChangePasswordRequest;
import com.dp.uheadmaster.models.response.LoginResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 24/09/2017.
 */

public class LecturesFrag extends Fragment implements ParentPositionChecker{

        private RecyclerView recyclerView;
        private ProgressDialog progressDialog;
        private static ExpandedAdapter expandedAdapter;
        private SharedPrefManager sharedPrefManager;
        public static ArrayList<Resource>resources;
        private ArrayList<Content>quizes;
        private int courseId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag_lecture_layout,container,false);
        initializeUi(v);
        return v;
    }




    @Override
    public void getParentPosition(Context context, int position) {
        System.out.println("Adapters Count:"+expandedAdapter.getItemCount()+" "+position);
        for (int i=0;i<expandedAdapter.getItemCount();i++){
            if(i!=position)
                expandedAdapter.collapseParent(i);
        }

    }

    public void initializeUi(View v) {

        resources=new ArrayList<>();
        quizes=new ArrayList<>();
        courseId=getActivity().getIntent().getIntExtra("CourseId",0);
        sharedPrefManager=new SharedPrefManager(getActivity().getApplicationContext());
        recyclerView=(RecyclerView)v.findViewById(R.id.recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        getCourseContents();
    }


    public void getCourseContents() {
        if (NetWorkConnection.isConnectingToInternet(getActivity().getApplicationContext())) {
            progressDialog = ConfigurationFile.showDialog(getActivity());


            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);


            Call<CourseContentModel> call = apiService.getCourseContent(ConfigurationFile.ConnectionUrls.HEAD_KEY,ConfigurationFile.GlobalVariables.APP_LANGAUGE,sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN),sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),73);
            call.enqueue(new Callback<CourseContentModel>() {
                @Override
                public void onResponse(Call<CourseContentModel> call, Response<CourseContentModel> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {

                        CourseContentModel courseContentModel=response.body();
                        if (courseContentModel.getStatus() == 200) {

                            //initiateData();
                            expandedAdapter=new ExpandedAdapter(getActivity().getApplicationContext(),initiateData(courseContentModel.getSections()));
                            recyclerView.setAdapter(expandedAdapter);
                        }
                    }catch (NullPointerException ex){
                        ex.printStackTrace();

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<CourseContentModel> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Toasty.error(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println(" Fialer :" + t.getMessage());
                }
            });

        } else {
            Toasty.warning(getActivity().getApplicationContext(),getString(R.string.check_internet_connection),Toast.LENGTH_LONG).show();
        }
    }

        public List<TitleParent> initiateData(ArrayList<Section>sections){
            ArrayList<TitleParent>parents=new ArrayList<>();
            ArrayList<TitleChild>children=new ArrayList<>();
            for (int i=0;i<sections.size();i++){
                Section section=sections.get(i);
                children=new ArrayList<>();

                for (int j=0;j<section.getCourseDatas().size();j++){
                    CourseData courseData=section.getCourseDatas().get(j);
                    for (int z=0;z<courseData.getContents().size();z++){
                        Content content=courseData.getContents().get(z);
                        if(courseData.getType().equals("lecture")){

                            if(!content.getType().equals("resource")){


                                children.add(new TitleChild(courseData.getId(),courseData.getTitle(),content.getId(),content.getType(),content.getContent()));
                            }else {
                                ///  public Resource(int lectureId, String lectureTitle, int resourceId, String resourceTitle, String resourcePath)
                                resources.add(new Resource(courseData.getId(),courseData.getTitle(),content.getId(),content.getName(),content.getContent()));
                            }

                        }else if(courseData.getType().equals("quiz")){
                            quizes.add(content);
                        }


                    }


                }
                parents.add(new TitleParent(section.getId(),section.getTitle(),children));
            }

            return parents;
        }

}
