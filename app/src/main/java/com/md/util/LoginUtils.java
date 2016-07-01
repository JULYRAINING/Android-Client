package com.md.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.md.entity.UserBO;
import com.md_c_test.LoginActivity;
import com.md_c_test.R;


/**
 * Created by SECONDHEAVEN on 2015/9/11.
 */
public class LoginUtils {

    private String name;
    private String password;
    public static final String USERINFO = "SaveUserInfo";
    private LoginActivity context;

    private UserBO user = new UserBO();

    public LoginUtils(LoginActivity login_activity, String name, String password) {
        this.context = login_activity;

        this.name = name;
        this.password = password;
    }

    public boolean isSuccess() {


        return true;
    }

    public UserBO getUserInfo() {

        user.setName(context.getResources().getString(R.string.default_username));
        user.setGrade(0);
        user.setGender(Integer.valueOf(context.getResources().getString(R.string.default_usersex)
        ));
        user.setMajor(context.getResources().getString(R.string.default_usermajor));
        user.setMinor(context.getResources().getString(R.string.default_userpassword));
        user.setSignature(context.getResources().getString(R.string.default_usersigniture));
        user.setUserImage("assets://msg_content_user_img.png");

        return user;
    }

    public void saveUserInfo() {
        getUserInfo();
        SharedPreferences sharedPreferences = context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_name", user.getName());
        editor.putInt("user_sex", user.getGender());
        editor.putString("user_major", user.getMajor());
        editor.putString("user_signature", user.getSignature());
        editor.putString("user_password", user.getMinor());
        editor.putInt("user_grade", user.getGrade());
        editor.putString("user_img", user.getUserImage());
        editor.commit();

    }
}
