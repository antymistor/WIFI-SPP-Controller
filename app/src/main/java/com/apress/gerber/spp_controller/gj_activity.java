package com.apress.gerber.spp_controller;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class gj_activity extends Activity{
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    private TextView PE2;
    private static int SPP_MODE=0;
    private int CTRMODE=SPP_MODE;
    private final float cylinderpush =150;
    private boolean supportsEs2;
    private STLView stlView=null;
    private RelativeLayout stllay=null;
    private ArrayList<String> stlsourse =new ArrayList<>();
    private float rotate[]=new float[8];
    private float push[]=new float[8];
    private int pr[]=new int[4];
    private int ctrtarget=0;
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothChatService mChatService = null;
    private StringBuffer mOutStringBuffer;
    private String mConnectedDeviceName = null;
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    private ArrayList<SeekBar> contrlbar =new ArrayList<>();
    private ArrayList<Switch>  qgctrl =new ArrayList<>();
    private ArrayList<TextView> display=new ArrayList<>();
    private ArrayList<CheckBox> check=new ArrayList<>();
    private Boolean frflag=true;
    private Button PE1=null;
    private SeekBar pushtime;
    private SeekBar pushpower;
    private TextView timetext;
    private TextView powertext;
    private String strtemp="";
    private String[] tsr=new String[]{""," ","  ","   ","    "};
    private String[] tsr0 =new String[]{"","0","00","000"};
    private String generateconstruction(){
        String str="";
        if(CTRMODE==SPP_MODE){
         char Z;
         Z=(char)('A'+ctrtarget);
         String strtemp=""+pushtime.getProgress();
         String str1=tsr0[3-strtemp.length()];
         strtemp=""+pushpower.getProgress();
         String str2=tsr0[3-strtemp.length()];
            str=String.valueOf(Z)+"T"+str1+pushtime.getProgress()+"P"+str2+pushpower.getProgress()+"\r\n";
        }
        return str;
    }
    private void trtext(int n, TextView view){
        if(n>9){
            view.setTranslationX(+10);
            return;
        }
        if(n>=0)
        {
            view.setTranslationX(0);
            return;
        }
        if(n>-10) {
            view.setTranslationX(+13);
            return;
        }
        view.setTranslationX(23);
    }
    private final class Tseekbarlistener implements  SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            String str;
           switch (seekBar.getId()){
               case R.id.pushtime:
                   str="time:"+progress+"ms";
                   str="time:"+tsr[12-str.length()]+progress+"ms";
                   timetext.setText(str);
                   break;
               case R.id.pushpower:
                   str="power:"+(progress-99);
                   str="power:"+tsr[11-str.length()]+(progress-99);
                   powertext.setText(str);
                   break;
           }
        }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {; }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {; }
    }

        private final class myseekbarlistener implements SeekBar.OnSeekBarChangeListener{
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int i=0;
            for(i=0;i<8;i++){
                if(contrlbar.get(i).getId()==seekBar.getId())
                {
                    rotate[i]=progress-90;
                    display.get(i).setText(""+(int)rotate[i]);
                    trtext((int)rotate[i],display.get(i));
                    stlView.setrot(rotate);
                    break;
                }
            }

    }
        @Override public void onStartTrackingTouch(SeekBar seekBar) {; }
        @Override public void onStopTrackingTouch(SeekBar seekBar) {; }

    }
    private void resetmyseekbar(float f[]){
        int i=0;
        for(i=0;i<8;i++){
            contrlbar.get(i).setProgress((int)f[i]+90);
            }
    }
    private final class myswitchlistener implements Switch.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int i=0;
            for(i=0;i<4;i++){
                if(qgctrl.get(i).getId()==compoundButton.getId())
                {
                    if(b)
                    {push[i]=cylinderpush;
                    //sendMessage("IT00"+i+"P001\r\n");
                        pr[i]=1;
                    }
                    else
                    {push[i]=0;
                    //sendMessage("IT00"+i+"P000\r\n");
                        pr[i]=0;
                    }
                    stlView.setpush(push);
                    break;
                }
            }
        }
    }
    private final class mycheckboxlistener implements CheckBox.OnClickListener{
        @Override
        public void onClick(View view) {
            int i=0;
            for(i=0;i<8;i++){
                if(check.get(i).getId()==view.getId())
                {
                    int j;
                    for(j=0;j<8;j++){
                        check.get(j).setChecked(false);
                    }
                    ctrtarget=i;
                    check.get(i).setChecked(true);
                    sendMessage(generateconstruction());
                    break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkSupported();
        pr[0]=pr[1]=pr[2]=pr[3]=0;
        if (supportsEs2) {
            setContentView(R.layout.content2_main);
            stlsourse.add("gj0.STL");
            stlsourse.add("gj1.STL");
            stlsourse.add("gj2.STL");
            stlsourse.add("gj3.STL");
            stlView = new STLView(this);
            stlView.setstlsourse(stlsourse);
            stllay=findViewById(R.id.stllay);
            stllay.addView(stlView);
            PE2=findViewById(R.id.PE2);
            contrlbar.add((SeekBar)findViewById(R.id.r1));
            contrlbar.add((SeekBar)findViewById(R.id.r2));
            contrlbar.add((SeekBar)findViewById(R.id.r3));
            contrlbar.add((SeekBar)findViewById(R.id.r4));
            contrlbar.add((SeekBar)findViewById(R.id.p1));
            contrlbar.add((SeekBar)findViewById(R.id.p2));
            contrlbar.add((SeekBar)findViewById(R.id.p3));
            contrlbar.add((SeekBar)findViewById(R.id.p4));
            qgctrl.add((Switch)findViewById(R.id.s1));
            qgctrl.add((Switch)findViewById(R.id.s2));
            qgctrl.add((Switch)findViewById(R.id.s3));
            qgctrl.add((Switch)findViewById(R.id.s4));
            display.add((TextView)findViewById(R.id.tr1));
            display.add((TextView)findViewById(R.id.tr2));
            display.add((TextView)findViewById(R.id.tr3));
            display.add((TextView)findViewById(R.id.tr4));
            display.add((TextView)findViewById(R.id.tp1));
            display.add((TextView)findViewById(R.id.tp2));
            display.add((TextView)findViewById(R.id.tp3));
            display.add((TextView)findViewById(R.id.tp4));
            check.add((CheckBox)findViewById(R.id.br1));
            check.add((CheckBox)findViewById(R.id.br2));
            check.add((CheckBox)findViewById(R.id.br3));
            check.add((CheckBox)findViewById(R.id.br4));
            check.add((CheckBox)findViewById(R.id.bp1));
            check.add((CheckBox)findViewById(R.id.bp2));
            check.add((CheckBox)findViewById(R.id.bp3));
            check.add((CheckBox)findViewById(R.id.bp4));
            pushpower=findViewById(R.id.pushpower);
            pushtime=findViewById(R.id.pushtime);
            pushtime.setOnSeekBarChangeListener(new Tseekbarlistener());
            pushpower.setOnSeekBarChangeListener(new Tseekbarlistener());
            timetext=findViewById(R.id.timetext);
            powertext=findViewById(R.id.powertext);
            PE1=findViewById(R.id.PE1);
            PE1.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessage("IT"+pr[0]+pr[1]+pr[2]+"P"+pr[3]+"00\r\n");
                }
            });
            int i=0;
            for(i=0;i<8;i++){
                contrlbar.get(i).setOnSeekBarChangeListener(new myseekbarlistener());
            }
            for(i=0;i<4;i++){
                qgctrl.get(i).setOnCheckedChangeListener(new myswitchlistener());
            }
            for(i=0;i<8;i++){
                check.get(i).setOnClickListener(new mycheckboxlistener());
            }

        } else {
            setContentView(R.layout.content2_main);
            Toast.makeText(this, "当前设备不支持OpenGL ES 2.0!", Toast.LENGTH_SHORT).show();
        }


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        }


        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {
            if (mChatService == null) setupChat();
        }


            startActivityForResult(new Intent(this, DeviceListActivity.class), REQUEST_CONNECT_DEVICE_SECURE);

    }
    private void setupChat() {


        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
        }
    }
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }
    private final Handler mHandler = new  Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            //setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            PE2.setText("已连接");
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            //setStatus(R.string.title_connecting);
                            PE2.setText(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            //setStatus(R.string.title_not_connected);
                            PE2.setText(R.string.title_not_connected);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    //byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    //String writeMessage = new String(writeBuf);
                    //mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    strtemp=strtemp+readMessage;
                    if(strtemp.endsWith("Z")) {
                        strtemp=strtemp.substring(0,strtemp.length()-1);
                        String[] rec = strtemp.split(" ");
                        if (rec.length == 9&&rec[0].equals("10.0")) {
                            int i;
                            for (i = 0; i < 8; i++) {
                                rotate[i] = Float.parseFloat(rec[i+1]);
                            }
                            resetmyseekbar(rotate);
                            stlView.setrot(rotate);
                        }
                        strtemp="";
                    }
                    else  {
                        if(strtemp.length()>50){
                            strtemp="";
                        }
                    }
                    //PE.setText(readMessage);

                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                   // mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    //Toast.makeText(getApplicationContext(), "Connected to "
                   //         + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                   // Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                    //        Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });
    @Override
    protected void onResume() {
        super.onResume();
        if(stlView !=null){
            stlView.onResume();
        }
    }

    private void checkSupported() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.
                getDeviceConfigurationInfo();
        supportsEs2 = configurationInfo.reqGlEsVersion >= 0x2000;

        boolean isEmulator = Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                && (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86"));

        supportsEs2 = supportsEs2 || isEmulator;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (stlView != null) {
            stlView.onPause();
        }
    }



}
