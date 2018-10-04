package com.apress.gerber.spp_controller;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import java.io.IOException;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer{
    private Context context;
    private ArrayList<Model> model=new ArrayList<>();
    private Point mCenterPoint;
    private Point eye = new Point(0, 0, -3);
    private Point up = new Point(0, 1, 0);
    private Point center = new Point(0, 0, 0);
    private float mScalef = 1;
    private float mDegreex = 0;
    private float mDegreey = 0;
    private float [] rotate=new float[8];
    private float [] push=new float[4];

    public GLRenderer(Context con)
    {
        context=con;
    }

    public void setrot(float r []){
        rotate=r;
    }
    public void setpush(float p[]){push=p;}
    public void setstlsourse(ArrayList<String> stlsourse)
    {
        try {
            model.add(new STLReader().parserBinStlInAssets(context, stlsourse.get(0)));
            mCenterPoint = model.get(0).getCentrePoint();
            mScalef=0.5f/model.get(0).getR();
            model.get(0).reZ();
            model.get(0).movemodel(-mCenterPoint.x,-mCenterPoint.y,-mCenterPoint.z);
            //model.get(0).resetcolor(0.2f,0.8f,0);

            //一级关节组置位
            model.add(new STLReader().parserBinStlInAssets(context, stlsourse.get(1)));
            model.add(new STLReader().parserBinStlInAssets(context, stlsourse.get(1)));
            model.add(new STLReader().parserBinStlInAssets(context, stlsourse.get(1)));
            model.add(new STLReader().parserBinStlInAssets(context, stlsourse.get(1)));
            model.get(1).reZ();
            model.get(2).reZ();model.get(2).reY();
            model.get(3).reZ();model.get(3).reX();
            model.get(4).reZ();model.get(4).reY();model.get(4).reX();
            mCenterPoint = model.get(1).getCentrePoint();
            model.get(1).movemodel(-mCenterPoint.x+0.1037f*(model.get(1).maxX-model.get(1).minX),-mCenterPoint.y-0.19f*(model.get(1).maxY-model.get(1).minY),-mCenterPoint.z+0.2246f*(model.get(1).maxZ-model.get(1).minZ));
            mCenterPoint = model.get(2).getCentrePoint();
            model.get(2).movemodel(-mCenterPoint.x+0.1037f*(model.get(2).maxX-model.get(2).minX),-mCenterPoint.y+0.19f*(model.get(2).maxY-model.get(2).minY),-mCenterPoint.z+0.2246f*(model.get(2).maxZ-model.get(2).minZ));
            mCenterPoint = model.get(3).getCentrePoint();
            model.get(3).movemodel(-mCenterPoint.x-0.1037f*(model.get(3).maxX-model.get(3).minX),-mCenterPoint.y-0.19f*(model.get(3).maxY-model.get(3).minY),-mCenterPoint.z+0.2246f*(model.get(3).maxZ-model.get(3).minZ));
            mCenterPoint = model.get(4).getCentrePoint();
            model.get(4).movemodel(-mCenterPoint.x-0.1037f*(model.get(4).maxX-model.get(4).minX),-mCenterPoint.y+0.19f*(model.get(4).maxY-model.get(4).minY),-mCenterPoint.z+0.2246f*(model.get(4).maxZ-model.get(4).minZ));

            //二级关节置位
            model.add(new STLReader().parserBinStlInAssets(context, stlsourse.get(2)));
            model.add(new STLReader().parserBinStlInAssets(context, stlsourse.get(2)));
            model.add(new STLReader().parserBinStlInAssets(context, stlsourse.get(2)));
            model.add(new STLReader().parserBinStlInAssets(context, stlsourse.get(2)));
            model.get(5).reZ();
            model.get(6).reZ();model.get(6).reY();
            model.get(7).reZ();model.get(7).reX();
            model.get(8).reZ();model.get(8).reX();model.get(8).reY();
            mCenterPoint = model.get(5).getCentrePoint();
            model.get(5).movemodel(-mCenterPoint.x,-mCenterPoint.y+(model.get(5).maxY-model.get(5).minY)/34,-mCenterPoint.z);
            mCenterPoint = model.get(6).getCentrePoint();
            model.get(6).movemodel(-mCenterPoint.x,-mCenterPoint.y-(model.get(6).maxY-model.get(6).minY)/34,-mCenterPoint.z);
            mCenterPoint = model.get(7).getCentrePoint();
            model.get(7).movemodel(-mCenterPoint.x,-mCenterPoint.y+(model.get(7).maxY-model.get(7).minY)/34,-mCenterPoint.z);
            mCenterPoint = model.get(8).getCentrePoint();
            model.get(8).movemodel(-mCenterPoint.x,-mCenterPoint.y-(model.get(8).maxY-model.get(8).minY)/34,-mCenterPoint.z);

            //三级关节置位
            model.add(new STLReader().parserBinStlInAssets(context, stlsourse.get(3)));
            model.add(new STLReader().parserBinStlInAssets(context, stlsourse.get(3)));
            model.add(new STLReader().parserBinStlInAssets(context, stlsourse.get(3)));
            model.add(new STLReader().parserBinStlInAssets(context, stlsourse.get(3)));
            model.get(9).reZ();
            model.get(10).reZ();model.get(10).reY();
            model.get(11).reZ();model.get(11).reX();
            model.get(12).reZ();model.get(12).reX();model.get(12).reY();
            mCenterPoint = model.get(9).getCentrePoint();
            model.get(9).movemodel(-mCenterPoint.x,-mCenterPoint.y-25.65f,-mCenterPoint.z+43.6f);
            mCenterPoint = model.get(10).getCentrePoint();
            model.get(10).movemodel(-mCenterPoint.x,-mCenterPoint.y+25.65f,-mCenterPoint.z+43.6f);
            mCenterPoint = model.get(11).getCentrePoint();
            model.get(11).movemodel(-mCenterPoint.x,-mCenterPoint.y-25.65f,-mCenterPoint.z+43.6f);
            mCenterPoint = model.get(12).getCentrePoint();
            model.get(12).movemodel(-mCenterPoint.x,-mCenterPoint.y+25.65f,-mCenterPoint.z+43.6f);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float getscale(){return mScalef;}
    public void rotateX(float degree) {
        mDegreex = degree/200;
    }
    public void rotateY(float degree) {
        mDegreey = degree/200;
    }
    public void setscale(float scale){mScalef=scale;}


    @Override
    public void onDrawFrame(GL10 gl) {

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        //gl.glClearColor(0, 0, 0, 0);
        gl.glLoadIdentity();
        eye.x=-3*(float)Math.cos(-mDegreey)*(float)Math.sin(-mDegreex);
        eye.y=3*(float)Math.sin(-mDegreey);
        eye.z=-3*(float)Math.cos(-mDegreey)*(float)Math.cos(-mDegreex);
        up.y=3*(float)Math.cos(-mDegreey);
        up.x=0;
        up.z=0;
        GLU.gluLookAt(gl, eye.x, eye.y, eye.z, center.x, center.y, center.z, up.x, up.y, up.z);
        gl.glPushMatrix();
        gl.glPushMatrix();
        gl.glPushMatrix();
        gl.glPushMatrix();

        gl.glScalef(mScalef, mScalef, mScalef);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, model.get(0).getVertBuffer());
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, model.get(0).getColorsBuffer());
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, model.get(0).getFacetCount() * 3);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);


        gl.glPopMatrix();
        gl.glScalef(mScalef, mScalef, mScalef);
        gl.glTranslatef(-(model.get(1).maxX-model.get(1).minX)*1.32f,(model.get(1).maxY-model.get(1).minY)*1.085f,0);
        gl.glRotatef(rotate[4],0,0,1);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, model.get(1).getVertBuffer());
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, model.get(1).getColorsBuffer());
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, model.get(1).getFacetCount() * 3);
        gl.glTranslatef(-(model.get(5).maxX-model.get(5).minX)*1.172f,8.95f,53.84f);
        gl.glRotatef(rotate[0],1,0,0);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, model.get(5).getVertBuffer());
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, model.get(5).getColorsBuffer());
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, model.get(5).getFacetCount() * 3);
        gl.glTranslatef(-7.5f,120,0);
        gl.glTranslatef(0,push[0],0);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, model.get(9).getVertBuffer());
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, model.get(9).getColorsBuffer());
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, model.get(9).getFacetCount() * 3);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

        gl.glPopMatrix();
        gl.glScalef(mScalef, mScalef, mScalef);
        gl.glTranslatef(-(model.get(2).maxX-model.get(2).minX)*1.32f,-(model.get(2).maxY-model.get(2).minY)*1.085f,0);
        gl.glRotatef(-rotate[5],0,0,1);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, model.get(2).getVertBuffer());
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, model.get(2).getColorsBuffer());
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, model.get(2).getFacetCount() * 3);
        gl.glTranslatef(-(model.get(6).maxX-model.get(6).minX)*1.172f,-8.95f,53.84f);
        gl.glRotatef(-rotate[1],1,0,0);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, model.get(6).getVertBuffer());
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, model.get(6).getColorsBuffer());
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, model.get(6).getFacetCount() * 3);
        gl.glTranslatef(-7.5f,-120,0);
        gl.glTranslatef(0,-push[1],0);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, model.get(10).getVertBuffer());
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, model.get(10).getColorsBuffer());
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, model.get(10).getFacetCount() * 3);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

        gl.glPopMatrix();
        gl.glScalef(mScalef, mScalef, mScalef);
        gl.glTranslatef(+(model.get(3).maxX-model.get(3).minX)*1.32f,(model.get(3).maxY-model.get(3).minY)*1.085f,0);
        gl.glRotatef(-rotate[6],0,0,1);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, model.get(3).getVertBuffer());
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, model.get(3).getColorsBuffer());
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, model.get(3).getFacetCount() * 3);
        gl.glTranslatef((model.get(7).maxX-model.get(7).minX)*1.172f,+8.95f,53.84f);
        gl.glRotatef(rotate[2],1,0,0);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, model.get(7).getVertBuffer());
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, model.get(7).getColorsBuffer());
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, model.get(7).getFacetCount() * 3);
        gl.glTranslatef(7.5f,120,0);
        gl.glTranslatef(0,push[2],0);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, model.get(11).getVertBuffer());
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, model.get(11).getColorsBuffer());
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, model.get(11).getFacetCount() * 3);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

        gl.glPopMatrix();
        gl.glScalef(mScalef, mScalef, mScalef);
        gl.glTranslatef(+(model.get(4).maxX-model.get(4).minX)*1.32f,-(model.get(4).maxY-model.get(4).minY)*1.085f,0);
        gl.glRotatef(rotate[7],0,0,1);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, model.get(4).getVertBuffer());
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, model.get(4).getColorsBuffer());
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, model.get(4).getFacetCount() * 3);
        gl.glTranslatef((model.get(8).maxX-model.get(8).minX)*1.172f,-8.95f,53.84f);
        gl.glRotatef(-rotate[3],1,0,0);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, model.get(8).getVertBuffer());
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, model.get(8).getColorsBuffer());
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, model.get(8).getFacetCount() * 3);
        gl.glTranslatef(7.5f,-120,0);
        gl.glTranslatef(0,-push[3],0);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, model.get(12).getVertBuffer());
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, model.get(12).getColorsBuffer());
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, model.get(12).getFacetCount() * 3);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        gl.glFinish();

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_PROJECTION); // 设置投影矩阵
        gl.glLoadIdentity(); // 设置矩阵为单位矩阵，相当于重置矩阵
        GLU.gluPerspective(gl, 45.0f, ((float) width) / height, 1f, 100f);// 设置透视范围
        gl.glClearColor(0.00f,0.0f,0f,0.0f);
        //以下两句声明，以后所有的变换都是针对模型(即我们绘制的图形)
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0, 0, 0, 0);
        gl.glEnable(GL10.GL_DEPTH_TEST); // 启用深度缓存
        gl.glClearDepthf(1.0f); // 设置深度缓存值
        gl.glDepthFunc(GL10.GL_LEQUAL); // 设置深度缓存比较函数
        gl.glShadeModel(GL10.GL_SMOOTH);// 设置阴影模式GL_SMOOTH
        // float r = model.getR();
        //mScalef = 0.5f / r;
        //mCenterPoint = model.get(0).getCentrePoint();
        //enableMaterial(gl);
        //openLight(gl);
    }

    /* float[] ambient = {1.0f, 1.0f, 1.0f, 1.0f};
     float[] diffuse = {1.0f, 1.0f, 0.0f, 1.0f};
     float[] specular = {0.8f, 0.8f, 0.0f, 1.0f};
     float[] lightPosition = {0.0f,1.0f, 1.0f, 0.0f};*/
    float[] ambient = {0.9f, 0.9f, 0.9f, 1.0f,};
    float[] diffuse = {0.5f, 0.5f, 0.5f, 1.0f,};
    float[] specular = {1.0f, 1.0f, 1.0f, 1.0f,};
    float[] lightPosition = {0.5f, 0.5f, 0.5f, 0.0f,};

    public void openLight(GL10 gl) {
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, Util.floatToBuffer(ambient));
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, Util.floatToBuffer(diffuse));
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, Util.floatToBuffer(specular));
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, Util.floatToBuffer(lightPosition));
    }

    float[] materialAmb = {0.4f, 0.4f, 1.0f, 1.0f,};
    float[] materialDiff = {1.0f, 1.0f, 1.0f, 1.0f,};
    float[] materialSpec = {1.0f, 0.0f, 0.0f, 1.0f,};

    public void enableMaterial(GL10 gl) {

        //材料对环境光的反射情况
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, Util.floatToBuffer(materialAmb));
        //散射光的反射情况
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, Util.floatToBuffer(materialDiff));
        //镜面光的反射情况
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, Util.floatToBuffer(materialSpec));

    }
}
