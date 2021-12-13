package com.example.myapplication.ui.WriteDiary_StuActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Stu_SriteDiary extends AppCompatActivity {
    private List<CharSequence> eduList = null;
    private ArrayAdapter<CharSequence> eduAdapter = null;
    private Spinner eduSpinner= null;
    private List<CharSequence> eduList2 = null;
    private ArrayAdapter<CharSequence> eduAdapter2 = null;
    private Spinner eduSpinner2= null;
    private Button bt1=null;
    private Button  bt2=null;
    private Button  bt3=null;
    private Button  bt4=null;
    private Button  bt5=null;
    private Button  bt6=null;
    private Button  bt7=null;

    String courceid = "";
    String courcename = "";
    String teacherid = "";
    String listenedteachername = "";
    String fromteacher = "";
    String classpic = "";
    String from_teacherid="";
    String academy="";

    int Value_1 = -1;
    int Value_2 = -1;
    int Value_3 = -1;
    int Value_4 = -1;
    int Value_5 = -1;
    int Value_6 = -1;
    String Value;

    String URL_="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu__srite_diary);

        URL_ = getResources().getString(R.string.Submit_Sut_Diary);
        //接收值
        Intent intent = getIntent();
        courceid=intent.getStringExtra("courceid");
        courcename=intent.getStringExtra("courcename");
        teacherid=intent.getStringExtra("teacherid");
        listenedteachername=intent.getStringExtra("listenedteachername");
        fromteacher=intent.getStringExtra("fromteacher");
        from_teacherid = intent.getStringExtra("from_teacher_id");
        academy = intent.getStringExtra("academy");


        //按钮1
        bt1 = (Button)super.findViewById(R.id.part21);
        bt1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(Stu_SriteDiary.this);
                AlertDialog  explain = builder.create();
                // 设置对话框标题
                explain.setTitle("教学态度");
                // 设置对话框消息
                explain.setMessage("工作认真有责任心。\n" +
                        "教学内容熟练。\n" +
                        "为人师表");
                // 显示对话框
                explain.show();
            }
        });
        //按钮2
        bt2 = (Button)super.findViewById(R.id.part31);
        bt2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(Stu_SriteDiary.this);
                AlertDialog  explain = builder.create();
                // 设置对话框标题
                explain.setTitle("教学内容");
                // 设置对话框消息
                explain.setMessage("教学目标明确，\n" +
                        "内容丰富，\n" +
                        "设计科学合理");
                // 显示对话框
                explain.show();
            }
        });
        //按钮3
        bt3 = (Button)super.findViewById(R.id.part41);
        bt3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(Stu_SriteDiary.this);
                AlertDialog  explain = builder.create();
                // 设置对话框标题
                explain.setTitle("教学方法");
                // 设置对话框消息
                explain.setMessage("语言清晰准确，\n" +
                        "重点难点突出，\n" +
                        "讲授有条理\n"+
                        "学生有兴趣听课");
                // 显示对话框
                explain.show();
            }
        });
        //按钮4
        bt4 = (Button)super.findViewById(R.id.part51);
        bt4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(Stu_SriteDiary.this);
                AlertDialog  explain = builder.create();
                // 设置对话框标题
                explain.setTitle("教学效果");
                // 设置对话框消息
                explain.setMessage("学生能理解并掌握主要教学内容，\n" +
                        "学习收获良好");
                // 显示对话框
                explain.show();
            }
        });
        //按钮5
        bt5 = (Button)super.findViewById(R.id.part61);
        bt5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(Stu_SriteDiary.this);
                AlertDialog  explain = builder.create();
                // 设置对话框标题
                explain.setTitle("课堂情况");
                // 设置对话框消息
                explain.setMessage("学生教师互相交流，\n" +
                        "学生课堂主动性高");
                // 显示对话框
                explain.show();
            }
        });
        //按钮6
        bt6 = (Button)super.findViewById(R.id.part71);
        bt6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(Stu_SriteDiary.this);
                AlertDialog  explain = builder.create();
                // 设置对话框标题
                explain.setTitle("总体评价");
                // 设置对话框消息
                explain.setMessage("对教师总体印象，\n" +
                        "（与本学期其他教师相比较）");
                // 显示对话框
                explain.show();
            }
        });
        //提交
        bt7 = (Button) super.findViewById(R.id.button6);
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et_age = (EditText) findViewById(R.id.part25);
                Value_1 = Integer.parseInt(et_age.getText().toString());
                et_age = (EditText) findViewById(R.id.part35);
                Value_2 = Integer.parseInt(et_age.getText().toString());
                et_age = (EditText) findViewById(R.id.part45);
                Value_3 = Integer.parseInt(et_age.getText().toString());
                et_age = (EditText) findViewById(R.id.part55);
                Value_4 = Integer.parseInt(et_age.getText().toString());
                et_age = (EditText) findViewById(R.id.part65);
                Value_5 = Integer.parseInt(et_age.getText().toString());
                et_age = (EditText) findViewById(R.id.part75);
                Value_6 = Integer.parseInt(et_age.getText().toString());
                et_age = (EditText) findViewById(R.id.part83);
                Value = et_age.getText().toString();
                if (Value_1 < 0 || Value_1 > 15) {
                    Toast.makeText(Stu_SriteDiary.this, "评价1 分数不合理", Toast.LENGTH_SHORT).show();
                } else if (Value_2 < 0 || Value_2 > 15) {
                    Toast.makeText(Stu_SriteDiary.this, "评价2 分数不合理", Toast.LENGTH_SHORT).show();
                } else if (Value_3 < 0 || Value_3 > 15) {
                    Toast.makeText(Stu_SriteDiary.this, "评价3 分数不合理", Toast.LENGTH_SHORT).show();
                } else if (Value_4 < 0 || Value_4 > 15) {
                    Toast.makeText(Stu_SriteDiary.this, "评价4 分数不合理", Toast.LENGTH_SHORT).show();
                } else if (Value_5 < 0 || Value_5 > 15) {
                    Toast.makeText(Stu_SriteDiary.this, "评价5 分数不合理", Toast.LENGTH_SHORT).show();
                } else if (Value_6 < 0 || Value_6 > 25) {
                    Toast.makeText(Stu_SriteDiary.this, "评价6 分数不合理", Toast.LENGTH_SHORT).show();
                } else if (Value == null) {
                    Toast.makeText(Stu_SriteDiary.this, "请填写评价", Toast.LENGTH_SHORT).show();
                }
                else {
                        sendMail_();
                    }
            }
        });
    }
    //向后端发送消息
    private void sendMail_(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("评价发送成功");
                    FormBody.Builder params = new FormBody.Builder();
                    params.add("courceId",""+courceid);
                    params.add("courceName",""+courcename);
                    params.add("teacherId",""+teacherid);
                    params.add("teacherName",""+listenedteachername);
                    params.add("evaluation1",""+ Value_1);
                    params.add("evaluation2",""+Value_2);
                    params.add("evaluation3",""+Value_3);
                    params.add("evaluation4",""+Value_4);
                    params.add("evaluation5",""+Value_5);
                    params.add("evaluation6",""+Value_6);
                    params.add("subjectiveEvaluation",""+Value);
                    params.add("formStudentName",""+fromteacher);
                    params.add("fromStudentId",""+from_teacherid);
                    System.out.println(academy);
                    OkHttpClient client = new OkHttpClient();//创建http客户端
                    Request request = new Request.Builder()
                            .url(URL_)
                            .post(params.build())
                            .build();//创建http请求
                    Response response = client.newCall(request).execute();//执行发送的命令
                    String responseData = response.body().string();//获取后端返回的json数据
                    /**
                     *  关闭本界面
                     *  数据是使用Intent返回
                     *  刷新Fragment界面 用于处理在选择课程界面返回时信息不更新
                     */
                    System.out.println("评价发送成功");
                    Intent intent = new Intent();
                    //把返回数据存入Intent
                    setResult(RESULT_OK, intent);

                    //关闭Activity

                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();



    }
}