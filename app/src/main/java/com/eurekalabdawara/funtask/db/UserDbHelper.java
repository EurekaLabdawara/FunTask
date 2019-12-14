package com.eurekalabdawara.funtask.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDbHelper extends SQLiteOpenHelper {

    public UserDbHelper(Context context) {
        super(context, UserContract.DB_NAME, null, UserContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + UserContract.UserEntry.TABLE + " ( " +
                UserContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserContract.UserEntry.COL_USER_USERNAME + " TEXT NOT NULL, " +
                UserContract.UserEntry.COL_USER_PASSWORD + " TEXT NOT NULL, " +
                UserContract.UserEntry.COL_USER_POINT + " INTEGER NOT NULL);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE);
        onCreate(db);
    }
}
