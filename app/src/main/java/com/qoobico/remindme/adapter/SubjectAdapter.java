package com.qoobico.remindme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.qoobico.remindme.R;
import com.qoobico.remindme.dto.SubjectDTO;

import java.util.List;

public class SubjectAdapter extends ArrayAdapter<SubjectDTO> {


    private LayoutInflater inflater;
    private int layout;
    private List<SubjectDTO> subjects;

    public SubjectAdapter(Context context, int resource, List<SubjectDTO> subjects) {
        super(context, resource, subjects);
        this.subjects = subjects;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return subjects.size();
    }

    @Override
    public SubjectDTO getItem(int position) {
        return (subjects != null && subjects.size() >= position) ? subjects.get(position) : null;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        TextView title = view.findViewById(R.id.title1);
        TextView type = view.findViewById(R.id.type1);

        SubjectDTO subject = subjects.get(position);

        title.setText(subject.getTitle());
        type.setText(subject.getType());


        return view;
    }


    public void setData(List<SubjectDTO> data) {
        this.subjects = data;
        notifyDataSetChanged();

    }
}
