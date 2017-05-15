package com.example.rshikkal.icafe.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.rshikkal.icafe.Adapters.MenuAdapter;
import com.example.rshikkal.icafe.Models.User;
import com.example.rshikkal.icafe.Preferences.LoginPreferences;
import com.example.rshikkal.icafe.R;
import com.example.rshikkal.icafe.ServerUtils.ServerRequests;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity {

    TextView useremailTV, usernameTV, usermobileTV, usercubeTV, navigationTitleTV;
    ImageView hamburgerIV;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    User user;
    LoginPreferences loginPreferences;
    SliderLayout mDemoSlider;
    RecyclerView recyclerView;
    private MenuAdapter mAdapter;
    RelativeLayout progressRL;
    FloatingActionButton cartFAB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applyFont(MainActivity.this,findViewById(R.id.baselayout));
        navigationTitleTV = (TextView)findViewById(R.id.navigationtitle);
        hamburgerIV = (ImageView) findViewById(R.id.hamburgericon);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.baselayout);
        navigationView = (NavigationView)findViewById(R.id.navigation_drawer);
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        progressRL = (RelativeLayout)findViewById(R.id.progressview);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        navigationTitleTV.setText("Home");
        cartFAB = (FloatingActionButton) findViewById(R.id.cart);


        loginPreferences = new LoginPreferences(MainActivity.this);
        user = loginPreferences.getUser();

        //get the navigation header view to show Email Id of LoggedIn user
        View view = navigationView.getHeaderView(0);
        hamburgerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        //this function sets the username,emailid... of logged In user
        init(view);
        loadMenu();
        //handling navigation item click listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                 switch (item.getItemId()){
                    case R.id.orders:
                        startActivity(new Intent(MainActivity.this,OrdersActivity.class));
                        break;
                    case R.id.help:
                        startActivity(new Intent(MainActivity.this,HelpActivity.class));
                        break;
                    case R.id.aboutus:
                        startActivity(new Intent(MainActivity.this,AboutusActivity.class));
                        break;
                }

                return true;
            }
        });

        //add the images here
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("1",R.drawable.first);
        file_maps.put("2",R.drawable.first);
        file_maps.put("3", R.drawable.first);


        for(String name : file_maps.keySet()){
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {

                        }
                    });

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(5000);
        mDemoSlider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        cartFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });


    }

    private void loadMenu() {
        new AsyncTask<Void,Void,List<com.example.rshikkal.icafe.Models.MenuItem>>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressRL.setVisibility(View.VISIBLE);
            }

            @Override
            protected List<com.example.rshikkal.icafe.Models.MenuItem> doInBackground(Void... params) {
                return new ServerRequests().loadMenu(MainActivity.this);
            }

            @Override
            protected void onPostExecute(List<com.example.rshikkal.icafe.Models.MenuItem> response) {
                super.onPostExecute(response);
                progressRL.setVisibility(View.GONE);
                Log.e("list",response.toString());
                mAdapter = new MenuAdapter(response,MainActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            }

        }.execute();
    }


    //sets the username,emailid... of user in the navigation header
    private void init(View view) {
        usernameTV = (TextView)view.findViewById(R.id.username);
        useremailTV = (TextView)view.findViewById(R.id.useremail);
        usermobileTV = (TextView)view.findViewById(R.id.usermobile);
        usercubeTV = (TextView)view.findViewById(R.id.usercube);
        if(user.getUseremail() != null)
            useremailTV.setText(user.getUseremail());
        if (user.getUsername() != null)
            usernameTV.setText(user.getUsername());
        if (user.getMobile() != null)
            usermobileTV.setText(user.getMobile());
        if (user.getCube() != null)
            usercubeTV.setText("Cube -" + user.getCube());

    }



}
