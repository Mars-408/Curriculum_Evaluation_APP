package com.example.myapplication.ui.dashboard;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MyRecyclerViewAdapter;
import com.example.myapplication.R;
import com.example.myapplication.SelectActivity;
import com.example.myapplication.WriteDiaryActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DashboardFragment extends Fragment {
    private Button btn_add;
    private View mView;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter mAdapter;

    private String TeacherId="13";
    private String TeacherName ="";
    private String UrlGetAllCourseId = "";
    private List<Card_Detail_Dashboard> ClickPosition;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ClickPosition = new ArrayList<Card_Detail_Dashboard>();
        mView=inflater.inflate(R.layout.fragment_dashboard, null);

        UrlGetAllCourseId = getResources().getString(R.string.Dashboard_GetList_1);

        SharedPreferences sp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);//fragment中获取SharedPreferences需要多加一步getActivity()方法
        TeacherId = sp.getString("username", "");
        TeacherName = sp.getString("name", "");
        btn_add = mView.findViewById(R.id.btn_add);
        creatrecalview();
        getallcourse();
        return mView;
    }

    /**
     * 接收返回值
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            //刷新本界面 用于处理在选择课程界面返回时信息不更新
        getallcourse();

    }

    private void creatrecalview(){

        mRecyclerView =mView.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getActivity(), mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ClickPosition = new ArrayList<Card_Detail_Dashboard>();
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i;
                i = new Intent(getActivity() , WriteDiaryActivity.class);
                i.putExtra("courceid",ClickPosition.get(position).getCourseId());
                i.putExtra("courcename",ClickPosition.get(position).getCourseName());
                i.putExtra("teacherid",ClickPosition.get(position).getCourseTeacherId());
                i.putExtra("listenedteachername",ClickPosition.get(position).getCourseTeacherName());
                i.putExtra("academy",ClickPosition.get(position).getCourseAcademy());
                i.putExtra("fromteacher",TeacherName);
                i.putExtra("from_teacher_id",TeacherId);
                System.out.println("listenedteachername");
                startActivity(i);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity() , SelectActivity.class);
                i.putExtra("date","tea");
                startActivityForResult(i,1);
            }
        });
    }

    public void getallcourse(){
        ClickPosition.clear();
        //开启线程，发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                String Gain = "";
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
                    String data = "teacherId=" + TeacherId;//拼装参数
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
                    String GetStatus = jsonObject.getString("status");
                    String Getdata = jsonObject.getString("data");

                    //分析出每一个大括号
                    String regex = "\\{([^}]*)\\}";//匹配大括号
                    Pattern compile = Pattern.compile(regex);
                    Matcher matcher = compile.matcher(Getdata);
                    System.out.println(Gain);
                    while(matcher.find()){
                        String group = matcher.group();
                        //分析出大括号中的详细内容
                        jsonObject = new JSONObject(group);
                        String teacherName = jsonObject.getString("teacherName");
                        String teacherId = jsonObject.getString("teacherId");
                        String courseId = jsonObject.getString("courseId");
                        String courseName = jsonObject.getString("courseName");
                        String courseTeacherName = jsonObject.getString("courseTeacherName");
                        String courseTeacherId = jsonObject.getString("courseTeacherId");
                        String courseAcademy = jsonObject.getString("courseAcademy");
                        String courseLocal = jsonObject.getString("courseLocal");
                        String courseTime = jsonObject.getString("courseTime");
                        String status = jsonObject.getString("status");
                        String semester = jsonObject.getString("semester");

                        Card_Detail_Dashboard temp = new Card_Detail_Dashboard();
                        temp.setTeacherName(teacherName);
                        temp.setTeacherId(teacherId);
                        temp.setCourseId(courseId);
                        temp.setCourseName(courseName);
                        temp.setCourseTeacherName(courseTeacherName);
                        temp.setCourseTeacherId(courseTeacherId);
                        temp.setCourseAcademy(courseAcademy);
                        temp.setCourseLocal(courseLocal);
                        temp.setCourseTime(courseTime);
                        temp.setStatus(status);
                        ClickPosition.add(temp);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {  onAddDataClick();
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                    connection.disconnect();//关闭此次网络请求

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

    private void onAddDataClick(){
        mAdapter.setDataSource(ClickPosition);

    }


}

