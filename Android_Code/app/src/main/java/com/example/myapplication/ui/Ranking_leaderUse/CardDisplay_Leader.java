package com.example.myapplication.ui.Ranking_leaderUse;

public class CardDisplay_Leader implements Comparable<CardDisplay_Leader>{
    private String course_id;
    private String course_name;
    private String course_teacher_name;
    private String course_teacher_id;
    private String detail;
    private int Rankint;
    private String semester;
    private String totalAverage;

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getTotalAverage() {
        return totalAverage;
    }

    public void setTotalAverage(String totalAverage) {
        this.totalAverage = totalAverage;
    }

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

    public int getRankint() {
        return Rankint;
    }

    public void setRankint(int rankint) {
        Rankint = rankint;
    }
    @Override
    public int compareTo(CardDisplay_Leader cardDisplay_Leader) {
        return this.Rankint - cardDisplay_Leader.getRankint();
    }
}
