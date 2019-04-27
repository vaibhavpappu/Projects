package com.matrixhub.vaibhav.merabyke;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

public class Restarter extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Toast.makeText(context, "Broadcast Listened Service tried to stop", Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            context.startForegroundService(new Intent(context, FloatingViewService.class));
        }
        else
            {
            context.startService(new Intent(context, FloatingViewService.class));
        }
    }
}

