package com.qoobico.remindme.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qoobico.remindme.MainActivity;
import com.qoobico.remindme.MarkActivity;
import com.qoobico.remindme.R;
import com.qoobico.remindme.dto.ArrayStatsDTO;
import com.qoobico.remindme.dto.LessonDTO;
import com.qoobico.remindme.dto.StatisticsDTO;
import com.qoobico.remindme.dto.SubjectDTO;
import com.qoobico.remindme.fragment.AbstractTabFragment;
import com.qoobico.remindme.fragment.LastMarkFragment;
import com.qoobico.remindme.fragment.ScheduleFragment;
import com.qoobico.remindme.fragment.SubjectFragment;
import com.qoobico.remindme.helper.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;

public class TabsFragmentAdapter extends FragmentPagerAdapter {

    private Map<Integer, AbstractTabFragment> tabs;
    private Context context;

    private List<LessonDTO> data1;
    private List<LessonDTO> data2;
    private List<SubjectDTO> data3;

    private ScheduleFragment historyFragment;
    private ScheduleFragment secondWeekFragment;
    private SubjectFragment subjectFragment;
    private LastMarkFragment lastMarkFragment;

    public TabsFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        data1 = new ArrayList<>();
        data2 = new ArrayList<>();
        data3 = new ArrayList<>();
        this.context = context;
        initTabsMap(context);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }


    private void initTabsMap(Context context) {
        tabs = new HashMap<>();
        historyFragment = ScheduleFragment.getInstance(context, data1);
        historyFragment.setTitle(context.getString(R.string.tab_item_history));

        secondWeekFragment = ScheduleFragment.getInstance(context, data2);
        secondWeekFragment.setTitle(context.getString(R.string.tab_item_ideas));

        tabs.put(0, historyFragment);
        tabs.put(1, secondWeekFragment);

        if(MainActivity.getRole().equals(Constants.TEACHER)) {
            subjectFragment = SubjectFragment.getInstance(context, data3);
            subjectFragment.setTitle(context.getString(R.string.tab_item_todo));
            tabs.put(2, subjectFragment);
        } else {
            lastMarkFragment = LastMarkFragment.getInstance(context,data3);
            lastMarkFragment.setTitle("Последние отметки");
            tabs.put(2,lastMarkFragment);
        }




    }

    public void setData(List<LessonDTO> data) {
        if (data.size() != 0) {
            int numEndOfWeek = 0;
            int i;
            for (i = 0; i < data.size(); i++) {
                if (data.get(i).getDate() != null)
                    numEndOfWeek++;
                if (numEndOfWeek == 7)
                    break;
            }

            this.data1 = data.subList(0, i - 1);
            this.data2 = data.subList(i, data.size());
        } else {
            LessonDTO lesson = new LessonDTO();
            lesson.setNumberOfLesson(0);
            lesson.setType("");
            lesson.setTitle("Расписание не найдено. Неверные данные!");
            lesson.setAuditorium("");
            lesson.setTeacherOrGroup("");
            this.data1.add(lesson);
            this.data2.add(lesson);
        }
        historyFragment.refreshList(data1);
        secondWeekFragment.refreshList(data2);
    }

    public void setDataObj(ArrayStatsDTO data){
        if(data != null) {
            for (StatisticsDTO stat : data.getStats()){
                this.data3.add(stat.getSubject());
            }
            subjectFragment.refreshList(data3,data);
        } else {

        }
    }

    public void setDataObj(List<SubjectDTO> data){
        if(data != null) {
            lastMarkFragment.refreshList(data);
        } else {

        }
    }
}
