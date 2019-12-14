package com.eurekalabdawara.funtask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eurekalabdawara.funtask.db.UserContract;
import com.eurekalabdawara.funtask.db.UserDbHelper;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;

    private static final String SHARED_PREF_NAME = "AUTH_PREF";
    private static final String USERNAME = "username";
    private static final String IS_LOGIN = "is_login";
    private UserDbHelper mUserHelper;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserHelper = new UserDbHelper(this);

        etPassword = findViewById(R.id.etPasswordLogin);
        etUsername = findViewById(R.id.etUsernameLogin);

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Validasi Form
                String inputPassword = etPassword.getText().toString();
                if (inputPassword.isEmpty()) {
                    etPassword.setError("Password tidak boleh kosong!");
                    etPassword.requestFocus();
                    return;
                }
                String inputUsername = etUsername.getText().toString();
                if (inputUsername.isEmpty()) {
                    etUsername.setError("Username tidak boleh kosong!");
                    etUsername.requestFocus();
                    return;
                }

                //Ambil Data Dari DB
                SQLiteDatabase db = mUserHelper.getReadableDatabase();
                Cursor cursor = db.query(UserContract.UserEntry.TABLE,
                        new String[]{UserContract.UserEntry._ID, UserContract.UserEntry.COL_USER_PASSWORD},
                        UserContract.UserEntry.COL_USER_USERNAME + " = ?",
                        new String[]{inputUsername}, null, null, null, "1");
                if (cursor.moveToFirst() && cursor.getCount() < 0) {
                    Toast.makeText(getApplicationContext(), "User Tidak ditemukan", Toast.LENGTH_SHORT).show();
                } else {
                    String passwordValue = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COL_USER_PASSWORD));
//                    Check Pass
                    if (passwordValue.equals(inputPassword)) {
                        //                Login Berhasil
                        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(USERNAME, inputUsername);
                        editor.putBoolean(IS_LOGIN, true);
                        editor.apply();
                        Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                        Intent move = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(move);
                    } else {
                        // Login Gagal
                        Toast.makeText(getApplicationContext(), "Username atau Password Salah", Toast.LENGTH_SHORT).show();
                    }
                }
                cursor.close();
            }
        });

        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(moveRegister);
            }
        });
    }
}
