package com.example.service.impl;

import com.example.dao.ResultFromStudentMapper;
import com.example.dao.ResultFromleaderMapper;
import com.example.dao.S_EvaluationMapper;
import com.example.dao.TeacherEvaluationMapper;
import com.example.pojo.ResultFromStudent;
import com.example.pojo.ResultFromleader;
import com.example.pojo.S_Evaluation;
import com.example.pojo.TeacherEvaluation;
import com.example.service.ICalculateSever;
import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.RemoveSame;
import utils.ServerResponse;

import javax.xml.transform.Result;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class CalculateSever implements ICalculateSever {

    @Autowired
    S_EvaluationMapper StuEvaSQL;
    @Autowired
    TeacherEvaluationMapper TeaEvaSQL;
    @Autowired
    ResultFromStudentMapper ResultStudent;
    @Autowired
    ResultFromleaderMapper ResultLeader;
    @Override
    public ServerResponse calculate(){
        /**
         * 本方法功能是计算各科的平均分数
         * 利用course区分不同课程
         */
        clear();
        RemoveSame remove = new RemoveSame();
        List list = StuEvaSQL.getcourseid();
        remove.ToRemove(list);
        /**
         * 开始处理每一个课程代码
         */
        for(Object index:list){
            List<S_Evaluation> select = StuEvaSQL.getappoint((String) index);
            /**
             * 获取到了单个课程iD的所有评价
             */
            int eva1=0,eva2=0,eva3=0,eva4=0,eva5=0,eva6=0,num=0;
            for (S_Evaluation extract:select){
                num++;
                eva1=eva1+Integer.parseInt(extract.getEvaluation1());
                eva2=eva2+Integer.parseInt(extract.getEvaluation2());
                eva3=eva3+Integer.parseInt(extract.getEvaluation3());
                eva4=eva4+Integer.parseInt(extract.getEvaluation4());
                eva5=eva5+Integer.parseInt(extract.getEvaluation5());
                eva6=eva6+Integer.parseInt(extract.getEvaluation6());
            }
            if(num!=0){
                double aver1 = eva1/num;
                double aver2 = eva2/num;
                double aver3 = eva3/num;
                double aver4 = eva4/num;
                double aver5 = eva5/num;
                double aver6 = eva6/num;
                double total = aver1+aver2+aver3+aver4+aver5+aver6;
                ResultFromStudent INSQL= new ResultFromStudent();
                INSQL.setAverage1(aver1);
                INSQL.setAverage2(aver2);
                INSQL.setAverage3(aver3);
                INSQL.setAverage4(aver4);
                INSQL.setAverage5(aver5);
                INSQL.setAverage6(aver6);
                INSQL.setTotalAverage(total);
                INSQL.setCourseId(select.get(0).getCourceId());
                INSQL.setCourseName(select.get(0).getCourceName());
                INSQL.setTeacherName(select.get(0).getTeacherName());
                INSQL.setTeacherId(select.get(0).getTeacherId());
                ResultStudent.insert(INSQL);
            }

        }
        /**
         * 开始处理老师评价
         */
        list = TeaEvaSQL.selectCourseid();
        remove.ToRemove(list);
        for(Object index:list){
            List<TeacherEvaluation> select = TeaEvaSQL.getappoint((String) index);
            int eva1=0,eva2=0,eva3=0,eva4=0,eva5=0,eva6=0,num=0;
            for(TeacherEvaluation extract:select){
                num++;
                eva1=eva1+Integer.parseInt(extract.getEvaluation1());
                eva2=eva2+Integer.parseInt(extract.getEvaluation2());
                eva3=eva3+Integer.parseInt(extract.getEvaluation3());
                eva4=eva4+Integer.parseInt(extract.getEvaluation4());
                eva5=eva5+Integer.parseInt(extract.getEvaluation5());
                eva6=eva6+Integer.parseInt(extract.getEvaluation6());
            }
            if(num!=0){
                double aver1 = eva1/num;
                double aver2 = eva2/num;
                double aver3 = eva3/num;
                double aver4 = eva4/num;
                double aver5 = eva5/num;
                double aver6 = eva6/num;
                double total = aver1+aver2+aver3+aver4+aver5+aver6;
                ResultFromleader INSQL = new ResultFromleader();
                INSQL.setAverage1(aver1);
                INSQL.setAverage2(aver2);
                INSQL.setAverage3(aver3);
                INSQL.setAverage4(aver4);
                INSQL.setAverage5(aver5);
                INSQL.setAverage6(aver6);
                INSQL.setTotalAverage(total);
                INSQL.setCourseId(select.get(0).getCourceId());
                INSQL.setCourseName(select.get(0).getCourceName());
                INSQL.setTeacherName(select.get(0).getListenedTeacherName());
                INSQL.setTeacherId(select.get(0).getTeacherId());
                ResultLeader.insert(INSQL);
            }
        }
        /**
         * 计算排名——来自学生的评分
         */
        List<ResultFromStudent> List_Rank_stu = ResultStudent.selectAll();
        List_Rank_stu.sort(new Comparator<ResultFromStudent>(){
            @Override
            public int compare(ResultFromStudent A,ResultFromStudent B){
                if(A.getTotalAverage()<B.getTotalAverage()){
                    return 1;
                }
                else if((A.getTotalAverage()>B.getTotalAverage())){
                    return -1;
                }
                else{
                    if(A.getAverage1()<B.getAverage1()){
                        return 1;
                    }
                    else if((A.getAverage1()>B.getAverage1())){
                        return -1;
                    }
                    else{
                        if(A.getAverage2()<B.getAverage2()){
                            return 1;
                        }
                        else if((A.getAverage2()>B.getAverage2())){
                            return -1;
                        }
                        else{
                            if(A.getAverage3()<B.getAverage3()){
                                return 1;
                            }
                            else if((A.getAverage3()>B.getAverage3())){
                                return -1;
                            }
                            else{
                                if(A.getAverage4()<B.getAverage4()){
                                    return 1;
                                }
                                else if((A.getAverage4()>B.getAverage4())){
                                    return -1;
                                }
                                else{
                                    if(A.getAverage5()<B.getAverage5()){
                                        return 1;
                                    }
                                    else if((A.getAverage5()>B.getAverage5())){
                                        return -1;
                                    }
                                    else{
                                        if(A.getAverage6()<B.getAverage6()){
                                            return 1;
                                        }
                                        else if((A.getAverage6()>B.getAverage6())){
                                            return -1;
                                        }
                                        else{
                                            return 0;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
        int count = 1;
        for(ResultFromStudent in:List_Rank_stu){
            ResultStudent.setrank(new Integer(count),in.getCourseId());
            count++;
        }
        /**
         * 计算排名——来自老师的评分
         */
        List<ResultFromleader> List_Rank_Leader = ResultLeader.selectAll();
        List_Rank_Leader.sort(new Comparator<ResultFromleader>(){
            @Override
            public int compare(ResultFromleader A,ResultFromleader B){
                if(A.getTotalAverage()<B.getTotalAverage()){
                    return 1;
                }
                else if((A.getTotalAverage()>B.getTotalAverage())){
                    return -1;
                }
                else{
                    if(A.getAverage1()<B.getAverage1()){
                        return 1;
                    }
                    else if((A.getAverage1()>B.getAverage1())){
                        return -1;
                    }
                    else{
                        if(A.getAverage2()<B.getAverage2()){
                            return 1;
                        }
                        else if((A.getAverage2()>B.getAverage2())){
                            return -1;
                        }
                        else{
                            if(A.getAverage3()<B.getAverage3()){
                                return 1;
                            }
                            else if((A.getAverage3()>B.getAverage3())){
                                return -1;
                            }
                            else{
                                if(A.getAverage4()<B.getAverage4()){
                                    return 1;
                                }
                                else if((A.getAverage4()>B.getAverage4())){
                                    return -1;
                                }
                                else{
                                    if(A.getAverage5()<B.getAverage5()){
                                        return 1;
                                    }
                                    else if((A.getAverage5()>B.getAverage5())){
                                        return -1;
                                    }
                                    else{
                                        if(A.getAverage6()<B.getAverage6()){
                                            return 1;
                                        }
                                        else if((A.getAverage6()>B.getAverage6())){
                                            return -1;
                                        }
                                        else{
                                            return 0;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
        count = 1;
        for(ResultFromleader in_leader:List_Rank_Leader){
            ResultLeader.setrank(new Integer(count),in_leader.getCourseId());
            count++;
        }
         /**
         * 计算平均分_学生
         */
        List<ResultFromStudent> TempList = ResultStudent.selectAll();
        double eva1=0,eva2=0,eva3=0,eva4=0,eva5=0,eva6=0,total=0;
        int num=0;
        for(ResultFromStudent index:TempList){
            num=num+1;
            eva1=eva1+index.getAverage1();
            eva2=eva2+index.getAverage2();
            eva3=eva3+index.getAverage3();
            eva4=eva4+index.getAverage4();
            eva5=eva5+index.getAverage5();
            eva6=eva6+index.getAverage6();
        }
        if(num!=0){
            eva1=eva1/num;
            eva2=eva2/num;
            eva3=eva3/num;
            eva4=eva4/num;
            eva5=eva5/num;
            eva6=eva6/num;
            total = eva1+eva2+eva3+eva4+eva5+eva6;
            ResultFromStudent INSQL= new ResultFromStudent();
            INSQL.setAverage1(eva1);
            INSQL.setAverage2(eva2);
            INSQL.setAverage3(eva3);
            INSQL.setAverage4(eva4);
            INSQL.setAverage5(eva5);
            INSQL.setAverage6(eva6);
            INSQL.setTotalAverage(total);
            INSQL.setCourseId("scoreaverage");
            INSQL.setCourseName("scoreaverage");
            INSQL.setTeacherName("admin");
            INSQL.setTeacherId("-1");
            ResultStudent.insert(INSQL);
        }


        /**
         * 计算平均分_老师
         */
        List<ResultFromleader> TempList_leader = ResultLeader.selectAll();
        eva1=0;eva2=0;eva3=0;eva4=0;eva5=0;eva6=0;total=0;
        num=0;
        for(ResultFromleader index:TempList_leader){
            num=num+1;
            eva1=eva1+index.getAverage1();
            eva2=eva2+index.getAverage2();
            eva3=eva3+index.getAverage3();
            eva4=eva4+index.getAverage4();
            eva5=eva5+index.getAverage5();
            eva6=eva6+index.getAverage6();
        }
        if(num!=0){
            eva1=eva1/num;
            eva2=eva2/num;
            eva3=eva3/num;
            eva4=eva4/num;
            eva5=eva5/num;
            eva6=eva6/num;
            total = eva1+eva2+eva3+eva4+eva5+eva6;
            ResultFromleader IN= new ResultFromleader();
            IN.setAverage1(eva1);
            IN.setAverage2(eva2);
            IN.setAverage3(eva3);
            IN.setAverage4(eva4);
            IN.setAverage5(eva5);
            IN.setAverage6(eva6);
            IN.setTotalAverage(total);
            IN.setCourseId("scoreaverage");
            IN.setCourseName("scoreaverage");
            IN.setTeacherName("admin");
            IN.setTeacherId("-1");
            ResultLeader.insert(IN);
        }

        return ServerResponse.createServerResponseBySuccess();
    }

    /**
     * 老师获取自己的学生评分分析结果
     * @param courseid
     * @return
     */
    @Override
    public ServerResponse teacherget(String courseid){
        ResultFromStudent gainsore = ResultStudent.selectcourseid(courseid);
        return ServerResponse.createServerResponseBySuccess(gainsore);
    }
    /**
     * 老师获取关于领导对自己的评价的分析结果
     */
    public ServerResponse teachergetleader(String courseid){
        ResultFromleader gainsore = ResultLeader.selectcourseid(courseid);
        return ServerResponse.createServerResponseBySuccess(gainsore);
    }
    /**
     * 清空这个关于评论分析的表
     */
    public ServerResponse clear(){
        ResultStudent.clear();
        ResultLeader.clear();
        return ServerResponse.createServerResponseBySuccess();
    }
    /**
     * 获取领导获取老师的学生评价
     */
    public ServerResponse leaderget(String Academy){
        List<ResultFromStudent> gainsore;
        if(Academy.equals("全校")){
            gainsore = ResultStudent.selectAll();
        }
        else{
            gainsore = ResultStudent.selectAcademy(Academy);
        }
        return ServerResponse.createServerResponseBySuccess(gainsore);
    }
}
