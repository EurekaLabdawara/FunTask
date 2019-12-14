package com.eurekalabdawara.funtask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eurekalabdawara.funtask.db.Task;
import com.eurekalabdawara.funtask.db.TaskContract;
import com.eurekalabdawara.funtask.db.TaskDbHelper;

import java.util.ArrayList;

public class CompletedTaskActivity extends AppCompatActivity {

    private static final String TAG = "CompletedTaskActivity";
    private TaskDbHelper mTaskHelper;
    private ListView mCompletedTaskListView;

    private static final String SHARED_PREF_NAME = "AUTH_PREF";
    private static final String USERNAME = "username";

    private TaskCompletedAdapter tAdapter;

    private String login_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_task);

        mTaskHelper = new TaskDbHelper(this);

        mCompletedTaskListView = findViewById(R.id.list_completed_todo);

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        login_username = sp.getString(USERNAME, null);

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.completed_task_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        switch (item.getItemId()) {
            case R.id.action_view_dashboard:
                Intent moveDashboard = new Intent(CompletedTaskActivity.this, MainActivity.class);
                startActivity(moveDashboard);
                return true;

            case R.id.action_logout:
                SharedPreferences.Editor editor = sp.edit();
                editor.clear().apply();
                Toast.makeText(getApplicationContext(), "Logout Berhasil", Toast.LENGTH_SHORT).show();
                Intent logout = new Intent(CompletedTaskActivity.this, LoginActivity.class);
                startActivity(logout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        ArrayList<Task> tasksList = new ArrayList<>();

//        Task List Update
        SQLiteDatabase dbTask = mTaskHelper.getReadableDatabase();
        Cursor cursor = dbTask.rawQuery(
                "SELECT  * FROM " + TaskContract.TaskEntry.TABLE
                        + " WHERE " + TaskContract.TaskEntry.COL_TASK_USER + " = ?  AND "
                        + TaskContract.TaskEntry.COL_TASK_STATUS
                        + " = ?",
                new String[]{login_username, "Done"});
        while (cursor.moveToNext()) {
            tasksList.add(new Task(
                    cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE)),
                    cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_POINT))
            ));
        }
        cursor.close();
        dbTask.close();

        if (tAdapter == null) {
            tAdapter = new TaskCompletedAdapter(CompletedTaskActivity.this, tasksList);
            mCompletedTaskListView.setAdapter(tAdapter);
        } else {
            tAdapter.clear();
            tAdapter.addAll(tasksList);
            tAdapter.notifyDataSetChanged();
        }
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mTaskHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }
}
