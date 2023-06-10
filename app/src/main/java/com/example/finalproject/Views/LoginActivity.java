package com.example.finalproject.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.finalproject.Adapters.LoginFragmentsAdapter;
import com.example.finalproject.Fragments.LoginTabFragment;
import com.example.finalproject.Fragments.SignupTabFragment;
import com.example.finalproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FloatingActionButton googleBtn;
    private LoginFragmentsAdapter loginAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();


        loginAdapter = new LoginFragmentsAdapter(getSupportFragmentManager(), getLifecycle());

        // add Fragments in your ViewPagerFragmentAdapter class
        loginAdapter.addFragment(new LoginTabFragment());
        loginAdapter.addFragment(new SignupTabFragment());

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.login)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.signup)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager.setAdapter(loginAdapter);

        googleBtn.setTranslationY(300);
        googleBtn.setAlpha(100);

        tabLayout.setTranslationY(300);
        tabLayout.setAlpha(1);

        googleBtn.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });



    }

    private void findViews(){
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        viewPager = (ViewPager2)findViewById(R.id.view_pager);
        googleBtn = (FloatingActionButton)findViewById(R.id.fab_google);
    }
}