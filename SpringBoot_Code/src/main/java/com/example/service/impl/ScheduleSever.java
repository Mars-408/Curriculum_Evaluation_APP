package com.example.service.impl;

import com.example.dao.ScheduleMapper;
import com.example.service.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ServerResponse;

import java.util.List;

@Service
public class ScheduleSever implements IScheduleService {
    @Autowired
    ScheduleMapper schedulemapper;

    //由学院获取课程
    @Override
    public ServerResponse GetCourseByAcademy(String academyname){
        List list = schedulemapper.GetCourse(academyname);
        //此处搜索失败和课程数为0结果相同 不发送错误代码
        utils.RemoveSame.ToRemove(list);
        return ServerResponse.createServerResponseBySuccess(list);
    }
    //由课程获得老师

    @Override
    public ServerResponse GetTeacherByCourse(String Course,String Academyname){
        List list = schedulemapper.GetTeacher(Course,Academyname);
        utils.RemoveSame.ToRemove(list);
        return ServerResponse.createServerResponseBySuccess(list);
    }
    /**
     * 由教师id获取给学生授课的课程id列表
     */
    @Override
    public ServerResponse Getcourseidbyteacherid(String teacherid){
        List list = schedulemapper.Getcourseidbyteacherid(teacherid);
        return ServerResponse.createServerResponseBySuccess(list);
    }
}
