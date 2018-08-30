package com.dp.uheadmaster.holders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.QuestionAnswerInstructor;
import com.dp.uheadmaster.models.AddAnswer;
import com.dp.uheadmaster.models.Question;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.models.response.QuestionsResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 18/10/2017.
 */

public class QuestionAnswerInstructorHolder extends RecyclerView.ViewHolder {
    private Context context;
    private Activity activity;
    private SharedPrefManager sharedPrefManager;
    private ProgressDialog progressDialog;
    @BindView(R.id.tv_student_question)
    TextView tvStudentQuestion;


    @BindView(R.id.et_instructor_answer)
    EditText etInstructorAnswer;

    @BindView(R.id.btn_add_answer)
    Button btnAddAnswer;

    public QuestionAnswerInstructorHolder(View itemView, Context context, Activity activity) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.context=context;
        this.activity=activity;
        sharedPrefManager=new SharedPrefManager(context);
    }

    public void onBind(final Question question){
        tvStudentQuestion.setText(question.getTitle());
        btnAddAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = activity.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                addAnswer(question.getId());
            }
        });

    }


    public void addAnswer(int questionId){
        if (NetWorkConnection.isConnectingToInternet(context,activity.findViewById(R.id.content))) {
            if (!ConfigurationFile.isEmpty(etInstructorAnswer)) {
                progressDialog = ConfigurationFile.showDialog(activity);

                final EndPointInterfaces apiService =
                        ApiClient.getClient().create(EndPointInterfaces.class);
                AddAnswer addAnswer = new AddAnswer(questionId, etInstructorAnswer.getText().toString().trim());
                Call<DefaultResponse> call = apiService.AddAnswer(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), addAnswer);
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        ConfigurationFile.hideDialog(progressDialog);
                        DefaultResponse defaultResponse = response.body();
                        try {
                            //Toast.makeText(QuestionAnswerInstructor.this, " status:"+questionsResponse.getStatus(), Toast.LENGTH_SHORT).show();
                            if (defaultResponse.getStatus() == 200) {

                               // Toast.makeText(context, "Answer Added Successfully", Toast.LENGTH_SHORT).show();
                                Toasty.success(context,context.getString(R.string.answer_added), Toast.LENGTH_SHORT).show();
                                etInstructorAnswer.setText("");
                            } else {

                              //  Toast.makeText(context, "" + defaultResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                Toasty.error(context,""+defaultResponse.getMessage(),Toast.LENGTH_LONG).show();
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
                        Toasty.error(context, t.getMessage(), Toast.LENGTH_LONG, true).show();
                    }
                });
            }else {
                Toasty.error(context,context.getResources().getString(R.string.fill_data),Toast.LENGTH_LONG).show();
            }




        } else {
            ConfigurationFile.hideDialog(progressDialog);
           // Toasty.error(context,context.getResources().getString(R.string.internet_message),Toast.LENGTH_LONG).show();
        }
    }

}
