package com.example.myapplication.ui.TeacherProof;

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
import com.example.myapplication.ui.TeacherProof.CardDisplay;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<com.example.myapplication.ui.TeacherProof.RecyclerViewAdapter.MyViewHolder> {
    private com.example.myapplication.ui.TeacherProof.RecyclerViewAdapter.OnItemClickListener onItemClickListener;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private List<CardDisplay> dataSource;

    private int addDataPosition = -1;

    public void setOnItemClickListener(com.example.myapplication.ui.TeacherProof.RecyclerViewAdapter.OnItemClickListener onItemClickListener) {
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
    public com.example.myapplication.ui.TeacherProof.RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new com.example.myapplication.ui.TeacherProof.RecyclerViewAdapter.MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.select_teaproof_item_view, parent, false));
    }

    /**
     * 通过 ViewHolder 来绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull com.example.myapplication.ui.TeacherProof.RecyclerViewAdapter.MyViewHolder holder, final int position) {
        holder.mTv_teacherName.setText(dataSource.get(position).getTeacherName());
        holder.mTv_courseName.setText(dataSource.get(position).getCourseName());
        holder.mTv_listened_teacherName.setText(dataSource.get(position).getListened_teacherName());
        holder.mTv_Date.setText(dataSource.get(position).getDate());

        /**
         * 只在瀑布流布局中使用随机高度
         */
        if (mRecyclerView.getLayoutManager().getClass() == StaggeredGridLayoutManager.class) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getRandomHeight());
            holder.mTv_teacherName.setLayoutParams(params);
            holder.mTv_courseName.setLayoutParams(params);
            holder.mTv_listened_teacherName.setLayoutParams(params);
            holder.mTv_Date.setLayoutParams(params);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.mTv_teacherName.setLayoutParams(params);
            holder.mTv_courseName.setLayoutParams(params);
            holder.mTv_listened_teacherName.setLayoutParams(params);
            holder.mTv_Date.setLayoutParams(params);
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

        private TextView mTv_teacherName;
        private TextView mTv_courseName;
        private TextView mTv_listened_teacherName;
        private TextView mTv_Date;
        private View mItemView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mTv_teacherName = itemView.findViewById(R.id.tv_teacherline);
            mTv_courseName = itemView.findViewById(R.id.tv_courseline);
            mTv_listened_teacherName = itemView.findViewById(R.id.tv_listenedTea_line);
            mTv_Date = itemView.findViewById(R.id.tv_dateline);
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
