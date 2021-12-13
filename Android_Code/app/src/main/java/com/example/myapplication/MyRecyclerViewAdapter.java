package com.example.myapplication;

import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.myapplication.startUI.RegisterActivity;
import com.example.myapplication.ui.dashboard.Card_Detail_Dashboard;
import com.example.myapplication.ui.dashboard.DashboardFragment;

/*
 *  项目名:    RecyclerViewDemo
 *  包名:      com.zjgsu.recyclerviewdemo
 *  文件名:    MyRecyclerViewAdapter
 *  创建者:    Geely
 *  创建时间:  2020/10/12 19:07
 *  描述:       1、继承 RecyclerView.Adapter
 *              2、绑定 ViewHolder
 *              3、实现 Adapter 的相关方法
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private OnItemClickListener onItemClickListener;

    private RecyclerView mRecyclerView;
    private Context mContext;
    private List<Card_Detail_Dashboard> dataSource;


    private int addDataPosition = -1;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MyRecyclerViewAdapter(Context mContext, RecyclerView recyclerView) {
        this.mContext = mContext;
        this.dataSource = new ArrayList<>();
        this.mRecyclerView = recyclerView;
    }

    public void setDataSource(List<Card_Detail_Dashboard> dataSource) {
        this.dataSource = dataSource;
        notifyDataSetChanged();
    }

    /**
     * 创建并且返回 ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.activity_item, parent, false));
    }

    /**
     * 通过 ViewHolder 来绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //for(int i=0;i<2;i++){
            holder.tv4.setText(this.dataSource.get(position).getCourseName());
            holder.tv3.setText(this.dataSource.get(position).getCourseTeacherName());
            holder.tv1.setText(this.dataSource.get(position).getCourseLocal());
            holder.tv2.setText(this.dataSource.get(position).getCourseTime());
            if(this.dataSource.get(position).getStatus().equals("1")){
                //已评价
                holder.btn_pj.setText("已评价");
                holder.btn_pj.setEnabled(false);
            }
            else{
                holder.btn_pj.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                });
            }

        //}

    }

    /**
     * 返回数据数量
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    /**
     * 返回不同的 ItemView 的高度
     * @return
     */
    private int getRandomHeight() {
        return (int) (Math.random() * 1000);
    }

    /**
     * 添加一条数据
     * @param position
     */
    public void addData(int position,Card_Detail_Dashboard in) {
        addDataPosition = position;
        dataSource.add(position, in);
        notifyItemInserted(position);

        notifyItemRangeChanged(position, dataSource.size() - position);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv1;
        private TextView tv2;
        private TextView tv3;
        private TextView tv4;
        private Button btn_pj;
        private View mItemView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            tv4 = itemView.findViewById(R.id.tv4);
            btn_pj = itemView.findViewById(R.id.btn_pj);
            mItemView = itemView;
        }
    }

    /**
     * ItemView 点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
