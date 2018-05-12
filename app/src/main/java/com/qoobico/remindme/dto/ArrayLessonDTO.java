package com.qoobico.remindme.dto;

import java.util.ArrayList;
import java.util.List;

public class ArrayLessonDTO {

    private List<LessonDTO> lessons = new ArrayList<>();

    public List<LessonDTO> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonDTO> lessons) {
        this.lessons = lessons;
    }
}
