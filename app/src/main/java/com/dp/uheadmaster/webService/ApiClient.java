package com.dp.uheadmaster.webService;

import com.dp.uheadmaster.utilities.ConfigurationFile;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DELL on 04/04/2017.
 */

public class ApiClient {
   // public static final String BASE_URL="http://192.168.1.100/master/findandfix/public/api";
   // public static final String BASE_URL="http://192.168.1.100/master/findandfix/public/api";
   // public static final String BASE_URL="http://87.101.224.197/findandfix/api/";
  //  public static final String BASE_URL = "http:///osama/findandfix/public/api/";
  //public static final String BASE_URL = "https://umrapp.com/test/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15,TimeUnit.SECONDS).build();
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ConfigurationFile.ConnectionUrls.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
