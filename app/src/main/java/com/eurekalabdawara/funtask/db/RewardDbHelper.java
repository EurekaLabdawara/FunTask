package com.eurekalabdawara.funtask.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RewardDbHelper extends SQLiteOpenHelper {
    public RewardDbHelper(Context context) {
        super(context, RewardContract.DB_NAME, null, RewardContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + RewardContract.RewardEntry.TABLE + " ( " +
                RewardContract.RewardEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RewardContract.RewardEntry.COL_REWARD_TITLE + " TEXT NOT NULL, " +
                RewardContract.RewardEntry.COL_REWARD_USER + " TEXT NOT NULL, " +
                RewardContract.RewardEntry.COL_REWARD_COST + " INTEGER NOT NULL);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RewardContract.RewardEntry.TABLE);
        onCreate(db);
    }
}
