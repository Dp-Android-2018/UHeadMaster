package com.dp.uheadmaster.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.LoginAct;
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
 * Created by DELL on 28/08/2017.
 */

public class DeleteCourseDialog extends Dialog {


    private Button btnCancel,btnDeleteCourse;
    private int type;
    private Context context;
    private ProgressDialog progressDialog;
    private Activity activity;
    private SharedPrefManager sharedPrefManager;
    private RelativeLayout relativeLayout;
    public DeleteCourseDialog(@NonNull Context context, Activity activity) {
        super(context);
        this.context=context;
        this.activity=activity;
        sharedPrefManager=new SharedPrefManager(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_course_dialog_layout);
        initializeUi();
    }

    public void initializeUi(){
        relativeLayout=(RelativeLayout) findViewById(R.id.content);
        btnCancel=(Button)findViewById(R.id.btn_cancel);
        btnDeleteCourse=(Button)findViewById(R.id.btn_delete_course);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteCourseDialog.this.cancel();
            }
        });

        btnDeleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


             //   Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                deleteUserAccount();

            }
        });
    }

    public void deleteUserAccount(){

        if (NetWorkConnection.isConnectingToInternet(context,activity.findViewById(R.id.drawer))) {
            progressDialog = ConfigurationFile.showDialog(activity);

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            System.out.println("Account Delete:"+ ConfigurationFile.ConnectionUrls.HEAD_KEY +" "+ConfigurationFile.GlobalVariables.APP_LANGAUGE +"  "+sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN)+" "+sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            Call<DefaultResponse> call = apiService.deleteUserAccount(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    DefaultResponse defaultResponse = response.body();
                    try {

                        if (defaultResponse.getStatus() == 200) {
                            DeleteCourseDialog.this.cancel();
                            Snackbar.make(activity.findViewById(R.id.drawer), context.getResources().getString(R.string.account_deleted), Snackbar.LENGTH_LONG).show();
                            SharedPrefManager sharedPrefManager=new SharedPrefManager(activity.getApplicationContext());
                            sharedPrefManager.clearToken();
                            Intent i=new Intent(activity, LoginAct.class);
                            activity.finish();
                            context.startActivity(i);
                           
                        } else {
                            Snackbar.make(activity.findViewById(R.id.drawer), defaultResponse.getMessage(), Snackbar.LENGTH_LONG).show();

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
                    Snackbar.make(activity.findViewById(R.id.drawer), t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });


        } else {
            ConfigurationFile.hideDialog(progressDialog);
            Snackbar.make(activity.findViewById(R.id.drawer), context.getResources().getString(R.string.internet_message), Snackbar.LENGTH_LONG).show();
        }

    }
}
