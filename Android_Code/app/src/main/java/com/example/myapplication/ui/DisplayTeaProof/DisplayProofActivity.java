package com.example.myapplication.ui.DisplayTeaProof;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DisplayProofActivity extends AppCompatActivity {

    private String getProofUrl = "";

    private String teacherName;
    private  String courseName;
    private String listenedTeaName;
    private  String date;

    private List<String> urls = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_proof);

        getProofUrl = getResources().getString(R.string.Display_Proof);

        LinearLayout img =  (LinearLayout)findViewById(R.id.imgProof);
        TextView textView = findViewById(R.id.detailProof);

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        teacherName = bundle.getString("teacherName");
        courseName = bundle.getString("courseName");
        listenedTeaName = bundle.getString("listenedTeaName");
        date = bundle.getString("date");

        textView.setText("详情如下: \n"+"听课教师: "+teacherName+"\n"+"课程名: "+courseName+"\n"+"授课教师: "+
                listenedTeaName+"\n"+"时间: "+date+"\n\n"+"听课现场状况: ");

        //在消息队列中实现对控件的更改
        Handler handle = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Bitmap bmp=(Bitmap)msg.obj;
                        final ImageView image = new ImageView(DisplayProofActivity.this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 30, 0, 0);
                        image.setImageBitmap(bmp);
                        image.setScaleType(ImageView.ScaleType.FIT_XY);//使图片充满控件大小
                        img.addView(image,params);
                        break;
                }
            }
        };


        new Thread(new Runnable() {
            @Override
            public void run() {
                getUrl();
                System.out.println(urls.size());
                try{
                    // TODO Auto-generated method stub
                    for(int i=0;i<urls.size();i++){
                        Bitmap bmp = getURLimage(urls.get(i));
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = bmp;
                        handle.sendMessage(msg);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DisplayProofActivity.this,"线程通信出现问题！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }



    /**
     * 发送HTTP请求获取图片URL
     */
    public void getUrl(){
        try{
            String url = null;
            OkHttpClient client = new OkHttpClient();//创建http客户端
            FormBody.Builder params = new FormBody.Builder();
            params.add("teacherName",""+teacherName);
            params.add("courseName",""+courseName);
            params.add("listenedTeaName",""+listenedTeaName);
            params.add("date",""+date);
            Request request = new Request.Builder()
                    .url(getProofUrl)
                    .post(params.build())
                    .build();//创建http请求
            Response response = client.newCall(request).execute();//执行发送的命令
            String responseData = response.body().string();//获取后端返回的json数据
            System.out.println(responseData);
            JSONObject obj = new JSONObject(responseData);
            //将获取的url字符串解析成装有url的字符串数组
            url = obj.getString("data");
            System.out.println(url);
            String[] strArr= url.split(",");
            for(int i=0;i<strArr.length;i++){
                urls.add(strArr[i]);
            }
        }catch(Exception e){
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(DisplayProofActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 加载图片
     */
    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("图片流出现问题！");
        }
        return bmp;
    }
}