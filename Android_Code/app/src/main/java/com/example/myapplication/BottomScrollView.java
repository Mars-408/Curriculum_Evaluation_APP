package com.example.myapplication;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class BottomScrollView extends ScrollView {

    private OnScrollToBottomListener onScrollToBottom;
    private int contentHeight;
    private int height;

    public BottomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomScrollView(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        contentHeight = getChildAt(0).getHeight();
        height = getHeight();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        boolean isBottom = contentHeight == height + getScrollY();
        if (onScrollToBottom != null) {
            onScrollToBottom.onScrollBottomListener(isBottom);
        }
    }


    public void setOnScrollToBottomLintener(OnScrollToBottomListener listener) {
        onScrollToBottom = listener;
    }

    public interface OnScrollToBottomListener {
        public void onScrollBottomListener(boolean isBottom);
    }
}
