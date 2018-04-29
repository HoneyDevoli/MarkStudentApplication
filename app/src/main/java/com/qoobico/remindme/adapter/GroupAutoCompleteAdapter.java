package com.qoobico.remindme.adapter;

import android.content.Context;
import android.mtp.MtpConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.qoobico.remindme.R;
import com.qoobico.remindme.dto.GroupFromSstuDTO;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GroupAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private static final int MAX_RESULTS = 10;

    private final Context mContext;
    private List<String> mResults;
    private List<String> mAllResults;

    public GroupAutoCompleteAdapter(Context context, List<String> groups) {
        mContext = context;
        mAllResults = groups;
        mResults = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public String getItem(int index) {
        return mResults.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }
        String group = getItem(position);
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(group);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<String> filterGroup= new ArrayList<>();
                    String myConstraint = constraint.toString().toLowerCase();
                    for(String group : mAllResults) {
                        if(group.toLowerCase().contains(myConstraint))
                            filterGroup.add(group);
                    }
                    filterResults.values = filterGroup;
                    filterResults.count = filterGroup.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    mResults = (List<String>)results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};

        return filter;
    }

    public Collection getCurrentGroups() {
        return mResults;
    }

}
