package com.md_c_test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.VolleyError;
import com.db.dao.UserDbDao;
import com.db.daoImpl.UserDbDaoImpl;
import com.md.entity.UserBO;
import com.md.util.httpupload.ResponseEntity;
import com.md.util.net.JsonConverter;
import com.md.util.networkapi.GetUserInfoApi;
import com.md.util.networkapi.RegistDetailApi;
import com.md.util.request.ResponseListener;

import org.xutils.common.Callback;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.materialdialog.MaterialDialog;

public class UserDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REGIST_INFO = 0;
    public static final int CHECK_INFO = 1;


    private CircleImageView userImageView;
    private TextInputLayout userInputName;
    private TextInputLayout userInputSignature;
    private TextInputLayout userInputGender;
    private EditText userNameEditText;
    private EditText userSignatureEditText;
    private EditText userGenderEditText;
    private UserDetailActivity context;
    private LinearLayout genderLayout;
    private FloatingActionButton fab;


    //标识子Activity
    //这里用来表示选择图片界面
    private static final int ACTIVITY_SELECTUSERIMAGE = 1;
    private String userName = "userName";
    private String userSignature = "signature";
    private String userMajor = "major";
    private String userMinor = "minor";
    private int userGrade = 3;
    private String userImage = Environment.getExternalStorageDirectory() + File.separator + "image2.jpg";

    private int userGender = 0;
    private int userId;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        this.context = this;

        intent = getIntent();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("详细资料");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        initView();

        userImageView.setImageResource(R.drawable.user_img_png);


        initEvent();

    }

    private void initEvent() {
        genderLayout.setOnClickListener(this);
        userImageView.setOnClickListener(this);

    }

    private void initView() {
        userImageView = (CircleImageView) findViewById(R.id.id_user_detail_image);
        userInputName = (TextInputLayout) findViewById(R.id.id_user_detail_name);
        userInputSignature = (TextInputLayout) findViewById(R.id.id_user_detail_signature);
        userInputGender = (TextInputLayout) findViewById(R.id.id_user_detail_gender);
        genderLayout = (LinearLayout) findViewById(R.id.id_user_detail_layout_gender);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        userNameEditText = userInputName.getEditText();
        userSignatureEditText = userInputSignature.getEditText();
        userGenderEditText = userInputGender.getEditText();

        userInputName.setHint("用户名");
        userInputSignature.setHint("个人签名");
        userInputGender.setHint("性别");

        int userIdOther = intent.getIntExtra("userId", 0);
        int startFlag = intent.getIntExtra(
                getString(R.string.UserDetailStartFlag), -1);

        SharedPreferences sharedPreferences =
                getSharedPreferences(getString(R.string.sharedPreference_userInfo), Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        //若由外部调用
        if (startFlag != -1) {
            //若注册新用户
            if (startFlag == REGIST_INFO) {
                //不加载任何信息
            }
            //若查看用户信息
            else if (startFlag == CHECK_INFO) {
                showUserInfo(userIdOther);

            }
        }


        //保存或更新用户信息
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //获取用户id


                if (userId != -1) {
                    getEditData();
                    UserBO userBO = new UserBO();
                    userBO.setName(userName);
                    userBO.setGender(userGender);
                    userBO.setUserId(userId);
                    userBO.setMinor(userMinor);
                    userBO.setMajor(userMajor);
                    userBO.setSignature(userSignature);
                    userBO.setGrade(userGrade);
                    userBO.setUserImage(userImage);
                    RegistDetailApi.improve(userBO, new Callback.CommonCallback<ResponseEntity>() {
                        @Override
                        public void onSuccess(ResponseEntity result) {
                            Snackbar.make(fab, "用户资料保存成功", Snackbar.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences =
                                    getSharedPreferences(getString(R.string.sharedPreference_userInfo), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt(
                                    getString(R.string.SharedPreference_userInfo_userId), userId);
                            editor.putString(
                                    getString(R.string.SharedPreference_userInfo_userName), userName);

                            editor.putInt(
                                    getString(R.string.SharedPreference_userInfo_userGender), userGender);
                            editor.putInt(
                                    getString(R.string.SharedPreference_userInfo_userGrade), userGrade);
                            editor.putString(
                                    getString(R.string.SharedPreference_userInfo_userMajor), userMajor);
                            editor.putString(
                                    getString(R.string.SharedPreference_userInfo_userMinor), userMinor);
                            editor.putString(
                                    getString(R.string.SharedPreference_userInfo_userSignature), userSignature);
                            editor.putString(
                                    getString(R.string.SharedPreference_userInfo_userImage), userImage);

                            editor.commit();
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Snackbar.make(fab, "用户资料保存失败", Snackbar.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {

                        }
                    });


                }


            }
        });
    }

    private void showUserInfo(int userIdOther) {
        //若userId有效
        if (userIdOther != 0 && userId != -1) {
            Log.e("userIdOther", userIdOther + "");
            Log.e("userId", userId + "");
            //显示信息
            final UserDbDao userDbDao = new UserDbDaoImpl(this);
            UserBO userBO = userDbDao.getUserById(userIdOther);
            if (userBO != null) {


                userNameEditText.setText(userBO.getName());
                userSignatureEditText.setText(userBO.getSignature());
                //与下面同样的exception
                userGenderEditText.setText(userBO.getGender() + "");
            } else {
                GetUserInfoApi.getUserById(userIdOther, new ResponseListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Snackbar.make(fab, "用户信息获取失败", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Object o) {
                        String resultUser = (String) o;
                        Log.e("userInfo", resultUser);
                        UserBO userBO = JsonConverter.toBean(resultUser, UserBO.class);
                        if (userBO != null) {
                            userNameEditText.setText(userBO.getName());
                            userSignatureEditText.setText(userBO.getSignature());
                            //exception 当setText的参数为int型时，默认会加载资源文件，所以应该将int转为string
                            // android.content.res.Resources$NotFoundException: String resource ID #0x0
                            userGenderEditText.setText(userBO.getGender() + "");
                            userDbDao.addUser(userBO);
                            Snackbar.make(fab, "用户信息已加载", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Log.e("userInfo", resultUser);
                            Snackbar.make(fab, "用户信息加载失败", Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });
            }
            //若是用户查看自己的信息
            if (userIdOther == userId) {
                //设置允许编辑
                enableEdit();
                fab.show();
                //若是用户查看他人的信息
            } else {
                //设置不允许编辑
                unEnableEdit();
                fab.hide();
            }
        } else {
            Log.e("info", "无效的用户名");
        }
    }

    private void enableEdit() {
        userNameEditText.setEnabled(true);
        userSignatureEditText.setEnabled(true);
        userGenderEditText.setEnabled(true);
    }

    private void unEnableEdit() {
        userNameEditText.setEnabled(false);
        userSignatureEditText.setEnabled(false);
        userGenderEditText.setEnabled(false);
    }

    private void getEditData() {
        userName = userNameEditText.getText().toString();
        userSignature = userSignatureEditText.getText().toString();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.id_user_detail_image:
                //跳转到选择用户图像界面
                Intent intentSelectImage = new Intent(context, UserInfoSelectImageActivity.class);
                startActivityForResult(intentSelectImage, ACTIVITY_SELECTUSERIMAGE);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_SELECTUSERIMAGE:
                if (resultCode == RESULT_OK) {
                    userImage = data.getStringExtra("imagePath");
                    Uri uri = Uri.parse(userImage);
                    // LoadImageApi.display(userImageView, userImage);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    Bitmap bitmap = BitmapFactory.decodeFile(userImage, options);
                    options.inSampleSize = 1;
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeFile(userImage, options);
                    userImageView.setImageBitmap(bitmap);
                    Log.e("用户头像路径", userImage);
                    break;
                }

        }
    }

    public void selectGender(View view) {

        RadioGroup radioGroup = new RadioGroup(context);
        final RadioButton radioButtonBoy = new RadioButton(context);

        final RadioButton radioButtonGirl = new RadioButton(context);


        radioButtonBoy.setWidth(1500);
        radioButtonBoy.setHeight(150);
        radioButtonBoy.setHint("男");
        radioButtonBoy.setId(R.id.id_radioButtonBoy);

        radioButtonGirl.setWidth(1500);
        radioButtonGirl.setHeight(150);
        radioButtonGirl.setHint("女");

        radioButtonGirl.setId(R.id.id_radioButtonGirl);

        radioGroup.check(R.id.id_radioButtonGirl);

        radioGroup.addView(radioButtonBoy);
        radioGroup.addView(radioButtonGirl);
        final MaterialDialog materialDialog = new MaterialDialog(UserDetailActivity.this);
        materialDialog.setTitle("选择性别").setContentView(radioGroup).setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonBoy.isChecked()) {
                    radioButtonGirl.setChecked(false);
                    userGender = 1;
                    userGenderEditText.setText("男");
                    Log.e("userGender", String.valueOf(userGender));
                } else if (radioButtonGirl.isChecked()) {
                    radioButtonBoy.setChecked(false);
                    userGender = 0;
                    userGenderEditText.setText("女");
                    Log.e("userGender", String.valueOf(userGender));
                }
                materialDialog.dismiss();
            }
        });
        materialDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
