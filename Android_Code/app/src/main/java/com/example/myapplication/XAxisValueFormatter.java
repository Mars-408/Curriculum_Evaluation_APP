package com.example.myapplication;


import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * 本类是写X轴横坐标使用
 */
public class XAxisValueFormatter implements IAxisValueFormatter {
    private String[] xStrs = new String[]{"教学态度",
            "教学内容",
            "教学方法",
            "教学效果",
            "课堂情况",
            "总体评价"};

    @Override

    public String getFormattedValue(float value, AxisBase axis) {
        int position = (int) value;
        if (position >= 6) {
            position = 0;
        }
        return xStrs[position];
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
