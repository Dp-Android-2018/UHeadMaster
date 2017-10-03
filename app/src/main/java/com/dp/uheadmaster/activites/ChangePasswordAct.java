package com.dp.uheadmaster.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.fragments.ProfileFrag;
import com.dp.uheadmaster.models.request.ChangePasswordRequest;
import com.dp.uheadmaster.models.response.LoginResponse;
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
 * Created by DELL on 28/08/2017.
 */

public class ChangePasswordAct extends AppCompatActivity {

    private EditText etOldPassword,etNewPassword,etConfirmPassword;
    private Button btnResetPassword,btnCancel;
    private ProgressDialog progressDialog;
    private SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_change_password_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView search=(ImageView)toolbar.findViewById(R.id.iv_search);
        search.setVisibility(View.GONE);
        initializeUi();
    }

    public void initializeUi(){
        sharedPrefManager =new SharedPrefManager(getApplicationContext());
        etOldPassword=(EditText)findViewById(R.id.et_old_password);
        etNewPassword=(EditText)findViewById(R.id.et_new_password);
        etConfirmPassword=(EditText)findViewById(R.id.et_confirm_password);
        btnResetPassword=(Button)findViewById(R.id.btn_reset);
        btnCancel=(Button)findViewById(R.id.btn_cancel);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetWorkConnection.isConnectingToInternet(getApplicationContext())) {

                    if (!ConfigurationFile.isEmpty(etOldPassword)&&!ConfigurationFile.isEmpty(etNewPassword)&&!ConfigurationFile.isEmpty(etConfirmPassword)) {

                        if(etNewPassword.getText().toString().trim().equals(etConfirmPassword.getText().toString().trim())){
                            changePassword();
                        }else {
                            Toasty.error(ChangePasswordAct.this,getString(R.string.password_match),Toast.LENGTH_LONG).show();
                        }

                    }else {
                        Toasty.error(ChangePasswordAct.this,getString(R.string.fill_data),Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toasty.warning(ChangePasswordAct.this,getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ChangePasswordAct.this,MainAct.class);
                startActivity(i);
                finish();
            }
        });
    }



    public void changePassword() {
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext())) {
            progressDialog = ConfigurationFile.showDialog(ChangePasswordAct.this);

            ChangePasswordRequest changePasswordRequest=new ChangePasswordRequest(etOldPassword.getText().toString().trim(),etNewPassword.getText().toString().trim(),etNewPassword.getText().toString().trim());
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);


            Call<LoginResponse> call = apiService.changePassword(ConfigurationFile.ConnectionUrls.HEAD_KEY,ConfigurationFile.GlobalVariables.APP_LANGAUGE,sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN),sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),changePasswordRequest);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {


                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            Toasty.success(ChangePasswordAct.this, getString(R.string.password_changed), Toast.LENGTH_LONG, true).show();
                            Intent i = new Intent(ChangePasswordAct.this, MainAct.class);
                            startActivity(i);
                            finish();
                        } else {
                            // parse the response body …
                            System.out.println("error Code message :" + response.body().getMessage());
                            Toasty.error(ChangePasswordAct.this, response.body().getMessage(), Toast.LENGTH_LONG, true).show();


                        }
                    }catch (NullPointerException ex){
                        ex.printStackTrace();

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Toasty.error(ChangePasswordAct.this, t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println(" Fialer :" + t.getMessage());
                }
            });

        } else {
            Toasty.warning(ChangePasswordAct.this,getString(R.string.check_internet_connection),Toast.LENGTH_LONG).show();
        }
    }
}
