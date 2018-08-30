package com.dp.uheadmaster.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.CategoriesSpinnerAdapter;
import com.dp.uheadmaster.adapters.LanguagesAdapter;
import com.dp.uheadmaster.adapters.LevelsAdapter;
import com.dp.uheadmaster.models.CategoryModel;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.Languages;
import com.dp.uheadmaster.models.request.CreateCourseRequest;
import com.dp.uheadmaster.models.request.SubCategoryRequest;
import com.dp.uheadmaster.models.response.CategoriesResponse;
import com.dp.uheadmaster.models.response.CreateCourseResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;
import com.google.gson.JsonElement;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 22/10/2017.
 */

public class CreateCourseAct extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private ProgressDialog progressDialog,progressDialog1;
    private SharedPrefManager sharedPrefManager;
    private List<CategoryModel> categoriesData;
    private List<CategoryModel> subcategoriesData;
    private List<String> levels;
    private CategoriesSpinnerAdapter adapter;
    @BindView(R.id.sp_category)
    Spinner categories;

    @BindView(R.id.sp_sub_category)
    Spinner subcategories;

    ///////////////////////////////////////////////////
    private int categoryId;
    private int subCategoryId;
    private String language;
    private String level;


    ////////////////////////////////////////////////
    private List<Languages> languageData;
    private LanguagesAdapter Ladapter;
    @BindView(R.id.sp_instruction_level)
    Spinner spInstructionLevel;


    @BindView(R.id.sp_language)
    Spinner languageSpinner;


    @BindView(R.id.et_course_prequests)
    EditText etCourseName;

    @BindView(R.id.et_course_subtile)
    EditText etCourseSubTitle;

    @BindView(R.id.et_course_summary)
    EditText etCourseSummary;

    @BindView(R.id.btn_create_course)
    Button btnCreateCourse;

    @BindView(R.id.content)
    LinearLayout linearLayout;
    
    private int courseId;

    private FontChangeCrawler fontChanger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_course_info_layout);
        ButterKnife.bind(this);
        initializeUi();

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






    public void initializeUi() {


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        categoriesData = new ArrayList<>();
        subcategoriesData = new ArrayList<>();
        languageData = new ArrayList<>();
        levels = new ArrayList<>();
        levels.add(getString(R.string.beginner));
        levels.add(getString(R.string.intermediate));
        levels.add(getString(R.string.advanced));

        //adapter=;
        //categories.setAdapter(adapter);
        categories.setOnItemSelectedListener(this);
        subcategories.setOnItemSelectedListener(this);
        languageSpinner.setOnItemSelectedListener(this);
        spInstructionLevel.setOnItemSelectedListener(this);

        spInstructionLevel.setAdapter(new LevelsAdapter(getApplicationContext(), levels));
        getLanguages();
        getCategories();

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN)) {
            fontChanger = new FontChangeCrawler(getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR)) {
            fontChanger = new FontChangeCrawler(getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        }
    }

    @OnClick(R.id.btn_create_course)
    public void courseCreationProcess() {

        if (!ConfigurationFile.isEmpty(etCourseName) && !ConfigurationFile.isEmpty(etCourseSubTitle)) {
            createCourse();

        } else {
            Snackbar.make(linearLayout, getResources().getString(R.string.fill_data), Snackbar.LENGTH_LONG).show();
        }

    }



    public void getCategories() {
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
            progressDialog1 = ConfigurationFile.showDialog(this);

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<CategoriesResponse> call = apiService.getCategories(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), 0);
            // System.out.println("Category / id : " + sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            // System.out.println("Category / authro : " + sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN));
            call.enqueue(new Callback<CategoriesResponse>() {
                @Override
                public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog1);
                    // System.out.println("Category Body : "+response.body().getCategoriesList().size());
                    try {

                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            CategoriesResponse categoriesResponse = response.body();

                            categoriesData = categoriesResponse.getCategoriesList();
                            //  Toast.makeText(CreateCourseAct.this, "Size:"+categoriesData.size(), Toast.LENGTH_SHORT).show();
                            //adapter.notifyDataSetChanged();
                            notifyChanges();


                        } else {
                            // parse the response body …
                            System.out.println("Category /error Code message :" + response.body().getMessage());
                            Snackbar.make(linearLayout, response.body().getMessage(), Snackbar.LENGTH_LONG).show();


                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog1);

                    Snackbar.make(linearLayout, t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println("Category / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog1);
        }
    }


    public void getSubCategories(int categoryId) {
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {

            progressDialog = ConfigurationFile.showDialog(this);
            SubCategoryRequest subCategoryRequest = new SubCategoryRequest(categoryId);
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<CategoriesResponse> call = apiService.getSubCategories(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), subCategoryRequest, 0);
            call.enqueue(new Callback<CategoriesResponse>() {
                @Override
                public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    System.out.println("Sub Category Body : " + response.body().getCategoriesList().size());
                    try {

                        CategoriesResponse categoriesResponse = response.body();
                        if (categoriesResponse.getStatus() == 200) {

                            subcategoriesData = categoriesResponse.getCategoriesList();
                            notifySubCategoryChanges();
                        } else {
                            // parse the response body …
                            System.out.println("Sub Category /error Code message :" + response.body().getMessage());
                            Snackbar.make(linearLayout, categoriesResponse.getMessage(), Snackbar.LENGTH_LONG).show();


                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(linearLayout, t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println("Sub Category / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }


    public void getLanguages() {
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
            progressDialog = ConfigurationFile.showDialog(this);


            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);

            System.out.println("User Token:" + sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN) + "\n Key:" + ConfigurationFile.ConnectionUrls.HEAD_KEY +
                    "\n id:" + sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));

            Call<JsonElement> call = apiService.getLanguages(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, ConfigurationFile.GlobalVariables.APP_LANGAUGE);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {

                        JsonElement element = response.body();
                        System.out.println("Linkat:" + element.toString());
                        JSONObject links = new JSONObject(element.toString());
                        //  if (links.getInt("status") == 200) {
                        System.out.println("Linkat:" + links.toString());
                        //  HashMap<String, String> map = new HashMap<String, String>();
                        Iterator iter = links.keys();

                        while (iter.hasNext()) {
                            String key = (String) iter.next();
                            String value = links.getString(key);
                            //  map.put(key, value);
                            languageData.add(new Languages(key, value));
                        }

                        notifyLangyageDataChanged();

                    } catch (NullPointerException ex) {
                        ex.printStackTrace();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(linearLayout, t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println(" Fialer :" + t.getMessage());
                }
            });

        } else {
            Snackbar.make(linearLayout, getString(R.string.check_internet_connection), Snackbar.LENGTH_LONG).show();
        }
    }


    public void createCourse() {
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
            progressDialog = ConfigurationFile.showDialog(this);

            System.out.println("Create Course Date: Course Name :" + etCourseName.getText().toString().trim() + " SubTitle:" + etCourseSubTitle.getText().toString().trim() + " lang :" + language
                    + "Category Id :" + categoryId + " Sub Cat:" + subCategoryId + " Level:" + level + " Summary :" + etCourseSummary.getText().toString().trim());
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            CreateCourseRequest createCourseRequest = new CreateCourseRequest(etCourseName.getText().toString().trim(), etCourseSubTitle.getText().toString().trim(), language, categoryId, subCategoryId, level, etCourseSummary.getText().toString().trim());
            Call<CreateCourseResponse> call = apiService.createCourse(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), createCourseRequest);
            call.enqueue(new Callback<CreateCourseResponse>() {
                @Override
                public void onResponse(Call<CreateCourseResponse> call, Response<CreateCourseResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    try {
                        CreateCourseResponse createCourseResponse = response.body();
                        if (createCourseResponse.getStatus() == 200) {
                            Snackbar.make(linearLayout, getString(R.string.course_created_success), Snackbar.LENGTH_LONG).show();
                            courseId = createCourseResponse.getCourseId();
                            if (courseId > 0) {
                                Intent i = new Intent(getApplicationContext(), CourseGoalAct.class);
                                i.putExtra("CourseId", courseId);
                                startActivity(i);
                                finish();
                            }else{
                                Snackbar.make(linearLayout, response.body().getMessage(), Snackbar.LENGTH_LONG).show();

                            }


                        } else {
                            System.out.println("Category /error Code message :" + response.body().getMessage());
                            Snackbar.make(linearLayout, response.body().getMessage(), Snackbar.LENGTH_LONG).show();


                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<CreateCourseResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(linearLayout, t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println("Category / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }


    public void notifyChanges() {
        categories.setAdapter(new CategoriesSpinnerAdapter(getApplicationContext(), categoriesData));
    }

    public void notifySubCategoryChanges() {
        subcategories.setAdapter(new CategoriesSpinnerAdapter(getApplicationContext(), subcategoriesData));
    }

    public void notifyLangyageDataChanged() {
        languageSpinner.setAdapter(new LanguagesAdapter(getApplicationContext(), languageData));

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_category:
                //      Toast.makeText(this, "Pos:"+position, Snackbar.LENGTH_LONG).show();


                categoryId = categoriesData.get(position).getId();
                subcategoriesData.clear();
                getSubCategories(categoryId);
                break;

            case R.id.sp_sub_category:

                subCategoryId = subcategoriesData.get(position).getId();
                break;

            case R.id.sp_language:
                language = languageData.get(position).getKey();
                break;

            case R.id.sp_instruction_level:
                if (levels.get(position).equals(getResources().getString(R.string.beginner)))
                    level = "beginner";

                else if (levels.get(position).equals(getResources().getString(R.string.intermediate)))
                    level = "intermediate";

                else if (levels.get(position).equals(getResources().getString(R.string.advanced)))
                    level = "advanced";
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
