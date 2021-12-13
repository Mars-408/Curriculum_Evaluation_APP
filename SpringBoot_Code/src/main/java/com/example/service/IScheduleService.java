package com.example.service;

import utils.ServerResponse;

public interface IScheduleService {
    /**
     * 由学院获取课程
     */
    public ServerResponse GetCourseByAcademy(String academyname);

    /**
     * 由课程获取老师
     */
    public ServerResponse GetTeacherByCourse(String course,String academyname);


    public ServerResponse Getcourseidbyteacherid(String teacherid);
}
