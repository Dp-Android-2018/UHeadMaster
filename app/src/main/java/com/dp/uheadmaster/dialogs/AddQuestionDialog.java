package com.dp.uheadmaster.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.interfaces.RefreshFragmentInterface;
import com.dp.uheadmaster.models.request.AddCourseQuestionRequest;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 26/09/2017.
 */

public class AddQuestionDialog extends Dialog {


    Activity context;
    private ProgressDialog progressDialog;
    private int courseID = -1;
    private SharedPrefManager sharedPrefManager;


    public AddQuestionDialog(@NonNull Activity context, int courseId) {
        super(context);
        this.context = context;
        this.courseID = courseId;
        this.sharedPrefManager = new SharedPrefManager(context);

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
        edQuestionTitle = (EditText) findViewById(R.id.ed_question_title);
        edQuestionContent = (EditText) findViewById(R.id.ed_question_content);
        btnPost = (Button) findViewById(R.id.btn_question_post);

    }

    private void initEventDriven() {

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = edQuestionTitle.getText().toString();
                String content = edQuestionContent.getText().toString();
                if (!title.equals("") && !content.equals("") && content != null && title != null) {

                    addRequestAPI(title, content);
                } else {
                    Toasty.error(context, context.getString(R.string.enter_all_fields), Toast.LENGTH_LONG, true).show();

                }
            }
        });
    }

    private void addRequestAPI(String title, final String content) {

        if (NetWorkConnection.isConnectingToInternet(context)) {
            progressDialog = ConfigurationFile.showDialog(context);

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
                        if (defaultResponse.getStatus() == 200) {
                            Toasty.success(context, defaultResponse.getMessage(), Toast.LENGTH_LONG, true).show();

                        } else {
                            // parse the response body …
                            System.out.println("Add Question error Code message :" + response.body().getMessage());
                            Toasty.error(context, defaultResponse.getMessage(), Toast.LENGTH_LONG, true).show();
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

                    Toasty.error(context, t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println(" Fialer :" + t.getMessage());
                    dismiss();
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }
}
