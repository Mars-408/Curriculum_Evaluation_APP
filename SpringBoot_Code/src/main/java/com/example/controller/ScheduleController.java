package com.example.controller;

import com.example.service.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.ServerResponse;

@RestController
public class ScheduleController {

    @Autowired
    IScheduleService scheduleservice;

    /**
     * 由学院获取课程
     */
    @RequestMapping(value = "/portal/schedule/ByAcademyname.do")
    public ServerResponse GetCourseByAcademy(String academyname){
        ServerResponse serverresponse = scheduleservice.GetCourseByAcademy(academyname);
        return serverresponse;
    }

    /**
     * 由课程与学院获取老师
     */
    @RequestMapping(value = "/portal/schedule/ByCourse.do")
    public ServerResponse GetTeacherByCourse(String course,String academyname){
        ServerResponse serverresponse = scheduleservice.GetTeacherByCourse(course,academyname);
        return serverresponse;
    }


    /**
     * 由教师id获取给学生授课的课程所有的列表
     */
    @RequestMapping(value = "/portal/schedule/Getcourseidbyteacherid.do")
    public ServerResponse Getcourseidbyteacherid(String teacherid){
        ServerResponse serverresponse = scheduleservice.Getcourseidbyteacherid(teacherid);
        return serverresponse;
    }
}
