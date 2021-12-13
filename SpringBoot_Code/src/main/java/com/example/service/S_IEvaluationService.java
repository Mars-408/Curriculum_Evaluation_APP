package com.example.service;

import com.example.pojo.S_Evaluation;
import utils.ServerResponse;

public interface S_IEvaluationService {
    /**
     * 提交评价
     */
    public ServerResponse EvaSubmit(S_Evaluation s_evaluation);

    /**
     * 获取来自学生的评价
     */
    public ServerResponse GainComment(String teacherid);
    /**
     * 获取来自学生的评价 通过课程ID
     */
    public ServerResponse GainCommentByCourseId(String CourseId);
    /**
     * 根据学生ID获取所有课程评价情况
     */
    public ServerResponse GetCourseList(String studentId);
}
