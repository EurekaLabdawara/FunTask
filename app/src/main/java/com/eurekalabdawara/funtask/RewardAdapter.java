package com.eurekalabdawara.funtask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eurekalabdawara.funtask.db.Reward;

import java.util.ArrayList;

public class RewardAdapter extends ArrayAdapter<Reward> {
    private Context mContext;
    private ArrayList<Reward> rewardList;

    RewardAdapter(@NonNull Context context, ArrayList<Reward> list) {
        super(context, 0, list);
        mContext = context;
        rewardList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_reward, parent, false);

        Reward currentReward = rewardList.get(position);

        TextView name = listItem.findViewById(R.id.reward_title);
        name.setText(currentReward.getrTitle());

        TextView release = listItem.findViewById(R.id.tvRewardCost);
        release.setText(currentReward.getrCost().toString());

        return listItem;
    }
}
