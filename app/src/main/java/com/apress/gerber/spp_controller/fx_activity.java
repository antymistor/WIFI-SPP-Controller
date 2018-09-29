package com.apress.gerber.spp_controller;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class fx_activity extends Activity{
    TextView t1=null;
    TextView t2=null;
    private int jcflag=0;
    private MotionEvent eventp=null;
    Button back=null;
    Button bA=null;
    Button bB=null;
    SeekBar s1 = null;
    SeekBar s2 =null;
    Boolean aflag=true;
    Boolean bflag=true;
    private float dx=0.f;
    private float dy=0.f;
    public float px=0;
    public volatile int nd=0;
    public volatile int na=0;
    public float py=0;
    public datasampleThread datasample=null;
    public jcwzThread jcwz=null;
    public esp8266_communication sender=null;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    t2.setText(""+msg.obj);
            }
        }
    };
    ImageView huaqiu=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        back=findViewById(R.id.back);
        bA=findViewById(R.id.bA);
        bB=findViewById(R.id.bB);
        t1=findViewById(R.id.t1);
        t1.setTextColor(Color.WHITE);
        t1.setTextSize(15);
        t2=findViewById(R.id.t2);
        t2.setTextColor(Color.WHITE);
        t2.setTextSize(15);
        huaqiu=findViewById(R.id.huaqiu);
        s1=findViewById(R.id.s1);
        s2=findViewById(R.id.s2);
        sender=new esp8266_communication(handler);
        datasample=new datasampleThread();
        datasample.start();
    }
    @Override
    protected void onDestroy()
    {
      // receiver.destroy();
      // stopjcwz=true;
    //    stopdataout=true;
        if(sender!=null)
        {sender.close();}
        if(jcwz!=null)
        {jcwz.close();}
        if(datasample!=null)
        { datasample.close();}
        super.onDestroy();
    }

    public String dataout(){
        String str;
        int S1=s1.getProgress();
        int S2=s2.getProgress();
        str="Speed:"+nd+" "+"Angel:"+na+"\r\n"
                +"ButtonA:"+aflag+" "+"ButtonB:"+bflag+"\r\n"
                +"ProgressA:"+S1+" "+"ProgressB:"+S2;
        return str;
    }
    private class  datasampleThread extends Thread{
        private Boolean stop=false;
        @Override
        public void run() {
            while (!stop){
                try{
                    Thread.sleep(100);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            t1.setText(dataout());
                        }
                    });
                    sender.send(dataout()+"\r\n"+System.currentTimeMillis()+"\r\n");
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        public void close()
        {
            stop=true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                if(event.getY()<1000){
                    if(jcflag==0)
                    {
                        jcflag=1;
                        eventp=event;
                        jcwz=new jcwzThread();
                        jcwz.start();
                        //sender.startreceive();
                    }
                    else
                    {jcflag=1;eventp=event;}
                    break;}
                    break;
            case MotionEvent.ACTION_MOVE:
                eventp=event;
                break;
            case MotionEvent.ACTION_UP:
                if(event.getY()<1000){
                    jcflag=2;}
                else{
                    if(touchEventInView(back,event.getX(),event.getY()))
                    {
                        finish();
                    }
                    else
                    {
                        if (touchEventInView(bA, event.getX(), event.getY())) {
                            if(aflag)
                            {bA.setBackground(getResources().getDrawable((R.drawable.button_style2)));aflag=false;}
                            else {bA.setBackground(getResources().getDrawable((R.drawable.button_style3)));aflag=true;}
                        }
                        else
                        {
                            if (touchEventInView(bB, event.getX(), event.getY()))
                            {
                                if(bflag)
                                {bB.setBackground(getResources().getDrawable((R.drawable.button_style2)));bflag=false;}
                                else {bB.setBackground(getResources().getDrawable((R.drawable.button_style3)));bflag=true;}
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if(touchEventInView(back,event.getX(1),event.getY(1)))
                {
                    finish();
                }
                else
                {
                    if (touchEventInView(bA, event.getX(1), event.getY(1))) {
                            if(aflag)
                            {bA.setBackground(getResources().getDrawable((R.drawable.button_style2)));aflag=false;}
                            else {bA.setBackground(getResources().getDrawable((R.drawable.button_style3)));aflag=true;}
                    }
                    else
                    {
                        if (touchEventInView(bB, event.getX(1), event.getY(1)))
                        {
                            if(bflag)
                            {bB.setBackground(getResources().getDrawable((R.drawable.button_style2)));bflag=false;}
                            else {bB.setBackground(getResources().getDrawable((R.drawable.button_style3)));bflag=true;}
                        }
                    }
                }
                break;
        }
        return false;
    }

    private class jcwzThread extends Thread{
        private Boolean stop=false;
        @Override
        public void run() {
            while (!stop) {
                try {
                    Thread.sleep(50);
                    if(jcflag==1&eventp.getY(0)<800&eventp.getX(0)<800) {
                        //if(jcflag==1) {
                        float h=0;
                        na=(int)(Math.atan2(eventp.getY(0)-330,eventp.getX(0)-250)*180/3.1415926f);
                        px = (eventp.getX(0)-250)/1333;
                        py = (eventp.getY(0)-330)/2217;
                        h=(float)Math.sqrt(px*px+py*py);
                        if( h>0.085f)
                        {px=px*0.085f/h;py=py*0.085f/h;nd=100;h=0.085f;}
                        nd=(int)(100*h/0.085);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                huaqiumove(px*1.5f,py);
                            }
                        });
                    }
                    else {
                        nd=na=0;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                huaqiumove(0,0);
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        public void close()
        {
            stop=true;
        }
    }

    public void huaqiumove(float tx ,float ty)
    {
        TranslateAnimation anim = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, dx,
                Animation.RELATIVE_TO_PARENT, tx,
                Animation.RELATIVE_TO_PARENT, dy,
                Animation.RELATIVE_TO_PARENT, ty);
        anim.setDuration(50);
        anim.setFillAfter(true);
        dx=tx;
        dy=ty;
        huaqiu.startAnimation(anim);
    }


    private boolean touchEventInView(View view, float x, float y) {
        if (view == null) {
            return false;
        }

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int right = location[0];
        int top = location[1];
        int left =  right - view.getMeasuredHeight();
        int bottom = top + view.getMeasuredWidth();

        if (y >= top && y <= bottom && x >= left && x <= right) {
            return true;
        }
        return false;
    }
}
