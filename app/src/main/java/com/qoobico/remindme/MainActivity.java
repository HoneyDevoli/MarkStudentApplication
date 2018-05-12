package com.qoobico.remindme;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qoobico.remindme.adapter.CodeAdapter;
import com.qoobico.remindme.adapter.TabsFragmentAdapter;
import com.qoobico.remindme.dto.ArrayCodeDTO;
import com.qoobico.remindme.dto.ArrayLessonDTO;
import com.qoobico.remindme.dto.ArrayStatsDTO;
import com.qoobico.remindme.dto.ArrayStudentDTO;
import com.qoobico.remindme.dto.LessonDTO;
import com.qoobico.remindme.dto.StudentDTO;
import com.qoobico.remindme.dto.SubjectDTO;
import com.qoobico.remindme.dto.TeacherDTO;
import com.qoobico.remindme.helper.Constants;
import com.qoobico.remindme.helper.ParseSchedule;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_main;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private TeacherDTO teacher;
    private StudentDTO student;
    private TabsFragmentAdapter adapter;
    private static String role;
    private static long id;

    //qr code scanner object
    private IntentIntegrator qrScan;

    //background task
    private boolean runTimerTask = true;
    private Handler handler;
    private TextView noticeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            role = extras.getString(Constants.ROLE);
            if (role.equals(Constants.TEACHER)) {
                teacher = (TeacherDTO) extras.getSerializable(TeacherDTO.class.getSimpleName());
                id = teacher.getId();
            } else {
                student = (StudentDTO) extras.getSerializable(StudentDTO.class.getSimpleName());
                id = student.getId();
            }
        }

        initToolbar();
        initNavigationView();
        initTabs();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ArrayCodeDTO codes = (ArrayCodeDTO) msg.obj;
                noticeTextView.setText(String.valueOf(codes.getCodes().size()));
            }
        };

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.menu);
    }

    private void initTabs() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new TabsFragmentAdapter(getApplicationContext(), getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        new LessonTask().execute();
        if(role.equals(Constants.TEACHER)) {
            new SubjectTask(teacher.getId()).execute();
        } else{
            new LastMarkTask(student.getId()).execute();
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.view_navigation_open, R.string.view_navigation_close);
        //drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.actionNotificationItem:
                        showNotificationTab1();
                        break;
                    case R.id.actionNotificationItemExit:
                        goToLoginForm();
                        break;
                    case R.id.actionCamera:
                        goToScanCode();
                        break;
                    case R.id.actionNotificationItem2:
                        showNotificationTab2();
                        break;
                    case R.id.actionNotificationItem3:
                        showNotificationTab3();
                        break;
                    case R.id.actionNotificationItem4:
                        showNotificationTab4();
                        break;
                }
                return true;
            }
        });

        View headerView = navigationView.getHeaderView(0);
        TextView name = headerView.findViewById(R.id.man_name);
        TextView login = headerView.findViewById(R.id.man_login);

        if(role.equals(Constants.TEACHER)) {
            name.setText(String.format("%s %s %s", teacher.getFirstName(), teacher.getMiddleName(), teacher.getLastName()));
            login.setText(teacher.getLogin());
            navigationView.getMenu().findItem(R.id.actionCamera).setVisible(false);

            noticeTextView = (TextView) navigationView.getMenu().findItem(R.id.actionNotificationItem4).getActionView();
            noticeTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            noticeTextView.setBackground(getResources().getDrawable(R.drawable.oval_button));
            noticeTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            //noticeTextView.setWidth(noticeTextView.getHeight());
            noticeTextView.setPadding(50,0,50,0);
            noticeTextView.setGravity(Gravity.CENTER);
            noticeTextView.setText("0");
            noticeTextView.setTextColor(Color.WHITE);
            updateListOfStudentTask();


        } else if(role.equals((Constants.STUDENT))) {
            name.setText(String.format("%s %s %s", student.getFirstName(), student.getMiddleName(), student.getLastName()));
            login.setText(student.getLogin());
            navigationView.getMenu().findItem(R.id.actionNotificationItem3).setTitle("Последние отмеки");
            navigationView.getMenu().findItem(R.id.actionNotificationItem4).setVisible(false);
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

    private void showNotificationTab1() {
        viewPager.setCurrentItem(Constants.TAB_ONE);
    }

    private void showNotificationTab2() {
        viewPager.setCurrentItem(Constants.TAB_TWO);
    }

    private void showNotificationTab3() {
        viewPager.setCurrentItem(Constants.TAB_THREE);
    }

    private void showNotificationTab4() {
        Intent intent = new Intent(this,FakeMarkListActivity.class);
        startActivity(intent);
    }

    private void goToLoginForm() {
        finish();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    private void goToScanCode(){

        qrScan = new IntentIntegrator(this);
        qrScan.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        qrScan.setPrompt("Наведите на сгенерированный на экране учителя QR-код чтобы отметиться");
        qrScan.setCameraId(0);
        qrScan.setOrientationLocked(true);
        qrScan.setBeepEnabled(true);
        qrScan.setCaptureActivity(CaptureActivityPortrait.class);
        qrScan.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "QR-код не найден", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    String idLesson = result.getContents();
                    MarkStudentTask ms = new MarkStudentTask(idLesson,student.getId());
                    Toast toast = Toast.makeText(this, ms.execute().get(5, TimeUnit.SECONDS), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Ошибка отметки, сервер не отвечает", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static long getId() {
        return id;
    }

    public static String getRole(){
        return role;
    }


    private class LessonTask extends  AsyncTask<Void, Void, List<LessonDTO>> {
       @Override
       protected List<LessonDTO> doInBackground(Void... params) {
           try {
               if(role.equals(Constants.TEACHER))
                   return ParseSchedule.Schedule(ParseSchedule.TypeSchedule.TEACHER,teacher.getIdRasp());
               else
                    return ParseSchedule.Schedule(ParseSchedule.TypeSchedule.STUDENT,student.getGroup().getIdRasp());
           } catch (IOException e) {
               return null;
           }
       }

       @Override
       protected void onPostExecute(List<LessonDTO> lessons) {
           adapter.setData(lessons);
       }
   }

    private class SubjectTask extends  AsyncTask<Void, Void, ArrayStatsDTO> {

        private long idTeacher;

        public SubjectTask(long idTeacher){
            this.idTeacher = idTeacher;
        }
        @Override
        protected ArrayStatsDTO doInBackground(Void... voids) {


            Map<String, String> param = new HashMap<String, String>();
            param.put("idTeacher",String.valueOf(idTeacher));

            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ArrayStatsDTO stats = template.getForObject(Constants.URL.GET_SUBJECTS, ArrayStatsDTO.class, param);
            return stats;
        }

        @Override
        protected void onPostExecute(ArrayStatsDTO stats) {
            adapter.setDataObj(stats);
        }
    }

    private class MarkStudentTask extends AsyncTask<Void, Void, String>{

        private String idLesson;
        private long idStudent;

        MarkStudentTask(String idLesson,long idStudent){
            this.idLesson = idLesson;
            this.idStudent = idStudent;
        }

        @Override
        protected String doInBackground(Void... voids) {

            Map<String, String> param = new HashMap<>();
            param.put("idLesson", String.valueOf(idLesson));
            param.put("idStudent", String.valueOf(idStudent));

            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            String respons = template.getForObject(Constants.URL.MARK_STUDENT, String.class, param);
            return respons;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }


    }

    private class LastMarkTask extends AsyncTask<Void, Void, ArrayLessonDTO> {

        private long idStudent;

        public LastMarkTask(long idStudent){
            this.idStudent = idStudent;
        }
        @Override
        protected ArrayLessonDTO doInBackground(Void... voids) {

            Map<String, String> param = new HashMap<String, String>();
            param.put("idStudent",String.valueOf(idStudent));

            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ArrayLessonDTO stats = template.getForObject(Constants.URL.GET_LAST_MARK, ArrayLessonDTO.class, param);
            return stats;
        }

        @Override
        protected void onPostExecute(ArrayLessonDTO lessons) {
            List<SubjectDTO> mapLessons = new ArrayList<>();
            List<LessonDTO> lessonDTOs = lessons.getLessons();

            for (int i = lessons.getLessons().size()-1;i>=0; --i) {
                SubjectDTO subjectDTO = new SubjectDTO();
                subjectDTO.setTitle(lessonDTOs.get(i).getAuditorium());
                subjectDTO.setType(lessonDTOs.get(i).getDate());

                mapLessons.add(subjectDTO);
            }
            adapter.setDataObj(mapLessons);
        }
    }
}
