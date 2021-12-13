package com.example.myapplication.ui.CommentStu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ui.DisplayComment.GeneralCommentDisplay;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mob.MobSDK.getContext;

/**
 * 评价获取使用步骤
 *  * 1. 向课表数据库获取课程id
 *  * 2. 向课表详情数据库获取该课程的详情信息（增添）
 *  * 3. 点击进去课程 获取该课程评价
 *
 *  注意 这个界面使用的是卡片式布局参考：https://developer.android.com/guide/topics/ui/layout/cardview?hl=zh-cn
 */
public class CommentStu extends AppCompatActivity {
    /**
     * 注意这里数据请求 使用http 需要在 androidmanifest添加android:usesCleartextTraffic="true"
     * 注意 这里修改了后端内容 的Getcourseidbyteacherid 原本返回仅包括courseid的字符串list现在返回实体的list
     */
    private String UrlGetAllCourseId = "";
    private String TeacherId;
    private List<String> CourseId;
    private String GetStatus = "";
    private TextView textView;

    private List<CardDisplay> ClickPosition;

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ClickPosition = new ArrayList<>();
        super.onCreate(savedInstanceState);

        UrlGetAllCourseId = getResources().getString(R.string.CommentStu_GetList);

        setContentView(R.layout.activity_comment_stu);
        //读取缓存
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        TeacherId = sp.getString("username",null);
        //设定标题
        setTitle ("学生评语");
        //标题栏的返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        mRecyclerView = findViewById(R.id.recycler_view_commentstu);

        // 线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new RecyclerViewAdapter(this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                /**
                 * 这里是点击事件 点击的内容会在这里获取
                 *
                 */
              //  Toast.makeText(CommentStu.this,ClickPosition.get(position).getCourse_id() , Toast.LENGTH_SHORT).show();
                //传入下一个Activity 去取评论
                OpenNew(ClickPosition.get(position).getCourse_id());
            }
        });
        onAddDataClick();
        GetCourseID();
    }
    /**
     * 这里是携带参数的页面跳转 Activity->Activity
     */
    public void OpenNew(String courseid){
        //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
        Intent intent =new Intent(CommentStu.this, GeneralCommentDisplay.class);
        //用Bundle携带数据
        Bundle bundle=new Bundle();
        /**
         * 传递name参数为tinyphp
         * Who该变量 只有两个取值 Student和 Teacher
         */
        bundle.putString("Who", "Students");
        bundle.putString("CourseId",courseid);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    /**
     * 添加数据
     * 这里是初始化列表的位置
     */
    public void onAddDataClick() {
        mAdapter.setDataSource(ClickPosition);
    }

    /**
     * 发送HTTP请求 获取CourseId
     */
    private void GetCourseID() {
        //开启线程，发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                String Gain = "";
                CourseId = new ArrayList<String>();
                try {
                    URL url = new URL(UrlGetAllCourseId);
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
                    String data = "teacherid=" + TeacherId;//拼装参数
                    outputStream.write(data.getBytes());//上传参数


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
                    GetStatus = jsonObject.getString("status");
                    String Getdata = jsonObject.getString("data");

                    //分析出每一个大括号
                    String regex = "\\{([^}]*)\\}";//匹配大括号
                    Pattern compile = Pattern.compile(regex);
                    Matcher matcher = compile.matcher(Getdata);
                    while(matcher.find()){
                        String group = matcher.group();
                        //分析出大括号中的详细内容
                        jsonObject = new JSONObject(group);
                        String courseId = jsonObject.getString("courseId");
                        String course = jsonObject.getString("course");
                        String teacherId = jsonObject.getString("teacherId");
                        String teacher = jsonObject.getString("teacher");
                        String academy = jsonObject.getString("academy");
                        CardDisplay temp = new CardDisplay();
                        temp.setCourse_id(courseId);
                        temp.setCourse_name("课程名称:"+course);
                        temp.setCourse_teacher_id("教师ID:"+teacherId);
                        temp.setCourse_teacher_name("教师姓名:"+teacher);
                        temp.setDetail("详情:"+academy);
                        ClickPosition.add(temp);
                    }
                    if(ClickPosition.size()==0){
                        /**
                         * 此处为暂时没有数据的接口
                         * 但是 如果没有数据 上面json解析会失败
                         * 所以 到下面的Exceotio会报网络异常
                         * 所以 应该自定义上抛的错误
                         */


                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onAddDataClick();
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    connection.disconnect();//关闭此次网络请求
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"网络连接失败,请检查网络",Toast.LENGTH_SHORT).show();
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
}