package com.apress.gerber.spp_controller;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import java.util.ArrayList;

public class STLView extends GLSurfaceView {
    private GLRenderer GL=null;
    private Context context;
    private float x=0;
    private float y=0;
    private float degreex=0;
    private float degreey=0;
    private float distance=300;
    private int   dflag=0;
    private float mscale=0;
    private float mscaleorg=0;
    public STLView(Context con) {
        super(con);
        context=con;
        this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        this.setZOrderOnTop(true);
    }
    public void setstlsourse(ArrayList<String> stlsourse){
        GL=new GLRenderer(context);
        GL.setstlsourse(stlsourse);
        this.setRenderer(GL);
        mscale=GL.getscale();
        mscaleorg=mscale;
    }
    public void setrot(float r[]){
        GL.setrot(r);
        this.invalidate();
    }
    public void setpush(float p[]){
        GL.setpush(p);
        this.invalidate();
    }
    public void rotateX(float degree) {
        if(GL!=null){
            GL.rotateX(degree);
            this.invalidate();}
    }

    public void rotateY(float degree) {
        if(GL!=null){
            GL.rotateY(degree);
            this.invalidate();}
    }

    private void setscale(float scale){
        if(GL!=null){
            GL.setscale(scale);
            this.invalidate();}
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                x=event.getX();
                y=event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(event.getPointerCount()==1&&dflag!=2){
                    degreex=degreex+(event.getX()-x)/5;
                    degreey=degreey-(event.getY()-y)/5;
                    y=event.getY();
                    x=event.getX();
                    rotateX(degreex);
                    rotateY(degreey);
                }
                if(event.getPointerCount()==2){
                    if(dflag==0){
                        float dx=event.getX(0)-event.getX(1);
                        float dy=event.getX(0)-event.getX(1);
                        distance=(float)Math.sqrt(dx*dx+dy*dy);
                        dflag=1;
                    }
                    else {
                        if(dflag==1){
                            float dx=event.getX(0)-event.getX(1);
                            float dy=event.getX(0)-event.getX(1);
                            mscale=mscale*(1+((float)Math.sqrt(dx*dx+dy*dy)-distance)/1000);
                            if(mscale<0.1*mscaleorg){mscale=0.1f*mscaleorg;}
                            //if(mscale>3){mscale=3.0f;}
                            setscale(mscale);
                            distance=(float)Math.sqrt(dx*dx+dy*dy);}
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if( (event.getPointerCount()-1)>0.5)
                {dflag=2;}
                else {dflag=0;}
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if( (event.getPointerCount()-1)>0.5)
                {dflag=2;}
                else {dflag=0;}
                break;
        }
        return true;
    }

}
