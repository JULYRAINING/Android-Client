package com.md_c_test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.md.parcelable.IntentDataParcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImageFullScreenActivity extends AppCompatActivity {

    @ViewInject(R.id.id_top_tv)
    private TextView mTopTv;
    @ViewInject(R.id.id_top_right)
    private TextView mTopRight;
    @ViewInject(R.id.id_image_viewpager)
    private ViewPager mImgViewPager;


    private Toolbar toolbar;
    private List<HashMap<String, String>> list;
    private PagerAdapter adapter;
    private List<View> mImgViews;
    private int index;
    private int pos;
    private static final int SUCCESS = 711;

    private boolean FLAG = true;
    private BitmapUtils mBtpUtils;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    creatAdapterInstence();
                    mImgViewPager.setAdapter(adapter);
                    mImgViewPager.setCurrentItem(pos);

                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image_activity);

        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("查看图片");
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        ViewUtils.inject(this);
        mImgViews = new ArrayList<View>();

        getIntentData();
        Thread mThread = new Thread(mWork);
        mThread.start();


    }

    private void getIntentData() {
        mBtpUtils = new BitmapUtils(getApplicationContext());
        mTopRight.setText(getResources().getString(R.string.default_ok_tv));
        Intent intent = getIntent();
        IntentDataParcelable idp = intent.getParcelableExtra("Image_Path");
        pos = idp.mInt;
        list = idp.mList;
        setToolbarTitle();
    }

    private void creatAdapterInstence() {
        adapter = new PagerAdapter() {

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mImgViews.get(position));
                return mImgViews.get(position);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return list.size();
            }

            @Override
            public int getItemPosition(Object object) {
                // TODO Auto-generated method stub
                return POSITION_NONE;
            }

        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTopTv = null;
        mTopRight = null;
        mImgViewPager = null;

        mImgViews = null;
        list = null;
        mTopRight = null;
        mBtpUtils = null;
        setContentView(R.layout.view_null);
    }

    private void loadViewPagerItem() {

        for (int i = 0; i < list.size(); i++) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.image_full_screen_viewpager_item, null);
            ImageView img = (ImageView) view
                    .findViewById(R.id.id_image_full_screen_viewpager_item);

            mBtpUtils.display(img, list.get(i).get("Image"));

            mImgViews.add(view);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.id_delete_image:
                if (index > 0) {
                    removeView();
                } else if (index == 0 && mImgViews.size() != 1) {
                    removeView();
                } else if (index == 0 && mImgViews.size() == 1) {
                    removeView();
                    Toast.makeText(ImageFullScreenActivity.this,
                            getResources().getString(R.string.alert_fullImage_Reselect),
                            Toast.LENGTH_SHORT).show();
                    resultData();
                }
                break;
            case android.R.id.home:
                resultData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_full_image, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTopRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                resultData();

            }

        });


        mImgViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                index = arg0;

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                setToolbarTitle();
            }
        });
    }

    private void removeView() {
        mImgViews.remove(index);
        list.remove(index);
        adapter.notifyDataSetChanged();
        setToolbarTitle();


    }

    private void setToolbarTitle() {
        toolbar.setTitle(getResources().getString(R.string.alert_fullImage_title)
                + (mImgViewPager.getCurrentItem() + 1)
                + "/" + list.size());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                resultData();
                break;
        }
        return true;
    }

    private void resultData() {
        Intent intent_Result_Img_List = new Intent();
        IntentDataParcelable idp = new IntentDataParcelable();
        idp.mList = (ArrayList<HashMap<String, String>>) list;
        intent_Result_Img_List.putExtra("Select_Img_Result", idp);
        setResult(RESULT_OK, intent_Result_Img_List);
        finish();
    }

    private Runnable mWork = new Runnable() {

        @Override
        public void run() {
            loadViewPagerItem();// TODO Auto-generated method stub
            mHandler.obtainMessage(SUCCESS).sendToTarget();
        }
    };
}
