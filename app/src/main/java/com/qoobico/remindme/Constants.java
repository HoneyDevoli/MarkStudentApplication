package com.qoobico.remindme;

import java.net.URI;

public class Constants {

    public static final int TAB_ONE = 0;
    public static final int TAB_TWO = 1;
    public static final int TAB_THREE = 2;

    public static class URL {
        //88.147.140.24:8080
        private static final String HOST = "http://192.168.1.117:8080/";
        public static final String GET_REMIND = HOST + "reminder/get";
        public static final String LOGIN_TEACHER = HOST + "account/loginteacher?login={login}&password={password}";
    }

}
