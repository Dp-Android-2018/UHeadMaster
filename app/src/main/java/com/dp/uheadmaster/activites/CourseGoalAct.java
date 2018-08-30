package com.dp.uheadmaster.activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.CourseGoalsAdapter;
import com.dp.uheadmaster.fragments.CourseInstructorFragment;
import com.dp.uheadmaster.interfaces.OnInstructorValueChanged;
import com.dp.uheadmaster.models.GoalAnswerModel;
import com.dp.uheadmaster.models.request.CourseLearnRequestModel;
import com.dp.uheadmaster.models.request.CoursePrerequirementsRequestModel;
import com.dp.uheadmaster.models.request.CourseTargetRequestModel;
import com.dp.uheadmaster.models.response.CourseDetailsModel;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 11/11/2017.
 */

public class CourseGoalAct extends AppCompatActivity {





    @BindView(R.id.tv_course_question)
    TextView tvQuestion;
    @BindView(R.id.et_course_answer)
    EditText edAnswer;
    @BindView(R.id.btn_add_answer)
    Button btnAddAnswer;
    @BindView(R.id.recycler_answers)
    RecyclerView recyclerAnswers;
    @BindView(R.id.btn_next_question)
    Button btnNextQuestion;
    String[] QuestionsArray;
    static ArrayList<GoalAnswerModel> answerList = new ArrayList();
    private int postions = 0;
    CourseGoalsAdapter courseGoalsAdapter;
    ProgressDialog progressDialog;
    SharedPrefManager sharedPrefManager;
    int courseId = -1;
    private RelativeLayout relativeLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_course_goal_layout);
        relativeLayout=(RelativeLayout)findViewById(R.id.content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ButterKnife.bind(this);
        //hidden keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(getIntent().getExtras() != null){
            courseId = getIntent().getExtras().getInt("CourseId");
        }
        QuestionsArray = new String[]{getString(R.string.course_prequests), getString(R.string.course_target), getString(R.string.course_learn)};
        sharedPrefManager = new SharedPrefManager(this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerAnswers.setLayoutManager(mLayoutManager);

        setQuestion();

    }

    @OnClick(R.id.btn_add_answer)
    public void addAnswer() {
        String answer = edAnswer.getText().toString().replace(" ", "");
        edAnswer.setText("");
        if(!answer.equals("")){
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            GoalAnswerModel goalAnswerModel = new GoalAnswerModel(answer);
            CourseGoalAct.answerList.add(goalAnswerModel);
            courseGoalsAdapter = new CourseGoalsAdapter(this, CourseGoalAct.answerList);
            recyclerAnswers.setAdapter(courseGoalsAdapter);
            courseGoalsAdapter.notifyDataSetChanged();

        }else{
         //   Toasty.warning(CourseGoalAct.this , "Write your answer !" , Snackbar.LENGTH_LONG , true ).show();
            Snackbar.make(relativeLayout, R.string.write_answer , Snackbar.LENGTH_LONG ).show();
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getStartIntent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public  void getStartIntent(){
        Intent i=new Intent(this,MainAct.class);
        startActivity(i);
        finish();
    }


    @OnClick(R.id.btn_next_question)
    public void nextQuestion() {
        if(courseId != -1){
            rquestOfGoals();
        }
    }

    private void rquestOfGoals() {
        //add to request
        try {

            if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),relativeLayout)) {
                progressDialog = ConfigurationFile.showDialog(CourseGoalAct.this);
                final EndPointInterfaces apiService =
                        ApiClient.getClient().create(EndPointInterfaces.class);
                //choose question service
                Call<DefaultResponse> call = null;
                switch (postions) {
                    case 0:
                        CoursePrerequirementsRequestModel coursePrerequirementsRequestModel = new CoursePrerequirementsRequestModel(courseId, getArrayOfAnswer());
                        call = apiService.addPreReqyirements(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), coursePrerequirementsRequestModel);
                        break;
                    case 1:
                        CourseTargetRequestModel targetRequestModel = new CourseTargetRequestModel(courseId, getArrayOfAnswer());
                        call = apiService.addTarget(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), targetRequestModel);
                        break;
                    case 2:
                        CourseLearnRequestModel courseLearn = new CourseLearnRequestModel(courseId, getArrayOfAnswer());
                        call = apiService.addToLearn(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), courseLearn);
                        break;
                }

                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        ConfigurationFile.hideDialog(progressDialog);
                        try {
                            if (response.body().getStatus() == 200) {
                                // use response data and do some fancy stuff :)
                                postions++;
                                setQuestion();

                            } else {
                                // parse the response body …
                             //   Toasty.error(CourseGoalAct.this, response.body().getMessage(), Toast.LENGTH_LONG, true).show();

                                Snackbar.make(relativeLayout,  response.body().getMessage() , Snackbar.LENGTH_LONG ).show();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        ConfigurationFile.hideDialog(progressDialog);

                    //    Toasty.error(CourseGoalAct.this, t.getMessage(), Toast.LENGTH_LONG, true).show();
                        Snackbar.make(relativeLayout,   t.getMessage() , Snackbar.LENGTH_LONG ).show();
                        System.out.println("Course Details / Fialer :" + t.getMessage());
                    }
                });

            } else {
                ConfigurationFile.hideDialog(progressDialog);
            }


        } catch (Exception e) {
            System.out.println("Error in goals " + e.getMessage());
        }

    }

    private String[] getArrayOfAnswer() {
        String[] arrStrings = new String[answerList.size()];
        for (int i = 0; i < answerList.size(); i++) {
            arrStrings[i] = answerList.get(i).getAnswer();
        }
        return arrStrings;

    }

    private void setQuestion() {
        if (postions > 2) {
            finish();
        } else {
            tvQuestion.setText(QuestionsArray[postions]);
            CourseGoalAct.answerList.clear();
            courseGoalsAdapter = new CourseGoalsAdapter(this, CourseGoalAct.answerList);
            recyclerAnswers.setAdapter(courseGoalsAdapter);
            courseGoalsAdapter.notifyDataSetChanged();        }

    }
}
