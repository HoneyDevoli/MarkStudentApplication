package com.qoobico.remindme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.qoobico.remindme.R;
import com.qoobico.remindme.dto.StatisticsDTO;
import com.qoobico.remindme.dto.StudentDTO;
import com.qoobico.remindme.dto.SubjectDTO;
import com.qoobico.remindme.helper.AdvancedTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StudentProgressAdapter extends ArrayAdapter<StudentDTO> {


    private LayoutInflater inflater;
    private int layout;
    private StatisticsDTO stats;
    private List<StudentDTO> students;

    public StudentProgressAdapter(Context context, int resource, List<StudentDTO> stud, StatisticsDTO stats) {
        super(context, resource, stud);
        this.stats = stats;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.students = stud;

    }

    @Override
    public int getCount() {
        return students.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        AdvancedTextView progressBar = view.findViewById(R.id.progress_view);
        TextView group = view.findViewById(R.id.student_group);
        TextView name = view.findViewById(R.id.student_name);

        StudentDTO stud = students.get(position);

        group.setText(stud.getGroup().getName());
        name.setText(String.format("%s %s.%s.",stud.getLastName(),stud.getFirstName().substring(0,1),stud.getMiddleName().substring(0,1)));
        progressBar.setMaxValue(stats.getTotalLesson());
        progressBar.setValue(stats.getCountLesson().get(position));

        return view;
    }
}
