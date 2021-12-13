package com.example.myapplication;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.constraintlayout.widget.ConstraintLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class WriteDairyAdapter extends BaseAdapter {
    private List<String> list = new ArrayList<String>();//图片url列表

    public Context context;

    private static int MAX_SINGLE_LINE =3;//每行显示4张图片
    ImageLoader imageLoader = ImageLoader.getInstance();

    public WriteDairyAdapter(List<String> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @Override

    public int getCount() {

        return list.size();

    }

    @Override

    public Object getItem(int position) {

        return list.get(position);

    }

    @Override

    public long getItemId(int position) {

        return position;

    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        MyHolderView viewholder = null;
        Log.i("adapter---->",list.toString());

        if (convertView == null) {

            convertView = View.inflate(context, R.layout.activity_write_dairy_item, null);
            viewholder = new MyHolderView();


            viewholder.iv_thum = convertView.findViewById(R.id.iv_thum);

            convertView.setTag(viewholder);

        } else {

            viewholder = (MyHolderView) convertView.getTag();

        }
        if (list.get(position).equals("default")) {//添加默认图片

//            int width = WindowSize.getWidth(context);
//            int heigth = 0;
//            width = (width-30)/ 3;
//            heigth = width+20;
//            viewholder.iv_thum.setLayoutParams(new LinearLayout.LayoutParams(width, heigth));
            System.out.println(position+"那里");
            viewholder.iv_thum.setImageResource(R.mipmap.add_icon);


        } else {

//            int width = WindowSize.getWidth(context);
//            int heigth = 0;
//            width = width / 3;
//            heigth = width;
//            viewholder.iv_thum.setLayoutParams(new LinearLayout.LayoutParams(width, heigth));
            System.out.println(position+"这里");
//Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(ImageUri));
            Bitmap loacalBitmap = LoadUrl(list.get(position));
            Log.i("adapter------->url",list.get(position));
            if (loacalBitmap != null) {
                viewholder.iv_thum.setImageBitmap(loacalBitmap);

            }

        }

        return convertView;

    }

//读取图片url

    public static Bitmap LoadUrl(String url) {

        try {

            FileInputStream fis = new FileInputStream(url);

            return BitmapFactory.decodeStream(fis);

        } catch (FileNotFoundException e) {

            e.printStackTrace();

            return null;

        }

    }

    class MyHolderView {
        ImageView iv_thum;

    }
}
