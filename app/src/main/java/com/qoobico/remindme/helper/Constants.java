package com.qoobico.remindme.helper;

import java.net.URI;

public class Constants {

    public static final int TAB_ONE = 0;
    public static final int TAB_TWO = 1;
    public static final int TAB_THREE = 2;

    final static public String TEACHER = "Teacher";
    final static public String STUDENT = "Student";
    final static public String ROLE = "Role";


    public static class URL {
        //88.147.140.24:8080
        //http://192.168.1.117:8080/
        //http://devoli.asuscomm.com:8080
        private static final String HOST = "http://devoli.asuscomm.com:8080/";
        public static final String LOGIN_TEACHER = HOST + "account/loginteacher?login={login}&password={password}";
        public static final String LOGIN_STUDENT = HOST + "account/loginstudent?login={login}&password={password}";

        public static final String CHECK_LOGIN = HOST + "account/checklogin?login={login}";
        public static final String GETGROUPS = HOST + "account/getgroup";
        public static final String REGISTER_TEACHER = HOST + "account/registerteacher?login={login}&password={password}&firstname={firstn}&secondname={secondn}&lastname={lastn}";
        public static final String REGISTER_STUDENT = HOST + "account/registerstudent?login={login}&password={password}&firstname={firstn}&secondname={secondn}&lastname={lastn}&group={group}";
    }

}
