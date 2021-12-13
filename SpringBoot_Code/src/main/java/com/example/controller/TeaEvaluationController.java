package com.example.controller;

import com.example.common.Const;
import com.example.pojo.User;
import com.example.service.ITeaEvaluationSever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.ServerResponse;

import javax.servlet.http.HttpSession;

@RestController
public class TeaEvaluationController {

    @Autowired
    ITeaEvaluationSever TeaEvaluation;

    /**
     * 提交教师评价
     * 参数说明
     * cource_id——课程id
     * cource_name--课程名
     * teacher_id——老师id
     * listened_teacher_name--开课老师名
     * evaluation_1,
     * evaluation_2,
     * evaluation_3,
     * evaluation_4,
     * evaluation_5,
     * evaluation_6,
     * subjective_evaluation,——主观评论
     * from_teacher——老师姓名
     * evaluation_time——评论时间
     * classpic——照片url
     */
    @RequestMapping(value = "/portal/TeaEvaluation/submit.do")
    public ServerResponse ClientSubmit(String courceid,
                                       String courcename,
                                       String teacherid,
                                       String listenedteachername,
                                       String evaluation1,
                                       String evaluation2,
                                       String evaluation3,
                                       String evaluation4,
                                       String evaluation5,
                                       String evaluation6,
                                       String subjectiveevaluation,
                                       String fromteacher,
                                        String from_teacher_id,
                                        String classpic,
                                       String academy){
        ServerResponse serverresponse = TeaEvaluation.Send(courceid,
                courcename, teacherid, listenedteachername,
                evaluation1, evaluation2, evaluation3, evaluation4,
                evaluation5, evaluation6, subjectiveevaluation, fromteacher, from_teacher_id,classpic,academy);
        return serverresponse;
    }

    /**
     * 【此处做出了修改！！！】！！！
     * 老师获取自己课程来自老师领导的评论
     * 这里 来自领导及督导团对老师的评价也在其中
     * 此处做出修改 原本为老师按照自己的ID获取所有评价 现在改为老师 按照课程ID获取
     */
    /*
    @RequestMapping(value = "/portal/TeaEvaluation/GainComment.do")
    public ServerResponse ClientSubmit(HttpSession session){
        User userInfo = (User)session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverresponse = TeaEvaluation.Gaincommment(userInfo.getUsername());
        return serverresponse;
    }
     */
    @RequestMapping(value = "/portal/TeaEvaluation/GainComment.do")
    public ServerResponse ClientSubmit(String courseid){
        ServerResponse serverresponse = TeaEvaluation.Gaincommment(courseid);
        return serverresponse;
    }



    /**
     * 根据学院获取听课凭证列表
     * @param academy
     * @return
     */
    @RequestMapping(value = "/portal/TeaEvaluation/GainTeaProof.do")
    public ServerResponse GainTeaProofItem(String academy){
        ServerResponse serverresponse = TeaEvaluation.GainTeaProof(academy);
        return serverresponse;
    }

    /**
     * 获取所有教师的听课凭证
     */
    @RequestMapping(value = "/portal/TeaEvaluation/GainAllTeaProof.do")
    public ServerResponse GainAllTeaProofItem(){
        ServerResponse serverresponse = TeaEvaluation.GainAllTeaProof();
        return serverresponse;
    }

    /**
     * 获取听课凭证的url
     */
    @RequestMapping(value = "/portal/TeaEvaluation/GainTeaProofUrl.do")
    public ServerResponse GainTeaProofUrl(String teacherName, String courseName, String listenedTeaName, String date){
        ServerResponse serverresponse = TeaEvaluation.GainProofUrl(teacherName, courseName, listenedTeaName, date);
        return serverresponse;
    }
}
