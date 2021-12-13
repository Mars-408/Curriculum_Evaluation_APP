package com.example.dao;

import com.example.pojo.ResultFromStudent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResultFromStudentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TeacherResultFromStudent
     *
     * @mbg.generated
     */
    int insert(ResultFromStudent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TeacherResultFromStudent
     *
     * @mbg.generated
     */
    List<ResultFromStudent> selectAll();
    /**
     * 老师获取自己的评分
     */
    ResultFromStudent selectcourseid(String courseid);

    /**
     * 清空关于评论分析的表
     */
    void clear();
    /**
     * 输入courseId 仅仅设定排名
     */
    void setrank(@Param("rank")int rank, @Param("courseId")String courseId);
    /**
     * 输入课程获取评价——老师给学生授课
     */
    List<ResultFromStudent> selectAcademy(String Academy);

}