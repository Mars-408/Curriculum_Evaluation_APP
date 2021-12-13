package com.example.myapplication.ui.TeacherProof;

import android.content.Context;
import android.content.Intent;
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
import com.example.myapplication.startUI.RegisterActivity;
import com.example.myapplication.ui.DisScoreAnalyseFromStu.GeneralScoreDisplay;
import com.example.myapplication.ui.DisplayTeaProof.DisplayProofActivity;
import com.example.myapplication.ui.TeacherProof.CardDisplay;
import com.example.myapplication.ui.TeacherProof.RecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SelectTeaProofActivity extends AppCompatActivity {
    private String getAllProofList = "";
    private String getProofList = "";
    private String Academy;
    private String role;

    private List<CardDisplay> ClickPosition;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private Spinner academySp;
    private String [] academyInfo;
    private ArrayAdapter<String> adapterSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ClickPosition = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tea_proof);

        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        Academy = sp.getString("academy","null");
        role = sp.getString("role","null");

        getAllProofList = getResources().getString(R.string.Get_All_Proof_List);
        getProofList = getResources().getString(R.string.Get_Proof_List);

        mRecyclerView = findViewById(R.id.recycler_view_teaProof);

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
                //注意截取字符串
                OpenNew(ClickPosition.get(position).getTeacherName().substring(5),
                        ClickPosition.get(position).getCourseName().substring(4),
                        ClickPosition.get(position).getListened_teacherName().substring(5),
                        ClickPosition.get(position).getDate().substring(3));
            }
        });
        //返回键
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        /**
         * 根据所选择的选择的学院获取听课凭证列表
         */
        academySp = (Spinner)this.findViewById(R.id.Spinner_academy);
        //准备要加载的字符串数组资源
        if(role.equals("院督导员")){
            academyInfo = new String[1];
            academyInfo[0] = Academy;
            adapterSp = new ArrayAdapter<String>(SelectTeaProofActivity.this,android.R.layout.simple_spinner_item,academyInfo);
            //第三步：为适配器设置下拉列表下拉时的菜单样式。
            adapterSp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            academySp.setAdapter(adapterSp);
            sendByAcademy(Academy);
        }
        else{
            GetTeaproofList();
            academyInfo = getResources().getStringArray(R.array.academy);
            /*
             * 初始化适配器时各参数
             * context: 上下文对象，当前类.this
             * resource：表示列表item的布局资源id 默认android.R.layout.simple_spinner_item,logmethod
             * object：要适配的数据资源
             * */
            adapterSp = new ArrayAdapter<String>(SelectTeaProofActivity.this,android.R.layout.simple_spinner_item,academyInfo);
            //第三步：为适配器设置下拉列表下拉时的菜单样式。
            adapterSp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            academySp.setAdapter(adapterSp);
            academySp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /*
            参数详解：
            AdapterView<?> adapterView：触发当前事件的Spinner对象
            View view表示当前备选中的item
            int i:表示当前被选中item的下表
            long l：表示当前被选中item的id
             */

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    String str = adapterSp.getItem(position); //在数据源中获取
                    //发送请求
                    sendByAcademy(str);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });


        }


    }

    /**
     * 这里是携带参数的页面跳转 Activity->Activity
     */
    public void OpenNew(String teacherName,String courseName,String listenedTeaName,String date){
        //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
        Intent intent =new Intent(SelectTeaProofActivity.this, DisplayProofActivity.class);
        //用Bundle携带数据
        Bundle bundle=new Bundle();
        /**
         * 传递name参数为tinyphp
         * Who该变量 只有两个取值 Student和 Teacher
         */
        bundle.putString("teacherName", teacherName);
        bundle.putString("courseName",courseName);
        bundle.putString("listenedTeaName",listenedTeaName);
        bundle.putString("date",date);
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
     * 发送http请求，根据学院检索听课凭证的信息
     */
    public void sendByAcademy(String academy){
        System.out.println(academy);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.removeAllViews();
                        }
                    });
                    ClickPosition.clear();
                    OkHttpClient client = new OkHttpClient();//创建http客户端
                    FormBody.Builder params = new FormBody.Builder();
                    params.add("academy",""+academy);
                    Request request = new Request.Builder()
                            .url(getProofList)
                            .post(params.build())
                            .build();//创建http请求
                    Response response = client.newCall(request).execute();//执行发送的命令
                    String responseData = response.body().string();//获取后端返回的json数据
                    System.out.println(responseData);
                    //提取多层嵌套的json数据
                    JSONObject obj = new JSONObject(responseData);
                    JSONArray result = obj.getJSONArray("data");
                    for (int i = 0; i < result.length(); i++) {
                        String teacherName = result.getJSONObject(i).getString("teacherName");
                        String courseName = result.getJSONObject(i).getString("courseName");
                        String listenedTeacherName = result.getJSONObject(i).getString("listenedTeacherName");
                        String listenTime = result.getJSONObject(i).getString("listenTime");
                        CardDisplay temp = new CardDisplay();
                        temp.setTeacherName("听课教师："+teacherName);
                        temp.setCourseName("课程名："+courseName);
                        temp.setListened_teacherName("授课教师："+listenedTeacherName);
                        temp.setDate("时间："+listenTime);
                        ClickPosition.add(temp);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onAddDataClick();
                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SelectTeaProofActivity.this,"网络连接失败!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 发送HTTP请求，请求所有的听课凭证信息
     */
    private void GetTeaproofList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();//创建http客户端
                    FormBody.Builder params = new FormBody.Builder();
                    Request request = new Request.Builder()
                            .url(getAllProofList)
                            .post(params.build())
                            .build();//创建http请求
                    Response response = client.newCall(request).execute();//执行发送的命令
                    String responseData = response.body().string();//获取后端返回的json数据

                    //提取多层嵌套的json数据
                    JSONObject obj = new JSONObject(responseData);
                    JSONArray result = obj.getJSONArray("data");
                    for (int i = 0; i < result.length(); i++) {
                        String teacherName = result.getJSONObject(i).getString("teacherName");
                        String courseName = result.getJSONObject(i).getString("courseName");
                        String listenedTeacherName = result.getJSONObject(i).getString("listenedTeacherName");
                        String listenTime = result.getJSONObject(i).getString("listenTime");
                        CardDisplay temp = new CardDisplay();
                        temp.setTeacherName("听课教师："+teacherName);
                        temp.setCourseName("课程名："+courseName);
                        temp.setListened_teacherName("授课教师："+listenedTeacherName);
                        temp.setDate("时间："+listenTime);
                        ClickPosition.add(temp);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onAddDataClick();
                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SelectTeaProofActivity.this,"网络连接失败!", Toast.LENGTH_SHORT).show();
                            System.out.println("error");
                        }
                    });
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