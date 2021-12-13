package com.example.service.impl;

import com.example.common.ResponseCode;
import com.example.dao.TeaAssignmentListMapper;
import com.example.dao.TeaAssignmentMapper;
import com.example.pojo.TeaAssignment;
import com.example.pojo.TeaAssignmentList;
import com.example.service.ITeaCourseSever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.RemoveSame;
import utils.ServerResponse;

import java.util.List;

@Service
public class TeaCourseSever implements ITeaCourseSever {
    @Autowired
    TeaAssignmentListMapper List_teaassignmentmapper;
    @Autowired
    TeaAssignmentMapper Pool_teaassignmentmapper;
    /**
     * 获取课程池中所有课程
     */
    @Override
    public ServerResponse GetAllCourse(String courseAcademy){
        List list = Pool_teaassignmentmapper.GetAllCourse(courseAcademy);
        RemoveSame.ToRemove(list);
        return ServerResponse.createServerResponseBySuccess(list);
    }
    /**
     * 获取该课程下的老师
     */
    @Override
    public ServerResponse GetTeacherByCourse(String Coursename){
        List list = Pool_teaassignmentmapper.GetTeacherByCourse(Coursename);
        RemoveSame.ToRemove(list);
        return ServerResponse.createServerResponseBySuccess(list);
    }
    /**
     * 获取该课程的听课时间 以及 其他详情
     */
    @Override
    public ServerResponse GetDetailByCourse(String Courseteacher,String Coursename){
        List<TeaAssignment> detail = Pool_teaassignmentmapper.GetDetailByCourse(Courseteacher,Coursename);
        return ServerResponse.createServerResponseBySuccess(detail);
    }
    /**
     * 将本课程加入到老师课表中
     */
    @Override
    public ServerResponse AddList(String TeacherName,String TeacherID,TeaAssignment Assignment){
        TeaAssignmentList ListInsert = new TeaAssignmentList();
        ListInsert.setTeacherName(TeacherName);
        ListInsert.setTeacherId(TeacherID);

        ListInsert.setCourseName(Assignment.getCourseName());
        ListInsert.setCourseTime(Assignment.getCourseTime());
        ListInsert.setCourseId(Assignment.getCourseId());
        ListInsert.setCourseTeacherName(Assignment.getCourseTeacher());
        ListInsert.setCourseTeacherId(Assignment.getCourseTeacherId());
        ListInsert.setCourseAcademy(Assignment.getCourseAcademy());
        ListInsert.setCourseLocal(Assignment.getCourseLocal());
        ListInsert.setStatus(0);
        ListInsert.setSemester(Assignment.getSemester());
        int result = List_teaassignmentmapper.insert(ListInsert);
        if(result == 0){
            //数据库注入失败
            return ServerResponse.createServerResponseByFail(ResponseCode.DB_insert_Error.getCode(),ResponseCode.DB_insert_Error.getMsg());
        }
        return ServerResponse.createServerResponseBySuccess();
    }



    /**
     * 获取某老师的所有课表
     */
    @Override
    public ServerResponse GetCourseList(String TeacherId){
        List list = List_teaassignmentmapper.GetCourseByTeacher(TeacherId);
        return ServerResponse.createServerResponseBySuccess(list);
    }
    /**
     * 删除某老师的任务
     */
    @Override
    public ServerResponse DeletList(TeaAssignmentList Delete){
        String CourseId = Delete.getCourseId();
        String TeacherId=Delete.getTeacherId();
        int result = List_teaassignmentmapper.DeleteList(CourseId, TeacherId);
        if(result == 0){
            //数据库注入失败
            return ServerResponse.createServerResponseByFail(ResponseCode.DB_insert_Error.getCode(),ResponseCode.DB_insert_Error.getMsg());
        }
        return ServerResponse.createServerResponseBySuccess();
    }
    /**
     * 授课者查看自己的所有课程
     */
    @Override
    public ServerResponse MyCourse(String teacherId){
        List list = Pool_teaassignmentmapper.MyCourse(teacherId);
        return ServerResponse.createServerResponseBySuccess(list);
    }
    /**
     * 修改任务池里的某任务
     */
    @Override
    public ServerResponse AlterAssignment(String courseid,TeaAssignment newinf){
        int result = Pool_teaassignmentmapper.update(courseid,newinf);
        if(result == 0){
            //数据库注入失败
            return ServerResponse.createServerResponseByFail(ResponseCode.DB_insert_Error.getCode(),ResponseCode.DB_insert_Error.getMsg());
        }
        return ServerResponse.createServerResponseBySuccess();
    }
}
