package com.matrixhub.vaibhav.merabyke;
import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class FloatingViewService extends Service implements View.OnClickListener
{


    private WindowManager mWindowManager;
    private View mFloatingView;
    private View collapsedView;
    private View expandedView;
    ProgressBar progressBar;
    BluetoothDevice device;
    String devicename;
    ArrayList btAddressList,btNameList;
    public FloatingViewService()
    {
    }

    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        super.onTaskRemoved(rootIntent);
        startService(new Intent(FloatingViewService.this, FloatingViewService.class));


    }

    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;

    }
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;

    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        //getting the widget layout from xml using layout inflater

        searchDevice();

        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

        //setting the layout parameters
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams
                (
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

                    //getting windows services and adding the floating view to it
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);
        //getting the collapsed and expanded view from the floating view
        collapsedView = mFloatingView.findViewById(R.id.layoutCollapsed);
        expandedView = mFloatingView.findViewById(R.id.layoutExpanded);
        //adding click listener to close button and expanded view

      //  mFloatingView.findViewById(R.id.buttonClose).setOnClickListener(this);
        //expandedView.setOnClickListener(this);
        expandedView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String savedAddress = null;
                String savedName=null;
                btNameList=new ArrayList();
                btAddressList=new ArrayList();

                searchDevice();
                DbHandler dbHandler=new DbHandler(FloatingViewService.this);


                Cursor cursor=dbHandler.getAllUser();
                if (cursor.moveToFirst())
                {
                    do 
                        {
                        savedName=(cursor.getString(cursor.getColumnIndex("name")));
                    savedAddress=(cursor.getString(cursor.getColumnIndex("id")));

                    }while (cursor.moveToNext());
                }
                if (savedAddress.equals(devicename))
                {
                    Toast.makeText(FloatingViewService.this, savedName+" & "+devicename+" connected", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(FloatingViewService.this," Not connected", Toast.LENGTH_SHORT).show();
                }




                //        listView.setAdapter(new DeviceAdapter(context,btAddressList,btNameList));

            }

        
        });
        expandedView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                Intent intent=new Intent(FloatingViewService.this,Settings.class);
                startActivity(intent);
                return false;
            }
        });
        //adding an touchlistener to make drag movement of the floating widget
        mFloatingView.findViewById(R.id.relativeLayoutParent).setOnTouchListener(new View.OnTouchListener()
        {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:
                        //when the drag is ended switching the state of the widget
                        collapsedView.setVisibility(View.GONE);
                        expandedView.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        //this code is helping the widget to move around the screen with fingers
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });
    }

    private void searchDevice()
    {

         final BroadcastReceiver mReceiver = new BroadcastReceiver()
        {
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    device = intent
                            .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    devicename=device.getAddress();
                }

            }
        };

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
    }



    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutExpanded:

                //switching views
                collapsedView.setVisibility(View.VISIBLE);
                //expandedView.setVisibility(View.GONE);
                break;

       /*     case R.id.buttonClose:
                //closing the widget
                stopSelf();
                break;
*/        }
    }
}