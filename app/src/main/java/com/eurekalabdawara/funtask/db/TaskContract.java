package com.eurekalabdawara.funtask.db;

import android.provider.BaseColumns;

public class TaskContract {
    static final String DB_NAME = "com.eurekalabdawara.funtask.db";
    static final int DB_VERSION = 3;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";

        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_STATUS = "status";
        public static final String COL_TASK_POINT = "point";
        public static final String COL_TASK_USER = "user";
    }
}
