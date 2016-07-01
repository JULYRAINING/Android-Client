package com.md_c_test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.md.entity.UserValidate;
import com.md.util.REValidate;
import com.md.util.net.MD5Convert;
import com.md.util.networkapi.SimpleRegisterApi;
import com.md.util.request.ResponseListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private TextInputLayout registerPasswordContainer;
    private TextInputLayout registerEmailContainer;
    private LinearLayout registerLayout;
    private Button registerButton;
    private EditText passwordEditText;
    private EditText emailEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        initEvent();

    }

    private void initEvent() {
        registerButton.setOnClickListener(this);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_register_toolbar);
        toolbar.setTitle("注册");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        registerPasswordContainer = (TextInputLayout) findViewById(R.id.id_user_register_password);
        registerEmailContainer = (TextInputLayout) findViewById(R.id.id_user_register_email);
        registerButton = (Button) findViewById(R.id.id_register_button);
        registerLayout = (LinearLayout) findViewById(R.id.id_register_container);
        passwordEditText = registerPasswordContainer.getEditText();
        emailEditText = registerEmailContainer.getEditText();
        registerPasswordContainer.setHint("密码");
        registerEmailContainer.setHint("邮箱");
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                registerEmailContainer.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerEmailContainer.setErrorEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isValidate = REValidate.isEmail(s.toString());

                if (!isValidate) {


                    registerEmailContainer.setError("格式不正确!(若未输入完毕，请继续输入)");
                } else {
                    registerEmailContainer.setErrorEnabled(false);
                }
            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                registerPasswordContainer.setErrorEnabled(false);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerPasswordContainer.setErrorEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                String trimStr = str.trim();
                if (str.length() == trimStr.length()) {
                    registerPasswordContainer.setErrorEnabled(false);
                } else {
                    registerPasswordContainer.setError("密码首尾不要包含空格!(若未输入完毕，请继续输入)");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.id_register_button:
                regist();

                break;
        }
    }

    private void regist() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (email.trim() == null
                || password.trim() == null
                || (email.trim().equals(""))
                || (password.trim().equals(""))) {
            registerPasswordContainer.setErrorEnabled(true);
            registerPasswordContainer.setError("密码不能为空");

        } else {
            //验证信息
            UserValidate userValidate = new UserValidate();
            userValidate.setEmail(email);
            userValidate.setPassword(MD5Convert.getMd5(password));
            //发送注册信息
            SimpleRegisterApi.regist(
                    userValidate,
                    new ResponseListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Object o) {
                            //注册成功 返回注册成功的用户id
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            String result = (String) o;
                            int userId = Integer.valueOf(result);
                            //跳转到详细信息填写界面
                            addUserDetail(userId);
                        }
                    });

        }
    }


    private void addUserDetail(int userId) {


        //保存用户id到sharedPreferences
        SharedPreferences sharedPreferences =
                getSharedPreferences(getString(R.string.sharedPreference_userInfo), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", userId);


        editor.commit();


        Intent intentUserDetail = new Intent(RegisterActivity.this, UserDetailActivity.class);
        intentUserDetail.putExtra("userId", userId);
        //输入标记，表示注册新用户
        intentUserDetail.putExtra(getString(R.string.UserDetailStartFlag), UserDetailActivity.REGIST_INFO);
        startActivity(intentUserDetail);


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
