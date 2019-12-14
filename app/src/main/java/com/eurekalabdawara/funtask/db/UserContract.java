package com.eurekalabdawara.funtask.db;

import android.provider.BaseColumns;

public class UserContract {
    static final String DB_NAME = "com.eurekalabdawara.funtask.db";
    static final int DB_VERSION = 3;

    public class UserEntry implements BaseColumns {
        public static final String TABLE = "users";

        public static final String COL_USER_USERNAME = "username";
        public static final String COL_USER_PASSWORD = "password";
        public static final String COL_USER_POINT = "point";
    }
}
