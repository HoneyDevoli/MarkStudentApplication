package com.qoobico.remindme.dto;

import java.util.ArrayList;
import java.util.List;

public class DayDTO {
    private String date;
    private List<LessonDTO> lessons = new ArrayList<>();



    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}
