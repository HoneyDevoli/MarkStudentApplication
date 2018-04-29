package com.qoobico.remindme.helper;

import com.qoobico.remindme.dto.LessonDTO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParseSchedule {

    public enum TypeSchedule {TEACHER, STUDENT}

    private final static String URL_TEACHER = "http://rasp.sstu.ru/teacher/";
    private final static String URL_STUDENT = "http://rasp.sstu.ru/group/";

    public static List<LessonDTO> Schedule(TypeSchedule t, int idRasp) throws IOException {

        Document dc;
        if (t == TypeSchedule.STUDENT) {
            dc = Jsoup.connect(URL_STUDENT + idRasp).timeout(6000).get();
        } else {
            dc = Jsoup.connect(URL_TEACHER + idRasp).timeout(6000).get();
        }

        //String owner = dc.select("div.pull-right").first().text();
        //System.out.println(owner);

        List<LessonDTO> lessonsList = new ArrayList<>();
        int numLesson = 0;
        Elements lessions = dc.select("div.rasp-table-inner-cell");
        for (Element lession : lessions) {
            LessonDTO lessonDTO = new LessonDTO();

            Elements attributes = lession.select("div.small");
            Elements date = lession.select("div.date");
            if (attributes.size() > 0) {
                numLesson++;
                lessonDTO.setNumberOfLesson(numLesson);
                lessonDTO.setAuditorium(attributes.select("div.aud").text());
                lessonDTO.setTitle(attributes.select("div.subject").text());
                lessonDTO.setType(attributes.select("div.type").text());

                lessonDTO.setTeacherOrGroup(attributes.select("div.group").text());
                if(lessonDTO.getTeacherOrGroup().equals(""))
                    lessonDTO.setTeacherOrGroup(attributes.select("div.teacher a").text());
            } else if (date.size() != 0) {
                numLesson = 0;
                lessonDTO.setDate(date.text());
            } else if (!(lession.text().length() > 0) && lession.childNodeSize() == 1) {
                numLesson++;
            }
            if (lessonDTO.getDate() != null || lessonDTO.getTitle() != null)
                lessonsList.add(lessonDTO);
        }
        return  lessonsList;
    }
}
