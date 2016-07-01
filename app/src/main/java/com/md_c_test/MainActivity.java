package com.md_c_test;


import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.com.fragment.FragmentCourse;
import com.com.fragment.FragmentMsg;
import com.com.fragment.FragmentnNotice;
import com.md.adapter.FragmentViewPagerAdapter;
import com.service.SendMessageService;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> titles;
    private Toolbar toolbar;
    private List<Fragment> fragmentList;
    private FragmentViewPagerAdapter adapter;
    private UploadMsgBroadcastReceiver uploadMsgBroadcastReceiver;
    private ImageView uploadImage;
    private AnimationDrawable animationDrawable;
    private IntentFilter uploadMsgIntentFilter;
    private CircleImageView userImage;
    private TextView userName;
    private TextView userSingature;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SendMessageService.UPLOAD_START:
                    Snackbar.make(tabLayout, "发送开始", Snackbar.LENGTH_SHORT).show();
                    uploadImage.setVisibility(View.VISIBLE);
                    animationDrawable.start();
                    Log.e("send", "发送开始");
                    break;
                case SendMessageService.UPLOAD_FINISH:
                    Snackbar.make(tabLayout, "发送完成", Snackbar.LENGTH_SHORT).show();

                    uploadImage.setVisibility(View.INVISIBLE);
                    animationDrawable.stop();
                    Log.e("send", "发送结束");
                    break;
                case SendMessageService.UPLOAD_FAILED:
                    Snackbar.make(tabLayout, "发送失败", Snackbar.LENGTH_SHORT).show();

                    uploadImage.setVisibility(View.INVISIBLE);
                    animationDrawable.stop();
                    Log.e("send", "发送失败");
                    break;

            }

        }
    };

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("The Cup");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.main_custom_view);
        uploadImage = (ImageView) findViewById(R.id.id_main_custom_upload_image);
        uploadImage.setVisibility(View.INVISIBLE);

        //uploadImage.setBackgroundResource(R.drawable.main_upload_animation);

        animationDrawable = (AnimationDrawable) uploadImage.getBackground();

        initDrawerLayout(toolbar);
        initTabTitle();
        initTabLayout();
        initViewPager();
        cooTwo();

        uploadMsgBroadcastReceiver = new UploadMsgBroadcastReceiver();
        uploadMsgIntentFilter = new IntentFilter();
        uploadMsgIntentFilter.addAction(SendMessageService.UNIQUE_STRING);
        registerReceiver(uploadMsgBroadcastReceiver, uploadMsgIntentFilter);

    }


    private void cooTwo() {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);


    }

    private void initTabTitle() {
        titles = new ArrayList<>();
        titles.add("精选");
        titles.add("课程");
        titles.add("其它");
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        fragmentList = new ArrayList<>();
        FragmentMsg fragmentMsg = new FragmentMsg();
        FragmentCourse fragmentcourse = new FragmentCourse();
        FragmentnNotice fragmentOther = new FragmentnNotice();
        fragmentList.add(fragmentMsg);
        fragmentList.add(fragmentcourse);
        fragmentList.add(fragmentOther);

        adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragmentList, titles);

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    }

    private void initTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(2)));

    }

    private void initDrawerLayout(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setStatusBarBackgroundColor(getResources().getColor(
                android.R.color.holo_blue_light
        ));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        LinearLayout headerLayout = (LinearLayout) headerView.findViewById(R.id.id_header_layout);
        headerLayout.setBackgroundResource(R.drawable.blackcase);
        userImage = (CircleImageView) headerView.findViewById(R.id.navigation_header_imageView);
        userName = (TextView) headerView.findViewById(R.id.navigation_header_userName);
        userSingature = (TextView) headerView.findViewById(R.id.navigation_header_signature);

        navigationView.setNavigationItemSelectedListener(this);
        userImage.setOnClickListener(this);
        userName.setOnClickListener(this);
        userSingature.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.add_message:
                //发表新文章
                Intent intent = new Intent(MainActivity.this, MsgSend.class);

                startActivityForResult(intent, FragmentMsg.SEND_MESSAGE_AVTIVITY);
                return true;

            case R.id.search_message:
                //搜索
                break;
            case R.id.new_message:
                //查看私信
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.collect:
                Snackbar.make(tabLayout, "暂时无收藏信息", Snackbar.LENGTH_LONG)
                        .show();
                break;
            case R.id.share:
                Snackbar.make(tabLayout, "分享到", Snackbar.LENGTH_LONG)
                        .show();
                break;
            case R.id.send:
                Snackbar.make(tabLayout, "问题反馈", Snackbar.LENGTH_LONG)
                        .show();
                break;
            case R.id.info:
                Snackbar.make(tabLayout, "我的信息", Snackbar.LENGTH_LONG)
                        .show();
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //弃用，不再接收MsgSend的数据，而是再次从服务器获取信息
//        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`
        //之前从Msg_Send返回的数据
        //经由这里发送到Fragment_Msg
        List<Fragment> fragments = this.getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof FragmentMsg) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }

            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.navigation_header_imageView:
            case R.id.navigation_header_userName:
            case R.id.navigation_header_signature:


                SharedPreferences sharedPreferences =
                        getSharedPreferences(getString(R.string.sharedPreference_userInfo), Context.MODE_PRIVATE);
                int userId = sharedPreferences.getInt("userId", 0);


                Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
                intent.putExtra("userId", userId);
                //输入标记，表示查看用户信息
                intent.putExtra(getString(R.string.UserDetailStartFlag),
                        UserDetailActivity.CHECK_INFO);

                startActivity(intent);
                break;
        }
    }

    public class UploadMsgBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("uploadFile", "MainActivity UploadMsgBroadcastReceiver已启动");
            if (intent.getAction().equals(SendMessageService.UNIQUE_STRING)) {
                Log.e("uploadState","data:"+intent.getIntExtra(
                        getString(
                                R.string.SendMessageService_SendBroadcast_Intent_flag), -1));
                handler.sendEmptyMessage(intent.getIntExtra(
                        getString(
                                R.string.SendMessageService_SendBroadcast_Intent_flag), -1));

            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(uploadMsgBroadcastReceiver);

    }


}
