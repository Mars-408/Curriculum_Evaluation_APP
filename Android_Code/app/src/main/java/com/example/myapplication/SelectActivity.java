package com.example.myapplication;

import com.example.myapplication.Select_GET_Course_Detail;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectActivity extends AppCompatActivity {
    /**
     * 选课滑动
     */
    private ViewPager mViewPager;
    private List<Select_GET_Course_Detail> mData = new ArrayList<>();

    Button btn_OK;

    private String URL_Course_Tea;
    private String URL_Course_Stu;
    private String URL_Teacher_Tea;
    private String URL_Teacher_Stu;
    private String URL_Detail;
    private String Send_Mail;

    List<String> acadamy;
    List<String> course ;
    List<String> Teacher;
    Select_GET_Course_Detail Select_Detail;
    String select_academy;
    String select_course;
    String select_Teacher;

    Spinner spinner_2;
    select_adapter adapter_2;
    Spinner spinner_3;
    select_adapter adapter_3;
    ViewPager vpager_one;
    ArrayList<View> aList;

    private String TeacherId;
    private String TeacherName;

    private String Url_;
    private String value_;

    private String Role;

    TextView TV6;

    List<Select_GET_Course_Detail> GetList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        Select_Detail = new Select_GET_Course_Detail();
        TV6 = findViewById(R.id.TV_6);
        //初始化选课逻辑
        DisplaySelect_Detail();

        Intent i = getIntent();
        Role = i.getStringExtra("date");
        URL_Course_Tea  = getResources().getString(R.string.URL_Course_Tea );
        URL_Course_Stu  = getResources().getString(R.string.URL_Course_Stu );
        URL_Teacher_Tea = getResources().getString(R.string.URL_Teacher_Tea);
        URL_Teacher_Stu = getResources().getString(R.string.URL_Teacher_Stu);
        URL_Detail  = getResources().getString(R.string.URL_Detail);
        Send_Mail = getResources().getString(R.string.Send_Mail);

        acadamy = new ArrayList<>();
        course = new ArrayList<>();
        Teacher = new ArrayList<>();

        SharedPreferences sp = getSharedPreferences("login",Context.MODE_PRIVATE);
        TeacherId = sp.getString("username",null);
        TeacherName = sp.getString("name",null);


        GetList = new ArrayList<Select_GET_Course_Detail>();
        btn_OK = findViewById(R.id.btn_OK);

        //---------
        btn_OK.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Send_Select();
                //数据是使用Intent返回
                //刷新Fragment界面 用于处理在选择课程界面返回时信息不更新
                Intent intent = new Intent();
                //把返回数据存入Intent
                setResult(RESULT_OK, intent);

                //关闭Activity
                finish();
            }
        });
        //返回键
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //定义 sprinner
        setSprinner_one();
        SetSprinner_two();
        SetSprinner_three();
    }

    /**
     * 选择完成 发送请求
     */
    public void Send_Select(){
        //注意 由于后端错误teacher ID 和teacher name的值反了！！

        new Thread(new Runnable() {
            @Override
            public void run() {
                Url_ = Send_Mail;
                //这里name与id需要颠倒！！！
                value_ = "teacherName=" + TeacherId+
                        "&teacherId=" + TeacherName+
                        "&courseId=" + Select_Detail.getCourseId()+
                        "&courseName=" + Select_Detail.getCourseName()+
                        "&courseTeacher=" + Select_Detail.getCourseTeacher()+
                        "&courseTeacherId=" + Select_Detail.getCourseTeacherId()+
                        "&courseAcademy=" + Select_Detail.getCourseAcademy()+
                        "&courseLocal=" + Select_Detail.getCourseLocal()+
                        "&courseTime=" + Select_Detail.getCourseTime();
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                String Gain = "";
                try {
                    URL url = new URL(Url_);
                    //添加参数
                    connection = (HttpURLConnection) url.openConnection();
                    //设置请求方法
                    connection.setRequestMethod("POST");
                    //设置连接超时时间（毫秒）
                    connection.setConnectTimeout(5000);
                    //设置读取超时时间（毫秒）
                    connection.setReadTimeout(5000);
                    //添加参数
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(value_.getBytes());//上传参数


                    //返回输入流
                    InputStream in = connection.getInputStream();

                    //读取输入流
                    reader = new BufferedReader(new InputStreamReader(in));
                    //把接收的String 分别转换到status 和 data
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    Gain = result.toString();

                    JSONObject jsonObject = new JSONObject(Gain);
                    String Getdata = jsonObject.getString("status");


                } catch (Exception e) {
                    e.printStackTrace();
                    connection.disconnect();//关闭此次网络请求
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                }finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {//关闭连接
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
    public void getData(int flag){
        if(flag == 1){

            //获取学院
            acadamy.add(" ");
            acadamy.add("文学院");
            acadamy.add("马克思主义学院");
            acadamy.add("法学院");
            acadamy.add("经济管理学院");
            acadamy.add("音乐学院");
            acadamy.add("美术学院");
            acadamy.add("历史学院");
            acadamy.add("教育科学学院");
            acadamy.add("外国语学院");
            acadamy.add("体育学院");
            acadamy.add("新闻与传播学院");
            acadamy.add("数学与统计学院");
            acadamy.add("计算机与信息学院");
            acadamy.add("物理与电子信息学院");
            acadamy.add("化学与材料科学学院");
            acadamy.add("地理与旅游学院");
            acadamy.add("生命科学学院");
            acadamy.add("生态与环境学院");
            return ;

        }
        else if(flag == 2){
            //获取获取第二个多选框
            new Thread(new Runnable() {
                @Override
                public void run() {
                    course.clear();
                    if(Role.equals("tea")){
                        Url_ = URL_Course_Tea;
                        value_ = "courseAcademy=" + select_academy;
                    }
                    else{
                        Url_ = URL_Course_Stu;
                        value_ = "academyname=" + select_academy;

                    }


                    System.out.println(select_academy);
                    HttpURLConnection connection = null;
                    BufferedReader reader = null;
                    String Gain = "";
                    try {
                        URL url = new URL(Url_);
                        //添加参数
                        connection = (HttpURLConnection) url.openConnection();
                        //设置请求方法
                        connection.setRequestMethod("POST");
                        //设置连接超时时间（毫秒）
                        connection.setConnectTimeout(5000);
                        //设置读取超时时间（毫秒）
                        connection.setReadTimeout(5000);
                        //添加参数
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        OutputStream outputStream = connection.getOutputStream();
                        outputStream.write(value_.getBytes());//上传参数


                        //返回输入流
                        InputStream in = connection.getInputStream();

                        //读取输入流
                        reader = new BufferedReader(new InputStreamReader(in));
                        //把接收的String 分别转换到status 和 data
                        StringBuilder result = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                        Gain = result.toString();

                        JSONObject jsonObject = new JSONObject(Gain);
                        String Getdata = jsonObject.getString("data");
                        Getdata = Getdata.replace("[","");
                        Getdata = Getdata.replace("]","");
                        Getdata = Getdata.replace("\"","");
                        List<String> notdou = Arrays.asList(Getdata.split(","));
                        course.add(" ");
                        for(String i:notdou){
                            course.add(i);
                        }
                        System.out.println(course);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter_2.setDatas(course);

                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        connection.disconnect();//关闭此次网络请求
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });

                    }finally {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (connection != null) {//关闭连接
                            connection.disconnect();
                        }
                    }
                }
            }).start();
        }
        else if(flag == 3){
            //获取老师
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Teacher.clear();


                    if(Role.equals("tea")){
                        Url_ = URL_Teacher_Tea;
                        value_ = "coursename=" + select_course;
                    }
                    else{
                        Url_ = URL_Teacher_Stu;
                        value_ = "course=" + select_course + "&academyname="+acadamy;

                    }
                    System.out.println(select_course);
                    HttpURLConnection connection = null;
                    BufferedReader reader = null;
                    String Gain = "";
                    try {
                        URL url = new URL(Url_);
                        //添加参数
                        connection = (HttpURLConnection) url.openConnection();
                        //设置请求方法
                        connection.setRequestMethod("POST");
                        //设置连接超时时间（毫秒）
                        connection.setConnectTimeout(5000);
                        //设置读取超时时间（毫秒）
                        connection.setReadTimeout(5000);
                        //添加参数
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        OutputStream outputStream = connection.getOutputStream();
                        outputStream.write(value_.getBytes());//上传参数


                        //返回输入流
                        InputStream in = connection.getInputStream();

                        //读取输入流
                        reader = new BufferedReader(new InputStreamReader(in));
                        //把接收的String 分别转换到status 和 data
                        StringBuilder result = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                        Gain = result.toString();

                        JSONObject jsonObject = new JSONObject(Gain);
                        String Getdata = jsonObject.getString("data");
                        Getdata = Getdata.replace("[","");
                        Getdata = Getdata.replace("]","");
                        Getdata = Getdata.replace("\"","");
                        List<String> notdou = Arrays.asList(Getdata.split(","));
                        Teacher.add(" ");
                        for(String i:notdou){
                            Teacher.add(i);
                        }
                        System.out.println(Teacher);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter_3.setDatas(Teacher);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        connection.disconnect();//关闭此次网络请求
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });

                    }finally {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (connection != null) {//关闭连接
                            connection.disconnect();
                        }
                    }
                }
            }).start();

        }
        else{
            //获取详情
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Url_ = URL_Detail;
                    value_ = "courseteacher=" + select_Teacher+"&coursename="+select_course;
                    System.out.println(select_course);
                    HttpURLConnection connection = null;
                    BufferedReader reader = null;
                    String Gain = "";
                    try {
                        URL url = new URL(Url_);
                        //添加参数
                        connection = (HttpURLConnection) url.openConnection();
                        //设置请求方法
                        connection.setRequestMethod("POST");
                        //设置连接超时时间（毫秒）
                        connection.setConnectTimeout(5000);
                        //设置读取超时时间（毫秒）
                        connection.setReadTimeout(5000);
                        //添加参数
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        OutputStream outputStream = connection.getOutputStream();
                        outputStream.write(value_.getBytes());//上传参数


                        //返回输入流
                        InputStream in = connection.getInputStream();

                        //读取输入流
                        reader = new BufferedReader(new InputStreamReader(in));
                        //把接收的String 分别转换到status 和 data
                        StringBuilder result = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                        Gain = result.toString();

                        JSONObject jsonObject = new JSONObject(Gain);
                        String Getdata = jsonObject.getString("data");
                        System.out.println(Getdata);

                        //分析出每一个大括号
                        String regex = "\\{([^}]*)\\}";//匹配大括号
                        Pattern compile = Pattern.compile(regex);
                        Matcher matcher = compile.matcher(Getdata);
                        GetList.clear();
                        while(matcher.find()) {
                            String group = matcher.group();
                            //分析出大括号中的详细内容
                            JSONObject jsonObject_2 = new JSONObject(group);
                            String courseId = jsonObject_2.getString("courseId");
                            String courseName = jsonObject_2.getString("courseName");
                            String courseTeacher = jsonObject_2.getString("courseTeacher");
                            String courseTeacherId = jsonObject_2.getString("courseTeacherId");
                            String courseAcademy = jsonObject_2.getString("courseAcademy");
                            String courseLocal = jsonObject_2.getString("courseLocal");
                            String courseTime = jsonObject_2.getString("courseTime");
                            String semester = jsonObject_2.getString("semester");
                            Select_GET_Course_Detail temp = new Select_GET_Course_Detail();
                            temp.setCourseId(courseId);
                            temp.setCourseName(courseName);
                            temp.setCourseTeacher(courseTeacher);
                            temp.setCourseTeacherId(courseTeacherId);
                            temp.setCourseAcademy(courseAcademy);
                            temp.setCourseLocal(courseLocal);
                            temp.setCourseTime(courseTime);
                            temp.setSemester(semester);
                            GetList.add(temp);
                        }
                        /**
                         * 注意 这里由于课程详情显示为 TEXTVIEW所以 暂时以文字 展示出来 第一个元素
                         */
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mData = GetList;
                                DisplaySelect_Detail();

                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        connection.disconnect();//关闭此次网络请求
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });

                    }finally {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (connection != null) {//关闭连接
                            connection.disconnect();
                        }
                    }
                }
                }).start();
        }

    }
    public void SetSprinner_three(){
        spinner_3 = (Spinner) findViewById(R.id.spinner3);
        adapter_3 = new select_adapter(this);
        spinner_3.setAdapter(adapter_3);


        /**选项选择监听*/
        spinner_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mData.clear();
                DisplaySelect_Detail();

                select_Teacher = Teacher.get(position);
                getData(4);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void SetSprinner_two(){
        spinner_2 = (Spinner) findViewById(R.id.spinner2);
        adapter_2 = new select_adapter(this);
        spinner_2.setAdapter(adapter_2);


        /**选项选择监听*/
        spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_Teacher = "";
                Teacher.clear();
                adapter_3.setDatas(Teacher);
                mData.clear();
                DisplaySelect_Detail();
                select_course = course.get(position);
                getData(3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void setSprinner_one(){
        /**Spinner主要用于下来菜单的选项，数据加载和绑定很类似Listview，但是应用场景不同*/
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        getData(1);
        select_adapter adapter = new select_adapter(this);
        spinner.setAdapter(adapter);

        adapter.setDatas(acadamy);

        /**选项选择监听*/
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_Teacher = "";
                select_course = "";
                Teacher.clear();
                course.clear();
                adapter_3.setDatas(Teacher);
                spinner_2.setSelection(0);
                adapter_2.setDatas(course);
                spinner_3.setSelection(0);
                mData.clear();
                DisplaySelect_Detail();
                select_academy = acadamy.get(position);
                getData(2);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 监听标题栏按钮点击事件，实现返回按键
     *
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //返回按钮点击事件
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * 以下是有关选课时滑动的逻辑
     */

    private void DisplaySelect_Detail(){

        int Len = mData.size();
        if(Len != 0){
            TV6.setText("总计:"+mData.size());
            TV6.setVisibility(View.VISIBLE);
        }
        else{
            TV6.setVisibility(View.INVISIBLE);
        }
        if(Len >= 1){
            Select_Detail.setCourseAcademy(mData.get(0).getCourseAcademy());
            Select_Detail.setCourseId(mData.get(0).getCourseId());
            Select_Detail.setCourseLocal(mData.get(0).getCourseLocal());
            Select_Detail.setCourseName(mData.get(0).getCourseName());
            Select_Detail.setCourseTeacher(mData.get(0).getCourseTeacher());
            Select_Detail.setCourseTeacherId(mData.get(0).getCourseTeacherId());
            Select_Detail.setCourseTime(mData.get(0).getCourseTime());
            Select_Detail.setSemester(mData.get(0).getSemester());
            System.out.println(mData.get(0).getCourseName());
        }

        mViewPager = findViewById(R.id.Display_ViewPages);
        mViewPager.setAdapter(new Adapter());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Select_Detail.setCourseAcademy(mData.get(position).getCourseAcademy());
                Select_Detail.setCourseId(mData.get(position).getCourseId());
                Select_Detail.setCourseLocal(mData.get(position).getCourseLocal());
                Select_Detail.setCourseName(mData.get(position).getCourseName());
                Select_Detail.setCourseTeacher(mData.get(position).getCourseTeacher());
                Select_Detail.setCourseTeacherId(mData.get(position).getCourseTeacherId());
                Select_Detail.setCourseTime(mData.get(position).getCourseTime());
                Select_Detail.setSemester(mData.get(position).getSemester());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }



    private class Adapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View itemView = getLayoutInflater().inflate(R.layout.page_itme, container, false);
            container.addView(itemView);
            bindData(position, itemView);
            return itemView;
        }

        private void bindData(int position, View itemView) {
            TextView T_academy = itemView.findViewById(R.id.TV_academy);
            TextView T_local = itemView.findViewById(R.id.TV_local);
            TextView T_time = itemView.findViewById(R.id.TV_time);
            T_academy.setText(mData.get(position).getCourseAcademy());
            T_local.setText(mData.get(position).getCourseLocal());
            T_time.setText(mData.get(position).getCourseTime());
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return super.getItemPosition(object);
        }
    }
}