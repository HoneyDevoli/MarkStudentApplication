package com.qoobico.remindme.dto;

import java.util.ArrayList;
import java.util.List;

public class ArrayStudentDTO {

    private List<StudentDTO> students = new ArrayList<>();

    public List<StudentDTO> getStudents() {
        return students;
    }

    public void setStudents(List<StudentDTO> students) {
        this.students = students;
    }
}
