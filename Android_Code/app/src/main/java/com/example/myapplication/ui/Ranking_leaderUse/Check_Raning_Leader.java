package com.example.myapplication.ui.Ranking_leaderUse;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Check_Raning_Leader extends AppCompatActivity {
    private TextView textView;
    //首先还是先声明这个Spinner控件
    private Spinner spinner;
    //定义一个String类型的List数组作为数据源
    private List<String> dataList;
    //定义一个ArrayAdapter适配器作为spinner的数据适配器
    private ArrayAdapter<String> adapter;

    private List<CardDisplay_Leader> ClickPosition;

    private String Academy;
    private String role;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter_leader mAdapter;

    private String flag=". . .";

    private String URL_="http://10.0.2.2:8888/portal/calculate/leaderget.do";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ClickPosition = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check__raning__leader);
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        Academy = sp.getString("academy","null");
        role = sp.getString("role","null");
        URL_ = getResources().getString(R.string.Check_Ranking);

        //返回键
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Sprinner_put();
        card();
    }
    /**
     * 获取网络内容
     */
    public void getRanking(){
        ClickPosition.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();//创建http客户端
                    FormBody.Builder params = new FormBody.Builder();
                    params.add("Academy",""+flag);
                    Request request = new Request.Builder()
                            .url(URL_)
                            .post(params.build())
                            .build();//创建http请求
                    Response response = client.newCall(request).execute();//执行发送的命令
                    String responseData = response.body().string();//获取后端返回的json数据
                    JSONObject obj = new JSONObject(responseData);
                    String Getdata = obj.getString("data");
                    String regex = "\\{([^}]*)\\}";//匹配大括号
                    Pattern compile = Pattern.compile(regex);
                    Matcher matcher = compile.matcher(Getdata);
                    while(matcher.find()){
                        String group = matcher.group();
                        //分析出大括号中的详细内容
                        JSONObject jsonObject = new JSONObject(group);
                        String teacherId = jsonObject.getString("teacherId");
                        String teacherName = jsonObject.getString("teacherName");
                        String courseId = jsonObject.getString("courseId");
                        String courseName = jsonObject.getString("courseName");
                        String totalAverage = jsonObject.getString("totalAverage");
                        String semester = jsonObject.getString("semester");
                        int ranking;
                        try{
                            ranking = Integer.parseInt(jsonObject.getString("ranking"));
                        }
                        catch (Exception e){
                            ranking = 0;
                        }

                        if(teacherId.equals("-1")){
                            continue;
                        }
                        CardDisplay_Leader temp = new CardDisplay_Leader();
                        temp.setCourse_id(courseId);
                        temp.setCourse_name(courseName);
                        temp.setCourse_teacher_id(teacherId);
                        temp.setCourse_teacher_name(teacherName);
                        temp.setRankint(ranking);
                        temp.setSemester(semester);
                        temp.setTotalAverage(totalAverage);
                        ClickPosition.add(temp);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onAddDataClick();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //子线程不能直接操作UI,需要调用runOnUiThread
                            Toast.makeText(Check_Raning_Leader.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
    /**
     * 卡片布局
     */
    public void card(){
        mRecyclerView = findViewById(R.id.RecyclerView_Learde_Data);
        // 线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new RecyclerViewAdapter_leader(this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        onAddDataClick();
        mAdapter.setOnItemClickListener(new RecyclerViewAdapter_leader.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                /**
                 * 这里是点击事件 点击的内容会在这里获取
                 *
                 */
              //  Toast.makeText(Check_Raning_Leader.this,ClickPosition.get(position).getCourse_id() , Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * 设定卡片布局的数据
     */
    public void onAddDataClick() {
        System.out.println(ClickPosition.size());
        Collections.sort(ClickPosition);
        mAdapter.setDataSource(ClickPosition);
    }

    /**
     * 刷新UI界面的显示数据
     */
    public void DisplayDate(){


    }
    /**
     * 设定下拉框的数据
     */
    public void SetOnlyDate(){
        dataList = new ArrayList<>();
        dataList.add(Academy);

    }


    public void SetDate(){
        //为dataList赋值，将下面这些数据添加到数据源中
        dataList = Arrays.asList(getResources().getStringArray(R.array.academy));
    }

    /**
     * 设定下拉框
     */
    public void Sprinner_put(){
        //spinner
        spinner = (Spinner) findViewById(R.id.spinner);
        textView = (TextView) findViewById(R.id.tv);



        /*为spinner定义适配器，也就是将数据源存入adapter，这里需要三个参数
        1. 第一个是Context（当前上下文），这里就是this
        2. 第二个是spinner的布局样式，这里用android系统提供的一个样式
        3. 第三个就是spinner的数据源，这里就是dataList*/
        if(role.equals("院督导员")) {
            SetOnlyDate();
        }
        else{
            SetDate();
        }

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dataList);

        //为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //为spinner绑定我们定义好的数据适配器
        spinner.setAdapter(adapter);
        //为spinner绑定监听器，这里我们使用匿名内部类的方式实现监听器

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textView.setText("您当前查看的范围是："+adapter.getItem(position));
                flag = adapter.getItem(position);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getRanking();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textView.setText("请选择您的城市");


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
}
