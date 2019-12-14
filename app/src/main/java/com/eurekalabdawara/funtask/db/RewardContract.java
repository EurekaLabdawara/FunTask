package com.eurekalabdawara.funtask.db;

import android.provider.BaseColumns;

public class RewardContract {
    static final String DB_NAME = "com.eurekalabdawara.funtask.db";
    static final int DB_VERSION = 3;

    public class RewardEntry implements BaseColumns {
        public static final String TABLE = "rewards";

        public static final String COL_REWARD_TITLE = "reward";
        public static final String COL_REWARD_COST = "cost";
        public static final String COL_REWARD_USER = "user";
    }
}
