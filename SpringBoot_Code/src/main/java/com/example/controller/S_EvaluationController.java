package com.example.controller;

import com.example.common.Const;
import com.example.pojo.S_Evaluation;
import com.example.pojo.User;
import com.example.service.S_IEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.ServerResponse;

import javax.servlet.http.HttpSession;
@RestController
public class S_EvaluationController {

    @Autowired
    S_IEvaluationService StuEvaService;

    /**
     * 提交评价
     */
    @RequestMapping(value = "/portal/stuevaluation/submit.do")
    public ServerResponse ClientSubmit(S_Evaluation s_evaluation){
        //直接写入数据库 注意:此功能不对内容进行判断
        ServerResponse serverresponse =StuEvaService.EvaSubmit(s_evaluation);
        return serverresponse;
    }
    /**
     * 获取来自学生的语言评论_以用户名获取
     */
    @RequestMapping(value = "/portal/stuevaluation/gain.do")
    public ServerResponse gaincommentformstudent(HttpSession session){
        User userInfo = (User)session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverresponse =StuEvaService.GainComment(userInfo.getUsername());
        return serverresponse;
    }

    /**
     * 获取来自学生的语言评论_以课程id获取
     */
    @RequestMapping(value = "/portal/stuevaluation/gainbycourseid.do")
    public ServerResponse gaincommentformstudent(String courseid){
        ServerResponse serverresponse =StuEvaService.GainCommentByCourseId(courseid);
        return serverresponse;
    }
    /**
     * 根据学生ID获取所有课程评价情况
     */
    @RequestMapping(value = "/portal/stuevaluation/getcourselist.do")
    public ServerResponse GetCourseList(String studentId){
        ServerResponse serverResponse = StuEvaService.GetCourseList(studentId);
        return serverResponse;
    }


}
