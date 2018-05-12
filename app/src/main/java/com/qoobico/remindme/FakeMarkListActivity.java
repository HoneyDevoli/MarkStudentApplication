package com.qoobico.remindme;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.qoobico.remindme.adapter.CodeAdapter;
import com.qoobico.remindme.dto.ArrayCodeDTO;
import com.qoobico.remindme.helper.Constants;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class FakeMarkListActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_fake_mark_list;

    private Toolbar toolbar;
    private ListView listStudents;
    private boolean runTimerTask = true;
    private CodeAdapter codeAdapter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppToolbar);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ArrayCodeDTO codes = (ArrayCodeDTO) msg.obj;
                codeAdapter.setData(codes.getCodes());
            }
        };


        initToolbar();
        initListOfStudent();

        Button buttonDelete = findViewById(R.id.delete_fake_mark_button);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListOfStudentTask();
            }
        });

        listStudents = findViewById(R.id.list_students);
        codeAdapter = new CodeAdapter(this, R.layout.code_item);
        listStudents.setAdapter(codeAdapter);
    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Фейковые отметки");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runTimerTask = false;
                finish();
                onBackPressed();
            }
        });
        toolbar.inflateMenu(R.menu.menu);
    }

    private void initListOfStudent(){
        try{

                updateListOfStudentTask();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Timed out. Server error", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateListOfStudentTask(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                checkUpdateStudents();
            }
        }).start();

        long delay = 5000;
        final Timer timer = new Timer(true);
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(runTimerTask) {
                    //запрос на сервер
                    checkUpdateStudents();
                } else {
                    timer.cancel();
                    timer.purge();
                }
            }

        };
        timer.scheduleAtFixedRate(task, delay, 10*1000);

    }

    private void deleteListOfStudentTask(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                deleteListOfstudent();
            }
        }).start();

    }
    private void checkUpdateStudents(){
        Map<String, String> param = new HashMap<>();
        param.put("idTeacher", String.valueOf(MainActivity.getId()));

        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        ArrayCodeDTO codes = template.getForObject(Constants.URL.GET_CODES, ArrayCodeDTO.class, param);

        Message msg = new Message();
        msg.obj = codes;
        handler.sendMessage(msg);
    }

    private void deleteListOfstudent(){
        Map<String, String> param = new HashMap<>();
        param.put("idTeacher", String.valueOf(MainActivity.getId()));

        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        ArrayCodeDTO codes = template.getForObject(Constants.URL.DELETE_CODES, ArrayCodeDTO.class, param);

    }

}
