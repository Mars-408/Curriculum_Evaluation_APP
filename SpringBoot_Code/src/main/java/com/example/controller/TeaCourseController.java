package com.example.controller;

import com.example.common.Const;
import com.example.pojo.TeaAssignment;
import com.example.pojo.TeaAssignmentList;
import com.example.pojo.User;
import com.example.service.impl.TeaCourseSever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.ServerResponse;

import javax.servlet.http.HttpSession;

@RestController
public class TeaCourseController {

    @Autowired
    TeaCourseSever teacoursesever;
    /**
     * 获取课程池中所有课程
     */
    @RequestMapping(value = "/portal/teachercourse/getallname.do")
    public ServerResponse GetAllCourse(String courseAcademy){
        ServerResponse serverResponse = teacoursesever.GetAllCourse(courseAcademy);
        return serverResponse;
    }

    /**
     * 获取该课程下的老师
     */
    @RequestMapping(value = "/portal/teachercourse/getteacherbycourse.do")
    public ServerResponse GetTeacherByCourse(String coursename){
        ServerResponse serverResponse = teacoursesever.GetTeacherByCourse(coursename);
        return serverResponse;
    }

    /**
     * 获取该课程的听课时间 以及 其他详情
     */
    @RequestMapping(value = "/portal/teachercourse/getdetail.do")
    public ServerResponse GetDetailByCourse(String courseteacher,String coursename){
        ServerResponse serverResponse = teacoursesever.GetDetailByCourse(courseteacher,coursename);
        return serverResponse;
    }





    /**
     * 将选择的听课任务添加进表中
     * ！ 会出现空指针错误
     */
    @RequestMapping(value = "/portal/teachercourse/addlist.do")
    public ServerResponse AddList(String teacherId, String teacherName,TeaAssignment assignment){
        ServerResponse serverResponse = teacoursesever.AddList(teacherId,teacherName,assignment);
        return serverResponse;
    }


    /**
     * 获取某老师所有的听课任务
     */
    @RequestMapping(value = "/portal/teachercourse/getcourselist.do")
    public ServerResponse GetCourseList(String teacherId){
        ServerResponse serverResponse = teacoursesever.GetCourseList(teacherId);
        return serverResponse;
    }

    /**
     * 删除某老师的任务
     */
    @RequestMapping(value = "/portal/teachercourse/deletlist.do")
    public ServerResponse DeletList(TeaAssignmentList Delect){
        ServerResponse serverResponse = teacoursesever.DeletList(Delect);
        return serverResponse;
    }

    /**
     * 授课者查找自己所授的所有课程
     */
    @RequestMapping(value = "/portal/teachercourse/MyCourse.do")
    public ServerResponse MyCourse(String teacherId){
        ServerResponse serverResponse = teacoursesever.MyCourse(teacherId);
        return serverResponse;
    }
    /**
     * 修改授课者的课程信息
     */
    @RequestMapping(value = "/portal/teachercourse/alter.do")
    public ServerResponse AlterAssignment(String IDcourse,TeaAssignment newinf){
        ServerResponse serverResponse = teacoursesever.AlterAssignment(IDcourse,newinf);
        return serverResponse;
    }
}
