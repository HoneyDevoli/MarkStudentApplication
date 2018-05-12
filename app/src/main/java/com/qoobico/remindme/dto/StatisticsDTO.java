package com.qoobico.remindme.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatisticsDTO implements Serializable{

    private List<Integer> CountLesson = new ArrayList<>();
    private Set<StudentDTO> students = new HashSet<>();
    private int totalLesson;
    private SubjectDTO subject;

    public void setTotalLesson(int totalLesson) {
        this.totalLesson = totalLesson;
    }

    public void setSubject(SubjectDTO subject) {
        this.subject = subject;
    }

    public SubjectDTO getSubject() {
        return subject;
    }

    public int getTotalLesson() {
        return totalLesson;
    }

    public List<Integer> getCountLesson() {
        return CountLesson;
    }

    public void setCountLesson(List<Integer> countLesson) {
        CountLesson = countLesson;
    }

    public Set<StudentDTO> getStudents() {
        return students;
    }

    public void setStudents(Set<StudentDTO> students) {
        this.students = students;
    }
}
