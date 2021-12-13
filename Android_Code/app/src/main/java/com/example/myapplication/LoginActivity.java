package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.startUI.ForgetActivity;
import com.example.myapplication.startUI.RegisterActivity;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText username_Text,password_Text;

    private String URL_;
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        URL_ = getResources().getString(R.string.Login);


        username_Text = findViewById(R.id.userNameText);
        password_Text = findViewById(R.id.passwordText);



        //注册
        TextView registertextview = (TextView) findViewById(R.id.registertextview);
        registertextview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(LoginActivity.this , RegisterActivity.class);
                startActivity(i);
            }
        });

        //忘记密码
        TextView forgettextview = (TextView) findViewById(R.id.forgettextview);
        forgettextview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(LoginActivity.this , ForgetActivity.class);
                startActivity(i);
            }
        });

        //登录按钮
        Button loginbutton = (Button) findViewById(R.id.loginbutton);
        loginbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            String username = username_Text.getText().toString();
                            String password = password_Text.getText().toString();
                            FormBody.Builder params = new FormBody.Builder();
                            params.add("username",""+username);
                            params.add("password",""+password);
                            OkHttpClient client = new OkHttpClient();//创建http客户端
                            Request request = new Request.Builder()
                                    .url(URL_)
                                    .post(params.build())
                                    .build();//创建http请求
                            Response response = client.newCall(request).execute();//执行发送的命令
                            String responseData = response.body().string();//获取后端返回的json数据
                            JSONObject obj = new JSONObject(responseData);
                            System.out.println(obj);
                            switch(obj.getInt("status")){
                                case 0 :
                                    JSONObject dataObj = new JSONObject(obj.getString("data"));
                                    System.out.println(dataObj.getString("role"));

                                    if(dataObj.getString("role").equals("教师")){
                                        // 设定登录缓存
                                        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                                        sp.edit()
                                                .putString("name",dataObj.getString("name"))
                                                .putString("username",dataObj.getString("username"))
                                                .putString("password",dataObj.getString("password"))
                                                .putString("email",dataObj.getString("email"))
                                                .putString("role",dataObj.getString("role"))
                                                .putString("createTime",dataObj.getString("createTime"))
                                                .putString("updateTime",dataObj.getString("updateTime"))
                                                .putString("userpic",dataObj.getString("userpic"))
                                                .putString("academy",dataObj.getString("academy"))
                                                .putString("task",dataObj.getString("task"))
                                                .putInt("status",obj.getInt("status"))
                                                .apply();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(LoginActivity.this,"登录成功！",Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(LoginActivity.this , BottomMenuActivity.class);
                                                startActivity(i);
                                            }
                                        });
                                    }
                                    if(dataObj.getString("role").equals("学生")){
                                        // 设定登录缓存
                                        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                                        sp.edit()
                                                .putString("name",dataObj.getString("name"))
                                                .putString("username",dataObj.getString("username"))
                                                .putString("password",dataObj.getString("password"))
                                                .putString("email",dataObj.getString("email"))
                                                .putString("role",dataObj.getString("role"))
                                                .putString("createTime",dataObj.getString("createTime"))
                                                .putString("updateTime",dataObj.getString("updateTime"))
                                                .putString("userpic",dataObj.getString("userpic"))
                                                .putString("academy",dataObj.getString("academy"))
                                                .putString("task",dataObj.getString("task"))
                                                .putInt("status",obj.getInt("status"))
                                                .apply();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(LoginActivity.this,"登录成功！",Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(LoginActivity.this , BottomMenuActivity2.class);
                                                startActivity(i);
                                            }
                                        });
                                    }
                                    if(dataObj.getString("role").equals("院督导员")||dataObj.getString("role").equals("校督导员")){
                                        // 设定登录缓存
                                        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                                        sp.edit()
                                                .putString("name",dataObj.getString("name"))
                                                .putString("username",dataObj.getString("username"))
                                                .putString("password",dataObj.getString("password"))
                                                .putString("email",dataObj.getString("email"))
                                                .putString("role",dataObj.getString("role"))
                                                .putString("createTime",dataObj.getString("createTime"))
                                                .putString("updateTime",dataObj.getString("updateTime"))
                                                .putString("userpic",dataObj.getString("userpic"))
                                                .putString("academy",dataObj.getString("academy"))
                                                .putString("task",dataObj.getString("task"))
                                                .putInt("status",obj.getInt("status"))
                                                .apply();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(LoginActivity.this,"登录成功！",Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(LoginActivity.this , BottomMenuActivity3.class);
                                                startActivity(i);
                                            }
                                        });
                                    }
                                    break;
                                case 3 :
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this,"用户名不存在！",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                                case 4 :
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this,"密码错误！",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                                default :
                                    break;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //子线程不能直接操作UI,需要调用runOnUiThread
                                    Toast.makeText(LoginActivity.this,"网络连接失败！",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}
