package com.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SECONDHEAVEN on 2016/3/15.
 */
public class RegexAnalysis {
    public static Matcher matcher(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        return matcher;
    }
}
