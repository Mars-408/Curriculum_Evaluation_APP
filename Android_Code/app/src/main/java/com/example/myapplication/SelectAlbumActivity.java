package com.example.myapplication;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;



import java.util.ArrayList;

public class SelectAlbumActivity extends BaseActivity {

    private ArrayList<String> imageUrls;
    private DisplayImageOptions options;
    private ImageAdapter imageAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_image_grid);

        initData();
        initGallery();
    }

    private void initData() {
        this.imageUrls = new ArrayList<String>();//初始化；

        final String[] columns = {MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor imagecursor = this
                .getApplicationContext()
                .getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                        null, null, orderBy + " DESC");
        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            imageUrls.add(imagecursor.getString(dataColumnIndex));
            Log.i("imageUrl", imageUrls.get(i));

        }
    }

    private void initGallery() {
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.add_icon)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .cacheInMemory().cacheOnDisc().build();

        imageAdapter = new ImageAdapter(this, imageUrls);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(imageAdapter);
        // gridView.setOnItemClickListener(new OnItemClickListener() {
        // @Override
        // public void onItemClick(AdapterView<?> parent, View view,
        // int position, long id) {
        // startImageGalleryActivity(position);
        // }
        // });
    }

    @Override
    protected void onStop() {
        imageLoader.stop();
        super.onStop();
    }

    public void btnChoosePhotosClick(View v) {
        Intent intent=getIntent();
        ArrayList<String> selectedItems = imageAdapter.getCheckedItems();

        for (int i=0;i<selectedItems.size();i++){//返回被选择的图片;
            Log.i("TAg------->Select",selectedItems.get(i));
            Toast.makeText(SelectAlbumActivity.this,selectedItems.get(i)+"",Toast.LENGTH_LONG).show();
        }
        Toast.makeText(SelectAlbumActivity.this,
                "Total photos selected: " + selectedItems.size(),
                Toast.LENGTH_SHORT).show();


        intent.putExtra("selectPic",selectedItems);
        setResult(RESULT_OK,intent);
        finish();
    }
    public class ImageAdapter extends BaseAdapter {

        ArrayList<String> mList;
        LayoutInflater mInflater;
        Context mContext;
        SparseBooleanArray mSparseBooleanArray;

        public ImageAdapter(Context context, ArrayList<String> imageList) {
            // TODO Auto-generated constructor stub
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mSparseBooleanArray = new SparseBooleanArray();
            mList = new ArrayList<String>();
            this.mList = imageList;
        }

        public ArrayList<String> getCheckedItems() {
            ArrayList<String> mTempArry = new ArrayList<String>();

            for (int i = 0; i < mList.size(); i++) {
                if (mSparseBooleanArray.get(i)) {
                    mTempArry.add(mList.get(i));
                }
            }

            return mTempArry;
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.universal_image_loader,
                        null);
            }

            CheckBox mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.checkBox1);
            final ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.imageView1);
//            int width = WindowSize.getWidth(mContext);
//            int heigth = 0;
//            width = (width-30)/ 3;
//            heigth = width+20;
//            imageView.setLayoutParams(new FrameLayout.LayoutParams(width, heigth));
//            imageLoader.displayImage(imageUrls.get(position),imageView,options,);
           mCheckBox.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Toast.makeText(mContext, imageUrls.get(position),Toast.LENGTH_LONG).show();
//                   mList.add(imageUrls.get(position));
               }
           });
            imageLoader.init(ImageLoaderConfiguration.createDefault(SelectAlbumActivity.this));
            imageLoader.displayImage("file://"+imageUrls.get(position),
                    imageView, options, new SimpleImageLoadingListener() {

                        public void onLoadingComplete(Bitmap loadedImage) {
//                            Animation anim = AnimationUtils.loadAnimation(
//                                    SelectAlbumActivity.this, R.anim.fade_in);
//                            imageView.setImageBitmap(loadedImage);
//                            anim.start();
                        }
                    });

            mCheckBox.setTag(position);
            mCheckBox.setChecked(mSparseBooleanArray.get(position));
            mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);

            return convertView;
        }

        CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                mSparseBooleanArray.put((Integer) buttonView.getTag(),
                        isChecked);
            }
        };
    }

}