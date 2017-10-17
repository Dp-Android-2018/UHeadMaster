package com.dp.uheadmaster.activites;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
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
import com.dp.uheadmaster.models.CustomTypefaceSpan;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.squareup.picasso.Picasso;

import android.support.design.widget.NavigationView;

import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class MainAct extends AppCompatActivity implements CheckOutDialogInterface {


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Fragment nextFrag=null;
    public static boolean isSearchOpened=false;
    public Fragment nexFragment=null;

    private Typeface typeface;
    private SharedPrefManager sharedPrefManager;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onResume() {
        super.onResume();
        SubCategoriesAct.subCategoryId=0;
        CategoriesFrag.categoryId=0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        sharedPrefManager =new SharedPrefManager(getApplicationContext());

        SubCategoriesAct.subCategoryId=0;
        CategoriesFrag.categoryId=0;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        final TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView search=(ImageView)toolbar.findViewById(R.id.iv_search);
        search.setVisibility(View.VISIBLE);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSearchOpened==false) {
                    isSearchOpened=true;
                    BasicSearchFrag fragment = new BasicSearchFrag();
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.commit();
                }
                else if(isSearchOpened) {
                    isSearchOpened=false;

                    SearchCategoriesFrag.search_category_id=0;
                    SearchSubCategoriesFrag.search_sub_category_id=0;
                    MainFrag fragment = new MainFrag();
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
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

            MainFrag fragment = new MainFrag();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();


        }

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        Menu m = navigationView.getMenu();

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
                        nexFragment=new MainFrag();
                        MainFrag fragment = new MainFrag();
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.commit();
                        return true;
                    }


                    case R.id.categories: {
                        nexFragment=new CategoriesFrag();
                        CategoriesFrag categoriesFrag = new CategoriesFrag();
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, categoriesFrag).addToBackStack(null);
                        fragmentTransaction.commit();


                        return true;
                    }

                    case R.id.courses: {
                        nexFragment=new MyCoursesFrag();
                        MyCoursesFrag myCoursesFrag = new MyCoursesFrag();
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, myCoursesFrag).addToBackStack(null);
                        fragmentTransaction.commit();
                        return true;
                    }

                    case R.id.shopping_cart: {
                        nexFragment=new CartFrag();
                        CartFrag cartFrag = new CartFrag();
                        cartFrag.verify=(CheckOutDialogInterface) MainAct.this;
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, cartFrag).addToBackStack(null);
                        fragmentTransaction.commit();

                        return true;
                    }


                    case R.id.become_instructor_item: {
                        nexFragment=new InstructorFragment();
                        InstructorFragment instructorFragment = new InstructorFragment();
                       // InstructorFragment.verify=(CheckOutDialogInterface) MainAct.this;
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, instructorFragment).addToBackStack(null);
                        fragmentTransaction.commit();

                        return true;
                    }

                    case R.id.edit_profile: {
                        nexFragment=new ProfileFrag();
                        ProfileFrag fragment = new ProfileFrag();
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment).addToBackStack(null);
                        fragmentTransaction.commit();

                        return true;
                    }


                    case R.id.policy: {

                        return true;
                    }

                    case R.id.settings: {
                        nexFragment=new SettingsFragment();
                        SettingsFragment fragment = new SettingsFragment();
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment).addToBackStack(null);
                        fragmentTransaction.commit();

                        return true;
                    }

                    case R.id.logout:
                    {
                        sharedPrefManager.clearToken();
                        Intent i = new Intent(MainAct.this, LoginAct.class);
                        startActivity(i);
                        finish();
                        return true;
                    }
                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
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
        ImageView ivUserProfile = (ImageView) headerView.findViewById(R.id.iv_user_img);
        TextView tvUserName = (TextView) headerView.findViewById(R.id.tv_user_name);

        tvUserName.setText(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_NAME));
        //Toast.makeText(this, " "+sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_MOBILE), Toast.LENGTH_LONG).show();
        if(!sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_IMAGE_URL).equals("")){
            Picasso.with(getApplicationContext()).load(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_IMAGE_URL)).into(ivUserProfile);
        }
    }


    private void applyFontToMenuItem(MenuItem mi) {

        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
                     mNewTitle.setSpan(new CustomTypefaceSpan("" , ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("en_font1")), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        else if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) )
                   mNewTitle.setSpan(new CustomTypefaceSpan("" , ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("ar_font")), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu1);
        MenuItem item2 = menu.findItem(R.id.menu2);
        item.setTitle(getResources().getString(R.string.settings));
        item2.setTitle(getResources().getString(R.string.feedback));
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        SpannableString spanString2 = new SpannableString(item2.getTitle().toString());



        spanString.setSpan(new TextAppearanceSpan(getApplicationContext(),android.R.style.TextAppearance_Medium), 0,spanString.length(), 0);
        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white

        spanString2.setSpan(new TextAppearanceSpan(getApplicationContext(),android.R.style.TextAppearance_Medium), 0,spanString2.length(), 0);
        spanString2.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString2.length(), 0); //fix the color to white
        item.setTitle(spanString);
        item2.setTitle(spanString2);
        return true;
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

        return super.onCreateOptionsMenu(menu);
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
            com.dp.uheadmaster.dialogs.CheckOutDialog checkOutDialog = new com.dp.uheadmaster.dialogs.CheckOutDialog(MainAct.this);
            checkOutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            checkOutDialog.show();
        }
        else if(code==2){
            DeleteCourseDialog deleteCourseDialog=new DeleteCourseDialog(MainAct.this);
            deleteCourseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            deleteCourseDialog.show();
        }
    }



    @Override
    public void onBackPressed() {

       if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
        }
        else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                SearchCategoriesFrag.search_category_id = 0;
                SearchSubCategoriesFrag.search_sub_category_id = 0;
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toasty.info(this, getResources().getString(R.string.press_again), Toast.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }

    }
}
