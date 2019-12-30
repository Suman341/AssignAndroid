package com.softwarica.printstation.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.softwarica.printstation.PrintStationApplication;
import com.softwarica.printstation.R;
import com.softwarica.printstation.api.API;
import com.softwarica.printstation.api.response.ApiResponse;
import com.softwarica.printstation.entity.UserEntity;
import com.softwarica.printstation.ui.auth.LoginActivity;
import com.softwarica.printstation.ui.dashboard.about.AboutUsFragment;
import com.softwarica.printstation.ui.dashboard.contact.ContactUsFragment;
import com.softwarica.printstation.ui.dashboard.home.HomeFragment;
import com.softwarica.printstation.ui.profile.EditProfileActivity;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {



    private ActionBarDrawerToggle mDrawerToggle;
    private ImageView profileImage;
    private TextView name;
    private TextView email;
    private Toolbar toolbar;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        initUI();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, new HomeFragment())
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.nav_home: {
                    toolbar.setTitle("PrintStation Nepal");
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container, new HomeFragment())
                            .commitAllowingStateLoss();
                    drawer.closeDrawer(Gravity.LEFT);
                    break;
                }
                case R.id.nav_about_us: {
                    toolbar.setTitle("About Us");
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container, new AboutUsFragment())
                            .commitAllowingStateLoss();
                    drawer.closeDrawer(Gravity.LEFT);
                    break;
                }
}
