package com.qoobico.remindme;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.DialogPreference;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.QRCode;
import com.qoobico.remindme.adapter.StudentAdapter;
import com.qoobico.remindme.adapter.TabsFragmentAdapter;
import com.qoobico.remindme.dto.ArrayGroupDTO;
import com.qoobico.remindme.dto.ArrayStudentDTO;
import com.qoobico.remindme.dto.GroupDTO;
import com.qoobico.remindme.dto.LessonDTO;
import com.qoobico.remindme.dto.StudentDTO;
import com.qoobico.remindme.dto.TeacherDTO;
import com.qoobico.remindme.helper.Constants;
import com.qoobico.remindme.helper.ParseSchedule;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MarkActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_mark;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private Button txtView;
    private TabsFragmentAdapter adapter;
    private ListView listStudents;
    private LessonDTO lesson;
    private long idLesson;
    private boolean runTimerTask = true;
    private StudentAdapter studentAdapter;
    private Handler handler;
    private int numberOfCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppToolbar);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            lesson = (LessonDTO) extras.getSerializable(LessonDTO.class.getSimpleName());
        }

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                List<StudentDTO> students = (ArrayList<StudentDTO>) msg.obj;
                studentAdapter.setData(students);
            }
        };

        txtView = (Button) findViewById(R.id.button2);
        txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idLesson!=0){
                    showRatingDialog();
                } else {
                    addNewLesson();
                }
            }
        });
        initToolbar();
        initListOfStudent();

        listStudents = findViewById(R.id.list_students);
        studentAdapter = new StudentAdapter(this, R.layout.student_item);
        listStudents.setAdapter(studentAdapter);
    }

    private void addNewLesson(){
        try {
            GetIdLessonTask task = new GetIdLessonTask(lesson, Constants.CREATE);
            task.execute();
            idLesson = task.get(4, TimeUnit.SECONDS);
            if (idLesson == 0) {
                throw new Exception();
            } else {
                showRatingDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Timed out. Server error", Toast.LENGTH_SHORT).show();
        }
    }

    private List<StudentDTO> mokData() {
        List<StudentDTO> s = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            StudentDTO d = new StudentDTO();
            d.setFirstName("name" + i);
            d.setMiddleName("mname" + i);
            d.setLastName("surname" + i);
            GroupDTO g = new GroupDTO();
            g.setName("group");
            d.setGroup(g);
            s.add(d);
        }
        return s;
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(lesson.getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runTimerTask = false;
                onBackPressed();
            }
        });
        toolbar.inflateMenu(R.menu.menu);
    }

    private void initListOfStudent(){
        GetIdLessonTask task = new GetIdLessonTask(lesson, Constants.CHECK);
        task.execute();
        try{
            idLesson = task.get(4, TimeUnit.SECONDS);
            if(idLesson==0){
                throw new InterruptedException();
            } else {
                updateListOfStudent();
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Timed out. Server error", Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Пара еще не сохранена", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Unknown error", Toast.LENGTH_SHORT).show();
        }
}

    private void updateListOfStudent(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                checkUpdateStudents();
            }
        }).start();

        long delay = 1000;
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
        timer.scheduleAtFixedRate(task, delay, 5*1000);

}
    private void checkUpdateStudents(){
        Map<String, String> param = new HashMap<>();
        param.put("idLesson", String.valueOf(idLesson));
        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        ArrayStudentDTO students = template.getForObject(Constants.URL.GET_STUDENTS, ArrayStudentDTO.class, param);

        Message msg = new Message();
        msg.obj = students.getStudents();
        handler.sendMessage(msg);
    }

    public void showRatingDialog() {

        if(numberOfCode!=1){
            new GenerateCodeTask(idLesson, numberOfCode, true).execute();
        }

        final AlertDialog.Builder ratingdialog = new AlertDialog.Builder(this);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(getQRCode());
        linearLayout.addView(imageView);

        Button button = new Button(this);
        button.setText("Сгенерировать новый код");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                new GenerateCodeTask(idLesson,numberOfCode,false).execute();

                numberOfCode++;
                new GenerateCodeTask(idLesson, numberOfCode, true).execute();
                imageView.setImageBitmap(getQRCode());
            }
        });
        linearLayout.addView(button);

        ratingdialog.setView(linearLayout);



        ratingdialog.setPositiveButton("Закрыть",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //txtView.setText(String.valueOf(rating.getRating()));
                        new GenerateCodeTask(idLesson, numberOfCode, false).execute();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                checkUpdateStudents();
                            }
                        }).start();
                        dialog.dismiss();
                    }
                });


        ratingdialog.create();
        ratingdialog.show();
    }

    private Bitmap getQRCode(){
        QRCodeWriter writer = new QRCodeWriter();
        String content = String.valueOf(idLesson) + "/" + numberOfCode; //512
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 700, 700);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    private class GetIdLessonTask extends AsyncTask<Void, Void, Long> {

        private LessonDTO lesson;
        private long id;
        private String createOrCheck;

        GetIdLessonTask(LessonDTO lesson, String createOrCheck) {
            this.lesson = lesson;
            this.id = MainActivity.getId();
            this.createOrCheck = createOrCheck;
        }

        @Override
        protected Long doInBackground(Void... params) {

            Map<String, String> param = new HashMap<>();
            param.put("idTeacher", String.valueOf(id));
            param.put("aud",lesson.getAuditorium());
            param.put("date",lesson.getDate());
            param.put("numLesson",String.valueOf(lesson.getNumberOfLesson()));
            param.put("title",lesson.getTitle());
            param.put("type",lesson.getType());
            param.put("createorcheck", createOrCheck);

            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            long lessonId = template.getForObject(Constants.URL.GET_ID_LESSON, long.class, param);
            return lessonId;
        }


        @Override
        protected void onPostExecute(final Long id) {
            super.onPostExecute(id);
        }
    }

    private class GenerateCodeTask extends AsyncTask<Void, Void, Void> {

        private long idLesson;
        private long idTeacher;
        private long numberOfCode;
        private boolean isMarkOpport;

        GenerateCodeTask(long idLesson, long numberOfCode, boolean isMarkOpport) {
            this.idLesson = idLesson;
            this.idTeacher = MainActivity.getId();
            this.numberOfCode = numberOfCode;
            this.isMarkOpport = isMarkOpport;
        }

        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> param = new HashMap<>();
            param.put("idTeacher", String.valueOf(idTeacher));
            param.put("idLesson", String.valueOf(idLesson));
            param.put("numberOfCode", String.valueOf(numberOfCode));
            param.put("isMarkOpport", String.valueOf(isMarkOpport));

            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            template.getForObject(Constants.URL.GENERATE_CODE, long.class, param);
            return null;
        }

    }
}