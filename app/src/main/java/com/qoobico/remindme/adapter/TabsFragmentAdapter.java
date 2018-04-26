package com.qoobico.remindme.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.qoobico.remindme.R;
import com.qoobico.remindme.dto.LessonDTO;
import com.qoobico.remindme.dto.RemindDTO;
import com.qoobico.remindme.fragment.AbstractTabFragment;
import com.qoobico.remindme.fragment.HistoryFragment;
import com.qoobico.remindme.fragment.IdeasFragment;
import com.qoobico.remindme.fragment.TodoFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabsFragmentAdapter extends FragmentPagerAdapter {

    private Map<Integer, AbstractTabFragment> tabs;
    private Context context;

    private List<LessonDTO> data1;
    private List<LessonDTO> data2;

    private HistoryFragment historyFragment;
    private HistoryFragment secondWeekFragment;

    public TabsFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        data1 = new ArrayList<>();
        data2 = new ArrayList<>();
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
        historyFragment = HistoryFragment.getInstance(context, data1);
        historyFragment.setTitle(context.getString(R.string.tab_item_history));
        secondWeekFragment = HistoryFragment.getInstance(context, data2);
        secondWeekFragment.setTitle(context.getString(R.string.tab_item_ideas));
        tabs.put(0, historyFragment);
        tabs.put(1, secondWeekFragment);
        tabs.put(2, TodoFragment.getInstance(context));
    }

    public void setData(List<LessonDTO> data) {
        int numEndOfWeek = 0;
        int i;
        for (i= 0; i < data.size(); i++) {
            if(data.get(i).getDate() != null)
                numEndOfWeek++;
            if(numEndOfWeek == 7)
                break;
        }
        this.data1 = data.subList(0,i-1);
        historyFragment.refreshList(data1);
        this.data2 = data.subList(i,data.size());
        secondWeekFragment.refreshList(data2);
    }
}
