package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.CreateCourseAct;
import com.dp.uheadmaster.adapters.ExpandableRoadMapAdapter;
import com.dp.uheadmaster.adapters.RoadMapParent;
import com.dp.uheadmaster.models.CreationProcessModel;
import com.dp.uheadmaster.models.RoadMapChild;
import com.dp.uheadmaster.models.RoodMapModel;
import com.dp.uheadmaster.models.response.CreationProcessResponse;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.models.response.RoodMapResponse;
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

/**
 * Created by DELL on 18/10/2017.
 */

public class RoadMapFragment extends Fragment {

    private boolean nextScreen=false;
  //  ExpandableRelativeLayout expandableLayout1, expandableLayout2, expandableLayout3;
    Button btnNext;
    private SharedPrefManager sharedPrefManager;
    private ProgressDialog progressDialog;
    private TextView tvStep1Desc, tvStep2Desc, tvStep3Desc , tvProcessTitle;
    private RecyclerView recyclerView;
    private ExpandableRoadMapAdapter expandableRoadMapAdapter;
    private List<RoadMapParent>parents=new ArrayList<>();
    View v;
    private Activity mActivity;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_roadmap_layout, container, false);
        initializeUi(v);
        sharedPrefManager = new SharedPrefManager(getContext());
        if(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TYPE).equals("student")){
                becomeInstructor();
        }
        else
        getRoadMapRequest();

        initEventDriven();
        return v;
    }

    private void initEventDriven() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nextScreen==false) {
                    tvProcessTitle.setText(getContext().getString(R.string.create_course));
                    getCreationProcessRequest();
                }else {
                    Intent i=new Intent(getActivity(),CreateCourseAct.class);
                    getActivity().startActivity(i);
                }
            }
        });
        /*btnStep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableButton2(v);
            }
        });

        btnStep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableButton3(v);
            }
        });*/

    }


    public void becomeInstructor(){
        if (NetWorkConnection.isConnectingToInternet(mActivity.getApplicationContext(),mActivity.findViewById(R.id.content))) {
            progressDialog = ConfigurationFile.showDialog(getActivity());

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<DefaultResponse> call = apiService.becomeInstructor(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    DefaultResponse defaultResponse = response.body();
                    try {

                        if (defaultResponse.getStatus() == 200) {
                            Snackbar.make(mActivity.findViewById(R.id.content), getString(R.string.instructor_text), Snackbar.LENGTH_LONG).show();
                            sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.USER_TYPE,"instructor");
                            getRoadMapRequest();
                        } else {
                            Snackbar.make(mActivity.findViewById(R.id.content), defaultResponse.getMessage(), Snackbar.LENGTH_LONG).show();

                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);
                    Snackbar.make(mActivity.findViewById(R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });


        } else {
            ConfigurationFile.hideDialog(progressDialog);
          //  Snackbar.make(v, getActivity().getResources().getString(R.string.internet_message), Snackbar.LENGTH_LONG).show();
        }
    }


    public void getRoadMapRequest() {
        if (NetWorkConnection.isConnectingToInternet(mActivity.getApplicationContext(),mActivity.findViewById(R.id.content))) {
            progressDialog = ConfigurationFile.showDialog(getActivity());

           System.out.println("Data Of Instructor :"+sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN) +" "+ sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID) );
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<RoodMapResponse> call = apiService.getRoodMap(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            call.enqueue(new Callback<RoodMapResponse>() {
                @Override
                public void onResponse(Call<RoodMapResponse> call, Response<RoodMapResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    RoodMapResponse roodMapResponse = response.body();
                    try {

                        if (roodMapResponse.getStatus() == 200) {
                            updateUIForRoodMap(roodMapResponse);
                        } else {
                            Snackbar.make(mActivity.findViewById(R.id.content), roodMapResponse.getMessage(), Snackbar.LENGTH_LONG).show();

                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<RoodMapResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);
                    Snackbar.make(mActivity.findViewById(R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });


        } else {
            ConfigurationFile.hideDialog(progressDialog);
//            Snackbar.make(v, getActivity().getResources().getString(R.string.internet_message), Snackbar.LENGTH_LONG).show();
        }
    }

    public void getCreationProcessRequest() {
        if (NetWorkConnection.isConnectingToInternet(mActivity.getApplicationContext(),mActivity.findViewById(R.id.content))) {
            progressDialog = ConfigurationFile.showDialog(getActivity());

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<CreationProcessResponse> call = apiService.getCreationProcess(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            call.enqueue(new Callback<CreationProcessResponse>() {
                @Override
                public void onResponse(Call<CreationProcessResponse> call, Response<CreationProcessResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    CreationProcessResponse creationProcessResponse = response.body();
                    try {

                        if (creationProcessResponse.getStatus() == 200) {
                            nextScreen=true;
                            updateUIForCreation(creationProcessResponse);
                        } else {
                            Snackbar.make(mActivity.findViewById(R.id.content), creationProcessResponse.getMessage(), Snackbar.LENGTH_LONG).show();

                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<CreationProcessResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);
                    Snackbar.make(mActivity.findViewById(R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });


        } else {
            ConfigurationFile.hideDialog(progressDialog);
           // Snackbar.make(v, getActivity().getResources().getString(R.string.internet_message), Snackbar.LENGTH_LONG).show();
        }
    }

    private void updateUIForCreation(CreationProcessResponse creationProcessResponse) {
        parents.clear();
        for (int i = 0; i < creationProcessResponse.getTextModels().size(); i++) {
            CreationProcessModel mapModel = creationProcessResponse.getTextModels().get(i);

            ArrayList<RoadMapChild>children=new ArrayList<>();
            RoadMapChild roadMapChild=new RoadMapChild(creationProcessResponse.getTextModels().get(i).getStepDec());
            children.add(roadMapChild);
            RoadMapParent roadMapParent=new RoadMapParent(creationProcessResponse.getTextModels().get(i).getStepTitle(),children);
            parents.add(roadMapParent);
        }
        //Toast.makeText(getActivity().getApplicationContext(), "Size :"+parents.size(), Toast.LENGTH_SHORT).show();
        expandableRoadMapAdapter=new ExpandableRoadMapAdapter(getActivity().getApplicationContext(),parents);
        recyclerView.setAdapter(expandableRoadMapAdapter);
    }


    private void updateUIForRoodMap(RoodMapResponse roodMapResponse) {

        for (int i = 0; i < roodMapResponse.getTextModels().size(); i++) {

            ArrayList<RoadMapChild>children=new ArrayList<>();
            RoadMapChild roadMapChild=new RoadMapChild(roodMapResponse.getTextModels().get(i).getStepDec());
            children.add(roadMapChild);
            RoadMapParent roadMapParent=new RoadMapParent(roodMapResponse.getTextModels().get(i).getStepTitle(),children);
            parents.add(roadMapParent);
        }
       // Toast.makeText(getActivity().getApplicationContext(), "Size :"+parents.size(), Toast.LENGTH_SHORT).show();
        expandableRoadMapAdapter=new ExpandableRoadMapAdapter(getActivity().getApplicationContext(),parents);
        recyclerView.setAdapter(expandableRoadMapAdapter);
    }


    public void initializeUi(View v) {
      /*  expandableLayout1 = (ExpandableRelativeLayout) v.findViewById(R.id.expandableLayout1);
        expandableLayout2 = (ExpandableRelativeLayout) v.findViewById(R.id.expandableLayout2);
        expandableLayout3 = (ExpandableRelativeLayout) v.findViewById(R.id.expandableLayout3);

        btnStep1 = (Button) v.findViewById(R.id.expandableButton1);

        btnStep2 = (Button) v.findViewById(R.id.expandableButton2);
        btnStep3 = (Button) v.findViewById(R.id.expandableButton3);
        btnStep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableButton1(v);
            }
        });*/
        btnNext = (Button) v.findViewById(R.id.btn_next);
      /*  tvStep1Desc = (TextView) v.findViewById(R.id.tv_Step1);
        tvStep2Desc = (TextView) v.findViewById(R.id.tv_Step2);
        tvStep3Desc = (TextView) v.findViewById(R.id.tv_Step3);*/
        tvProcessTitle = (TextView) v.findViewById(R.id.tv_process_title);
        tvProcessTitle.setText(getContext().getString(R.string.roadmap));

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_road_map);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));


    }

   /* public void expandableButton1(View view) {

        expandableLayout1.toggle(); // toggle expand and collapse
    }

    public void expandableButton2(View view) {

        expandableLayout2.toggle(); // toggle expand and collapse
    }

    public void expandableButton3(View view) {
        expandableLayout3.toggle(); // toggle expand and collapse
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }
}
