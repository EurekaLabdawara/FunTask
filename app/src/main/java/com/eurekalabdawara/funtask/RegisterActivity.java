package com.eurekalabdawara.funtask;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eurekalabdawara.funtask.db.UserContract;
import com.eurekalabdawara.funtask.db.UserDbHelper;

public class RegisterActivity extends AppCompatActivity {

    EditText etUsername, etPassword, etConfirmPassword;
    private UserDbHelper mUserHelper;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsernameRegister);
        etPassword = findViewById(R.id.etPasswordRegister);
        etConfirmPassword = findViewById(R.id.etConfirmPasswordRegister);
        mUserHelper = new UserDbHelper(this);

        findViewById(R.id.btnActionRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Validasi Form
                String ConfirmPassword = etConfirmPassword.getText().toString();
                if (ConfirmPassword.isEmpty()) {
                    etConfirmPassword.setError("Confirm Password tidak boleh kosong!");
                    etConfirmPassword.requestFocus();
                    return;
                }
                String addPassword = etPassword.getText().toString();
                if (addPassword.isEmpty()) {
                    etPassword.setError("Password tidak boleh kosong!");
                    etPassword.requestFocus();
                    return;
                }
                if (!ConfirmPassword.equals(addPassword)) {
                    etConfirmPassword.setError("Confirm Password tidak sama dengan Password!");
                    etConfirmPassword.requestFocus();
                    return;
                }

                String addUsername = etUsername.getText().toString();
                if (addUsername.isEmpty()) {
                    etUsername.setError("Username tidak boleh kosong!");
                    etUsername.requestFocus();
                    return;
                }
//                Check username sudah dipakai
                SQLiteDatabase db = mUserHelper.getReadableDatabase();
                Cursor cursor = db.query(UserContract.UserEntry.TABLE,
                        new String[]{UserContract.UserEntry._ID, UserContract.UserEntry.COL_USER_USERNAME},
                        UserContract.UserEntry.COL_USER_USERNAME + " = ?",
                        new String[]{addUsername}, null, null, null, null);
                if (cursor.getCount() > 0) {
                    Toast.makeText(getApplicationContext(), "Username sudah dipakai", Toast.LENGTH_SHORT).show();
                    etUsername.setError("Username sudah dipakai!");
                    etUsername.requestFocus();
                    return;
                } else {
//               Buat row
                    Log.d(TAG, "User created: " + addUsername);
                    ContentValues values = new ContentValues();
                    values.put(UserContract.UserEntry.COL_USER_USERNAME, addUsername);
                    values.put(UserContract.UserEntry.COL_USER_PASSWORD, addPassword);
                    values.put(UserContract.UserEntry.COL_USER_POINT, 0);
                    db.insertWithOnConflict(UserContract.UserEntry.TABLE,
                            null,
                            values,
                            SQLiteDatabase.CONFLICT_REPLACE);
                    db.close();
//               Berhasil
                    Toast.makeText(getApplicationContext(), "User berhasil dibuat", Toast.LENGTH_SHORT).show();
                    Intent moveLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(moveLogin);
                }
                cursor.close();
            }
        });

        findViewById(R.id.btnViewLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(moveLogin);
            }
        });
    }
}
