package com.qoobico.remindme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.qoobico.remindme.adapter.StudentProgressAdapter;
import com.qoobico.remindme.dto.StatisticsDTO;
import com.qoobico.remindme.dto.StudentDTO;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SubjectStatsActivity extends AppCompatActivity {

    private  StatisticsDTO stats;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppToolbar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_stats);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            stats = (StatisticsDTO) extras.getSerializable(StatisticsDTO.class.getSimpleName());
        }

        TextView totalLesson = findViewById(R.id.total_lesson);
        totalLesson.setText("Всего пар было: " + String.valueOf(stats.getTotalLesson()));

        ListView studentsList = findViewById(R.id.list_students_stat);
        StudentProgressAdapter spAdapter = new StudentProgressAdapter(this,R.layout.student_progress_item, new ArrayList<StudentDTO>(stats.getStudents()),stats);
        studentsList.setAdapter(spAdapter);

        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(stats.getSubject().getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.inflateMenu(R.menu.menu);

        if(stats.getStudents().size() == 0) {
            TextView emptyView = findViewById(R.id.student_empty);
            emptyView.setText("Список студентов пуст.");
            emptyView.setVisibility(View.VISIBLE);
        }
    }

}
