package com.md.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SECONDHEAVEN on 2016/1/19.
 */
public class REValidate {
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
