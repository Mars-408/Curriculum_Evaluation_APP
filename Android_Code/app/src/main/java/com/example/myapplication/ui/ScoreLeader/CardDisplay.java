package com.example.myapplication.ui.ScoreLeader;

public class CardDisplay {
    /**
     * 定义一个类 这个类用在保存卡片布局的数据用的
     */
    private String course_id;
    private String course_name;
    private String course_teacher_name;
    private String course_teacher_id;

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        this.academy = academy;
    }

    private String academy;
    private String detail;

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_teacher_name() {
        return course_teacher_name;
    }

    public void setCourse_teacher_name(String course_teacher_name) {
        this.course_teacher_name = course_teacher_name;
    }

    public String getCourse_teacher_id() {
        return course_teacher_id;
    }

    public void setCourse_teacher_id(String course_teacher_id) {
        this.course_teacher_id = course_teacher_id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
