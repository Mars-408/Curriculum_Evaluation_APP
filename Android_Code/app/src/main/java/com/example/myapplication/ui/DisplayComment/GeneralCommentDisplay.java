package com.example.myapplication.ui.DisplayComment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

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

public class GeneralCommentDisplay extends AppCompatActivity {
    private String Course_id;
    private String role;
    private List<String> Comment;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter mAdapter;
    private String UrlGetFromStudent = "http://10.0.2.2:8888/portal/stuevaluation/gainbycourseid.do";
    private String UrlGetFromTeacher = "http://10.0.2.2:8888/portal/TeaEvaluation/GainComment.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_comment_display);
        //读取缓存
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        //设定标题
        setTitle ("评语详情");
        UrlGetFromStudent = getResources().getString(R.string.General_Comment_GetStu);
        UrlGetFromTeacher = getResources().getString(R.string.General_Comment_GetTea);
        //标题栏的返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        Course_id = bundle.getString("CourseId");
        role = bundle.getString("Who");
        // 线性布局
        mRecyclerView = findViewById(R.id.comment_display_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //发送网络请求 并且 显示
        Comment = new ArrayList<>();
        onAddDataClick();
        GetComment();
    }
    /**
     * 返回键
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
    /**
     * 添加数据
     * 这里是初始化列表的位置
     */
    public void onAddDataClick() {
        mAdapter.setDataSource(Comment);
    }
    /**
     * 发送请求
     */
    public void GetComment(){
        //开启线程，发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                String Gain = "";
                URL url;
                try {

                    if(role.equals("Teacher")){
                        //老师获取来自老师的评论
                        url = new URL(UrlGetFromTeacher);
                    }
                    else{
                        url = new URL(UrlGetFromStudent);
                    }

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
                    String data = "courseid=" + Course_id;//拼装参数
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
                    //分析出data
                    Gain = result.toString();
                    JSONObject jsonObject = new JSONObject(Gain);
                    String Getdata = jsonObject.getString("data");
                    //分析出每一个大括号
                    String regex = "\\{([^}]*)\\}";//匹配大括号
                    Pattern compile = Pattern.compile(regex);
                    Matcher matcher = compile.matcher(Getdata);
                    while(matcher.find()){
                        String group = matcher.group();
                        //分析出大括号中的comment
                        jsonObject = new JSONObject(group);
                        String GetComment = jsonObject.getString("comment");
                        Comment.add(GetComment);
                    }
                    /**
                     * 子线程去更新主线程的UI
                     */
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onAddDataClick();
                        }
                    });


                }catch (Exception e) {
                    e.printStackTrace();
                    connection.disconnect();//关闭此次网络请求
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"网络连接失败,请检查网络",Toast.LENGTH_SHORT).show();
                        }
                    });

                } finally {
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
