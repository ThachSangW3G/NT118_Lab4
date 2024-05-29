package com.example.lab4_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BatteryReceiver extends BroadcastReceiver {
    private static final String TAG = "BatteryReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (Intent.ACTION_POWER_CONNECTED.equals(action)) {
            Log.d(TAG, "Phone is charging");
            Toast.makeText(context, "Phone is charging", Toast.LENGTH_SHORT).show();
        } else if (Intent.ACTION_POWER_DISCONNECTED.equals(action)) {
            Log.d(TAG, "Phone is not charging");
            Toast.makeText(context, "Phone is not charging", Toast.LENGTH_SHORT).show();
        }
    }
}
