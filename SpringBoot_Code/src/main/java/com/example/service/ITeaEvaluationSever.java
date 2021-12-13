package com.example.service;

import utils.ServerResponse;

public interface ITeaEvaluationSever {
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
                               String academy);

    /**
     * 查看领导对老师 老师对老师 督导团对老师的评价
     */
    public ServerResponse Gaincommment(String Courseid);


    /**
     * 根据学院获取教师的听课凭证表中的所有信息
     */
    public ServerResponse GainTeaProof(String academy);

    /**
     * 获取所有教师的听课凭证
     */
    public ServerResponse GainAllTeaProof();

    /**
     * 获取听课凭证url
     */
    public ServerResponse GainProofUrl(String teacherName,String courseName,String listenedTeaName,String date);

}
