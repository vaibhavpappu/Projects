package com.matrixhub.vaibhav.merabyke;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DiscoveredList extends Activity
{

    Context context = this;
    private ListView listView;
    ArrayList<String> arrayList = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    BluetoothDevice device;
    Button btn_saveAllBT;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences sharedPreferences;
    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovered_list);
        getPermissions();
        swipeRefreshLayout=findViewById(R.id.pullToRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
            onListItemClick();
           onRestart();

            }
        });
        listView = (ListView) findViewById(R.id.listView);

        btn_save();

        }

    private void getPermissions()
    {
        int permissionCheck = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
        {
            permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");

            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0)
            {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
                onListItemClick();
            }
            else if (permissionCheck==0)
            {
                onListItemClick();

            }
            else
            {
                onListItemClick();
            }
            onListItemClick();

        }
        onListItemClick();

    }


    private void onListItemClick()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();


        final BroadcastReceiver mReceiver = new BroadcastReceiver()
        {
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    device = intent
                            .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    ArrayList addressArrayList=new ArrayList();
                    arrayList.add(device.getName());
                    addressArrayList.add(device.getAddress());
                    Log.i("BT", device.getName() + "\n" + device.getAddress());
                    //  adapter=new ArrayAdapter(context,android.R.layout.simple_list_item_1,arrayList);
                    listView.setAdapter(new CustomAdapter(context,arrayList,addressArrayList));
                }
            }
        };


        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        startService(new Intent(this,BackgroundService.class));

    }


    public void btn_save() {
        btn_saveAllBT = findViewById(R.id.btn_saveAllBT);
        btn_saveAllBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DiscoveredList.this, Settings.class));
            }
        });
    }
}
 class CustomAdapter extends BaseAdapter {
     Context context;
     ArrayList arrayList, addressArraylist;
     LayoutInflater inflter;
     ArrayList btAddressList=new ArrayList();

     public CustomAdapter(Context applicationContext, ArrayList arrayList, ArrayList addressArraylist)
     {
         this.context = applicationContext;
         this.arrayList = arrayList;
         this.addressArraylist = addressArraylist;
         inflter = (LayoutInflater.from(applicationContext));
     }

     @Override
     public int getCount()
     {
         int count = addressArraylist.size();
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
         TextView addressTextView = view.findViewById(R.id.tvDeviceName);
         final String names[] = (String[]) arrayList.toArray(new String[arrayList.size()]);
         final String addressNames[] = (String[]) addressArraylist.toArray(new String[addressArraylist.size()]);
         textView.setText(addressNames[i]);
         addressTextView.setText(names[i]);
         view.setOnClickListener(new View.OnClickListener()
         {
             @Override
             public void onClick(View view)
             {

                 Intent intent = new Intent();
                 intent.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                 view.getContext().startActivity(intent);
                 setting();
                 DbHandler dbHandler=new DbHandler(context);
                dbHandler.insertUserDetails(names[i],addressNames[i]);
             }
         });
         return view;
     }

     public void setting()
     {
             }


         }


