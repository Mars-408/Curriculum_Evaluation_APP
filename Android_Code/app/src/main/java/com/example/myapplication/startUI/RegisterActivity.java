package com.example.myapplication.startUI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_phone;
    private EditText edit_code;
    private EditText edit_password;
    private EditText edit_name;
    private EditText edit_email;
    private EditText edit_confirm_password;
    private Button btn_getCode,btn_register;
    private String phone_number;
    private String code_number;
    private RadioButton rb_1;
    private RadioButton rb_2;
    private TextView textView2;
    EventHandler eventHandler;
    private boolean flag = true;
    private TimeCountUtil mTimeCountUtil;
    private Spinner mSp1;
    private String [] logmethod;
    private ArrayAdapter<String> adapter;
    RadioGroup roleGroup;
    String academy,role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getId();
        //倒计时
        mTimeCountUtil = new TimeCountUtil(btn_getCode, 60000, 1000);

        roleGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton radioButton = (RadioButton)findViewById(checkedId);
                role = radioButton.getText().toString();
            }
        });

        //验证码线程
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
        //学院选择列表
        mSp1 = (Spinner)this.findViewById(R.id.sp);
        //准备要加载的字符串数组资源
        logmethod = getResources().getStringArray(R.array.academy);
        /*
         * 初始化适配器时各参数
         * context: 上下文对象，当前类.this
         * resource：表示列表item的布局资源id 默认android.R.layout.simple_spinner_item,logmethod
         * object：要适配的数据资源
         * */
        adapter = new ArrayAdapter<String>(RegisterActivity.this,android.R.layout.simple_spinner_item,logmethod);
        mSp1.setAdapter(adapter);
        mSp1.setOnItemSelectedListener(new OnItemSelectedListener() {
            /*
            参数详解：
            AdapterView<?> adapterView：触发当前事件的Spinner对象
            View view表示当前备选中的item
            int i:表示当前被选中item的下表
            long l：表示当前被选中item的id
             */

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                academy = logmethod[position]; //在数据源中获取
                String s2 = adapter.getItem(position); //在适配器中获取
                Toast.makeText(RegisterActivity.this,"s1 = "+academy,Toast.LENGTH_LONG);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void getId(){
        roleGroup = (RadioGroup)findViewById(R.id.roleRadioGroup);
        edit_name=findViewById(R.id.edit_name);
        edit_email=findViewById(R.id.edit_email);
        edit_phone=findViewById(R.id.edit_phone);
        edit_code=findViewById(R.id.edit_code);
        edit_password=findViewById(R.id.edit_password);
        edit_confirm_password=findViewById(R.id.edit_confirm_password);
        btn_getCode=findViewById(R.id.btn_getcode);
        btn_register=findViewById(R.id.btn_register);
        btn_getCode.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        rb_1=findViewById(R.id.rb_1);
        rb_2=findViewById(R.id.rb_2);
        rb_1.setOnClickListener(this);
        rb_2.setOnClickListener(this);
        textView2=findViewById(R.id.textView2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    //使用Handler来分发Message对象到主线程中，处理事件
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            int event=msg.arg1;
            int result=msg.arg2;
            Object data=msg.obj;
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                if(result == SMSSDK.RESULT_COMPLETE){
                    boolean smart = (Boolean)data;
                    if(smart){
                        Toast.makeText(getApplicationContext(),"该手机号已经注册过，请重新输入", Toast.LENGTH_LONG).show();
                        edit_phone.requestFocus();
                        return;
                    }
                }
            }
            if(result==SMSSDK.RESULT_COMPLETE){
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                String name = edit_name.getText().toString();
                                String email = edit_email.getText().toString();
                                String phone = edit_phone.getText().toString();
                                String password = edit_password.getText().toString();
                                String task = textView2.getText().toString();
                                FormBody.Builder params = new FormBody.Builder();
                                params.add("name",""+name);
                                params.add("email",""+email);
                                params.add("username",""+phone);
                                params.add("password",""+password);
                                params.add("role",""+role);
                                params.add("academy",""+academy);
                                params.add("task",""+task);
                                params.add("userpic","");
                                OkHttpClient client = new OkHttpClient();//创建http客户端
                                Request request = new Request.Builder()
                                        .url(getResources().getString(R.string.Register))
                                        .post(params.build())
                                        .build();//创建http请求
                                Response response = client.newCall(request).execute();//执行发送的命令
                                String responseData = response.body().string();//获取后端返回的json数据
                                JSONObject obj = new JSONObject(responseData);
                                switch(obj.getInt("status")){
                                    case 0:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "注册成功!", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                        break;
                                    case 9:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "用户名已存在!", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    case 10:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "邮箱已存在!", Toast.LENGTH_LONG).show();
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
                                        //子线程不能直接操作UI,需要调用runOnUiThread
                                        Toast.makeText(RegisterActivity.this,"网络连接失败！",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
            else{
                if(flag){
                    btn_getCode.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"验证码获取失败请重新获取", Toast.LENGTH_LONG).show();
                    edit_phone.requestFocus();
                }
                else{
                    Toast.makeText(getApplicationContext(),"验证码输入错误", Toast.LENGTH_LONG).show();
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
            case R.id.btn_register:
                if(judCode()){
                    if(TextUtils.isEmpty(edit_password.getText().toString().trim())||TextUtils.isEmpty(edit_confirm_password.getText().toString().trim())){
                        Toast.makeText(RegisterActivity.this,"以上内容不能为空!",Toast.LENGTH_LONG).show();
                    }
                    else {
                        if(isTrue(edit_password.getText().toString())&&isTrue(edit_confirm_password.getText().toString())){
                            if(edit_password.getText().toString().equals(edit_confirm_password.getText().toString())){
                                SMSSDK.submitVerificationCode("86",phone_number,code_number);
                            }
                            else{
                                Toast.makeText(RegisterActivity.this,"密码不一致，请重新输入!",Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(RegisterActivity.this,"格式错误!",Toast.LENGTH_LONG).show();
                        }
                    }
                }
                flag = false;
                break;
            case R.id.rb_1:
                textView2.setText("每学期听课不少于2学时");
                break;
            case R.id.rb_2:
                textView2.setText("按课表要求上课");
                break;
            default:
                break;
        }
    }

    private boolean judPhone(){
        if(TextUtils.isEmpty(edit_phone.getText().toString().trim())){
            Toast.makeText(RegisterActivity.this,"请输入您的电话号码",Toast.LENGTH_LONG).show();
            edit_phone.requestFocus();
            return false;
        }
        else if(edit_phone.getText().toString().trim().length()!=11){
            Toast.makeText(RegisterActivity.this,"您的电话号码位数不正确",Toast.LENGTH_LONG).show();
            edit_phone.requestFocus();
            return false;
        }
        else{
            phone_number=edit_phone.getText().toString().trim();
            String num="[1][3578]\\d{9}";
            if(phone_number.matches(num))
                return true;
            else{
                Toast.makeText(RegisterActivity.this,"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    private boolean judCode(){
        judPhone();
        if(TextUtils.isEmpty(edit_code.getText().toString().trim())){
            Toast.makeText(RegisterActivity.this,"请输入您的验证码",Toast.LENGTH_LONG).show();
            edit_code.requestFocus();
            return false;
        }
        else if(edit_code.getText().toString().trim().length()!=4){
            Toast.makeText(RegisterActivity.this,"您的验证码位数不正确",Toast.LENGTH_LONG).show();
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