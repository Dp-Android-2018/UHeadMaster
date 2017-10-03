package com.dp.uheadmaster.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.models.ProgressRequestBody;
import com.dp.uheadmaster.models.request.ChangePasswordRequest;
import com.dp.uheadmaster.models.request.UpdateProfile;
import com.dp.uheadmaster.models.response.LoginResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 21/08/2017.
 */

public class TempAct extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks{

    private ProgressDialog progressDialog,progressDialog2;
    private Button btnUploadImage;
    private ImageView ivUserProfile;
    private SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        sharedPrefManager =new SharedPrefManager(getApplicationContext());
       // btnUploadImage=(Button)findViewById(R.id.btn_upload_image);
       // ivUserProfile=(ImageView)findViewById(R.id.iv_user_profile);
        ivUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });

        updateProfile();
    }


    public void changePassword() {
        if (NetWorkConnection.isConnectingToInternet(TempAct.this)) {
            progressDialog = ConfigurationFile.showDialog(TempAct.this);

            ChangePasswordRequest changePasswordRequest=new ChangePasswordRequest("12345678aA","1234567bB","1234567bB");
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);


            Call<LoginResponse> call = apiService.changePassword(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE,sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN),sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),changePasswordRequest);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {


                    if (response.body().getStatus() == 200) {
                        // use response data and do some fancy stuff :)
                        Toasty.success(TempAct.this, "Success", Toast.LENGTH_LONG, true).show();
                    } else {
                        // parse the response body …
                        System.out.println("error Code message :" + response.body().getMessage());
                        Toasty.error(TempAct.this, response.body().getMessage(), Toast.LENGTH_LONG, true).show();
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

                    Toasty.error(TempAct.this, t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println(" Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ivUserProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            uploadImage(picturePath);
        }
    }

    private void uploadImage(String path)
    {
        File originalFile= new File(path);
        ProgressRequestBody fileBody = new ProgressRequestBody(originalFile, this);
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), originalFile);
        progressDialog2=new ProgressDialog(this);
        progressDialog2.setMessage("Uploading");
        progressDialog2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog2.setCancelable(false);
        progressDialog2.show();
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", originalFile.getName(), fileBody);
        final EndPointInterfaces apiService =
                ApiClient.getClient().create(EndPointInterfaces.class);
        Call<JsonElement> call = apiService.uploadImages(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE,sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN),sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),body);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                ConfigurationFile.hideDialog(progressDialog2);
                JsonElement element = response.body();
                System.out.println(element.toString());
                try {
                    JSONObject object = new JSONObject(element.toString());
                    int status = object.getInt("status");
                    if (status == 200) {
                        Toast.makeText(TempAct.this, "Success", Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException ex){
                    ex.getMessage();
                }catch (NullPointerException ex){
                ex.printStackTrace();
            }catch (Exception ex){
                ex.printStackTrace();
            }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                ConfigurationFile.hideDialog(progressDialog2);
                Toast.makeText(getApplicationContext(), "Error:"+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateProfile()
    {
        if (NetWorkConnection.isConnectingToInternet(TempAct.this)) {
            progressDialog = ConfigurationFile.showDialog(TempAct.this);

            UpdateProfile updateProfile=new UpdateProfile("Bahaa Gabal","01117502335",ConfigurationFile.GlobalVariables.APP_LANGAUGE,"Nice");
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);


            Call<LoginResponse> call = apiService.profileUpdate(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE,sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN),sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),updateProfile);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {


                    if (response.body().getStatus() == 200) {
                        // use response data and do some fancy stuff :)
                        Toasty.success(TempAct.this, "Success Update", Toast.LENGTH_LONG, true).show();
                    } else {
                        // parse the response body …
                        System.out.println("error Code message :" + response.body().getMessage());
                        Toasty.error(TempAct.this, response.body().getMessage(), Toast.LENGTH_LONG, true).show();
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

                    Toasty.error(TempAct.this, t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println(" Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {
        progressDialog2.setProgress(percentage);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {
        progressDialog2.setProgress(100);
        progressDialog2.dismiss();
    }
}
