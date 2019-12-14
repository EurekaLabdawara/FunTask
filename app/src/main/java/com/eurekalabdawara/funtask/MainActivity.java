package com.eurekalabdawara.funtask;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.eurekalabdawara.funtask.db.Reward;
import com.eurekalabdawara.funtask.db.RewardContract;
import com.eurekalabdawara.funtask.db.RewardDbHelper;
import com.eurekalabdawara.funtask.db.Task;
import com.eurekalabdawara.funtask.db.TaskContract;
import com.eurekalabdawara.funtask.db.TaskDbHelper;
import com.eurekalabdawara.funtask.db.UserContract;
import com.eurekalabdawara.funtask.db.UserDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private UserDbHelper mUserHelper;
    private TaskDbHelper mTaskHelper;
    private RewardDbHelper mRewardHelper;

    private ListView mTaskListView;
    private ListView mRewardListView;

    private TaskAdapter tAdapter;
    private RewardAdapter rAdapter;

    private static final String SHARED_PREF_NAME = "AUTH_PREF";
    private static final String USERNAME = "username";

    private Menu menu;
    private String login_username;
    private int coinCurrently;

    TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserHelper = new UserDbHelper(this);
        mTaskHelper = new TaskDbHelper(this);
        mRewardHelper = new RewardDbHelper(this);

        mTaskListView = findViewById(R.id.list_todo);
        mRewardListView = findViewById(R.id.list_reward);

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        login_username = sp.getString(USERNAME, null);

        tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setText("Hello " + login_username + "!");

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        this.menu = menu;
        updateMenuTitles();
        return super.onCreateOptionsMenu(menu);
    }

    private void updateMenuTitles() {
        MenuItem menuCoinValue = menu.findItem(R.id.coins_value);
//        SQLiteDatabase dbUser = mUserHelper.getReadableDatabase();
        SQLiteDatabase dbUser = mUserHelper.getReadableDatabase();
        Cursor cursorUser = dbUser.rawQuery(
                "SELECT  * FROM " + UserContract.UserEntry.TABLE
                        + " WHERE " + UserContract.UserEntry.COL_USER_USERNAME + " = ?",
                new String[]{login_username});
        if (cursorUser.getCount() > 0 && cursorUser.moveToFirst()) {
            coinCurrently = cursorUser.getInt(cursorUser.getColumnIndex(UserContract.UserEntry.COL_USER_POINT));
        }
        cursorUser.close();


        menuCoinValue.setTitle(Integer.toString(coinCurrently));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        switch (item.getItemId()) {
            case R.id.action_add_task:
                Log.d(TAG, "Add a new task");
                AlertDialog dialog = new AlertDialog.Builder(this).setView(R.layout.dialog_add_task)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Dialog f = (Dialog) dialog;
                                EditText taskName = f.findViewById(R.id.etTaskName);
                                EditText taskReward = f.findViewById(R.id.etTaskReward);
                                Log.d(TAG, "Task to add: " + taskName.getText().toString());
                                Log.d(TAG, "Reward Task to add: " + taskReward.getText());
                                SQLiteDatabase db = mTaskHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, taskName.getText().toString());
                                values.put(TaskContract.TaskEntry.COL_TASK_STATUS, "Not Done");
                                values.put(TaskContract.TaskEntry.COL_TASK_POINT, taskReward.getText().toString());
                                values.put(TaskContract.TaskEntry.COL_TASK_USER, login_username);
                                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                MainActivity.this.updateUI();
                                MainActivity.this.updateMenuTitles();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;

            case R.id.action_add_reward:
                Log.d(TAG, "Add a new reward");
                AlertDialog dialogReward = new AlertDialog.Builder(this).setView(R.layout.dialog_add_reward)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Dialog f = (Dialog) dialog;
                                EditText rewardName = f.findViewById(R.id.etRewardName);
                                EditText rewardCost = f.findViewById(R.id.etRewardCost);
//                                String rewardName = String.valueOf(R.id.etRewardName);
//                                Integer pointCost = R.id.etRewardCost;
                                Log.d(TAG, "Reward to add: " + rewardName);
                                SQLiteDatabase db = mRewardHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(RewardContract.RewardEntry.COL_REWARD_TITLE, rewardName.getText().toString());
                                values.put(RewardContract.RewardEntry.COL_REWARD_USER, login_username);
                                values.put(RewardContract.RewardEntry.COL_REWARD_COST, rewardCost.getText().toString());
                                db.insertWithOnConflict(RewardContract.RewardEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                MainActivity.this.updateUI();
                                MainActivity.this.updateMenuTitles();

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialogReward.show();
                return true;

            case R.id.action_logout:
                SharedPreferences.Editor editor = sp.edit();
                editor.clear().apply();
                Toast.makeText(getApplicationContext(), "Logout Berhasil", Toast.LENGTH_SHORT).show();
                Intent logout = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(logout);
                return true;
            case R.id.action_view_completed_task:
                Intent moveCompletedTask = new Intent(MainActivity.this, CompletedTaskActivity.class);
                startActivity(moveCompletedTask);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        ArrayList<Task> tasksList = new ArrayList<>();
        ArrayList<Reward> rewardsList = new ArrayList<>();

//        Task List Update
        SQLiteDatabase dbTask = mTaskHelper.getWritableDatabase();
        Cursor cursor = dbTask.rawQuery(
                "SELECT  * FROM " + TaskContract.TaskEntry.TABLE
                        + " WHERE " + TaskContract.TaskEntry.COL_TASK_USER + " = ?  AND "
                        + TaskContract.TaskEntry.COL_TASK_STATUS
                        + " = ?",
                new String[]{login_username, "Not Done"});
        while (cursor.moveToNext()) {
            tasksList.add(new Task(
                    cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE)),
                    cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_POINT))
            ));
        }
        cursor.close();
        dbTask.close();

        if (tAdapter == null) {
            tAdapter = new TaskAdapter(MainActivity.this, tasksList);
            mTaskListView.setAdapter(tAdapter);
        } else {
            tAdapter.clear();
            tAdapter.addAll(tasksList);
            tAdapter.notifyDataSetChanged();
        }

        SQLiteDatabase dbReward = mRewardHelper.getWritableDatabase();
        Cursor cursorReward = dbReward.rawQuery(
                "SELECT  * FROM " + RewardContract.RewardEntry.TABLE
                        + " WHERE " + RewardContract.RewardEntry.COL_REWARD_USER + " = ?",
                new String[]{login_username});
        while (cursorReward.moveToNext()) {
            rewardsList.add(new Reward(
                    cursorReward.getString(cursorReward.getColumnIndex(RewardContract.RewardEntry.COL_REWARD_TITLE)),
                    cursorReward.getInt(cursorReward.getColumnIndex(RewardContract.RewardEntry.COL_REWARD_COST))
            ));
        }
        cursorReward.close();
        dbReward.close();

        if (rAdapter == null) {
            rAdapter = new RewardAdapter(MainActivity.this, rewardsList);
            mRewardListView.setAdapter(rAdapter);
        } else {
            rAdapter.clear();
            rAdapter.addAll(rewardsList);
            rAdapter.notifyDataSetChanged();
        }
    }

    public void taskDone(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        TextView taskReward = parent.findViewById(R.id.tvTaskReward);
        String taskRewardValue = String.valueOf(taskReward.getText());
        SQLiteDatabase db = mTaskHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TaskContract.TaskEntry.COL_TASK_STATUS, "Done");
        db.update(TaskContract.TaskEntry.TABLE
                , cv,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();

        int newValue = coinCurrently + Integer.valueOf(taskRewardValue);
        String updatedCoinValue = String.valueOf(newValue);
        SQLiteDatabase dbUser = mUserHelper.getWritableDatabase();
        ContentValues cvUserUpdate = new ContentValues();
        cvUserUpdate.put(UserContract.UserEntry.COL_USER_POINT, updatedCoinValue);
        dbUser.update(UserContract.UserEntry.TABLE,
                cvUserUpdate,
                UserContract.UserEntry.COL_USER_USERNAME + " = ?",
                new String[]{login_username});
        dbUser.close();
        updateUI();
        updateMenuTitles();
    }

    public void useReward(View view) {
        View parent = (View) view.getParent();
        TextView usedReward = parent.findViewById(R.id.tvRewardCost);
        String rewardCost = String.valueOf(usedReward.getText());
        int newValue = coinCurrently - Integer.valueOf(rewardCost);
        String updatedCoinValue = String.valueOf(newValue);
        SQLiteDatabase dbUser = mUserHelper.getWritableDatabase();
        ContentValues cvUserUpdate = new ContentValues();
        cvUserUpdate.put(UserContract.UserEntry.COL_USER_POINT, updatedCoinValue);
        dbUser.update(UserContract.UserEntry.TABLE,
                cvUserUpdate,
                UserContract.UserEntry.COL_USER_USERNAME + " = ?",
                new String[]{login_username});
        dbUser.close();
        updateUI();
        updateMenuTitles();

    }

    public void deleteReward(View view) {
        View parent = (View) view.getParent();
        TextView rewardTextView = parent.findViewById(R.id.reward_title);
        String reward = String.valueOf(rewardTextView.getText());
        SQLiteDatabase db = mRewardHelper.getWritableDatabase();
        db.delete(RewardContract.RewardEntry.TABLE,
                RewardContract.RewardEntry.COL_REWARD_TITLE + " = ?",
                new String[]{reward});
        db.close();
        updateUI();
    }
}
