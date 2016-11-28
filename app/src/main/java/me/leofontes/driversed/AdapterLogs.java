package me.leofontes.driversed;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Leo on 3/22/16.
 */
public class AdapterLogs extends BaseAdapter {
    private Activity activity;
    private ArrayList<DriveInfo> logs;

    public AdapterLogs(Activity activity, ArrayList<DriveInfo> logs) {
        this.activity = activity;
        this.logs = logs;
    }

    @Override
    public int getCount() {
        return logs.size();
    }

    @Override
    public Object getItem(int position) {
        return logs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.lesson_log, null);

        if(convertView != null) {
            TextView hours = (TextView) convertView.findViewById(R.id.TextViewHours);
            TextView day = (TextView) convertView.findViewById(R.id.TextViewDay);
            TextView date = (TextView) convertView.findViewById(R.id.textViewDate);

            DriveInfo log = logs.get(position);

            hours.setText(log.getHours());
            day.setText(log.getDayOfTheWeek());
            date.setText(log.getDate());
        }

        return convertView;
    }
}

