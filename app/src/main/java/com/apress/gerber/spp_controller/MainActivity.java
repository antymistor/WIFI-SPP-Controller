package com.apress.gerber.spp_controller;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    Button fxcon=null;
    Button gjcon=null;
    TextView msg=null;
    Boolean conflag=false;
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fxcon=findViewById(R.id.fxcon);
        gjcon=findViewById(R.id.gjcon);
        msg=findViewById(R.id.msg);
        fxcon.setOnClickListener(mylistener);
        gjcon.setOnClickListener(mylistener);
    }
    View.OnClickListener mylistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            WifiManager wifiManager = (WifiManager) (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
            if (!wifiManager.isWifiEnabled()) {
                conflag=false;
            }
            else{
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();
                String ip = intToIp(ipAddress);
                if (ip.equals("192.168.4.2")){
                    conflag=true;
                }
                else {
                    conflag=false;
                }
            }
            switch (v.getId()) {
                case R.id.fxcon:
                    if(conflag){
                    Intent Int=new Intent(MainActivity.this,fx_activity.class);
                    startActivity(Int);}
                    else {
                        fxcon.setText("Please Check Wifi Connect");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fxcon.setText("方向控制");
                            }
                        },1000);
                    }
                    break;
                case R.id.gjcon:
                    if(true){Intent Int=new Intent(MainActivity.this,gj_activity.class);
                        startActivity(Int);}
                    else {
                        gjcon.setText("Please Check Wifi Connect");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                gjcon.setText("关节控制");
                            }
                        },1000);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public static String intToIp(int ipInt) {
       return new StringBuilder().append(((ipInt) & 0xff)).append('.').append((ipInt >> 8) & 0xff).append('.').append((ipInt >> 16) & 0xff).append('.').append((ipInt>>24) & 0xff).toString(); }
}
