package com.example.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.finalproject.Adapters.ContentsFragmentsAdapter;
import com.example.finalproject.Fragments.NewWorkFormFragment;
import com.example.finalproject.Fragments.WorksFormFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity  {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    /*-----------------Fragments variables----------------------*/
    private ViewPager2 viewPager;
    private ContentsFragmentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*-----------------Hooks----------------------*/
        findsViews();

        /*-----------------Navigation Drawer Menu----------------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        /*-----------------Contents Fragments----------------------*/
        /*adapter = new ContentsFragmentsAdapter(getSupportFragmentManager(), getLifecycle());

        // add Fragments to Adapter
        adapter.addFragment(new NewWorkFormFragment());
        adapter.addFragment(new WorksFormFragment());

        // Setting Adapter to viewPager
        viewPager.setAdapter(adapter);*/

        // Begin the transaction
        replaceFragment(R.id.frame_layout, new NewWorkFormFragment());

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            setFragment(menuItem);
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    private void setFragment(MenuItem menuItem){
        if(menuItem.getItemId() == R.id.nav_create_new_work){
            //viewPager.setCurrentItem(0);
            replaceFragment(R.id.frame_layout, new NewWorkFormFragment());
        }
        else if(menuItem.getItemId() == R.id.nav_my_works) {
            //viewPager.setCurrentItem(1);
            replaceFragment(R.id.frame_layout, new WorksFormFragment());
        }
        else{
            Log.e("Error", "onCreate: ", new Throwable("nav clicked not handled"));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void replaceFragment(Integer frameLayout, Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(frameLayout, fragment)
                .commit();
    }
    private void findsViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        //viewPager = findViewById(R.id.view_pager);
    }



}