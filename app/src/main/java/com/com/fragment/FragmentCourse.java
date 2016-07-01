package com.com.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.db.dao.CourseDao;
import com.db.dao.CourseTimeDao;
import com.db.daoImpl.CourseDaoImpl;
import com.db.daoImpl.CourseTimeDaoImpl;
import com.md.entity.ClassSchedule;
import com.md.entity.SchoolTime;
import com.md_c_test.MyApplication;
import com.md_c_test.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * 1/22
 * 1.需要判断学号及密码是否正确
 * 2.还需要判断是否解析正确
 * 上述两个问题已解决
 * Created by SECONDHEAVEN on 2015/12/29.
 */
public class FragmentCourse extends Fragment {
    private TextInputLayout studentIdLayout;
    private TextInputLayout studentPwdLayout;
    private EditText studentId;
    private EditText studentPwd;
    private Button login;

    private NestedScrollView scrollView;
    private int currentWeek = 1;
    private int[] colors;
    private List<Integer> colorList;
    private int standardHeight = 150;
    //内容布局 包括每天的relativelayout
    private LinearLayout contentLinearLayout;
    //包括 日期title 和每天的relativeLayout
    private LinearLayout rootLinearLayout;

    private List<LinearLayout> topTitleLinearLayoutList;
    //全局布局 包括 fab 和rootLinearLayout
    private CoordinatorLayout courseCoordinatorLayout;
    //日期title
    private LinearLayout topTitleLinearLayout;
    private Map<String, Integer> courseColorMaps;
    private FloatingActionButton fab;
    private Bitmap mBitmap;
    private Canvas canvas;
    private Paint paint;
    private ListView listView;
    private MaterialDialog materialDialog = null;
    private List<String> list;
    private ArrayAdapter arrayAdapter;
    private CourseDao courseDao;
    private CourseTimeDao courseTimeDao;

    /**
     * coordinatorLayout为根布局 由content_dynamic.xml定义 包括一个fabanniu
     * 初次进入时 判断数据库中是否有课程信息
     * 若没有
     * 则在coordinatorLayout中加载登录界面course_login.xml
     * 登录成功后 coordinatorLayout中removeAllViews
     * 然后代码生成界面 rootLinearLayout 布局方向为vertival
     * 内部有两个布局 上面为topTitleLinearLayout
     * 下面为 contentLinearLayout
     * topTitleLinearLayout 布局方向为horizontal
     * 从左至右共有8个LinearLayout 布局方向为vertical
     * 每个LinearLayout中有两个TextView 上方显示日期 下方显示星期
     * 而第一个LinearLayout例外 显示当前月份
     * contentLinearLayout 布局方向为horizontal
     * 从左至右共有8个RelativeLayout 布局方向为vertical
     * 每个RelativeLayout 在逻辑上被看作有11行 因此共有88个view需要做判断
     * 使用两个for循环
     * for(i=0;i<8;i++)
     * for(j=0;j<11;j++)
     * 由 i j来确定课程上课时间 搜索数据库 判断是否有课程
     * 若有则绘制textview 高度由上课节数来确定
     * 若有两节课在同一时间上课，但上课周数不同，则进一步判断 对非当前周次的课程进行隐藏
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager wm = getActivity().getWindowManager();

        int screenWidth = wm.getDefaultDisplay().getWidth();
        Log.e("screenWidth", screenWidth + "");
        standardHeight = screenWidth / 15 * 2 * 150 / 144;
        Log.e("standardHeight", standardHeight + "");


        listView = new ListView(getContext());

        materialDialog = new MaterialDialog(getContext()).setTitle("选择周次");

        list = new ArrayList<>();
        for (int b = 1; b < 21; b++) {
            list.add(b + "");
        }

        arrayAdapter = new ArrayAdapter(getContext(),
                android.R.layout.simple_list_item_1, list);

        courseDao = new CourseDaoImpl(MyApplication.getContext());
        courseTimeDao = new CourseTimeDaoImpl(MyApplication.getContext());

        //初始化内容布局
        contentLinearLayout = new LinearLayout(getContext());
        contentLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        rootLinearLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        rootLinearLayout.setOrientation(LinearLayout.VERTICAL);
        rootLinearLayout.setLayoutParams(llp);

        //应该将container置入，方便计算空间尺寸
        View courseDataLayout = inflater.inflate(R.layout.content_dynamic, container, false);
        //根布局
        courseCoordinatorLayout = (CoordinatorLayout) courseDataLayout.findViewById(R.id.id_course_relativelayout);

        fab = (FloatingActionButton) courseCoordinatorLayout.findViewById(R.id.fab);

        drawBitmap();


        if (courseTimeDao.getCourseTime().size() == 0) {


            initViews(inflater, courseCoordinatorLayout);
        } else {

            loadCourse();
        }


        return courseDataLayout;


    }

    private void drawBitmap() {
        int width = 200;
        int height = 200;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(mBitmap);
        canvas.drawColor(Color.TRANSPARENT);
        paint = new Paint();
        String familyName = "宋体";
        Typeface font = Typeface.create(familyName, Typeface.ITALIC);
        paint.setColor(Color.BLACK);
        paint.setTypeface(font);
        paint.setTextSize(180);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    private void initViews(final LayoutInflater inflater, final CoordinatorLayout coordinatorLayout) {
        View loginView = inflater.inflate(R.layout.course_login, coordinatorLayout, false);
        coordinatorLayout.addView(loginView);
        studentIdLayout = (TextInputLayout) loginView.findViewById(R.id.id_student_id);
        studentPwdLayout = (TextInputLayout) loginView.findViewById(R.id.id_student_pwd);
        login = (Button) loginView.findViewById(R.id.id_bind_to_jwc);

        studentIdLayout.setHint("学号");
        studentPwdLayout.setHint("密码");

        studentId = studentIdLayout.getEditText();
        studentPwd = studentPwdLayout.getEditText();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = studentId.getText().toString();
                String pwd = studentPwd.getText().toString();
                GetCourseAsyncTask getCourseAsyncTask = new GetCourseAsyncTask();
                getCourseAsyncTask.execute(id, pwd);
            }
        });
    }

    /**
     * 登录教务系统获取课程信息
     */
    class GetCourseAsyncTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        boolean isConnect = true;
        boolean isLogin;
        boolean isAnalysis;
        String connectError = "连接服务器失败";
        String loginError = "用户名或密码错误";
        String analysisError = "解析失败";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setCancelable(false);
            progressDialog.setMessage("登录中...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String pwd = params[1];
            try {
                //获取cookie
                String cookie = getCookie(id, pwd);

                if (cookie != null) {
                    Log.e("cookie", cookie);
                    isLogin = true;
                    Log.e("info", "登录成功！");

                    //获取html页面
                    Log.e("info", "正在获取课程信息...");

                    String courseInfo = getCourseInfo(cookie);


                    try {
                        //解析html页面 存储课程信息到sqlite
                        //可能解析不成功，抛出异常
                        saveCourse(courseInfo);
                        Log.e("info", "解析课程信息成功！");


                        isAnalysis = true;
                    } catch (Exception e) {
                        isAnalysis = false;
                    }

                } else {
                    //登录失败
                    isLogin = false;


                }


            } catch (java.net.ConnectException e) {
                isConnect = false;
                Log.e("ERROR", "无法连接到服务器");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("ERROR", "其他错误");

                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (isConnect) {
                Log.e("login", "连接服务器成功");
                if (isLogin) {
                    Log.e("login", "登录成功");
                    if (isAnalysis) {
                        Log.e("Analysis", "解析成功");
                        //加载课程显示到ui
                        progressDialog.setMessage("正在加载课程信息...");
                        loadCourse();
                    } else {
                        Log.e("Analysis", "解析或存储失败");
                        studentPwdLayout.setError(analysisError);

                    }
                } else {
                    Log.e("login", "登录失败");
                    studentPwdLayout.setError(loginError);
                }
            } else {
                studentPwdLayout.setError(connectError);

            }

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    /**
     * @param userId  学号
     * @param userPwd 教务密码
     * @return cookie
     * @throws IOException httpConnectionException
     */
    private String getCookie(String userId, String userPwd) throws IOException {
        String cookie = null;
        String urlStr = "http://202.204.193.215/loginAction.do";
        URL uri = new URL(urlStr);

        URLConnection urlConn = uri.openConnection();
        HttpURLConnection conn = (HttpURLConnection) urlConn;
        conn.setDoOutput(true);
        conn.setReadTimeout(5000);
        conn.setDoInput(true);
        conn.setUseCaches(false);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("zjh").append("=").append(userId).append("&").append("mm")
                .append("=").append(userPwd);
        byte[] params = stringBuilder.toString().getBytes();

        conn.getOutputStream().write(params);

        conn.connect();
        InputStream inputStream = conn.getInputStream();


        InputStreamReader inputStreamReader;

        BufferedReader bufferedReader;


        inputStreamReader = new InputStreamReader(inputStream, "gb2312");

        bufferedReader = new BufferedReader(inputStreamReader);


        String line = null;
        StringBuilder string = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            string.append(line);


        }

        String result = string.toString();
        Log.e("loginInfo", string.toString());

        if (result.contains("用户名或密码错误")) {
            cookie = null;
        } else if (result.contains("学分制综合教务")) {
            String key = null;
            for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++) {
                Log.e("key", key + ":" + conn.getHeaderField(key));
                if (key.equals("Set-Cookie")) {
                    //Set-Cookie 的值为：
                    //Set-Cookie:JSESSIONID=dfenXUNV1he9He0kb3lmv; path=/
                    //所以要分割
                    String[] coo = conn.getHeaderField(key).split(";");
                    cookie = coo[0];
                }

            }
        }


        Log.e("login", "完成");


        conn.disconnect();
        return cookie;

    }

    /**
     * @param cookie
     * @return 带有课程信息的html页面 读取为String
     * @throws IOException httpException
     */
    private String getCourseInfo(String cookie) throws IOException {
        String urlStr = "http://202.204.193.215/xkAction.do?actionType=6";

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoInput(true);
        conn.setDoOutput(true);


        conn.setDoOutput(true);

        conn.setReadTimeout(5000);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Cookie", cookie);

        InputStream inputStream = conn.getInputStream();

        String path = Environment.getExternalStorageDirectory() +
                File.separator + "courseInfo.txt";
        File courseInfoFile = new File(path);

        FileOutputStream fos = new FileOutputStream(
                courseInfoFile);


        InputStreamReader inputStreamReader;

        BufferedReader bufferedReader;

        OutputStreamWriter outputStreamWriter;

        BufferedWriter bufferedWriter;

        inputStreamReader = new InputStreamReader(inputStream, "gb2312");

        bufferedReader = new BufferedReader(inputStreamReader);

        outputStreamWriter = new OutputStreamWriter(fos, "gb2312");

        bufferedWriter = new BufferedWriter(outputStreamWriter);

        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
            bufferedWriter.write(line);

        }
        bufferedWriter.flush();


        Log.e("response", "完成");

        conn.disconnect();
        return stringBuilder.toString();
    }

    /**
     * @param courseInfo 读取为String的html页面
     *                   function 解析html页面 并储存到数据库
     */
    private void saveCourse(String courseInfo) {
        Pattern p1 = Pattern.compile("((\\b[a-zA-Z0-9\u4e00-\u9fa5 ()\\*-]{0,}?[0-9a-zA-Z\\.]{0,}?)(?=</td>))|(;(?=</td>))");

        Matcher m1 = p1.matcher(courseInfo);


        Log.e("", "开始解析");
        int aa = 0;
        ArrayList<String> strs = new ArrayList<String>();
        while (m1.find()) {
            String info = m1.group(0);
            if (!info.equals("")) {

                strs.add(info);
                Log.e("222222", aa + info);
                System.out.println(aa + "\t" + info);
                aa++;
            }

        }
        //动态删除，要谨慎对待
        int size = strs.size();
        for (int i = 0; i <= size; i++) {
            if (!strs.get(0).endsWith("培养方案")) {
                strs.remove(0);
                Log.e("3333333", i + "");
            } else {
                Log.e("3333333", strs.get(0));

                break;
            }

        }


        String trainingProgram = strs.get(0);
        Log.e("3333333", trainingProgram);
        List<Integer> integers = indexOf(strs, trainingProgram);
        List<ClassSchedule> classSchedules = new ArrayList<>();


        int indexCount = integers.size();
        System.out.println(indexCount);
        for (int m = 0; m < indexCount; m++) {
            List list;
            ArrayList<String> strCopy = new ArrayList<String>(strs);
            System.out.println(m);
            System.out.println(strCopy.size());
            if (m == indexCount - 1) {
                list = strCopy.subList(integers.get(m), strCopy.size());
            } else {
                list = strCopy.subList(integers.get(m), integers.get(m + 1));
            }

            ArrayList<String> strList = new ArrayList<String>(list);
            list.clear();
            classSchedules.add(getClassSchedule(strList));
            Log.e("44444444", m + "");
        }
        //存储课程信息到数据库

        int classCount = classSchedules.size();
        Log.e("course1111", classSchedules.toString());
        for (int j = 0; j < classCount; j++) {
            int classTimeCount = classSchedules.get(j).getSchoolTimes().size();
            courseDao.addCourse(classSchedules.get(j));
            for (int m = 0; m < classTimeCount; m++) {
                courseTimeDao.addCourseTime(classSchedules.get(j).getSchoolTimes().get(m));
            }
            Log.e("111", "sqlite已储存");
        }
    }

    /**
     * @param strs 由正则表达式读取到的信息，包括全部的课程，
     *             详细格式：
     *             培养方案
     *             课程号
     *             课序号
     *             学分
     *             课程属性
     *             考试类型
     *             教师
     *             修读方式
     *             选课方式
     *             周次
     *             星期
     *             节次
     *             节数
     *             校区
     *             教学楼
     *             教室
     * @return ClassSchedule
     * function 对课程数组进行解析 封装成实体
     */
    private ClassSchedule getClassSchedule(ArrayList<String> strs) {
        if (strs.size() == 17) {
            ClassSchedule classSchedule = new ClassSchedule();
            List<SchoolTime> schoolTimes = new ArrayList<>();
            SchoolTime schoolTime = new SchoolTime();

            initBaseInfo(strs, classSchedule);

            initFirstSchoolTime(strs, schoolTime);


            schoolTimes.add(schoolTime);

            classSchedule.setSchoolTimes(schoolTimes);
            return classSchedule;
        } else {
            ClassSchedule classSchedule = new ClassSchedule();
            List<SchoolTime> schoolTimes = new ArrayList<>();
            SchoolTime schoolTime = new SchoolTime();
            SchoolTime schoolTime1 = new SchoolTime();
            initBaseInfo(strs, classSchedule);

            initFirstSchoolTime(strs, schoolTime);
            initSecondSchoolTime(strs, schoolTime1);

            schoolTimes.add(schoolTime);
            schoolTimes.add(schoolTime1);

            classSchedule.setSchoolTimes(schoolTimes);
            return classSchedule;
        }


    }

    /**
     * @param strs       第二节课的数组
     * @param schoolTime
     */
    private void initSecondSchoolTime(ArrayList<String> strs,
                                      SchoolTime schoolTime) {
        schoolTime.setWeek_of_semester(strs.get(17));
        schoolTime.setDay_of_week(strs.get(18));
        schoolTime.setPart(String.valueOf(getPartValue(strs.get(19))));
        schoolTime.setPartLength(strs.get(20));
        schoolTime.setCampus(strs.get(21));
        schoolTime.setBuilding(strs.get(22));
        schoolTime.setClassRoom(strs.get(23));
        Log.e("课程号", strs.get(1));
        schoolTime.setCourseNumber(strs.get(1));
    }

    /**
     * @param strs
     * @param schoolTime
     */
    private static void initFirstSchoolTime(ArrayList<String> strs,
                                            SchoolTime schoolTime) {
        Log.e("schoolTimeInfo", strs.get(15));
        schoolTime.setWeek_of_semester(strs.get(10));
        schoolTime.setDay_of_week(strs.get(11));
        schoolTime.setPart(String.valueOf(getPartValue(strs.get(12))));
        schoolTime.setPartLength(strs.get(13));
        schoolTime.setCampus(strs.get(14));
        schoolTime.setBuilding(strs.get(15));
        schoolTime.setClassRoom(strs.get(16));
        schoolTime.setCourseNumber(strs.get(1));
    }

    /**
     * @param strs
     * @param classSchedule
     */
    private static void initBaseInfo(ArrayList<String> strs,
                                     ClassSchedule classSchedule) {
        classSchedule.setTrainingProgram(strs.get(0));
        classSchedule.setCourseNumber(strs.get(1));
        classSchedule.setCourseName(strs.get(2));
        classSchedule.setCourseSerialNumber(strs.get(3));
        classSchedule.setCourseCredit(strs.get(4));
        classSchedule.setCourseAttribute(strs.get(5));
        classSchedule.setTestType(strs.get(6));
        classSchedule.setTeacherName(strs.get(7));
        classSchedule.setReadMethod(strs.get(8));
        classSchedule.setElectiveState(strs.get(9));
    }

    /**
     * @param list 待分析的String 集合
     * @param str  给定的字符串
     * @return 返回list集合中出现给定字符串的位置集合
     */
    private static List<Integer> indexOf(List list, String str) {
        int size = list.size();
        List<Integer> integers = new ArrayList<>();
        if (str == null) {
            for (int i = 0; i < size; i++)
                if (list.get(i) == null)
                    integers.add(i);
        } else {
            for (int i = 0; i < size; i++)
                if (str.equals(list.get(i)))
                    integers.add(i);
        }
        return integers;
    }

    /**
     * @param part
     * @return
     */
    private static int getPartValue(String part) {
        int partValue = -1;
        switch (part) {
            case "一":
                partValue = 1;
                break;
            case "二":
                partValue = 2;
                break;
            case "三":
                partValue = 3;
                break;
            case "四":
                partValue = 4;
                break;
            case "五":
                partValue = 5;
                break;
            case "六":
                partValue = 6;
                break;
            case "七":
                partValue = 7;
                break;
            case "八":
                partValue = 8;
                break;
            case "九":
                partValue = 9;
                break;
            case "十":
                partValue = 10;
                break;
            case "十一":
                partValue = 11;
                break;
        }
        return partValue;
    }

    private void selectCurrentWeek() {

        listView.setAdapter(arrayAdapter);
        final MaterialDialog finalMaterialDialog = materialDialog;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //设置画笔划过的地方显示背景
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                //将之前画出的currentWeek擦除掉
                canvas.drawText(currentWeek + "", 100, 180, paint);

                currentWeek = position + 1;


                paint.setColor(Color.BLACK);
                //取消设置
                paint.setXfermode(null);
                //画出新的currentWeek
                canvas.drawText(currentWeek + "", 100, 180, paint);


                fab.setImageBitmap(mBitmap);
                fab.getBackground().setAlpha(android.R.color.transparent);

                scrollView.removeAllViews();
                contentLinearLayout.removeAllViews();
                rootLinearLayout.removeAllViews();
                courseCoordinatorLayout.removeView(rootLinearLayout);
                courseCoordinatorLayout.removeView(fab);
                finalMaterialDialog.dismiss();
                loadCourseFromSqlite();


            }
        });
        materialDialog.setContentView(listView);
        materialDialog.show();
    }

    private void loadCourseFromSqlite() {


        //一共8列，第一列为节次
        //其余列分别为星期一~七
        for (int m = 0; m < 8; m++) {

            //每列为一个RelativeLayout
            RelativeLayout relativeLayout = new RelativeLayout(getContext());

            //每列有11节课，分别进行判断
            for (int n = 0; n < 11; n++) {
                //若为第一列，则绘制节次标题
                if (m == 0) {
                    //设置参数，宽度0 高度自适应，权重1
                    relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT
                            , 1));
                    //内容控件为textview
                    TextView tv = new TextView(getContext());
                    //设置背景
                    tv.setBackgroundResource(R.drawable.stroke);
                    //设置textview高度为150，宽度为填充父控件
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            standardHeight
                    );
                    //设置textview与父控件上部距离为n*150
                    params.topMargin = n * standardHeight;
                    tv.setLayoutParams(params);
                    //设置textview的对齐方式为居中
                    tv.setGravity(Gravity.CENTER);
                    //设置节次标题
                    tv.setText((n + 1) + "");
                    //设置字体颜色
                    tv.setTextColor(getResources().getColor(R.color.N));
                    GradientDrawable drawable = (GradientDrawable) tv.getBackground();
                    drawable.setColor(getResources().getColor(android.R.color.white));
                    //将textview添加到relativelayout中
                    relativeLayout.addView(tv);
                    //若不为第一列，则查找sqlite中是否有课
                } else {
                    //设置relativelayout参数，权重为2
                    relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT
                            , 2));

                    //读取sqlite，查询参数为week = m, part = (n+1).
                    List<SchoolTime> courseTimes = courseTimeDao.getCourseTimeByWeek(m, n + 1);
                    //Log.e("size", (m) + " " + (n + 1) + " " + courseTimes.size() + "");
                    //若返回值不为空
                    if (courseTimes.size() != 0) {
                        //对返回值进行操作
                        for (int q = 0; q < courseTimes.size(); q++) {
                            //读取这节课持续长度
                            int partLength = Integer.valueOf(courseTimes.get(q).getPartLength());

                            TextView tv = new TextView(getContext());
                            tv.setGravity(Gravity.CENTER);
                            tv.setBackgroundResource(R.drawable.course_drawable);
                            //设置textview参数，高度为partLength*150
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    partLength * standardHeight
                            );
                            //设置textview与父控件的距离为n*150,即对应的上课时间
                            params.topMargin = n * standardHeight;
                            tv.setLayoutParams(params);

                            //查询课程名并显示
                            String courseNumber = courseTimes.get(q)
                                    .getCourseNumber();
                            ClassSchedule classSchedule = courseDao.getCourseByCourseNumber(courseNumber)
                                    .get(0);
                            String courseName = classSchedule.getCourseName();
                            String teacherName = classSchedule.getTeacherName();
                            //教师名字需要split 因为每个名字后面都有*
                            teacherName = teacherName.substring(0, teacherName.length() - 2);
                            String courseSerialNumber = classSchedule.getCourseSerialNumber();
                            String courseCredit = classSchedule.getCourseCredit();
                            String trainingProgram = classSchedule.getTrainingProgram();
                            tv.setText(courseName);
                            tv.setAlpha(1.0f);
                            List<SchoolTime> times = courseTimeDao.getCourseTimeByNumber(courseNumber);


                            String dialogStr =

                                    "\n课程号:  " + courseNumber +
                                            "\n课序号:  " + courseSerialNumber +
                                            "\n学    分:  " + courseCredit +

                                            "\n教    师:  " + teacherName;

                            for (SchoolTime time : times) {
                                int part = Integer.valueOf(time.getPart());
                                int length = Integer.valueOf(time.getPartLength());
                                dialogStr = dialogStr +
                                        "\n\n周    次:  " + time.getWeek_of_semester()
                                        + "\n时    间:  周" + time.getDay_of_week()
                                        + "     " + time.getPart() + "-"
                                        + (part + length - 1) + "节"
                                        + "\n教学楼:  " + time.getBuilding() + "\n教    室:  " + time.getClassRoom();
                            }

                            final String str = dialogStr;
                            final String courseNameFin = courseName;
                            classSchedule.getTeacherName();
                            tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final MaterialDialog materialDialog
                                            = new MaterialDialog(getContext());
                                    materialDialog.setTitle(courseNameFin);
                                    materialDialog.setMessage(str);
                                    materialDialog.setPositiveButton("确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            materialDialog.dismiss();
                                        }
                                    });
                                    materialDialog.show();
                                }
                            });


                            //将要赋予的颜色值
                            int courseColor = 0;
                            //若之未出现过此课程，则设置新的颜色给该课程

                            if (!courseColorMaps.containsKey(courseNumber)) {
                                Log.e("crash", m + " " + n);
                                int temp = colorList.get(0);
                                courseColorMaps.put(courseNumber, temp);
                                colorList.remove(0);
                                courseColor = temp;
                            }//否则取出之前保存的颜色
                            else {
                                courseColor = courseColorMaps.get(courseNumber);
                            }


                            //用正则表达式判断是否为当前周次currentWeek
                            Pattern p = Pattern.compile("(\\d{0,})-(\\d{0,})");
                            Matcher matcher = p.matcher(courseTimes.get(q).getWeek_of_semester());
                            GradientDrawable drawable = (GradientDrawable) tv.getBackground();
                            if (matcher.find()) {
                                //如1-8周 则matcher.group(1) = 1 matcher.group(2) = 8
                                //若currentWeek = 9
                                //则(8<9)||(1>9)为真
                                //说明不在当前周次
                                //设置该textview的背景色为灰色
                                if (Integer.valueOf(matcher.group(2)) < currentWeek ||
                                        Integer.valueOf(matcher.group(1)) > currentWeek) {
                                    drawable.setColor(getResources().getColor(
                                            R.color.lightgray));
                                    tv.setTextColor(getResources().getColor(
                                            R.color.sliver
                                    ));
                                    //再次判断是否与其他课程在同一时间段
                                    //当q=1时，表示该时间段有两门课程
                                    //则将此textview隐藏
                                    if (q != 0) {
                                        tv.setVisibility(View.INVISIBLE);
                                    }
                                }
                                //若在当前周次
                                //则设置强调色
                                else {
                                    tv.setTextColor(getResources().getColor(
                                            android.R.color.white
                                    ));
                                    drawable.setColor(getResources().getColor(courseColor));

                                }
                            }
                            //将textview添加到relativelayout
                            relativeLayout.addView(tv);

                        }

                    }
                }


            }
            //将该RelativeLayout添加到rootLinearLayout
            contentLinearLayout.addView(relativeLayout);
            Log.e("addView", "contentLinearLayout.addView(relativeLayout)");

        }

        scrollView = new NestedScrollView(getContext());


        //添加到ScrollView中
        scrollView.addView(contentLinearLayout);
        Log.e("addView", "scrollView.addView(contentLinearLayout)");

        rootLinearLayout.addView(scrollView);
        Log.e("addView", "rootLinearLayout.addView(scrollView)");


        courseCoordinatorLayout.addView(rootLinearLayout);
        Log.e("addView", "courseCoordinatorLayout.addView(rootLinearLayout)");

        //当第一次加载时， fab已被加载到courseCoordinatorLayout， 所以要先remove
        courseCoordinatorLayout.removeView(fab);

        courseCoordinatorLayout.addView(fab);


    }

    private void loadCourse() {

        courseCoordinatorLayout.removeAllViews();

        courseColorMaps = new HashMap<>();

        colorList = new ArrayList<>();

        colorList.add(R.color.Q);
        colorList.add(R.color.S);
        colorList.add(R.color.G);
        colorList.add(R.color.N);

        colorList.add(R.color.P);
        colorList.add(R.color.J);
        colorList.add(R.color.D);
        colorList.add(R.color.purple200);
        colorList.add(R.color.M);
        colorList.add(R.color.C);

        colorList.add(R.color.H);
        colorList.add(R.color.F);
        colorList.add(R.color.O);
        colorList.add(R.color.deeporange100);
        colorList.add(R.color.R);


        //顶部时间标题栏
        topTitleLinearLayout = new LinearLayout(getContext());
        topTitleLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams topTitleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        topTitleLinearLayout.setLayoutParams(topTitleParams);
        topTitleLinearLayout.setId(R.id.courseTopTitle);
        topTitleLinearLayoutList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        int month = calendar.get(Calendar.MONTH);
        int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
        //按标准算法，从星期日开始算day_of_week
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < 8; i++) {
            int weight = 0;
            String date = "";
            String week = "";
            if (i == 0) {
                weight = 1;
                date = String.valueOf(month + 1);
                week = getString(R.string.month);
            } else {
                weight = 2;
                //设置适当的日期
                //如
                calendar = Calendar.getInstance();
                int offset = (i + 1 - day_of_week);
                calendar.add(Calendar.DATE, offset);

                date = calendar.get(Calendar.DATE) + getString(R.string.day);


                week = getString(R.string.week) + i;
            }

            LinearLayout linearLayout = new LinearLayout(getContext());
            //日期textview
            TextView tv_date = new TextView(getContext());
            //星期textview
            TextView tv_week = new TextView(getContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams tv_params = new LinearLayout
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, weight);
            LinearLayout.LayoutParams layout_params = new LinearLayout
                    .LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, weight);

            linearLayout.setLayoutParams(layout_params);
            tv_date.setLayoutParams(tv_params);
            tv_week.setLayoutParams(tv_params);
            tv_date.setGravity(Gravity.CENTER);
            tv_week.setGravity(Gravity.CENTER);
            tv_date.setText(date);
            tv_week.setText(week);
            linearLayout.addView(tv_date);
            linearLayout.addView(tv_week);
            linearLayout.setBackgroundResource(R.drawable.stroke);
            GradientDrawable drawable = (GradientDrawable) linearLayout.getBackground();

            //若为周六 则day_of_week为7 ， 当i = 6即绘制第七列时，为true
            if (day_of_week == i + 1) {
                //若i为0，即第0列，则day_of_week为1，即周日为true
                if (i == 0) {
                    drawable.setColor(getResources().getColor(android.R.color.white));
                } else {
                    drawable.setColor(getResources().getColor(R.color.I));
                }
                Log.e("dayofweek", day_of_week + "");

            } else {
                if (day_of_week == 0 && i == 7) {
                    drawable.setColor(getResources().getColor(R.color.I));
                } else {

                    drawable.setColor(getResources().getColor(android.R.color.white));
                }

            }


            topTitleLinearLayoutList.add(linearLayout);
            topTitleLinearLayout.addView(linearLayout);

        }

        //将顶部标题栏添加到根布局中
        rootLinearLayout.addView(topTitleLinearLayout);


        loadCourseFromSqlite();


        canvas.drawText(currentWeek + "", 100, 180, paint);


        fab.setImageBitmap(mBitmap);
        fab.getBackground().setAlpha(android.R.color.transparent);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectCurrentWeek();
            }
        });

    }

}
