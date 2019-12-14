package com.eurekalabdawara.funtask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.eurekalabdawara.funtask.db.RewardDbHelper;
import com.eurekalabdawara.funtask.db.TaskDbHelper;
import com.eurekalabdawara.funtask.db.UserDbHelper;

public class LoadingActivity extends AppCompatActivity {

    private static final String SHARED_PREF_NAME = "AUTH_PREF";
    private static final String USERNAME = "username";
    private static final String IS_LOGIN = "is_login";

    private RewardDbHelper rhelper;
    private TaskDbHelper thelper;
    private UserDbHelper uhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                boolean is_login = sp.getBoolean(IS_LOGIN, false);
                if (is_login) {
                    Intent move = new Intent(LoadingActivity.this, MainActivity.class);
                    startActivity(move);
                } else {
                    Intent moveLogin = new Intent(LoadingActivity.this, LoginActivity.class);
                    startActivity(moveLogin);
                }
            }
        }, 2000);
    }
}
