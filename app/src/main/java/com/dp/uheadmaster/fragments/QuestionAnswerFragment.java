package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.dp.uheadmaster.adapters.QuestionAnswerAdapter;
import com.dp.uheadmaster.dialogs.AddQuestionDialog;
import com.dp.uheadmaster.models.Question;
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

public class QuestionAnswerFragment extends Fragment {
    private RecyclerView recyclerQustions;
    private LinearLayoutManager mLayoutManager;
    private FloatingActionButton fab;
    private Context mcontext;
    private int courseId;
    private ProgressDialog progressDialog;
    private SharedPrefManager sharedPrefManager;
    private ArrayList<Question>questions;
    private QuestionAnswerAdapter questionAnswerAdapter;
    private Activity mActivity;
    private TextView emptyView;

    private View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v=inflater.inflate(R.layout.frag_questions_answers_layout,container,false);

        initializeUi(v);
        getCourseQuestions();
            return v;
    }

    public void refresh(){
        System.out.println("Success");
//        Toast.makeText(mcontext, "Refresh>>>", Toast.LENGTH_SHORT).show();
       //initializeUi(v);
        getCourseQuestions();
    }
    public void initializeUi(View v){

        courseId=getActivity().getIntent().getIntExtra("CourseId",0);
        //Toasty.success(getActivity().getApplicationContext()," "+courseId, Toast.LENGTH_LONG).show();
        sharedPrefManager = new SharedPrefManager(getContext());
        questions=new ArrayList<>();
        recyclerQustions = (RecyclerView) v.findViewById(R.id.recycler1);
        emptyView=(TextView)v.findViewById(R.id.empty_view) ;
        fab=(FloatingActionButton)v.findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddQuestionDialog addResponseDialog=new AddQuestionDialog(mActivity , courseId);
                addResponseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                addResponseDialog.show();

            }
        });

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerQustions.setLayoutManager(mLayoutManager);
        recyclerQustions.setItemAnimator(new DefaultItemAnimator());
        questionAnswerAdapter=new QuestionAnswerAdapter(mcontext,questions,courseId);
        recyclerQustions.setAdapter(questionAnswerAdapter);

    }

    public void getCourseQuestions(){
        if (NetWorkConnection.isConnectingToInternet(getContext())) {
            progressDialog = ConfigurationFile.showDialog(getActivity());

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<QuestionsResponse> call = apiService.getCourseQuestions(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),courseId,0);
            call.enqueue(new Callback<QuestionsResponse>() {
                @Override
                public void onResponse(Call<QuestionsResponse> call, Response<QuestionsResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    QuestionsResponse questionsResponse=response.body();
                    try {

                        if (questionsResponse.getStatus() == 200) {

                            if(!questionsResponse.getQuestions().isEmpty()) {
                                for (int i = 0; i < questionsResponse.getQuestions().size(); i++) {
                                    questions.add(questionsResponse.getQuestions().get(i));
                                }
                                questionAnswerAdapter.notifyDataSetChanged();
                            }else {
                                emptyView.setVisibility(View.VISIBLE);
                                emptyView.setText(R.string.no_responses);
                                recyclerQustions.setVisibility(View.GONE);
                            }

                        }else {
                                //Toasty.error(getActivity().getApplicationContext(),questionsResponse.getMessage(),Toast.LENGTH_LONG).show();
                            emptyView.setVisibility(View.VISIBLE);
                            emptyView.setText(questionsResponse.getMessage());
                            recyclerQustions.setVisibility(View.GONE);
                        }
                    }catch (NullPointerException ex){
                        ex.printStackTrace();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<QuestionsResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);
                    Toasty.error(getContext(), t.getMessage(), Toast.LENGTH_LONG, true).show();
                }
            });




        } else {
            ConfigurationFile.hideDialog(progressDialog);
            Toasty.error(getActivity().getApplicationContext(),getActivity().getResources().getString(R.string.internet_message),Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcontext=context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity=activity;
    }
}
