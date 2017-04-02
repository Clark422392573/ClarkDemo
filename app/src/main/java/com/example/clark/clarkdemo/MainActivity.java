package com.example.clark.clarkdemo;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clark.clarkdemo.activity.HotActivity;
import com.example.clark.clarkdemo.activity.MomentsActivity;
import com.example.clark.clarkdemo.activity.SearchActivity;
import com.example.clark.clarkdemo.fantasyslide.SideBar;
import com.example.clark.clarkdemo.fantasyslide.Transformer;
import com.example.clark.clarkdemo.fragment.FragmentA;
import com.example.clark.clarkdemo.fragment.FragmentB;
import com.example.clark.clarkdemo.fragment.FragmentC;
import com.example.clark.clarkdemo.fragment.FragmentD;
import com.example.clark.clarkdemo.fragment.FragmentE;
import com.example.clark.clarkdemo.spacetablayout.SpaceTabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SpaceTabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (SpaceTabLayout) findViewById(R.id.spaceTabLayout);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentA());
        fragmentList.add(new FragmentB());
        fragmentList.add(new FragmentC());
        fragmentList.add(new FragmentD());
        fragmentList.add(new FragmentE());
        tabLayout.initialize(viewPager, getSupportFragmentManager(), fragmentList, savedInstanceState);

        setDrawerLayout();
    }

    private void setDrawerLayout() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);
        mToggle.syncState();
        drawerLayout.setDrawerListener(mToggle);
        setTransformer();
        drawerLayout.setScrimColor(Color.TRANSPARENT);
    }

    private void setTransformer() {
        final float spacing = getResources().getDimensionPixelSize(R.dimen.spacing);
        SideBar rightSideBar = (SideBar) findViewById(R.id.rightSideBar);
        rightSideBar.setTransformer(new Transformer() {
            private View lastHoverView;

            @Override
            public void apply(ViewGroup sideBar, View itemView, float touchY, float slideOffset, boolean isLeft) {
                boolean hovered = itemView.isPressed();
                if (hovered && lastHoverView != itemView) {
                    animateIn(itemView);
                    animateOut(lastHoverView);
                    lastHoverView = itemView;
                }
            }
            private void animateOut(View view) {
                if (view == null) {
                    return;
                }
                ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", -spacing, 0);
                translationX.setDuration(200);
                translationX.start();
            }
            private void animateIn(View view) {
                ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", 0, -spacing);
                translationX.setDuration(200);
                translationX.start();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
        return true;
    }

    public void onClick(View view) {
        if (view instanceof TextView) {
            String title = ((TextView) view).getText().toString();
            if (title.startsWith("星期")) {
                Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
            }else if (title.equals("朋友圈")){
                startActivity(new Intent(MainActivity.this, MomentsActivity.class));
            }else if (title.equals("搜索")){
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }else if (title.equals("热门")){
                startActivity(new Intent(MainActivity.this, HotActivity.class));
            }else {
                Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.userInfo) {
            Toast.makeText(this, "个人中心", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        tabLayout.saveState(outState);
        super.onSaveInstanceState(outState);
    }
}
