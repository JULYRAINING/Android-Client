package com.com.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.data.AcquireData;
import com.data.RegexAnalysis;
import com.md.adapter.NoticeAdapter;
import com.md.entity.CupNotice;
import com.md_c_test.R;
import com.recyclerview.listener.ClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by SECONDHEAVEN on 2015/12/29.
 */
public class FragmentnNotice extends Fragment implements ClickListener {

    private RecyclerView recyclerView;
    private NoticeAdapter adapter;
    private String url;
    private static final String encoding = "gb2312";
    private List<CupNotice> noticeLists;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice_layout, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_notice_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noticeLists = new ArrayList<CupNotice>();
        url = "http://www.cup.edu.cn/jwc/jwtz/index.htm";
        Task task = new Task();
        task.execute(url, encoding);


        return view;

    }

    private void initData(String result) {
        //匹配
        String regex = "(?<=<li><span class=\"fr\">(\\d{4}[-]\\d{2}-\\d{2})</span><a href=\").*?(?=((?<=htm).*?(?=</a></li>)))";
        Matcher matcher = null;
        if(result!=null){
             matcher = RegexAnalysis.matcher(regex, result);
        }


        while (matcher.find()) {
            CupNotice cupNotice = new CupNotice();

            String url = matcher.group(0);
            String date = matcher.group(1);
            String title = matcher.group(2);

            cupNotice.setUrl(url);
            cupNotice.setDate(date);
            cupNotice.setTitle(title);
            noticeLists.add(cupNotice);
        }
    }



    class Task extends AsyncTask<String, Integer, String> {


        ProgressDialog progressDialog = new ProgressDialog(getContext());

        public Task() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("加载中...");
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String encoding = params[1];
            String result = null;
            try {
                //获取网页源代码
                result = AcquireData.get(url, encoding);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //initList
            initData(result);
            adapter = new NoticeAdapter(getContext(), noticeLists);
            adapter.setClickListener(FragmentnNotice.this);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            recyclerView.setAdapter(adapter);
        }
    }
    @Override
    public void click(View v, int position) {
        String host = "http://www.cup.edu.cn/jwc/jwtz/";
        String host1 = "http://www.cup.edu.cn/jwc/";
        String url;
        String postFix = noticeLists.get(position).getUrl();
        if(postFix.startsWith(".")){
            url = host1+postFix.substring(3);
        }else {
            url = host+postFix;
        }


        AsyncTaskItem taskItem = new AsyncTaskItem();
        taskItem.execute(url, encoding);


    }
    class AsyncTaskItem extends AsyncTask<String, Integer, String>{

        ProgressDialog progressDialog;

        public AsyncTaskItem() {
            progressDialog = new ProgressDialog(getContext());

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("加载中...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String encoding = params[1];

            String content = null;
            try {
                //获取网页源代码
                content = AcquireData.get(url, encoding);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 去除标签
            content = content.replaceAll("<[^>]*>", "");
            // 截取内容
            content = content.substring(content.indexOf("首页 >"),
                    content.indexOf("中国石油大学(北京)教务处"));


            content = content.replaceAll("&nbsp;", " ");
            content = content.replaceAll("&mdash;", "-");
            //若有超过5个空格 则换行
            content = content.replaceAll("\\s{5,}", "\n");


            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            final MaterialDialog materialDialog;

            materialDialog = new MaterialDialog(getContext());
            materialDialog.setTitle(s.substring(s.indexOf(">")+1,s.indexOf("\n")));
            materialDialog.setMessage(s);
            materialDialog.setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    materialDialog.dismiss();
                }
            });
            materialDialog.show();
        }
    }
}
