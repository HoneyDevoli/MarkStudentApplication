package com.qoobico.remindme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.qoobico.remindme.R;
import com.qoobico.remindme.dto.CodeDTO;
import com.qoobico.remindme.dto.StudentDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CodeAdapter extends ArrayAdapter<CodeDTO> {

    private static int num;
    private LayoutInflater inflater;
    private int layout;
    private List<CodeDTO> codes;

    public CodeAdapter(Context context, int resource) {
        super(context, resource);
        this.codes = new ArrayList<>();
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        setNullList();
    }

    @Override
    public int getCount() {
        return codes.size();
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
        TextView dateView = view.findViewById(R.id.date_fake_mark);

        CodeDTO code = codes.get(codes.size()-1 - position);

        if(code.getStudent().getLastName() != null) {
            nameView.setText(String.format("%s %s %s", code.getStudent().getLastName(), code.getStudent().getFirstName(), code.getStudent().getMiddleName()));
            groupView.setText(code.getStudent().getGroup().getName());

            SimpleDateFormat dateFormat = new SimpleDateFormat();
            dateView.setText(String.valueOf(dateFormat.format(code.getDate())));
        } else {
            nameView.setText(code.getStudent().getFirstName());
        }
        return view;
    }

    public void setData(List<CodeDTO> data) {
        this.codes = data;
        setNullList();
        notifyDataSetChanged();
    }

    private void setNullList(){
        if(codes.size() == 0){
            CodeDTO nullCode = new CodeDTO();
            StudentDTO student = new StudentDTO();
            student.setFirstName("Список студентов пуст");

            nullCode.setStudent(student);
            codes.add(nullCode);
        }
    }
}
