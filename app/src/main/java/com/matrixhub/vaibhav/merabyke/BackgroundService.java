package com.matrixhub.vaibhav.merabyke;

import android.Manifest;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BackgroundService extends Service
{
    BluetoothDevice device;
    BroadcastReceiver mReceiver;
    String btName;
    @Override
    public IBinder onBind(Intent intent)
    {

        return null;
    }

    @Override
    public void onCreate()
    {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        startService(new Intent(this,BackgroundService.class));
         final BroadcastReceiver mReceiver = new BroadcastReceiver()
         {
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    device = intent
                            .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String name=device.getAddress();
                    Toast.makeText(context, "device:"+name, Toast.LENGTH_SHORT).show();
                }
            }
        };


        String manufacturer = "xiaomi";
        if(manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER))
        {
            //this will open auto start screen where user can enable permission for your app
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            startActivity(intent);
        }

        super.onCreate();
    }


    @Override
    public void onStart(Intent intent, int startId)
    {
                super.onStart(intent, startId);
    }



    @Override
    public void onDestroy()
    {

        super.onDestroy();
    }
}
