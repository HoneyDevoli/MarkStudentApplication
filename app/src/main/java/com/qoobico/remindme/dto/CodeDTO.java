package com.qoobico.remindme.dto;

import java.util.Date;

public class CodeDTO {

    private long id;
    private String code;
    private boolean markOpportunity;
    private Date date;
    private StudentDTO student;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isMarkOpportunity() {
        return markOpportunity;
    }

    public void setMarkOpportunity(boolean markOpportunity) {
        this.markOpportunity = markOpportunity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
    }
}
