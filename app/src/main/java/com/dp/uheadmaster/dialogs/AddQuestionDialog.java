package com.dp.uheadmaster.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.fragments.QuestionAnswerFragment;
import com.dp.uheadmaster.interfaces.RefreshFragmentInterface;
import com.dp.uheadmaster.interfaces.RefreshRecyclerListener;
import com.dp.uheadmaster.models.request.AddCourseQuestionRequest;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 26/09/2017.
 */

public class AddQuestionDialog extends Dialog {


    Context context;
    private ProgressDialog progressDialog;
    private int courseID = -1;
    private SharedPrefManager sharedPrefManager;
    private LinearLayout linearLayout;
    private Activity mActivity;
    private RefreshRecyclerListener refreshRecyclerListener;
    public AddQuestionDialog(@NonNull Context context, int courseId, Activity mActivity, RefreshRecyclerListener refreshRecyclerListener) {
        super(context);
        this.context = context;
        this.courseID = courseId;
        this.mActivity=mActivity;
        this.sharedPrefManager = new SharedPrefManager(context);
        this.refreshRecyclerListener=refreshRecyclerListener;

    }


    private EditText edQuestionTitle, edQuestionContent;
    private Button btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_response_layout);

        initView();
        initEventDriven();
    }

    private void initView() {
        linearLayout=(LinearLayout) findViewById(R.id.content);
        edQuestionTitle = (EditText) findViewById(R.id.ed_question_title);
        edQuestionContent = (EditText) findViewById(R.id.ed_question_content);
        btnPost = (Button) findViewById(R.id.btn_question_post);

    }

    private void initEventDriven() {

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = mActivity.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)context.getSystemService(context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                String title = edQuestionTitle.getText().toString();
                String content = edQuestionContent.getText().toString();
                if (!title.equals("") && !content.equals("") && content != null && title != null) {

                    addRequestAPI(title, content);
                } else {
                    Snackbar.make(mActivity.findViewById(R.id.content), context.getString(R.string.enter_all_fields), Snackbar.LENGTH_LONG).show();

                }
            }
        });
    }

    private void addRequestAPI(String title, final String content) {

        if (NetWorkConnection.isConnectingToInternet(context,mActivity.findViewById(R.id.content))) {
            progressDialog = ConfigurationFile.showDialog(mActivity);

            AddCourseQuestionRequest addQuestion = new AddCourseQuestionRequest(courseID, title, content);
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<DefaultResponse> call = apiService.addCourseQuestions(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), addQuestion);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {


                        DefaultResponse defaultResponse = response.body();
                        System.out.println("Code message :" + response.body().getStatus());
                        if (defaultResponse.getStatus() == 200) {
                            /*RefreshRecyclerListener refreshRecyclerListener=new QuestionAnswerFragment();
                            refreshRecyclerListener.refreshRecycler();*/
                            refreshRecyclerListener.refreshRecycler();
                            Snackbar.make(mActivity.findViewById(R.id.content), R.string.question_asked, Snackbar.LENGTH_LONG).show();

                        } else {
                            // parse the response body …
                            System.out.println("Add Question error Code message :" + response.body().getMessage());
                            Snackbar.make(mActivity.findViewById(R.id.content), defaultResponse.getMessage(), Snackbar.LENGTH_LONG).show();
                        }

                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    RefreshFragmentInterface refreshFragmentInterface=(RefreshFragmentInterface) context;
                    refreshFragmentInterface.onRefreshFragment();
                    dismiss();
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(mActivity.findViewById(R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println(" Fialer :" + t.getMessage());
                    dismiss();
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }
}
