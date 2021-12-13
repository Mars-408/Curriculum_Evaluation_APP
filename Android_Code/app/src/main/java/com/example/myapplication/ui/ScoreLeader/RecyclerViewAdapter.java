package com.example.myapplication.ui.ScoreLeader;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<com.example.myapplication.ui.ScoreLeader.RecyclerViewAdapter.MyViewHolder> {

    private com.example.myapplication.ui.ScoreLeader.RecyclerViewAdapter.OnItemClickListener onItemClickListener;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private List<com.example.myapplication.ui.ScoreLeader.CardDisplay> dataSource;

    private int addDataPosition = -1;

    public void setOnItemClickListener(com.example.myapplication.ui.ScoreLeader.RecyclerViewAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public RecyclerViewAdapter(Context mContext, RecyclerView recyclerView) {
        this.mContext = mContext;
        this.dataSource = new ArrayList<>();
        this.mRecyclerView = recyclerView;
    }

    public void setDataSource(List<CardDisplay> dataSource) {
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
        return new RecyclerViewAdapter.MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.select_item_view_leader, parent, false));
    }


    /**
     * 通过 ViewHolder 来绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull com.example.myapplication.ui.ScoreLeader.RecyclerViewAdapter.MyViewHolder holder, final int position) {
        holder.mTv_courseid.setText(dataSource.get(position).getCourse_id());
        holder.mTv_coursename.setText(dataSource.get(position).getCourse_name());
        holder.mTv_courseteaid.setText(dataSource.get(position).getCourse_teacher_id());
        holder.mTv_courseteaname.setText(dataSource.get(position).getCourse_teacher_name());
        holder.mTv_Academy.setText(dataSource.get(position).getAcademy());
        holder.mTv_detail.setText(dataSource.get(position).getDetail());

        /**
         * 只在瀑布流布局中使用随机高度
         */
        if (mRecyclerView.getLayoutManager().getClass() == StaggeredGridLayoutManager.class) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getRandomHeight());
            holder.mTv_courseteaid.setLayoutParams(params);
            holder.mTv_coursename.setLayoutParams(params);
            holder.mTv_courseid.setLayoutParams(params);
            holder.mTv_courseteaname.setLayoutParams(params);
            holder.mTv_Academy.setLayoutParams(params);
            holder.mTv_detail.setLayoutParams(params);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.mTv_courseteaid.setLayoutParams(params);
            holder.mTv_coursename.setLayoutParams(params);
            holder.mTv_courseid.setLayoutParams(params);
            holder.mTv_courseteaname.setLayoutParams(params);
            holder.mTv_Academy.setLayoutParams(params);
            holder.mTv_detail.setLayoutParams(params);
        }

        // 改变 ItemView 背景颜色
        if (addDataPosition == position) {
            holder.mItemView.setBackgroundColor(Color.RED);
        }

        holder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
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



    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTv_courseid;
        private TextView mTv_coursename;
        private TextView mTv_courseteaid;
        private TextView mTv_courseteaname;
        private TextView mTv_detail;
        private TextView mTv_Academy;
        private View mItemView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mTv_courseid = itemView.findViewById(R.id.tv_oneline);
            mTv_coursename = itemView.findViewById(R.id.tv_twoline);
            mTv_courseteaid = itemView.findViewById(R.id.tv_threeline);
            mTv_courseteaname = itemView.findViewById(R.id.tv_fourline);
            mTv_Academy= itemView.findViewById(R.id.tv_fiveline);
            mTv_detail = itemView.findViewById(R.id.tv_sixline);
            mItemView = itemView;
        }
    }

    /**
     * ItemView 点击事件
     */
    interface OnItemClickListener {
        void onItemClick(int position);
    }
}