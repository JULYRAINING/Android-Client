package com.md_c_test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.md.entity.TokenValidate;
import com.md.entity.UserBO;
import com.md.entity.UserValidate;
import com.md.util.net.JsonConverter;
import com.md.util.net.MD5Convert;
import com.md.util.networkapi.GetUserInfoApi;
import com.md.util.networkapi.LoginApi;
import com.md.util.request.ResponseListener;


public class LoginActivity extends Activity implements View.OnClickListener {


    private static int MODE = MODE_PRIVATE;
    private TextInputLayout user_name_ed_container;
    private TextInputLayout user_psd_ed_container;
    private TextView forgetPassword;
    private TextView register;
    private TextView customLogin;
    private LoginActivity context;
    private EditText user_name_ed, user_psd_ed;
    private Button login_btn;
    private String userName, userPassword;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        context = this;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("登录中");
        initView();
        initEvent();
        //获取EditText输入的数据，赋值给userName userPassword
        getEditData();
        //createUserDb();
        //createMessageDb();

    }


    private void getEditData() {
        userName = user_name_ed.getText().toString();
        userPassword = user_psd_ed.getText().toString();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //读取SharedPreferences中的数据
        loadSharedPreferences();

    }

    private void loadSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreference_userInfo), MODE);
        String name = sharedPreferences.getString("userName", "cytus");
        String password = sharedPreferences.getString("userPassword", "cytus");


        user_name_ed.setText(name);
        user_psd_ed.setText(password);

    }

    @Override
    protected void onStop() {
        super.onStop();
        //保存数据到SharedPreferences
        SaveSharedPreferences();

    }

    private void SaveSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreference_userInfo), MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("userName", userName);
        editor.putString("userPassword", userName);

        editor.commit();
    }

    private void initEvent() {
        user_name_ed.setOnClickListener(this);
        user_psd_ed.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        register.setOnClickListener(this);
        customLogin.setOnClickListener(this);

    }

    private void initView() {
        user_name_ed_container = (TextInputLayout) findViewById(R.id.id_login_name);
        user_psd_ed_container = (TextInputLayout) findViewById(R.id.id_login_password);
        forgetPassword = (TextView) findViewById(R.id.id_login_forget_password);
        register = (TextView) findViewById(R.id.id_login_register);
        customLogin = (TextView) findViewById(R.id.id_custom_login);

        user_name_ed_container.setHint(getString(R.string.input_name));
        user_psd_ed_container.setHint(getString(R.string.input_password));
        user_name_ed = user_name_ed_container.getEditText();
        user_psd_ed = user_psd_ed_container.getEditText();
        login_btn = (Button) findViewById(R.id.id_login_button);
        //监听password的输入 提示错误
        user_psd_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                user_psd_ed_container.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user_psd_ed_container.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_login_name:
                break;
            case R.id.id_login_password:
                break;
            case R.id.id_login_button:
                getEditData();
                if (userName.length() == 0 || userPassword.length() == 0) {
                    Toast.makeText(LoginActivity.this,
                            getResources().getString(R.string.alert_login_noNameOrPassword),
                            Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    userLogin();
                }
//                startActivityToMain();


                break;
            case R.id.id_login_forget_password:

                break;
            case R.id.id_login_register:
                //跳转到注册界面
                Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
                break;
            case R.id.id_custom_login:
                progressDialog.show();
                startActivityToMain();

                break;
        }
    }

    /**
     * 连接服务器 验证用户名密码
     * 验证成功后 服务器返回tokenInfo(userId,token)
     * 将tokenInfo保存至SharedPreference
     * <p/>
     * 再次发送请求 获取用户信息
     * 将用户信息userInfo保存至SharedPreference
     * 跳转到主界面
     */
    private void userLogin() {

        UserValidate userValidate = new UserValidate();

        userValidate.setName(userName);
        userValidate.setPassword(MD5Convert.getMd5(userPassword));
        LoginApi.login(userValidate, new ResponseListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                user_psd_ed_container.setError("用户名或密码不正确");
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(Object o) {


                String resultToken = (String) o;
                TokenValidate tokenValidate = JsonConverter.toBean(resultToken, TokenValidate.class);

                int userId = tokenValidate.getUserId();
                String token = tokenValidate.getToken();

                String hint_msg = userName + "," + getResources().getString(R.string.alert_login_welcome);

                //保存token信息到tokenInfo
                //数据项 userId token
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences_tokenInfo), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(getString(R.string.sharedPreference_tokenInfo_userId), userId);
                editor.putString(getString(R.string.sharedPreference_tokenInfo_token), token);
                editor.commit();

                progressDialog.setMessage("正在获取用户信息...");
                GetUserInfoApi.getUserById(userId, new ResponseListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("info", "用户信息获取失败");
                        Snackbar.make(user_psd_ed, "用户信息获取失败", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Object o) {
                        Log.e("info", "用户信息获取成功");

                        String resultUser = (String) o;
                        UserBO userBO = JsonConverter.toBean(resultUser, UserBO.class);

                        if (userBO != null) {
                            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreference_userInfo), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("userId", userBO.getUserId());
                            editor.putString("userName", userBO.getName());

                            editor.putInt("userGender", userBO.getGender());
                            editor.putInt("userGrade", userBO.getGrade());
                            editor.putString("userMajor", userBO.getMajor());
                            editor.putString("userMinor", userBO.getMinor());
                            editor.putString("userSignature", userBO.getSignature());
                            editor.putString("userImage", userBO.getUserImage());

                            editor.commit();
                        }


                        progressDialog.dismiss();
                        startActivityToMain();

                    }
                });


            }
        });


    }

    private void startActivityToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
