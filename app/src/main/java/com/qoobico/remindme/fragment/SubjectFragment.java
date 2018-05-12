package com.qoobico.remindme.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.qoobico.remindme.MainActivity;
import com.qoobico.remindme.R;
import com.qoobico.remindme.SubjectStatsActivity;
import com.qoobico.remindme.adapter.SubjectAdapter;
import com.qoobico.remindme.dto.ArrayStatsDTO;
import com.qoobico.remindme.dto.StatisticsDTO;
import com.qoobico.remindme.dto.SubjectDTO;
import com.qoobico.remindme.helper.Constants;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectFragment extends AbstractTabFragment {
    private static final int LAYOUT = R.layout.fragment_example;

    private SubjectAdapter adapter;
    private List<SubjectDTO> data;
    private ArrayStatsDTO stats;
    private Button updateBut;

    public static SubjectFragment getInstance(Context context, List<SubjectDTO> data) {
        Bundle args = new Bundle();
        SubjectFragment fragment = new SubjectFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setRetainInstance(true);
        fragment.setData(data);
        fragment.setTitle(context.getString(R.string.tab_item_todo));

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        data = new ArrayList<>();
        ListView lv = view.findViewById(R.id.subject_list);
        adapter = new SubjectAdapter(view.getContext(),R.layout.subject_item,data);
        lv.setAdapter(adapter);
        lv.setBackgroundColor(Color.WHITE);

        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubjectDTO selectedSub = (SubjectDTO) parent.getItemAtPosition(position);
                Intent intent = new Intent(getContext(), SubjectStatsActivity.class);

                for(StatisticsDTO stat : stats.getStats()){
                    if(stat.getSubject().getId() == selectedSub.getId()) {
                        intent.putExtra(StatisticsDTO.class.getSimpleName(), stat);
                        startActivity(intent);

                    }
                }

            }
        };
        lv.setOnItemClickListener(itemListener);

        updateBut = view.findViewById(R.id.button_update_subject);
        updateBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SubjectTask(MainActivity.getId()).execute();
            }
        });


        return view;
    }

    public void refreshList(List<SubjectDTO> data, ArrayStatsDTO stats) {
        adapter.setData(data);
        this.stats = stats;
    }

    public void refreshList(ArrayStatsDTO stats){
        this.stats = stats;
    }


    public void setContext(Context context) {
        this.context = context;
    }

    public void setData(List<SubjectDTO> data) {
        this.data = data;
    }


    private class SubjectTask extends AsyncTask<Void, Void, ArrayStatsDTO> {

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
            refreshList(stats);
        }
    }

}
