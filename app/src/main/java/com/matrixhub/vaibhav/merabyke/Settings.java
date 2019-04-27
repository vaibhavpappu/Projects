package com.matrixhub.vaibhav.merabyke;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Settings extends AppCompatActivity{

    BluetoothDevice device;
    private static final String MySharedPreference = "my_prefs";
    Switch quickBallSwitch;
    private ListView listView;
    ArrayAdapter<String> adapter;
    private BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
    TextView tv_addBT;
    ArrayList btNameList=new ArrayList();
    ArrayList btAddressList=new ArrayList();
    Context c;
    final Context context = this;
    Vibrator vibrator;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !android.provider.Settings.canDrawOverlays(this))
        {
            askPermission();
        }
        int permissionCheck = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
        {
            permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");

            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0)
            {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            }
        }
      search();
        startService(new Intent(this,BackgroundService.class));
        //  findViewById(R.id.buttonCreateWidget).setOnClickListener(this);


        findAllIds();
        popup_addBT();
     //   bluetoothList();
        Context context=this;
        listView=findViewById(R.id.listBluetooth);
        showDevices();

        quickBallSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
            if (isChecked)
            {
                launchQuickBall();
             }
             else if (!isChecked)
            {

                stopService(new Intent(Settings.this, FloatingViewService.class));
            }
            }
        });
    }

    private void search()
    {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();

        SharedPreferences sharedPreferences=context.getSharedPreferences("deviceStatus",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

         final BroadcastReceiver mReceiver = new BroadcastReceiver()
        {
            public void onReceive(Context context, Intent intent)
            {



                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    device = intent
                            .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String devicename=device.getAddress();
                    if (btAddressList.contains(devicename))
                    {
                        Toast.makeText(context, "Device Found"+devicename, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                    }
                }

            }
        };




        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
    }


    private void launchQuickBall()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            startService(new Intent(Settings.this, FloatingViewService.class));
            finish();
        }
        else if (android.provider.Settings.canDrawOverlays(this))
        {
            startService(new Intent(Settings.this, FloatingViewService.class));

        }
        else
         {
            askPermission();
            Toast.makeText(this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
        }

    }


    private void askPermission()
    {
        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }


    protected void showDevices()
    {


        DbHandler dbHandler=new DbHandler(this);


        Cursor cursor=dbHandler.getAllUser();
        if (cursor.moveToFirst())
        {
            do {
                btAddressList.add(cursor.getString(cursor.getColumnIndex("name")));
                btNameList.add(cursor.getString(cursor.getColumnIndex("id")));

            }while (cursor.moveToNext());
        }
        listView.setAdapter(new DeviceAdapter(context,btAddressList,btNameList));

    }

    public void findAllIds()
    {
        quickBallSwitch=findViewById(R.id.switcher_quickBall);
        tv_addBT = (TextView) findViewById(R.id.tv_addBT);
    }

    public void popup_addBT()
    {
        tv_addBT.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(Settings.this, DiscoveredList.class));
            }
        });
    }

}
class DeviceAdapter extends BaseAdapter
{
    Context context;
    ArrayList arrayList,nameArraylist;
    LayoutInflater inflter;

    public DeviceAdapter(Context applicationContext,ArrayList arrayList,ArrayList nameArraylist) {
        this.context = applicationContext;
        this.arrayList=arrayList;
        this.nameArraylist=nameArraylist;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount()
    {
        int count=arrayList.size();
        return count;

    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup)
    {
        view = inflter.inflate(R.layout.device_list, null);
        TextView textView = view.findViewById(R.id.tvDeviceAddress);
        TextView nameTextView = view.findViewById(R.id.tvDeviceName);
        final String address[]= (String[]) arrayList.toArray(new String[arrayList.size()]);
        final String names[]= (String[]) nameArraylist.toArray(new String[arrayList.size()]);
        textView.setText(names[i]);
        nameTextView.setText(address[i]);

        return view;
    }
}