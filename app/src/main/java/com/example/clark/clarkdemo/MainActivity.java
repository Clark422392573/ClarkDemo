package com.example.clark.clarkdemo;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clark.clarkdemo.activity.HotActivity;
import com.example.clark.clarkdemo.activity.MomentsActivity;
import com.example.clark.clarkdemo.activity.SearchActivity;
import com.example.clark.clarkdemo.bean.UpdateAppInfo;
import com.example.clark.clarkdemo.fantasyslide.SideBar;
import com.example.clark.clarkdemo.fantasyslide.Transformer;
import com.example.clark.clarkdemo.fragment.FragmentA;
import com.example.clark.clarkdemo.fragment.FragmentB;
import com.example.clark.clarkdemo.fragment.FragmentC;
import com.example.clark.clarkdemo.fragment.FragmentD;
import com.example.clark.clarkdemo.fragment.FragmentE;
import com.example.clark.clarkdemo.spacetablayout.SpaceTabLayout;
import com.example.clark.clarkdemo.update.AppInnerDownLoder;
import com.example.clark.clarkdemo.update.CheckUpdateUtils;
import com.example.clark.clarkdemo.update.DownLoadApk;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SpaceTabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar toolbar;
    private List<Fragment> fragmentList;
    private AlertDialog.Builder mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        tabLayout.initialize(viewPager, getSupportFragmentManager(), fragmentList, savedInstanceState);
        setDrawerLayout();

        // 网络检查版本是否需要更新
        CheckUpdateUtils.checkUpdate("apk", "1.0.0", new CheckUpdateUtils.CheckCallBack() {
            @Override
            public void onSuccess(UpdateAppInfo updateInfo) {
                // 是否需要强制更新
                String isForce = updateInfo.data.getLastForce();
                // apk下载地址
                String downUrl = updateInfo.data.getUpdateurl();
                // apk更新详情
                String updateinfo = updateInfo.data.getUpgradeinfo();
                String appName = updateInfo.data.getAppname();
                if (isForce.equals("1") && !TextUtils.isEmpty(updateinfo)) {
                    // 强制更新
                    forceUpdate(MainActivity.this, appName, downUrl, updateinfo);
                } else {
                    // 非强制更新
                    normalUpdate(MainActivity.this, appName, downUrl, updateinfo);
                }
            }

            @Override
            public void onError() {
                noneUpdate(MainActivity.this);
            }
        });
    }


    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (SpaceTabLayout) findViewById(R.id.spaceTabLayout);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentA());
        fragmentList.add(new FragmentB());
        fragmentList.add(new FragmentC());
        fragmentList.add(new FragmentD());
        fragmentList.add(new FragmentE());
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

    /**
     * 强制更新
     *
     * @param context
     * @param appName
     * @param downUrl
     * @param updateinfo
     */
    private void forceUpdate(final Context context, final String appName, final String downUrl, final String updateinfo) {
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle(appName + "又更新咯！");
        mDialog.setMessage(updateinfo);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!canDownloadState()) {
                    showDownloadSetting();
                    return;
                }
                //   DownLoadApk.download(MainActivity.this,downUrl,updateinfo,appName);
                AppInnerDownLoder.downLoadApk(MainActivity.this, downUrl, appName);
            }
        }).setCancelable(false).create().show();
    }

    /**
     * 正常更新
     *
     * @param context
     * @param appName
     * @param downUrl
     * @param updateinfo
     */
    private void normalUpdate(Context context, final String appName, final String downUrl, final String updateinfo) {
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle(appName + "又更新咯！");
        mDialog.setMessage(updateinfo);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!canDownloadState()) {
                    showDownloadSetting();
                    return;
                }
                // AppInnerDownLoder.downLoadApk(MainActivity.this,downUrl,appName);
                DownLoadApk.download(MainActivity.this, downUrl, updateinfo, appName);
            }
        }).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(false).create().show();
    }

    /**
     * 无需跟新
     *
     * @param context
     */
    private void noneUpdate(Context context) {
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle("版本更新")
                .setMessage("当前已是最新版本无需更新")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false).create().show();
    }

    private void showDownloadSetting() {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (intentAvailable(intent)) {
            startActivity(intent);
        }
    }

    private boolean intentAvailable(Intent intent) {
        PackageManager packageManager = getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    private boolean canDownloadState() {
        try {
            int state = this.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void onClick(View view) {
        if (view instanceof TextView) {
            String title = ((TextView) view).getText().toString();
            if (title.startsWith("星期")) {
                Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
            } else if (title.equals("朋友圈")) {
                startActivity(new Intent(MainActivity.this, MomentsActivity.class));
            } else if (title.equals("搜索")) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            } else if (title.equals("热门")) {
                startActivity(new Intent(MainActivity.this, HotActivity.class));
            } else {
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
