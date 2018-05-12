package com.qoobico.remindme.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.qoobico.remindme.R;
import com.qoobico.remindme.dto.StudentDTO;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends ArrayAdapter<StudentDTO> {

    private static int num;
    private LayoutInflater inflater;
    private int layout;
    private List<StudentDTO> students;

    public StudentAdapter(Context context, int resource) {
        super(context, resource);
        this.students = new ArrayList<>();
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        setNullList();
    }

    @Override
    public int getCount() {
        return students.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);
        if(num%2 == 0)
            view.setBackgroundColor(235235235);
        else
            view.setBackgroundColor(209209209);
        num++;

        TextView nameView =  view.findViewById(R.id.student_name);
        TextView groupView =  view.findViewById(R.id.student_group);

        StudentDTO student = students.get(position);

        if(student.getLastName() != null) {
            nameView.setText(String.format("%s %s %s", student.getLastName(), student.getFirstName(), student.getMiddleName()));
            groupView.setText(student.getGroup().getName());
        } else {
            nameView.setText(student.getFirstName());
        }
        return view;
    }

    public void setData(List<StudentDTO> data) {
        this.students = data;
        setNullList();
        notifyDataSetChanged();
    }

    private void setNullList(){
        if(students.size() == 0){
            StudentDTO student = new StudentDTO();
            student.setFirstName("Список студентов пуст");
            students.add(student);
        }
    }
}
