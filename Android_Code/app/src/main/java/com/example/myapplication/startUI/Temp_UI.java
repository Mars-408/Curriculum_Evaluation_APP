package com.example.myapplication.startUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.BottomMenuActivity;
import com.example.myapplication.BottomMenuActivity2;
import com.example.myapplication.BottomMenuActivity3;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;

public class Temp_UI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp__u_i);

        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        int status = sp.getInt("status",-1);
        switch (status){
            case(0):
                String Role = sp.getString("role","null");
                if(Role.equals("学生")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Temp_UI.this,"登录成功！",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Temp_UI.this , BottomMenuActivity2.class);
                            startActivity(i);
                        }
                    });
                    finish();
                    break;
                }
                else if(Role.equals("教师")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Temp_UI.this,"登录成功！",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Temp_UI.this , BottomMenuActivity.class);
                            startActivity(i);
                        }
                    });
                    finish();
                    break;
                }
                else if(Role.equals("院督导员") || Role.equals("校督导员")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Temp_UI.this,"登录成功！",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Temp_UI.this , BottomMenuActivity3.class);
                            startActivity(i);
                        }
                    });
                    finish();
                    break;
                }
                else{
                    finish();
                }
            default:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(Temp_UI.this , LoginActivity.class);
                        startActivity(i);
                    }
                });
                finish();
                break;
        }

    }
}