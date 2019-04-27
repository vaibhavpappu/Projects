package com.matrixhub.vaibhav.merabyke;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    TextView tv_connect;
    private BluetoothAdapter BA;
    final Context context = this;
    Button btn_yes, btn_no;
    View view;

    LinearLayout linearLayout;
    ImageView iv_setting;

    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_connect = (TextView) findViewById(R.id.tv_connect);
        BA = BluetoothAdapter.getDefaultAdapter();

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);

        registerReceiver(mBroadcastReceiver4, filter);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        /*final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.milkshake);
        btn_connect.setAnimation(myAnim);
        btn_connect.startAnimation(myAnim);*/
        iv_settings();
        linearLayout = findViewById(R.id.linearLayout);

    }

    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                Toast.makeText(context, "" + BluetoothAdapter.ACTION_STATE_CHANGED, Toast.LENGTH_SHORT).show();
                if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_ON) {

                    showEnabled();
                } else {
                    showDisabled();
                }
            }
        }
    };
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(intent.getAction())) {
                int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
                if (bondState == BluetoothDevice.BOND_BONDED) {
                    // wake up
                    showEnabled();
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        if (BA.isEnabled()) {
            showEnabled();
        }
        if (!BA.isEnabled()) {
            showDisabled();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    public void on(View v) {

        vibrator.vibrate(38);

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.milkshake);
        final Animation myAnim1 = AnimationUtils.loadAnimation(this, R.anim.bounce);
        linearLayout.setAnimation(myAnim);
        linearLayout.setAnimation(myAnim1);
        linearLayout.startAnimation(myAnim);
        linearLayout.startAnimation(myAnim1);

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_layout);
        dialog.setCancelable(true);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_layout, null, false);
        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
        btn_no = (Button) dialog.findViewById(R.id.btn_no);

        if (BA.isEnabled()) {
            //startActivity(new Intent(MainActivity.this, Setting.class));
            //showEnabled();
            iv_setting.setVisibility(View.VISIBLE);

            Toast.makeText(getApplicationContext(), "Bluetooth is ON", Toast.LENGTH_SHORT).show();
        } else {

            btn_yes.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {

                    vibrator.vibrate(38);

                    if (!BA.isEnabled())
                    {

                        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                        //intent.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
                        startActivityForResult(intent, 100);
                        dialog.dismiss();
                        iv_setting.setVisibility(View.INVISIBLE);

                    }
                    else
                        {
                        Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
                        showEnabled();
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();

            btn_no.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(38);
                    BA.disable();
                    dialog.dismiss();
                    finish();
                    Toast.makeText(getApplicationContext(), "Turned off", Toast.LENGTH_LONG).show();
                }
            });
            dialog.show();
        }
    }

    private void showEnabled() {
        linearLayout.setBackgroundColor(Color.GREEN);
        linearLayout.setEnabled(true);
        tv_connect.setText("Connected");
        iv_setting.setVisibility(View.VISIBLE);
    }

    private void showDisabled() {
        linearLayout.setBackgroundColor(Color.RED);
        linearLayout.setEnabled(true);
        tv_connect.setText("Connect Bluetooth");
    }

    public void setting(View view) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            showEnabled();
            iv_setting.setVisibility(View.VISIBLE);
        }
    }

    public void iv_settings() {
        iv_setting = findViewById(R.id.iv_setting);
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, com.matrixhub.vaibhav.merabyke.Settings.class));
            }
        });
    }

}