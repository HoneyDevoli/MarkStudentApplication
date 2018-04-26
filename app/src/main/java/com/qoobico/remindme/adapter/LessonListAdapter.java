package com.qoobico.remindme.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qoobico.remindme.LoginActivity;
import com.qoobico.remindme.MainActivity;
import com.qoobico.remindme.MarkActivity;
import com.qoobico.remindme.R;
import com.qoobico.remindme.dto.LessonDTO;

import org.w3c.dom.Text;

import java.util.List;

public class LessonListAdapter extends RecyclerView.Adapter<LessonListAdapter.LessonHolder> {

    private List<LessonDTO> data;
    boolean[] selects;
    public LessonListAdapter(List<LessonDTO> data) {
        this.data = data;
        selects = new boolean[data.size()];
    }

    @Override
    public LessonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_item, parent, false);


        return new LessonHolder(view);
    }


    @Override
    public void onBindViewHolder(LessonHolder holder, int position) {
        LessonDTO item = data.get(position);
        if (item.getDate() == null) {
            holder.date.setVisibility(View.GONE);
        } else {
            holder.date.setText(item.getDate());
            holder.date.setVisibility(View.VISIBLE);
        }

        if (item.getTitle() == null) {
            holder.lessonLayout.setVisibility(View.GONE);
            //holder.noLesson.setVisibility(View.VISIBLE);
        } else {
            holder.lessonLayout.setVisibility(View.VISIBLE);
            holder.title.setText(item.getTitle());
            holder.number.setText(String.valueOf(item.getNumberOfLesson()));
            holder.time.setText(selectTime(item.getNumberOfLesson()));
            holder.aud.setText(item.getAuditorium());
            holder.type.setText(item.getType());
            holder.teacherOrGroup.setText(item.getTeacherOrGroup());
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<LessonDTO> data) {
        this.data = data;
    }

    public static class LessonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView title, number, time, aud, type, teacherOrGroup, date;
        LinearLayout lessonLayout;

        public LessonHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            number = (TextView) itemView.findViewById(R.id.number);
            time = (TextView) itemView.findViewById(R.id.time);
            aud = (TextView) itemView.findViewById(R.id.aud);
            type = (TextView) itemView.findViewById(R.id.type);
            teacherOrGroup = (TextView) itemView.findViewById(R.id.teacher);
            date = (TextView) itemView.findViewById(R.id.date);
            lessonLayout = (LinearLayout) itemView.findViewById(R.id.lesson_layout);

            lessonLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(v.getContext(),number.getText().toString(),Toast.LENGTH_SHORT);
           v.getContext().startActivity(new Intent(v.getContext(), MarkActivity.class));
        }
    }

    private String selectTime(int numberLesson) {
        switch (numberLesson) {
            case 1:
                return "8:00 - 9:30";
            case 2:
                return "9:45 - 11:15";
            case 3:
                return "11:30 - 13 00";
            case 4:
                return "13:40 - 15:10";
            case 5:
                return "15:20 - 16:50";
            case 6:
                return "17:00 - 18:30";
            case 7:
                return "18:40 - 20:10";
            default:
                return "20:20";
        }
    }
}

