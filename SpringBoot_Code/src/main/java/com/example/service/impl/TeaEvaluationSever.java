package com.example.service.impl;

import com.example.common.ResponseCode;
import com.example.dao.TeaAssignmentListMapper;
import com.example.dao.TeacherEvaluationMapper;
import com.example.dao.TeacherProofMapper;
import com.example.dao.teachercommentMapper;
import com.example.pojo.TeaAssignmentList;
import com.example.pojo.TeacherEvaluation;
import com.example.pojo.TeacherProof;
import com.example.pojo.teachercomment;
import com.example.service.ITeaEvaluationSever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ServerResponse;

import java.util.List;

@Service
public class TeaEvaluationSever implements ITeaEvaluationSever {

    @Autowired
    TeacherEvaluationMapper TeaEvaluation;
    @Autowired
    TeacherProofMapper TeaProof;
    @Autowired
    teachercommentMapper TeaCommment;
    @Autowired
    TeaAssignmentListMapper TeaAssList;
    /**
     * 处理从前端发送的内容 写入分别写入teacherProof和TeacherEvaluation 数据库
     */
    @Override
    public ServerResponse Send(String cource_id,
                               String cource_name,
                               String teacher_id,
                               String listened_teacher_name,
                               String evaluation_1,
                               String evaluation_2,
                               String evaluation_3,
                               String evaluation_4,
                               String evaluation_5,
                               String evaluation_6,
                               String subjective_evaluation,
                               String from_teacher,
                               String from_teacher_id,
                               String classpic,
                               String academy){

        TeacherEvaluation insertEvaluation = new TeacherEvaluation();
        TeacherProof insertProot = new TeacherProof();
        teachercomment insertComment = new teachercomment();


        insertEvaluation.setCourceId(cource_id);
        insertEvaluation.setCourceName(cource_name);
        insertEvaluation.setTeacherId(teacher_id);
        insertEvaluation.setListenedTeacherName(listened_teacher_name);
        insertEvaluation.setEvaluation1(evaluation_1);
        insertEvaluation.setEvaluation2(evaluation_2);
        insertEvaluation.setEvaluation3(evaluation_3);
        insertEvaluation.setEvaluation4(evaluation_4);
        insertEvaluation.setEvaluation5(evaluation_5);
        insertEvaluation.setEvaluation6(evaluation_6);
        insertEvaluation.setSubjectiveEvaluation(subjective_evaluation);
        insertEvaluation.setFromTeacher(from_teacher);
        int result = TeaEvaluation.insert(insertEvaluation);
        if(result == 0){
            //数据库注入失败
            return ServerResponse.createServerResponseByFail(ResponseCode.DB_insert_Error.getCode(),ResponseCode.DB_insert_Error.getMsg());
        }
        insertComment.setCourseId(cource_id);
        insertComment.setCourseName(cource_name);
        insertComment.setLessonsTeacherId(teacher_id);
        insertComment.setLessonsTeacherName(listened_teacher_name);
        insertComment.setComment(subjective_evaluation);
        insertComment.setFromTeacher(from_teacher);
        result = TeaCommment.insert(insertComment);
        if(result == 0){
            //数据库注入失败
            return ServerResponse.createServerResponseByFail(ResponseCode.DB_insert_Error.getCode(),ResponseCode.DB_insert_Error.getMsg());
        }
        insertProot.setTeacherId(teacher_id);
        insertProot.setTeacherName(from_teacher);
        insertProot.setCourseId(cource_id);
        insertProot.setCourseName(cource_name);
        insertProot.setListenedTeacherName(listened_teacher_name);
        insertProot.setListenedTeacherId(teacher_id);
        insertProot.setClasspic(classpic);
        insertProot.setAcademy(academy);
        result = TeaProof.insert(insertProot);
        if(result == 0){
            //数据库注入失败
            return ServerResponse.createServerResponseByFail(ResponseCode.DB_insert_Error.getCode(),ResponseCode.DB_insert_Error.getMsg());
        }
        TeaAssList.setOK(cource_id,from_teacher_id);
        return ServerResponse.createServerResponseBySuccess();
    }

    /**
     * 获取对老师对老师 领导对老师 督导团对老师的评价
     * 由于写入本表时没有进行teacherid的写入 所以暂时以teachername查询
     * 已经修改（2021-4-12）见controll
     */
    @Override
    public ServerResponse Gaincommment(String Courseid){
        List list = TeaCommment.gaincomment(Courseid);
        return ServerResponse.createServerResponseBySuccess(list);
    }


    /**
     * 获取教师听课凭证表中的所有信息
     */
    @Override
    public ServerResponse GainTeaProof(String academy) {
        List list = TeaProof.selectByAcademy(academy);
        return ServerResponse.createServerResponseBySuccess(list);
    }

    /**
     * 获取所有教师的听课凭证
     */
    @Override
    public ServerResponse GainAllTeaProof() {
        List list = TeaProof.selectAll();
        return ServerResponse.createServerResponseBySuccess(list);
    }

    @Override
    public ServerResponse GainProofUrl(String teacherName, String courseName, String listenedTeaName, String date) {
        String url = TeaProof.getImageUrl(teacherName, courseName, listenedTeaName, date);
        return ServerResponse.createServerResponseBySuccess(url);
    }
}
