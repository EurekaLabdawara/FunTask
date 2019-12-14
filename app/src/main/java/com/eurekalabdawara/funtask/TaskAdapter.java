package com.eurekalabdawara.funtask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eurekalabdawara.funtask.db.Task;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<Task> {
    private Context mContext;
    private ArrayList<Task> tasksList;

    TaskAdapter(@NonNull Context context, ArrayList<Task> list) {
        super(context, 0, list);
        mContext = context;
        tasksList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_todo, parent, false);

        Task currentTask = tasksList.get(position);

        TextView name = listItem.findViewById(R.id.task_title);
        name.setText(currentTask.gettTitle());

        TextView release = listItem.findViewById(R.id.tvTaskReward);
        release.setText(currentTask.gettReward().toString());

        return listItem;
    }
}
