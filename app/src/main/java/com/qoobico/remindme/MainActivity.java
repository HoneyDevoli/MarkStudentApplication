package com.qoobico.remindme;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.qoobico.remindme.adapter.TabsFragmentAdapter;
import com.qoobico.remindme.dto.LessonDTO;
import com.qoobico.remindme.dto.StudentDTO;
import com.qoobico.remindme.dto.TeacherDTO;
import com.qoobico.remindme.helper.Constants;
import com.qoobico.remindme.helper.ParseSchedule;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_main;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private TeacherDTO teacher;
    private StudentDTO student;
    private TabsFragmentAdapter adapter;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            role = extras.getString(Constants.ROLE);
            if(role.equals(Constants.TEACHER)) teacher = (TeacherDTO) extras.getSerializable(TeacherDTO.class.getSimpleName());
            else student = (StudentDTO) extras.getSerializable(StudentDTO.class.getSimpleName());
        }

        initToolbar();
        initNavigationView();
        initTabs();
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
                        showNotificationTab();
                        break;
                    case R.id.actionNotificationItemExit:
                        goToLoginForm();
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
        } else if(role.equals((Constants.STUDENT))) {
            name.setText(String.format("%s %s %s", student.getFirstName(), student.getMiddleName(), student.getLastName()));
            login.setText(student.getLogin());
        }

    }

    private void showNotificationTab() {
        viewPager.setCurrentItem(Constants.TAB_ONE);
    }

    private void goToLoginForm() {
        finish();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

   /* private class RemindMeTask extends AsyncTask<Void, Void, RemindDTO> {

        @Override
        protected RemindDTO doInBackground(Void... params) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            return template.getForObject(Constants.URL.GET_REMIND, RemindDTO.class);
        }

        @Override
        protected void onPostExecute(RemindDTO remindDTO) {
            List<RemindDTO> data = new ArrayList<>();
            data.add(remindDTO);

            adapter.setData(data);
        }
    }*/
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
}
