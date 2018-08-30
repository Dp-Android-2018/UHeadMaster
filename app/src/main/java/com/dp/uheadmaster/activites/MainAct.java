package com.dp.uheadmaster.activites;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.CategorySearchAdapter;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.dialogs.CheckOutDialog;
import com.dp.uheadmaster.dialogs.DeleteCourseDialog;
import com.dp.uheadmaster.fragments.BasicSearchFrag;
import com.dp.uheadmaster.fragments.CartFrag;
import com.dp.uheadmaster.fragments.CategoriesFrag;
import com.dp.uheadmaster.fragments.InstructorFragment;
import com.dp.uheadmaster.fragments.MainFrag;
import com.dp.uheadmaster.fragments.MyCoursesFrag;
import com.dp.uheadmaster.fragments.ProfileFrag;
import com.dp.uheadmaster.fragments.SearchCategoriesFrag;
import com.dp.uheadmaster.fragments.SearchFrag;
import com.dp.uheadmaster.fragments.SearchSubCategoriesFrag;
import com.dp.uheadmaster.fragments.SettingsFragment;
import com.dp.uheadmaster.interfaces.CheckOutDialogInterface;
import com.dp.uheadmaster.interfaces.IvUserProfileChangedListener;
import com.dp.uheadmaster.interfaces.RefreshRecyclerListener;
import com.dp.uheadmaster.models.CustomTypefaceSpan;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.request.AddAnnouncementRequest;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;
import com.squareup.picasso.Picasso;

import android.support.design.widget.NavigationView;

import java.util.Locale;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dp.uheadmaster.R.id.item;

public class MainAct extends AppCompatActivity implements CheckOutDialogInterface,RefreshRecyclerListener , IvUserProfileChangedListener{


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Fragment nextFrag=null;
    public static boolean isSearchOpened=false;
    public Fragment nexFragment=null;
    private ProgressDialog progressDialog;
    private Typeface typeface;
    private SharedPrefManager sharedPrefManager;
    private boolean doubleBackToExitPressedOnce;
    private FontChangeCrawler fontChanger;
    private String mCurrentFragment="Home";
    private Menu m;
    private  ImageView search;
    TextView mTitle;
    private BroadcastReceiver receiver;
    ImageView ivUserProfile;

    @Override
    public void onUserProfileChanged() {
        System.out.println("Image Changed ");
        setUserProfile();
    }


    public class HandlerImp implements Runnable{



            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }

    }
    @Override
    protected void onResume() {
        super.onResume();
        SubCategoriesAct.subCategoryId=0;
        CategoriesFrag.categoryId=0;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21){
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(getApplicationContext()).inflateTransition(R.transition.shared_element_transition));
            //getWindow().setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transition));
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACCOUNT_VERIFIED");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
               System.out.println("Main Act Vrified");
                Intent i=new Intent(MainAct.this,MainAct.class);
                startActivity(i);
                finishAffinity();

            }
        };
        registerReceiver(receiver, intentFilter);
        setContentView(R.layout.activity_main_layout);

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
        }
        sharedPrefManager =new SharedPrefManager(getApplicationContext());
        SubCategoriesAct.subCategoryId=0;
        CategoriesFrag.categoryId=0;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

          mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
         search=(ImageView)toolbar.findViewById(R.id.iv_search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSearchOpened==false) {
                    isSearchOpened=true;
                    mCurrentFragment="Second";
                    BasicSearchFrag fragment = new BasicSearchFrag();
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.popBackStack();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.commit();
                }
                else if(isSearchOpened) {
                    isSearchOpened=false;
                    mCurrentFragment="Second";
                    SearchCategoriesFrag.search_category_id=0;
                    SearchSubCategoriesFrag.search_sub_category_id=0;
                    MainFrag fragment = new MainFrag();
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.popBackStack();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    if(nexFragment!=null)
                    fragmentTransaction.replace(R.id.frame, nexFragment);
                    else
                        fragmentTransaction.replace(R.id.frame, new MainFrag());
                    fragmentTransaction.commit();


                }
            }
        });
        if(nextFrag==null)
        {
            search.setVisibility(View.VISIBLE);
            MainFrag fragment = new MainFrag();
            mCurrentFragment="Home";
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();


        }

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
         m = navigationView.getMenu();

        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {




                menuItem.setChecked(true) ;
                //Closing drawer on item click
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()){


                    case R.id.home:

                    {
                        mCurrentFragment="Home";
                        nexFragment=new MainFrag();
                        search.setVisibility(View.VISIBLE);
                        startHomeFragment();
                        return true;
                    }


                    case R.id.categories: {
                        mCurrentFragment="Second";
                        nexFragment=new CategoriesFrag();
                        search.setVisibility(View.VISIBLE);
                        CategoriesFrag categoriesFrag = new CategoriesFrag();
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.popBackStack();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.frame)) // resolves to B_Fragment instance
                                .add(R.id.frame, new CategoriesFrag(), "fragment-c")
                                .commit();
                        return true;
                    }

                    case R.id.courses: {
                        mCurrentFragment="Second";
                        nexFragment=new MyCoursesFrag();
                        search.setVisibility(View.VISIBLE);
                        MyCoursesFrag myCoursesFrag = new MyCoursesFrag();
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.popBackStack();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.frame)) // resolves to B_Fragment instance
                                .add(R.id.frame, myCoursesFrag, "fragment-c")
                                .commit();

                        return true;
                    }

                    case R.id.shopping_cart: {
                        mCurrentFragment="Second";
                        nexFragment=new CartFrag();
                        search.setVisibility(View.GONE);
                        CartFrag cartFrag = new CartFrag();
                        cartFrag.verify=(CheckOutDialogInterface) MainAct.this;
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.popBackStack();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.frame)) // resolves to B_Fragment instance
                                .add(R.id.frame, cartFrag, "fragment-c")
                                .commit();


                        return true;
                    }


                    case R.id.become_instructor_item: {
                        mCurrentFragment="Second";
                        nexFragment=new InstructorFragment();
                        search.setVisibility(View.GONE);
                        InstructorFragment instructorFragment = new InstructorFragment();
                       // InstructorFragment.verify=(CheckOutDialogInterface) MainAct.this;
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.popBackStack();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.frame)) // resolves to B_Fragment instance
                                .add(R.id.frame, instructorFragment, "fragment-c")
                                .commit();

                        return true;
                    }

                    case R.id.edit_profile: {

                        newInstanceProfile();
                        return true;
                    }




                    case R.id.settings: {
                        mCurrentFragment="Second";
                        nexFragment=new SettingsFragment();

                        search.setVisibility(View.GONE);
                        SettingsFragment fragment = new SettingsFragment();
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.popBackStack();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.frame)) // resolves to B_Fragment instance
                                .add(R.id.frame, fragment, "fragment-c")
                                .commit();

                        return true;
                    }

                    case R.id.logout:
                    {
                       clearToken();
                        return true;
                    }
                    default:
                        Snackbar.make(drawerLayout,"Somethings Wrong",Snackbar.LENGTH_LONG).show();
                        return true;


                }
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);


        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        View headerView = navigationView.getHeaderView(0);
         ivUserProfile = (ImageView) headerView.findViewById(R.id.iv_user_img);
        ivUserProfile.setScaleType(ImageView.ScaleType.FIT_XY);
        ivUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search.getVisibility() == View.VISIBLE) {
                    newInstanceProfile();

                    if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                    } else {
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    }


                    navigationView.getMenu().getItem(5).setChecked(true);
                }
            }
        });
        TextView tvUserName = (TextView) headerView.findViewById(R.id.tv_user_name);


        tvUserName.setText(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_NAME));
        //Snackbar.make(this, " "+sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_MOBILE), Snackbar.LENGTH_LONG).show();
            setUserProfile();
    }

    public void setUserProfile(){
        if(!sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_IMAGE_URL).equals("")){
            Picasso.with(getApplicationContext()).load(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_IMAGE_URL)).into(ivUserProfile);
        }else {
            if(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TYPE).equals("instructor")){
                ivUserProfile.setImageResource(R.drawable.ic_instructor_default);
                ivUserProfile.setScaleType(ImageView.ScaleType.FIT_XY);

            }else {
                ivUserProfile.setImageResource(R.drawable.ic_student_default);
                ivUserProfile.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }


    private void newInstanceProfile(){
        mCurrentFragment="Second";
        nexFragment=new ProfileFrag();
        search.setVisibility(View.GONE);
        ProfileFrag fragment = new ProfileFrag();
        fragment.listener=MainAct.this;
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    private void applyFontToMenuItem(MenuItem mi) {

        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
                     mNewTitle.setSpan(new CustomTypefaceSpan("" , ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("en_font1")), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        else if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) )
                   mNewTitle.setSpan(new CustomTypefaceSpan("" , ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("ar_font")), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    private void startHomeFragment(){
        MainFrag fragment = new MainFrag();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();

    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {


            MenuItem item = menu.findItem(R.id.menu1);
            MenuItem item2 = menu.findItem(R.id.menu2);
            item.setTitle(getResources().getString(R.string.settings));
            item2.setTitle(getResources().getString(R.string.feedback));
            SpannableString spanString = new SpannableString(item.getTitle().toString());
            SpannableString spanString2 = new SpannableString(item2.getTitle().toString());


            spanString.setSpan(new TextAppearanceSpan(getApplicationContext(), android.R.style.TextAppearance_Medium), 0, spanString.length(), 0);
            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white

            spanString2.setSpan(new TextAppearanceSpan(getApplicationContext(), android.R.style.TextAppearance_Medium), 0, spanString2.length(), 0);
            spanString2.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString2.length(), 0); //fix the color to white
            item.setTitle(spanString);
            item2.setTitle(spanString2);
            return true;
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);
            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item != null && item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            } else {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void verifyDialog(int code) {
        if(code==1) {
            com.dp.uheadmaster.dialogs.CheckOutDialog checkOutDialog = new com.dp.uheadmaster.dialogs.CheckOutDialog(getApplicationContext());
            checkOutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            checkOutDialog.show();
        }
        else if(code==2){
            DeleteCourseDialog deleteCourseDialog=new DeleteCourseDialog(getApplicationContext(),MainAct.this);
            deleteCourseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            deleteCourseDialog.show();
        }
    }



    @Override
    public void onBackPressed() {


    /*    if(isSearchOpened){
            nexFragment=new MainFrag();
            search.setVisibility(View.VISIBLE);
            startHomeFragment();
            return;
        }*/

        if(mCurrentFragment.equals("Home")) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                SearchCategoriesFrag.search_category_id = 0;
                SearchSubCategoriesFrag.search_sub_category_id = 0;
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Snackbar.make(drawerLayout, getResources().getString(R.string.press_again), Snackbar.LENGTH_LONG).show();

            new Handler().postDelayed(new HandlerImp(), 2000);
        }else {
            mCurrentFragment="Home";
         //   mTitle.setText(getResources().getString(R.string.home));
          //  HomeFragment fragment = new HomeFragment();
          //  nextFragmen(fragment,getResources().getString(R.string.home));
            nexFragment=new MainFrag();
            search.setVisibility(View.VISIBLE);
            startHomeFragment();
            navigationView.getMenu().getItem(0).setChecked(true);


        }


    }


    public void clearToken(){
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),drawerLayout)) {
            progressDialog = ConfigurationFile.showDialog(this);
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);

            Call<DefaultResponse> call = apiService.clearToken(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {


                        DefaultResponse defaultResponse=response.body();
                        if (defaultResponse.getStatus() == 200) {


                            /*
                            sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.Langauge)
                             */
                            sharedPrefManager.clearToken();
                        //    sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.Langauge,ConfigurationFile.GlobalVariables.APP_LANGAUGE);
                           // sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.isFirstTime,"123");
                            finishAffinity();
                            Intent i = new Intent(MainAct.this, LoginAct.class);
                            startActivity(i);

                        } else {
                            // parse the response body …
                            Snackbar.make(drawerLayout,defaultResponse.getMessage(),Snackbar.LENGTH_LONG).show();
                            System.out.println("error Code message :" + defaultResponse.getMessage());


                        }
                    }catch (NullPointerException ex){
                        ex.printStackTrace();

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(drawerLayout, t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println(" Fialer :" + t.getMessage());
                }
            });

        } else {
            Snackbar.make(drawerLayout,getString(R.string.check_internet_connection),Snackbar.LENGTH_LONG).show();
        }
    }


    @Override
    public void refreshRecycler() {
        Toast.makeText(getApplicationContext(), "Second", Toast.LENGTH_SHORT).show();
    }

    public void hideItem(){
        search.setVisibility(View.GONE);
        m.findItem(R.id.categories).setVisible(false);
        m.findItem(R.id.courses).setVisible(false);
        m.findItem(R.id.shopping_cart).setVisible(false);
        m.findItem(R.id.become_instructor_item).setVisible(false);
        m.findItem(R.id.edit_profile).setVisible(false);
        m.findItem(R.id.settings).setVisible(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CategorySearchAdapter.delegate=null;
        SearchSubCategoriesFrag.delegate=null;
        SearchCategoriesFrag.delegate=null;
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        progressDialog=null;
        fontChanger=null;
        sharedPrefManager=null;
    }
}
