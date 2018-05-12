package com.qoobico.remindme.helper;

import java.net.URI;

public class Constants {

    public static final int TAB_ONE = 0;
    public static final int TAB_TWO = 1;
    public static final int TAB_THREE = 2;

    final static public String TEACHER = "Преподаватель";
    final static public String STUDENT = "Студент";
    final static public String ROLE = "Role";
    final static public String CREATE = "Create";
    final static public String CHECK = "Check";



    public static class URL {
        //88.147.140.24:8080
        //http://192.168.1.117:8080/
        //http://devoli.asuscomm.com:8080
        private static final String HOST = "http://devoli.sytes.net:8080";
        public static final String LOGIN_TEACHER = HOST + "account/loginteacher?login={login}&password={password}";
        public static final String LOGIN_STUDENT = HOST + "account/loginstudent?login={login}&password={password}";

        public static final String CHECK_LOGIN = HOST + "account/checklogin?login={login}";
        public static final String GETGROUPS = HOST + "account/getgroup";
        public static final String REGISTER_TEACHER = HOST + "account/registerteacher?login={login}&password={password}&firstname={firstn}&secondname={secondn}&lastname={lastn}";
        public static final String REGISTER_STUDENT = HOST + "account/registerstudent?login={login}&password={password}&firstname={firstn}&secondname={secondn}&lastname={lastn}&group={group}";
        public static final String GET_ID_LESSON = HOST + "user/addlesson?idTeacher={idTeacher}&aud={aud}&date={date}&numLesson={numLesson}&title={title}&type={type}&createOrCheck={createorcheck}";
        public static final String GET_STUDENTS = HOST + "user/getstudents?idLesson={idLesson}";
        public static final String MARK_STUDENT =  HOST + "user/markstudent?idLesson={idLesson}&idStudent={idStudent}";
        public static final String GET_SUBJECTS = HOST + "user/getstatistics?idTeacher={idTeacher}";
        public static final String GET_LAST_MARK = HOST + "user/last10mark?idStudent={idStudent}";
        public static final String GENERATE_CODE = HOST + "user/generatecode?idTeacher={idTeacher}&idLesson={idLesson}&numberOfCode={numberOfCode}&isMarkOpport={isMarkOpport}";
        public static final String GET_CODES =  HOST + "user/fakemark?idTeacher={idTeacher}";
        public static final String DELETE_CODES = HOST + "user/deletemarks?idTeacher={idTeacher}";
    }

}
