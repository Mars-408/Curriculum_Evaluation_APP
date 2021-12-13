package com.example.service.impl;

import com.example.common.ResponseCode;
import com.example.dao.S_EvaluationMapper;
import com.example.dao.StudentAssignmentMapper;
import com.example.dao.studentcommentMapper;
import com.example.pojo.S_Evaluation;
import com.example.pojo.studentcomment;
import com.example.service.S_IEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ServerResponse;

import java.util.List;

@Service
public class StuEvaSever implements S_IEvaluationService {

    @Autowired
    S_EvaluationMapper EvaluationMapper;
    @Autowired
    studentcommentMapper CommentMapper;
    @Autowired
    StudentAssignmentMapper Studentassignment;
    //提交评价
    @Override
    public ServerResponse EvaSubmit(S_Evaluation s_evaluation){
        studentcomment stucommentpojo = new studentcomment();
        stucommentpojo.setComment(s_evaluation.getSubjectiveEvaluation());
        stucommentpojo.setTeacherId(s_evaluation.getTeacherId());
        stucommentpojo.setTeacherName(s_evaluation.getTeacherName());
        stucommentpojo.setFromStudent(s_evaluation.getFromStudentId());
        stucommentpojo.setCourseId(s_evaluation.getCourceId());
        stucommentpojo.setCourseName(s_evaluation.getCourceName());
        int result = EvaluationMapper.insert(s_evaluation);
        int commentresult = CommentMapper.insert(stucommentpojo);
        if(result==0){
            return ServerResponse.createServerResponseByFail(ResponseCode.DB_insert_Error.getCode(),ResponseCode.DB_insert_Error.getMsg());
        }
        if(commentresult==0){
            return ServerResponse.createServerResponseByFail(ResponseCode.DB_insert_Error.getCode(),ResponseCode.DB_insert_Error.getMsg());
        }
        Studentassignment.setStatusOK(s_evaluation.getFromStudentId(),s_evaluation.getCourceId());
        return ServerResponse.createServerResponseBySuccess();
    }
    /**
     * 老师获取来自学生的评价
     */
    @Override
    public ServerResponse GainComment(String teacherid){
        List list = CommentMapper.selectteacher(teacherid);
        return ServerResponse.createServerResponseBySuccess(list);
    }
    /**
     * 老师获取来自学生的评价 by courseid
     */
    @Override
    public ServerResponse GainCommentByCourseId(String CourseId){
        List list = CommentMapper.selectcourseid(CourseId);
        return ServerResponse.createServerResponseBySuccess(list);
    }
    /**
     * 根据学生ID获取所有课程评价情况
     */
    @Override
    public ServerResponse GetCourseList(String studentId){
        List list = Studentassignment.GetAllCourse(studentId);
        return ServerResponse.createServerResponseBySuccess(list);
    }
}
