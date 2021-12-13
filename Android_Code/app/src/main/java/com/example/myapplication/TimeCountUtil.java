package com.example.myapplication;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.Button;

public class TimeCountUtil extends CountDownTimer {
    private Button mButton;

    public TimeCountUtil(Button button, long later, long countDownInterval){
        super(later, countDownInterval);
        this.mButton = button;
    }

    @Override
    public void onTick(long millFinished) {
        //按钮不可用
        mButton.setEnabled(false);
        mButton.setBackgroundColor(Color.parseColor("#dddddd"));
        String showText = millFinished / 1000 + "秒后可重新发送";
        mButton.setText(showText);
    }

    @Override
    public void onFinish() {
        //按钮可用
        mButton.setEnabled(true);
        mButton.setBackgroundColor(Color.parseColor("#800066ff"));
        mButton.setText("重新获取验证码");
    }
}
