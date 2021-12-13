package com.example.service;

import com.example.pojo.TeaAssignment;
import com.example.pojo.TeaAssignmentList;
import utils.ServerResponse;

public interface ITeaCourseSever {
    /**
     * 获取课程池中所有课程
     */
    public ServerResponse GetAllCourse(String courseAcademy);
    /**
     * 获取该课程下的老师
     */
    public ServerResponse GetTeacherByCourse(String Coursename);
    /**
     * 获取该课程的听课时间 以及 其他详情
     */
    public ServerResponse GetDetailByCourse(String Courseteacher,String Coursename);
    /**
     * 将本课程加入到老师课表中
     */
    public ServerResponse AddList(String TeacherName, String TeacherID, TeaAssignment Assignment);
    /**
     * 获取某老师的所有课表
     */
    public ServerResponse GetCourseList(String TeacherId);

    /**
     * 授课者查看自己的所有课程
     */
    public ServerResponse MyCourse(String teacherId);
    /**
     * 删除某老师的任务
     */
    public ServerResponse DeletList(TeaAssignmentList Delect);
    /**
     * 修改某任务
     */
    public ServerResponse AlterAssignment(String courseid,TeaAssignment newinf);
}