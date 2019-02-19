package com.example.qrbarcodescanner;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.qrbarcodescanner.addapter.ViewPagerAdapter;
import com.example.qrbarcodescanner.fragment.HistoryFragment;
import com.example.qrbarcodescanner.fragment.ScanFragment;
import com.example.qrbarcodescanner.util.AppConstants;
import com.google.android.gms.ads.AdView;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,TabLayout.OnTabSelectedListener {
    private TabLayout tabLayout;
    AdView adView;
    private ViewPager viewPager;
    boolean doubleBackToExitPressedOnce = false;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        requestpermission();
/*

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        adView=(AdView)findViewById(R.id.adView);
        AppConstants.codeForBannerAd(Main2Activity.this,adView);

        if(AppConstants.checkInternetConnection(Main2Activity.this)){
            adView.setVisibility(View.VISIBLE);
        }else{
            adView.setVisibility(View.GONE);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

   /* @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        String title = getString(R.string.app_name);
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (id == R.id.nav_home) {
            // Handle the camera action
            drawer.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle(title);

            return true;
        } /*else if (id == R.id.nav_how_to_use) {
            Intent guide = new Intent(MainActivity.this, HowToUseActivity.class);
            guide.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(guide);
            drawer.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle(title);
            return true;
        } else if (id == R.id.model_papers) {
            Intent intent = new Intent(MainActivity.this, ModelQuesPaperActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            drawer.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle(title);
            return true;

        } */
        else if (id == R.id.nav_rate_us) {

            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.example.qrbarcodescanner");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.example.qrbarcodescanner")));
            }
            drawer.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle(title);

            return true;

        } else if (id == R.id.about_us) {
            Intent policy = new Intent(Main2Activity.this, PolicyActivity.class);
            startActivity(policy);
            drawer.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle(title);
            return true;

        }
        else if (id == R.id.nav_share_app) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            String shareUrl = "https://play.google.com/store/apps/details?id=com.example.qrbarcodescanner";
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareUrl);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            drawer.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle(title);
            return true;
        }
        /*else if (id == R.id.more_apps) {

            Intent _intent = new Intent(Intent.ACTION_VIEW);
            _intent.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Aryaa+Infotech"));
            startActivity(_intent);
            drawer.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle(title);
            return true;
        }*/
        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition(),true);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ScanFragment(),"Scan");
        adapter.addFragment(new HistoryFragment(), "History");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }
    }
    private void requestpermission(){
        ActivityCompat.requestPermissions(Main2Activity.this, new
                String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
    }

}
