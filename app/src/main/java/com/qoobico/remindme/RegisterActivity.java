package com.qoobico.remindme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.qoobico.remindme.adapter.GroupAdapter;
import com.qoobico.remindme.adapter.GroupAutoCompleteAdapter;
import com.qoobico.remindme.dto.ArrayGroupDTO;
import com.qoobico.remindme.dto.GroupDTO;
import com.qoobico.remindme.dto.GroupFromSstuDTO;
import com.qoobico.remindme.dto.StudentDTO;
import com.qoobico.remindme.dto.TeacherDTO;
import com.qoobico.remindme.helper.Constants;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {




    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private AutoCompleteTextView mFirstNameView;
    private AutoCompleteTextView mSecondNameView;
    private AutoCompleteTextView mLastNameView;
    private View mProgressView;
    private View mLoginFormView;
    private ToggleButton mToggleButton;
    private String ROLE;

    private RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;
    private AutoCompleteTextView editsearch;
    private GroupAdapter mAdapter;

    private String group = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the login form.

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mFirstNameView = (AutoCompleteTextView) findViewById(R.id.first_name);
        mSecondNameView = (AutoCompleteTextView) findViewById(R.id.second_name);
        mLastNameView = (AutoCompleteTextView) findViewById(R.id.lastname);

        mToggleButton = (ToggleButton)findViewById(R.id.toggleButton1);
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LinearLayout layoutSearch = findViewById(R.id.layout_search);
                TextInputLayout placeLayout = findViewById(R.id.ssssst);
                if (isChecked) {
                    ROLE = mToggleButton.getTextOn().toString();
                    layoutSearch.setVisibility(View.VISIBLE);
                    placeLayout.setVisibility(View.INVISIBLE);
                } else {
                    ROLE = mToggleButton.getTextOff().toString();
                    layoutSearch.setVisibility(View.GONE);
                    placeLayout.setVisibility(View.GONE);
                }
            }
        });
        ROLE = mToggleButton.getTextOff().toString();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();

            }
        });

        Button mRegisterButton = (Button) findViewById(R.id.register_in_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
        mEmailView.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                new LoginCheckTask(s.toString()).execute();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);




        final ArrayList<String> myDataset = getDataSet();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // если мы уверены, что изменения в контенте не изменят размер layout-а RecyclerView
        // передаем параметр true - это увеличиваем производительность
        mRecyclerView.setHasFixedSize(false);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new GroupAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        editsearch =  findViewById(R.id.search);
        final GroupAutoCompleteAdapter groupAdapter = new GroupAutoCompleteAdapter(this,myDataset);
        editsearch.setAdapter(new GroupAutoCompleteAdapter(this, myDataset));


        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = editsearch.getText().toString();
                group = "";
                for (int i = 0; i < myDataset.size(); i++) {
                    if (myDataset.get(i).equals(text)) {
                        group = text;
                        break;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            }
        });

    }

    private ArrayList<String> getDataSet() {

        ArrayList<String> mDataSet = new ArrayList();
        List<GroupFromSstuDTO> g;

        GetGroupTask task = new GetGroupTask();
        task.execute();
        try {
            g = task.get(4, TimeUnit.SECONDS).getGroups();
            for (int i = 0; i < g.size(); i++) {
                mDataSet.add(i, g.get(i).getOwnPage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Timed out", Toast.LENGTH_SHORT).show();
        }
        return mDataSet;
    }




    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mFirstNameView.setError(null);
        mSecondNameView.setError(null);
        mLastNameView.setError(null);
        editsearch.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String firstName = mFirstNameView.getText().toString();
        String secondName = mSecondNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password) && password.equals("")) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (firstName.equals("")) {
            mFirstNameView.setError(getString(R.string.error_field_required));
            focusView = mFirstNameView;
            cancel = true;
        }
        if (secondName.equals("")) {
            mSecondNameView.setError(getString(R.string.error_field_required));
            focusView = mSecondNameView;
            cancel = true;
        }
        if (lastName.equals("")) {
            mLastNameView.setError(getString(R.string.error_field_required));
            focusView = mLastNameView;
            cancel = true;
        }

        if (group.equals("") && ROLE.equals(Constants.STUDENT) ) {
            editsearch.setError(getString(R.string.error_field_required));
            focusView = editsearch;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password, firstName, secondName, lastName, group);
            mAuthTask.execute((Void) null);
        }
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }




    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mFirstName;
        private final String mSecondName;
        private final String mLastName;
        private final String mGroup;
        private TeacherDTO teacher;
        private StudentDTO student;

        UserLoginTask(String email, String password, String firstName, String secondName, String lastName, String group) {
            mEmail = email;
            mPassword = password;
            mFirstName = firstName;
            mSecondName = secondName;
            mLastName = lastName;
            mGroup = group;

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Map<String, String> param = new HashMap<String, String>();
            param.put("login", mEmail);
            param.put("password",mPassword);
            param.put("firstn",mFirstName);
            param.put("secondn",mSecondName);
            param.put("lastn", mLastName);
            param.put("group",group);

            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            if(ROLE.equals(Constants.TEACHER)) {
                teacher = template.getForObject(Constants.URL.REGISTER_TEACHER, TeacherDTO.class, param);
            } else {
                student = template.getForObject(Constants.URL.REGISTER_STUDENT, StudentDTO.class, param);
            }
            // TODO: register the new account here.
            return teacher!=null || student!=null;
        }



        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                if(ROLE.equals(Constants.TEACHER)) {
                    intent.putExtra(TeacherDTO.class.getSimpleName(), teacher);
                } else{
                    intent.putExtra(StudentDTO.class.getSimpleName(), student);
                }
                intent.putExtra(Constants.ROLE,ROLE);
                startActivity(intent);
            } else {
                mPasswordView.setError(getString(R.string.error_unknown_error));
                mPasswordView.requestFocus();
            }

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public class LoginCheckTask extends AsyncTask<Void, Void, Boolean> {

        private List<GroupDTO> groups;
        private TeacherDTO teacher;
        private String login;
        LoginCheckTask(String login) {
            this.login = login;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Map<String, String> param = new HashMap<String, String>();
            param.put("login", login);

            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            teacher = template.getForObject(Constants.URL.CHECK_LOGIN, TeacherDTO.class, param);


            // TODO: register the new account here.
            return teacher.getLogin() != null;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {

            } else {
                mEmailView.setError(getString(R.string.error_occupied_email));
                mEmailView.requestFocus();
            }

        }
    }

    public class GetGroupTask extends AsyncTask<Void, Void, ArrayGroupDTO> {

        @Override
        protected ArrayGroupDTO doInBackground(Void... params) {

            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ArrayGroupDTO groups = template.getForObject(Constants.URL.GETGROUPS, ArrayGroupDTO.class);

            return groups;
        }


        @Override
        protected void onPostExecute(final ArrayGroupDTO groups) {
            super.onPostExecute(groups );

        }
    }




}

