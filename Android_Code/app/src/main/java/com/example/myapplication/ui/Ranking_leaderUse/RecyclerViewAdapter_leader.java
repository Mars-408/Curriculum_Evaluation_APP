package com.example.myapplication.ui.Ranking_leaderUse;

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

public class RecyclerViewAdapter_leader extends RecyclerView.Adapter<RecyclerViewAdapter_leader.MyViewHolder> {

    private OnItemClickListener onItemClickListener;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private List<CardDisplay_Leader> dataSource;

    private int addDataPosition = -1;

    public void setOnItemClickListener(RecyclerViewAdapter_leader.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public RecyclerViewAdapter_leader(Context mContext, RecyclerView recyclerView) {
        this.mContext = mContext;
        this.dataSource = new ArrayList<>();
        this.mRecyclerView = recyclerView;
    }

    public void setDataSource(List<CardDisplay_Leader> dataSource) {
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
    public RecyclerViewAdapter_leader.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewAdapter_leader.MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.ranking_display_item, parent, false));
    }

    /**
     * 通过 ViewHolder 来绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter_leader.MyViewHolder holder, final int position) {
        holder.mTv1.setText("课程名称："+dataSource.get(position).getCourse_name()+"("+dataSource.get(position).getCourse_id()+")");
        holder.mTv2.setText("任课老师:"+dataSource.get(position).getCourse_teacher_name()+"("+dataSource.get(position).getCourse_teacher_id()+")");
        holder.mTv3.setText("总分:"+dataSource.get(position).getTotalAverage());
        holder.mTv4.setText("排名:"+dataSource.get(position).getRankint());
        //holder.mTv5.setText(dataSource.get(position).getDetail());
        //holder.mTv6.setText("dataSource.get(position).getRankint()");

        /**
         * 只在瀑布流布局中使用随机高度
         */
        if (mRecyclerView.getLayoutManager().getClass() == StaggeredGridLayoutManager.class) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getRandomHeight());
            holder.mTv1.setLayoutParams(params);
            holder.mTv2.setLayoutParams(params);
            holder.mTv3.setLayoutParams(params);
            holder.mTv4.setLayoutParams(params);
            //holder.mTv5.setLayoutParams(params);
            //holder.mTv6.setLayoutParams(params);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.mTv1.setLayoutParams(params);
            holder.mTv2.setLayoutParams(params);
            holder.mTv3.setLayoutParams(params);
            holder.mTv4.setLayoutParams(params);
           // holder.mTv5.setLayoutParams(params);
            //holder.mTv6.setLayoutParams(params);
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



    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTv1;
        private TextView mTv2;
        private TextView mTv3;
        private TextView mTv4;
        //private TextView mTv5;
       // private TextView mTv6;
        private View mItemView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mTv1 = itemView.findViewById(R.id.tv_oneline);
            mTv2 = itemView.findViewById(R.id.tv_twoline);
            mTv3 = itemView.findViewById(R.id.tv_threeline);
            mTv4 = itemView.findViewById(R.id.tv_fourline);
          //  mTv5 = itemView.findViewById(R.id.tv_fiveline);
          //  mTv6 = itemView.findViewById(R.id.tv_sixline);
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
