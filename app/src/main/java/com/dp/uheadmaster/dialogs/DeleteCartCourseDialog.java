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
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.LoginAct;
import com.dp.uheadmaster.activites.SplashAct;
import com.dp.uheadmaster.fragments.CartFrag;
import com.dp.uheadmaster.interfaces.CartPositionInterface;
import com.dp.uheadmaster.models.CourseIDRequest;
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
 * Created by DELL on 07/11/2017.
 */

public class DeleteCartCourseDialog extends Dialog {


    private Button btnCancel,btnDeleteCourse;
    private TextView tvDialogTitle;
    private Context context;
   private int id;
    private ProgressDialog progressDialog;
    private SharedPrefManager sharedPrefManager;
    private  int flag;

    private RelativeLayout relativeLayout;
    public DeleteCartCourseDialog(@NonNull Context context,int id,int flag) {
        super(context);
        this.context=context;
        this.id=id;
        this.flag=flag;
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
        tvDialogTitle=(TextView)findViewById(R.id.tv_message_title);
        if(flag==0)
        tvDialogTitle.setText(R.string.delete_cart_coure);
        else if(flag==1)
            tvDialogTitle.setText(R.string.change_app_language);
        btnCancel=(Button)findViewById(R.id.btn_cancel);
        btnDeleteCourse=(Button)findViewById(R.id.btn_delete_course);
        btnDeleteCourse.setText((flag==0)?context.getResources().getString(R.string.delete):context.getResources().getString(R.string.ok));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteCartCourseDialog.this.cancel();
            }
        });

        btnDeleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             /*   CartPositionInterface cartPositionInterface=new CartFrag();
                cartPositionInterface.getCartPosition(id);*/
             if(flag==0)
             removefromCart(id);
                else
                    changeAppLang();
            }
        });
    }



    private void  changeAppLang(){
        if(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.Langauge).equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN)){
            sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.Langauge,ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR);
            ConfigurationFile.GlobalVariables.APP_LANGAUGE = ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR;
        }else {
            sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.Langauge,ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN);
            ConfigurationFile.GlobalVariables.APP_LANGAUGE = ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN;

        }

        context.startActivity(new Intent(context, SplashAct.class));
    }
    private void removefromCart(final int courseID) {
        if (NetWorkConnection.isConnectingToInternet(context,relativeLayout)) {

            progressDialog = ConfigurationFile.showDialog((Activity)context);
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            CourseIDRequest request = new CourseIDRequest(courseID);
            Call<DefaultResponse> call = apiService.addOrRemoveFromCartList(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), request);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    try {
                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)

                            removeFromList(courseID);
                            Snackbar.make(relativeLayout, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                            DeleteCartCourseDialog.this.cancel();


                        } else {
                            // parse the response body …
                            System.out.println("cart adapter      /error Code message :" + response.body().getMessage());
                            Snackbar.make(relativeLayout, response.body().getMessage(), Snackbar.LENGTH_LONG).show();


                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(relativeLayout, t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println("cart adapter / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }

    private void removeFromList(int courseID) {
        for (int i = 0; i < CartFrag.cartCoursesList.size(); i++) {

            if (courseID == CartFrag.cartCoursesList.get(i).getId()) {
                CartFrag.cartCoursesList.remove(i);
                calculateTotalPrice();
                CartFrag.cartCoursesAdapter.notifyDataSetChanged();
                return;
            }
        }



    }





    private void calculateTotalPrice() {
        double totalPrice = 0.0;
        String currency = "";
        for (int i = 0; i < CartFrag.cartCoursesList.size(); i++) {
            if(CartFrag.cartCoursesList.get(i).getPrice() !=null && !CartFrag.cartCoursesList.get(i).getPrice().equals("")){
                totalPrice +=Double.parseDouble(CartFrag.cartCoursesList.get(i).getPrice());
            }
            currency = CartFrag.cartCoursesList.get(0).getCurrency();
        }
        CartFrag.tvCourseCount.setText(CartFrag.cartCoursesList.size()+context.getResources().getString(R.string.course_in_cart));
        CartFrag.tvTotalPrice.setText(context.getResources().getString(R.string.total_payment)+totalPrice+" "+currency);
    }



}
