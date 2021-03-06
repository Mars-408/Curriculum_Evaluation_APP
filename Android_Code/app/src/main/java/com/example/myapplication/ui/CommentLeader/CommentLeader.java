package com.example.myapplication.ui.CommentLeader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.ui.CommentLeader.CardDisplay;

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

public class CommentLeader extends AppCompatActivity {


    private String URL_ = "";
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

        URL_ = getResources().getString(R.string.CommentLeader_GetList);

        setContentView(R.layout.activity_comment_leader);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //????????????
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        TeacherId = sp.getString("username",null);

        setTitle("????????????");
        //????????????
        mRecyclerView = findViewById(R.id.recycler_view_commentLeader);

        // ????????????
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new RecyclerViewAdapter(this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                /**
                 * ????????????????????? ?????????????????????????????????
                 *
                 */
               // Toast.makeText(CommentLeader.this,ClickPosition.get(position).getCourse_id() , Toast.LENGTH_SHORT).show();
                //???????????????Activity ????????????
                Pattern pattern = Pattern.compile("(?<=\\()[^\\)]+");
                Matcher matcher = pattern.matcher(ClickPosition.get(position).getCourse_id());
                matcher.find();
                OpenNew(matcher.group());

            }
        });
        onAddDataClick();
        GetCourseID();

    }
    /**
     * ???????????????????????????????????? Activity->Activity
     */
    public void OpenNew(String courseid){
        //???????????????????????????????????????????????????Activity?????????????????????????????????????????????Activity???
        Intent intent =new Intent(CommentLeader.this, GeneralCommentDisplay.class);
        //???Bundle????????????
        Bundle bundle=new Bundle();
        /**
         * ??????name?????????tinyphp
         * Who????????? ?????????????????? Student??? Teacher
         */
        bundle.putString("Who", "Teacher");
        bundle.putString("CourseId",courseid);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * ????????????
     * ?????????????????????????????????
     */
    public void onAddDataClick() {
        mAdapter.setDataSource(ClickPosition);
    }

    /**
     * ??????HTTP?????? ??????CourseId
     */
    private void GetCourseID() {

        //???????????????????????????
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                String Gain = "";
                CourseId = new ArrayList<String>();
                try {
                    URL url = new URL(URL_);
                    //????????????
                    connection = (HttpURLConnection) url.openConnection();
                    //??????????????????
                    connection.setRequestMethod("POST");
                    //????????????????????????????????????
                    connection.setConnectTimeout(5000);
                    //????????????????????????????????????
                    connection.setReadTimeout(5000);
                    //????????????
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    OutputStream outputStream = connection.getOutputStream();
                    String data = "teacherId=" + TeacherId;//????????????
                    outputStream.write(data.getBytes());//????????????


                    //???????????????
                    InputStream in = connection.getInputStream();

                    //???????????????
                    reader = new BufferedReader(new InputStreamReader(in));
                    //????????????String ???????????????status ??? data
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    Gain = result.toString();
                    JSONObject jsonObject = new JSONObject(Gain);
                    GetStatus = jsonObject.getString("status");
                    String Getdata = jsonObject.getString("data");

                    //???????????????????????????
                    String regex = "\\{([^}]*)\\}";//???????????????
                    Pattern compile = Pattern.compile(regex);
                    Matcher matcher = compile.matcher(Getdata);
                    while(matcher.find()){
                        String group = matcher.group();
                        //????????????????????????????????????
                        jsonObject = new JSONObject(group);
                        String courseId = jsonObject.getString("courseId");
                        String courseName = jsonObject.getString("courseName");
                        String courseTeacher = jsonObject.getString("courseTeacher");
                        String courseTeacherId = jsonObject.getString("courseTeacherId");
                        String courseAcademy = jsonObject.getString("courseAcademy");
                        String courseLocal = jsonObject.getString("courseLocal");
                        String courseTime = jsonObject.getString("courseTime");
                        String semester = jsonObject.getString("semester");

                        CardDisplay temp = new CardDisplay();
                        temp.setCourse_id("????????????:"+courseName+"("+courseId+")");
                        temp.setCourse_name("????????????:"+semester);
                        temp.setCourse_teacher_id("????????????:"+courseTime);
                        temp.setCourse_teacher_name("????????????:"+courseLocal);
                        temp.setAcademy("????????????:"+courseTeacher+"("+courseTeacherId+")");
                        temp.setDetail("????????????:"+courseAcademy);
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
                    connection.disconnect();//????????????????????????
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"??????????????????,???????????????",Toast.LENGTH_SHORT).show();
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
                    if (connection != null) {//????????????
                        connection.disconnect();
                    }
                }
            }
        }).start();


        /**
         * ?????? ??????????????????????????????????????????cook?????? ????????????????????????
         */
    }


    /**
     * ??????????????????????????????????????????????????????
     *
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //????????????????????????
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}