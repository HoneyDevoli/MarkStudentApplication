package com.qoobico.remindme.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.qoobico.remindme.MainActivity;
import com.qoobico.remindme.R;
import com.qoobico.remindme.adapter.SubjectAdapter;
import com.qoobico.remindme.dto.ArrayLessonDTO;
import com.qoobico.remindme.dto.ArrayStatsDTO;
import com.qoobico.remindme.dto.LessonDTO;
import com.qoobico.remindme.dto.SubjectDTO;
import com.qoobico.remindme.helper.Constants;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LastMarkFragment extends AbstractTabFragment {
    private static final int LAYOUT = R.layout.fragment_example;

    private SubjectAdapter adapter;
    private List<SubjectDTO> data;
    private Button updateBut;

    public static LastMarkFragment getInstance(Context context, List<SubjectDTO> data) {
        Bundle args = new Bundle();
        LastMarkFragment fragment = new LastMarkFragment();
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

        updateBut = view.findViewById(R.id.button_update_subject);
        updateBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SubjectTask(MainActivity.getId()).execute();
            }
        });


        return view;
    }

    public void refreshList(List<SubjectDTO> data) {
        adapter.setData(data);
    }



    public void setContext(Context context) {
        this.context = context;
    }

    public void setData(List<SubjectDTO> data) {
        this.data = data;
    }


    private class SubjectTask extends AsyncTask<Void, Void, ArrayLessonDTO> {

        private long idStudent;

        public SubjectTask(long idStudent){
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
            refreshList(mapLessons);
        }
    }

}
