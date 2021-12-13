package com.example.service;

import utils.ServerResponse;

public interface ICalculateSever {
    /**
     * 根据两个评价数据库计算老师以及学生的平均值
     */
    public ServerResponse calculate();
    /**
     * 数据库清除 - 仅用于测试
     */
    public ServerResponse clear();
    /**
     * 老师获取关于自己的评价
     */
    public ServerResponse teacherget(String courseId);
    /**
     * 老师获取关于领导对自己的评价的分析结果
     */
    public ServerResponse teachergetleader(String courseid);


}
