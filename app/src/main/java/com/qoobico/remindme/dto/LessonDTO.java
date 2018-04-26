package com.qoobico.remindme.dto;

public class LessonDTO {

    private String date;
    private String auditorium;
    private int numberOfLesson;
    private String type;
    private String title;
    private String teacherOrGroup;


    public String getAuditorium() {
        return auditorium;
    }

    public void setAuditorium(String auditorium) {
        this.auditorium = auditorium;
    }

    public int getNumberOfLesson() {
        return numberOfLesson;
    }

    public void setNumberOfLesson(int numberOfLesson) {
        this.numberOfLesson = numberOfLesson;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeacherOrGroup() {
        return teacherOrGroup;
    }

    public void setTeacherOrGroup(String teacherOrGroup) {
        this.teacherOrGroup = teacherOrGroup;
    }
}