package com.example.controller;

import com.example.service.impl.CalculateSever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.ServerResponse;

import javax.servlet.http.HttpSession;

@RestController
public class CalculateController {
    @Autowired
    CalculateSever calculateSever;

    /**
     * 根据两个评价数据库计算老师以及学生的平均值
     * 增加：2021.4.10
     * 增加 计算所有老师的平均分 并保存在当前数据库 用teacher_id=#{AllAverage}索引
     */
    @RequestMapping(value = "/portal/calculate/begin.do")
    public ServerResponse calculate(){
        //计算功能由管理员统一开启 此功能暂时不设置条件
        //本功能使用时是先清空本数据库 再写入
        //功能：对学生的评价 以及老师的评价进行数据上的一个处理
        ServerResponse serverResponse = calculateSever.calculate();
        return serverResponse;
    }

    /**
     * 老师获取关于自己的学生评价
     */
    @RequestMapping(value = "/portal/calculate/teacherget.do")
    public ServerResponse teacherget(String courseId){
        //老师获得与自己相关的评价（评分）
        //用课程id获取
        ServerResponse serverResponse = calculateSever.teacherget(courseId);
        return serverResponse;
    }
    /**
     * 老师获取关于领导对自己的评价
     */
    @RequestMapping(value = "/portal/calculate/teachergetleader.do")
    public ServerResponse teachergetleader(String courseId){
        ServerResponse serverResponse = calculateSever.teachergetleader(courseId);
        return serverResponse;
    }
    /**
     * 清空关于计算结果的表
     */
    @RequestMapping(value = "/portal/calculate/clear.do")
    public ServerResponse clear(){
        ServerResponse serverResponse = calculateSever.clear();
        return serverResponse;
    }
    /**
     * 领导和督导团获取关于老师的学生评价
     */
    @RequestMapping(value = "/portal/calculate/leaderget.do")
    public ServerResponse leaderget(String Academy){
        //管理员获得与老师相关的评价（评分）
        //输入身份可以查看不同数量老师的评价
        ServerResponse serverResponse = calculateSever.leaderget(Academy);
        return serverResponse;
    }
}
