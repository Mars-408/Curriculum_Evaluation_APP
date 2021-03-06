package com.example.myapplication.startUI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.TimeCountUtil;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edit_phone;
    private EditText edit_code;
    private EditText edit_password;
    private EditText edit_confirm_password;
    private Button btn_find,btn_getcode;
    private String phone_number;
    private String code_number;
    EventHandler eventHandler;
    private boolean flag = true;
    private TimeCountUtil mTimeCountUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        getId();

        mTimeCountUtil = new TimeCountUtil(btn_getcode, 60000, 1000);

        eventHandler = new EventHandler(){
            public void afterEvent(int event, int result, Object data){
                Message msg=new Message();
                msg.arg1=event;
                msg.arg2=result;
                msg.obj=data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }
    private void getId(){
        edit_phone=findViewById(R.id.edit_phone);
        edit_code=findViewById(R.id.edit_code);
        edit_password=findViewById(R.id.edit_password);
        edit_confirm_password=findViewById(R.id.edit_confirm_password);
        btn_getcode=findViewById(R.id.btn_getcode);
        btn_find=findViewById(R.id. btn_find);
        btn_getcode.setOnClickListener(this);
        btn_find.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    //??????Handler?????????Message????????????????????????????????????
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            int event=msg.arg1;
            int result=msg.arg2;
            Object data=msg.obj;
            if(result==SMSSDK.RESULT_COMPLETE){
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                String phone = edit_phone.getText().toString();
                                String password = edit_password.getText().toString();
                                FormBody.Builder params = new FormBody.Builder();
                                params.add("username",""+phone);
                                params.add("NewPassword",""+password);
                                OkHttpClient client = new OkHttpClient();//??????http?????????
                                Request request = new Request.Builder()
                                        .url(getResources().getString(R.string.ForgetActivity))
                                        .post(params.build())
                                        .build();//??????http??????
                                Response response = client.newCall(request).execute();//?????????????????????
                                String responseData = response.body().string();//?????????????????????json??????
                                JSONObject obj = new JSONObject(responseData);
                                switch(obj.getInt("status")){
                                    case 3:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "??????????????????!", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        break;
                                    case 0:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "???????????????!", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(ForgetActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    case 17:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "??????????????????!", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    default:
                                        break;
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ForgetActivity.this,"?????????????????????",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
            else{
                if(flag){
                    btn_getcode.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"????????????????????????????????????", Toast.LENGTH_LONG).show();
                    edit_phone.requestFocus();
                }
                else{
                    Toast.makeText(getApplicationContext(),"?????????????????????", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_getcode:
                if(judPhone()){
                    mTimeCountUtil.start();
                    SMSSDK.getVerificationCode("86",phone_number);
                    edit_code.requestFocus();
                }
                break;
            case R.id.btn_find:
                if(judCode()){
                    if(TextUtils.isEmpty(edit_password.getText().toString().trim())||TextUtils.isEmpty(edit_confirm_password.getText().toString().trim())){
                        Toast.makeText(ForgetActivity.this,"????????????????????????!",Toast.LENGTH_LONG).show();
                    }
                    else {
                        if(isTrue(edit_password.getText().toString())&&isTrue(edit_confirm_password.getText().toString())){
                            if(edit_password.getText().toString().equals(edit_confirm_password.getText().toString())){
                                SMSSDK.submitVerificationCode("86",phone_number,code_number);
                            }
                            else{
                                Toast.makeText(ForgetActivity.this,"?????????????????????????????????!",Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(ForgetActivity.this,"????????????!",Toast.LENGTH_LONG).show();
                        }
                    }
                }
                flag = false;
                break;
            default:
                break;
        }
    }

    private boolean judPhone(){
        if(TextUtils.isEmpty(edit_phone.getText().toString().trim())){
            Toast.makeText(ForgetActivity.this,"???????????????????????????",Toast.LENGTH_LONG).show();
            edit_phone.requestFocus();
            return false;
        }
        else if(edit_phone.getText().toString().trim().length()!=11){
            Toast.makeText(ForgetActivity.this,"?????????????????????????????????",Toast.LENGTH_LONG).show();
            edit_phone.requestFocus();
            return false;
        }
        else{
            phone_number=edit_phone.getText().toString().trim();
            String num="[1][3578]\\d{9}";
            if(phone_number.matches(num))
                return true;
            else{
                Toast.makeText(ForgetActivity.this,"??????????????????????????????",Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    private boolean judCode(){
        judPhone();
        if(TextUtils.isEmpty(edit_code.getText().toString().trim())){
            Toast.makeText(ForgetActivity.this,"????????????????????????",Toast.LENGTH_LONG).show();
            edit_code.requestFocus();
            return false;
        }
        else if(edit_code.getText().toString().trim().length()!=4){
            Toast.makeText(ForgetActivity.this,"??????????????????????????????",Toast.LENGTH_LONG).show();
            edit_code.requestFocus();
            return false;
        }
        else{
            code_number = edit_code.getText().toString().trim();
            return true;
        }
    }

    private boolean isTrue(String text){
        Pattern p = Pattern.compile("\\d{6}");
        Matcher m = p.matcher(text);
        return m.matches();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

