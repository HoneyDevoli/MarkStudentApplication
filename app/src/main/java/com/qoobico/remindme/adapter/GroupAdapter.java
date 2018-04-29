package com.qoobico.remindme.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.qoobico.remindme.R;

import java.util.ArrayList;
import java.util.Locale;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private ArrayList<String> mDataset;
    private ArrayList<String> mCleanCopyDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.group_text_view);

        }


    }

    public GroupAdapter(ArrayList<String> dataset) {
        mDataset = dataset;
        mCleanCopyDataset = mDataset;
    }

    public void setDataSet(ArrayList<String> dataset){
        mDataset = dataset;
        notifyDataSetChanged();
    }

    public ArrayList<String> getDataSet(){
        return mDataset;
    }

    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Заменяет контент отдельного view (вызывается layout manager-ом)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mTextView.setText(mDataset.get(position));

    }

    // Возвращает размер данных (вызывается layout manager-ом)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mDataset = new ArrayList<String>();
        if (charText.length() == 0) {
            mDataset.addAll(mCleanCopyDataset);
        } else {
            for (String item : mCleanCopyDataset) {
                if (item.toLowerCase(Locale.getDefault()).contains(charText)) {
                    mDataset.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}


