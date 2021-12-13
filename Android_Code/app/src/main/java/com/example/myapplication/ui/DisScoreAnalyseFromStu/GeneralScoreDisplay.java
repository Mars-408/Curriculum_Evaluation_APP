package com.example.myapplication.ui.DisScoreAnalyseFromStu;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.CombinedChartManager;
import com.example.myapplication.R;
import com.github.mikephil.charting.charts.CombinedChart;

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

public class GeneralScoreDisplay extends AppCompatActivity {

    private String Course_id;
    private String UrlGet_fromstu = "";
    private String UrlGet_fromtea = "";
    private String GetStatus;
    private String teacherId;
    private String teachername;
    private String courseid;
    private String coursename;
    private String semester;
    private String ranking;
    private String Who;
    private float ave1 ;
    private float ave2 ;
    private float ave3 ;
    private float ave4 ;
    private float ave5 ;
    private float ave6 ;
    private float toave;
    private float AVE_ave1;
    private float AVE_ave2;
    private float AVE_ave3;
    private float AVE_ave4;
    private float AVE_ave5;
    private float AVE_ave6;
    private float AVE_toave;

    private CombinedChart mCombinedChart1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_score_display);
        //设定标题
        setTitle ("评分数据分析");

        UrlGet_fromstu = getResources().getString(R.string.General_Score_GetStu);
        UrlGet_fromtea = getResources().getString(R.string.General_Score_GetTea);

        //标题栏的返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        Course_id = bundle.getString("CourseId");
        Who = bundle.getString("Who");


        //从服务器获取评分
        GetScore();


    }
    /**
     * 画出折线图和曲线图
     */
    private void chart(){
        //绘图
        mCombinedChart1 = (CombinedChart) findViewById(R.id.chart1);

        //y轴数据集合——柱状图
        List<List<Float>> yBarDatas = new ArrayList<>();
        List<Float> yData = new ArrayList<>();
        yData.add(ave1);
        yData.add(ave2);
        yData.add(ave3);
        yData.add(ave4);
        yData.add(ave5);
        yData.add(ave6);
        yBarDatas.add(yData);


        //y轴数据集合——折线图
        List<List<Float>> yLineDatas = new ArrayList<>();
        yData = new ArrayList<>();
        yData.add(AVE_ave1);
        yData.add(AVE_ave2);
        yData.add(AVE_ave3);
        yData.add(AVE_ave4);
        yData.add(AVE_ave5);
        yData.add(AVE_ave6);
        yLineDatas.add(yData);

        //颜色集合
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        //管理类
        CombinedChartManager combineChartManager1 = new CombinedChartManager(mCombinedChart1);
        combineChartManager1.showCombinedChart(yBarDatas.get(0), yLineDatas.get(0),
                "分数", "平均分", colors.get(0), colors.get(1));
    }
    /**
     * 分数写到textview
     */
    private void ToWritetextview() {
        TextView Tv_0 = (TextView) findViewById(R.id.Detail_coursename_value);
        TextView Tv_1 = (TextView) findViewById(R.id.evaluation_one_line_value);
        TextView Tv_2 = (TextView) findViewById(R.id.evaluation_two_line_value);
        TextView Tv_3 = (TextView) findViewById(R.id.evaluation_three_line_value);
        TextView Tv_4 = (TextView) findViewById(R.id.evaluation_four_line_value);
        TextView Tv_5 = (TextView) findViewById(R.id.evaluation_five_line_value);
        TextView Tv_6 = (TextView) findViewById(R.id.evaluation_six_line_value);
        TextView Tv_7 = (TextView) findViewById(R.id.evaluation_seven_line_value);
        TextView Tv_8 = (TextView) findViewById(R.id.RankDisplay_value);

        Tv_0.setText(coursename+"("+courseid+")");
        Tv_1.setText(String.valueOf(ave1) );
        Tv_2.setText(String.valueOf(ave2) );
        Tv_3.setText(String.valueOf(ave3) );
        Tv_4.setText(String.valueOf(ave4) );
        Tv_5.setText(String.valueOf(ave5) );
        Tv_6.setText(String.valueOf(ave6) );
        Tv_7.setText(String.valueOf(toave));
        Tv_8.setText(ranking);

        chart();
    }

    /**
     * 发送HTTP请求 获取CourseId
     */
    private void GetScore() {
        //开启线程，发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                BufferedReader AVEreader = null;
                String Gain = "";
                URL url;
                try {
                    if(Who.equals("Teacher")){
                        url = new URL(UrlGet_fromtea);
                    }
                    else{
                        url = new URL(UrlGet_fromstu);
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
                    //获取一般的数据
                    String data = "courseId=" + Course_id;//拼装参数
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
                    System.out.println(Gain);
                    JSONObject jsonObject = new JSONObject(Gain);
                    GetStatus = jsonObject.getString("status");
                    String Getdata = jsonObject.getString("data");
                    //分析出大括号中的详细内容
                    jsonObject = new JSONObject(Getdata);
                    teacherId = jsonObject.getString("teacherId");
                    teachername = jsonObject.getString("teacherName");
                    courseid = jsonObject.getString("courseId");
                    coursename = jsonObject.getString("courseName");
                    ave1 = Float.parseFloat(jsonObject.getString("average1"));
                    ave2 = Float.parseFloat(jsonObject.getString("average2"));
                    ave3 = Float.parseFloat(jsonObject.getString("average3"));
                    ave4 = Float.parseFloat(jsonObject.getString("average4"));
                    ave5 = Float.parseFloat(jsonObject.getString("average5"));
                    ave6 = Float.parseFloat(jsonObject.getString("average6"));
                    toave = Float.parseFloat(jsonObject.getString("totalAverage"));
                    semester = jsonObject.getString("semester");
                    ranking = jsonObject.getString("ranking");
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
                    outputStream = connection.getOutputStream();
                    String AVEdata = "courseId=scoreaverage";//拼装参数
                    outputStream.write(AVEdata.getBytes());//上传参数
                    InputStream AVEin = connection.getInputStream();
                    AVEreader = new BufferedReader(new InputStreamReader(AVEin));
                    StringBuilder AVEresult = new StringBuilder();
                    String AVEline;
                    while ((AVEline = AVEreader.readLine()) != null) {
                        AVEresult.append(AVEline);
                    }

                    Gain = AVEresult.toString();
                    System.out.println(Gain);
                    jsonObject = new JSONObject(Gain);
                    GetStatus = jsonObject.getString("status");
                    Getdata = jsonObject.getString("data");
                    jsonObject = new JSONObject(Getdata);
                    AVE_ave1 = Float.parseFloat(jsonObject.getString("average1"));
                    AVE_ave2 = Float.parseFloat(jsonObject.getString("average2"));
                    AVE_ave3 = Float.parseFloat(jsonObject.getString("average3"));
                    AVE_ave4 = Float.parseFloat(jsonObject.getString("average4"));
                    AVE_ave5 = Float.parseFloat(jsonObject.getString("average5"));
                    AVE_ave6 = Float.parseFloat(jsonObject.getString("average6"));
                    AVE_toave = Float.parseFloat(jsonObject.getString("totalAverage"));
                    //把分数写到textview
                    /**
                     * 子线程去更新主线程的UI
                     */
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToWritetextview();
                        }
                    });


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
                    if (AVEreader != null) {
                        try {
                            AVEreader.close();
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
}