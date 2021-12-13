package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.Sampler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * author:fengyujie
 * e-mail:2280184535@qq.com
 * time :2020/07/03
 * desc :写日记
 * version:
 * 注意 使用的公用图床 请注意隐私
 */
public class WriteDiaryActivity extends BaseActivity {
    private Context context;
    EditText etWrite;
    GridView gv;
    private List<String> list = new ArrayList<String>();//存储选中图片的url

    private final int MAX_PHOTO = 10;//最多20张图
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 2;
    private WriteDairyAdapter itemAdapter;//适配器


    private String imgPath;
    private int RESULT_LOAD_IMG=1;
    // 拍照的requestCode
    private static final int CAMERA_REQUEST_CODE = 0x00000010;
    // 申请相机权限的requestCode
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000012;
    /**
     * 用于保存拍照图片的uri
     */
    private Uri mCameraUri;

    /**
     * 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
     */
    private String mCameraImagePath;
    private int count=0;

    private String URL_= "";

    /**
     *  是否是Android 10以上手机
     */
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    private List<CharSequence> eduList = null;
    private ArrayAdapter<CharSequence> eduAdapter = null;
    private Spinner eduSpinner= null;
    private List<CharSequence> eduList2 = null;
    private List<String> URL_TOSQL;
    private ArrayAdapter<CharSequence> eduAdapter2 = null;
    private Spinner eduSpinner2= null;
    private Button  bt1=null;
    private Button  bt2=null;
    private Button  bt3=null;
    private Button  bt4=null;
    private Button  bt5=null;
    private Button  bt6=null;
    private Button  bt7=null;
    private ScrollView  sv=null;

    int Value_1 = -1;
    int Value_2 = -1;
    int Value_3 = -1;
    int Value_4 = -1;
    int Value_5 = -1;
    int Value_6 = -1;
    String Value;

    String courceid = "";
    String courcename = "";
    String teacherid = "";
    String listenedteachername = "";
    String fromteacher = "";
    String classpic = "";
    String from_teacherid="";
    String academy="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);

        URL_ = getResources().getString(R.string.Write_Diary);

        verifyStoragePermissions(this);
        initView();
        URL_TOSQL = new ArrayList<>();
        //接收值
        Intent intent = getIntent();
        courceid=intent.getStringExtra("courceid");
        courcename=intent.getStringExtra("courcename");
        teacherid=intent.getStringExtra("teacherid");
        listenedteachername=intent.getStringExtra("listenedteachername");
        fromteacher=intent.getStringExtra("fromteacher");
        from_teacherid = intent.getStringExtra("from_teacher_id");
        academy = intent.getStringExtra("academy");
        //按钮2
        bt2 = (Button) super.findViewById(R.id.part31);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WriteDiaryActivity.this);
                AlertDialog explain = builder.create();
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
        bt3 = (Button) super.findViewById(R.id.part41);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WriteDiaryActivity.this);
                AlertDialog explain = builder.create();
                // 设置对话框标题
                explain.setTitle("教学方法");
                // 设置对话框消息
                explain.setMessage("语言清晰准确，\n" +
                        "重点难点突出，\n" +
                        "讲授有条理\n" +
                        "学生有兴趣听课");
                // 显示对话框
                explain.show();
            }
        });
        //按钮4
        bt4 = (Button) super.findViewById(R.id.part51);
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WriteDiaryActivity.this);
                AlertDialog explain = builder.create();
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
        bt5 = (Button) super.findViewById(R.id.part61);
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WriteDiaryActivity.this);
                AlertDialog explain = builder.create();
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
        bt6 = (Button) super.findViewById(R.id.part71);
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WriteDiaryActivity.this);
                AlertDialog explain = builder.create();
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
                    Toast.makeText(WriteDiaryActivity.this, "评价1 分数不合理", Toast.LENGTH_SHORT).show();
                } else if (Value_2 < 0 || Value_2 > 15) {
                    Toast.makeText(WriteDiaryActivity.this, "评价2 分数不合理", Toast.LENGTH_SHORT).show();
                } else if (Value_3 < 0 || Value_3 > 15) {
                    Toast.makeText(WriteDiaryActivity.this, "评价3 分数不合理", Toast.LENGTH_SHORT).show();
                } else if (Value_4 < 0 || Value_4 > 15) {
                    Toast.makeText(WriteDiaryActivity.this, "评价4 分数不合理", Toast.LENGTH_SHORT).show();
                } else if (Value_5 < 0 || Value_5 > 15) {
                    Toast.makeText(WriteDiaryActivity.this, "评价5 分数不合理", Toast.LENGTH_SHORT).show();
                } else if (Value_6 < 0 || Value_6 > 25) {
                    Toast.makeText(WriteDiaryActivity.this, "评价6 分数不合理", Toast.LENGTH_SHORT).show();
                } else if (Value == null) {
                    Toast.makeText(WriteDiaryActivity.this, "请填写评价", Toast.LENGTH_SHORT).show();
                }
                //else if (list.size() == 0) {
                 //   Toast.makeText(WriteDiaryActivity.this, "请上传听课凭据", Toast.LENGTH_SHORT).show();
                //}
                else {
                    if(list.size()==1){

                        classpic="";

                        sendMail_();
                    }
                    else{
                        for(String i:list){
                            File file = new File(i);
                            if(i.equals("default"))
                                continue;
                            System.out.println(i);
                            sendPhoto_(file);
                        }
                    }

                }
            }
        });


    }
    //向图床发送照片
    private void sendPhoto_(File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();//创建http客户端
                    RequestBody fileBody = RequestBody.create(MediaType.parse("form-data"), file);
                    MultipartBody body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", "2.jpg", fileBody)
                            .build();
                    Request request = new Request.Builder()
                            .post(body)
                            .url("https://riyugo.com/file.php")
                            .build();
                    Response response_ = client.newCall(request).execute();//执行发送的命令
                    String response = response_.body().string();//获取后端返回的json数据
                    JSONObject obj = new JSONObject(response);
                    String GetUrl = obj.getString("url");
                    GetUrl = GetUrl.replace("\\", "");
                    System.out.println(GetUrl);
                    URL_TOSQL.add(GetUrl);
                    if(classpic.equals("")) {
                        classpic=classpic+GetUrl;
                    }
                    else {
                        classpic=classpic+","+GetUrl;
                    }
                    System.out.println(classpic);
                    sendMail_();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
        //向后端发送消息
        private void sendMail_(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("评价发送成功");
                        FormBody.Builder params = new FormBody.Builder();
                        params.add("courceid",""+courceid);
                        params.add("courcename",""+courcename);
                        params.add("teacherid",""+teacherid);
                        params.add("listenedteachername",""+listenedteachername);
                        params.add("evaluation1",""+ Value_1);
                        params.add("evaluation2",""+Value_2);
                        params.add("evaluation3",""+Value_3);
                        params.add("evaluation4",""+Value_4);
                        params.add("evaluation5",""+Value_5);
                        params.add("evaluation6",""+Value_6);
                        params.add("subjectiveevaluation",""+Value);
                        params.add("fromteacher",""+fromteacher);
                        params.add("classpic",""+classpic);
                        params.add("from_teacher_id",""+from_teacherid);
                        params.add("academy",""+academy);
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
    private void initView() {

        gv=findViewById(R.id.gvSelectPic);
        gv.setHorizontalSpacing(5);
        gv.setVerticalSpacing(5);


        list.add("default");

        itemAdapter = new WriteDairyAdapter(list,WriteDiaryActivity.this);

        gv.setAdapter(itemAdapter);//添加适配器;
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                     if(position==list.size()-1)
                    initDialogGrade();
                    else{
                        LayoutInflater inflater1=LayoutInflater.from(WriteDiaryActivity.this);
                        View view2=inflater1.inflate(R.layout.delete,null);

                        TextView tv2=view2.findViewById(R.id.tv2);
                        TextView tv3=view2.findViewById(R.id.tv3);
                        final Dialog dialog=new Dialog(WriteDiaryActivity.this);
                        dialog.setContentView(view2);
//                dialog.create();
//                任何区域关闭dialog
                        dialog.setCanceledOnTouchOutside(true);

//                dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_white_bg);
                        dialog.show();
                        Window window=dialog.getWindow();

                        window.setGravity(Gravity.BOTTOM);
                        window.getDecorView().setPadding(10,10,10,10);
                        WindowManager manager=getWindowManager();
//                获取屏幕的宽高
                        Display display= manager.getDefaultDisplay();
                        WindowManager.LayoutParams p=dialog.getWindow().getAttributes();
                        p.width= display.getWidth();
                        dialog.getWindow().setAttributes(p);
                        tv2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                list.remove(position - 1);
                                refreshAdapter();
                                dialog.dismiss();


                            }
                        });
                        tv3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        });

                }

            }
        });



    }
//    public void WriteClick(View view){
//        switch (view.getId()){
//            case R.id.ivBack:
//                onBackPressed();
//                break;
//            case R.id.ivPhotoAlbum:
//                initDialogGrade();
//                getSystemService(Context.INPUT_METHOD_SERVICE);
//                break;
//            case R.id.btnCreate:
//                break;
//        }
//    }
    private void initDialogGrade() {

        LayoutInflater inflater=LayoutInflater.from(WriteDiaryActivity.this);
        View view=inflater.inflate(R.layout.selelct_photo_dialog,null);
     
        TextView tv2=view.findViewById(R.id.tv2);
        TextView tv3=view.findViewById(R.id.tv3);

//                点击默认图片


//                view.setPadding(10,10,10,10);
        final Dialog dialog=new Dialog(WriteDiaryActivity.this);
        dialog.setContentView(view);
//                dialog.create();
//                任何区域关闭dialog
        dialog.setCanceledOnTouchOutside(true);

//                dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_white_bg);
        dialog.show();
        Window window=dialog.getWindow();

        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(10,10,10,10);
        WindowManager manager=getWindowManager();
//                获取屏幕的宽高
        Display display= manager.getDefaultDisplay();
        WindowManager.LayoutParams p=dialog.getWindow().getAttributes();
        p.width= display.getWidth();
        dialog.getWindow().setAttributes(p);
//                打开拍照
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                count++;

                dialog.dismiss();


            }
        });
//                打开相册
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pickPhoto();
//                count++;
//                startActivity(new Intent(WriteDiaryActivity.this,SelectAlbumActivity.class));
                Intent intent=new Intent(WriteDiaryActivity.this,SelectAlbumActivity.class);
                startActivityForResult(intent,RESULT_LOAD_IMG);
                dialog.dismiss();
            }
        });
    }

    //拍照
    private void takePhoto() {

        int hasCameraPermission = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.CAMERA);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {
            //有权限，调起相机拍照。
            openCamera();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},
                    PERMISSION_CAMERA_REQUEST_CODE);
        }
    }


    //    相册
    private void pickPhoto() {
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,RESULT_LOAD_IMG);
    }


    //当图片被选中的返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {

                if (list.size() == MAX_PHOTO) {

                    removeItem();
                    refreshAdapter();
                    return;
                }
                removeItem();

                ArrayList<String> arrayList = data.getStringArrayListExtra("selectPic");
                for (int i=0;i<arrayList.size();i++){
                    list.add(arrayList.get(i));
                }
                refreshAdapter();


                list.add("default");
                refreshAdapter();
            }
        }catch (Exception ex){
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }


        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (isAndroidQ) {
                    // Android 10 使用图片uri加载
//                    ivHeadPic.setImageURI(mCameraUri);
                    removeItem();
                    list.add(com.example.myshiyan.UriUtils.getFileAbsolutePath(itemAdapter.context,mCameraUri));
                    System.out.println("111"+mCameraUri.getPath());
                    list.add("default");
                    refreshAdapter();
                } else {
                    // 使用图片路径加载
                    removeItem();
                    list.add(mCameraImagePath);
                    System.out.println("222"+mCameraImagePath);
                    list.add("default");
                    refreshAdapter();
//                    ivHeadPic.setImageBitmap(BitmapFactory.decodeFile(mCameraImagePath));
                }
            } else {
                Toast.makeText(this,"取消",Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 调起相机拍照
     */
    private void openCamera() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断是否有相机
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            Uri photoUri = null;

            if (isAndroidQ) {
                // 适配android 10
                photoUri = createImageUri();
            } else {
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (photoFile != null) {
                    mCameraImagePath = photoFile.getAbsolutePath();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                        photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                    } else {
                        photoUri = Uri.fromFile(photoFile);
                    }
                }
            }


            if (photoUri != null) {

                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, CAMERA_REQUEST_CODE);
            }
            mCameraUri = photoUri;
        }
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     *
     * @return 图片的uri
     */
    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * 创建保存图片的文件
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String imageName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }
    /**
     * 处理权限申请的回调。
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //允许权限，有调起相机拍照。
                openCamera();
            } else {
                //拒绝权限，弹出提示框。
                Toast.makeText(this,"拍照权限被拒绝",Toast.LENGTH_LONG).show();
            }
        }
    }
    /**
     *     * 刷新视图
     * <p>
     *    
     */

    private void refreshAdapter() {

        if (list == null) {

            list = new ArrayList<String>();

        }

        if (itemAdapter == null) {

            itemAdapter = new WriteDairyAdapter(list, WriteDiaryActivity.this);

        }

        if (list.size() == MAX_PHOTO) {

            list.remove(list.size() - 1);

        }

        itemAdapter.notifyDataSetChanged();

    }

    private void removeItem() {

        if (list.size() - 1 != MAX_PHOTO) {

            if (list.size() != 0) {//删除默认图片

                list.remove(list.size() - 1);

            }

        }

    }

    //先定义
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    //然后通过一个函数来申请
    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
