package com.example.arfishing;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.google.ar.core.Session;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainRenderer implements GLSurfaceView.Renderer {

    interface RenderCallBack{
        void preRender();
    }
    int width, height;
    RenderCallBack myCallBack;
    boolean viewportChange = false;

    CameraPreView mCamera;
    ObjRenderer bucket, fishingRod, point, fish;

    MainRenderer(RenderCallBack myCallBack, Context context){
        this.myCallBack = myCallBack;
        mCamera = new CameraPreView();
        bucket = new ObjRenderer(context, "bucket.obj", "bucket.jpg");
        fishingRod = new ObjRenderer(context, "rod.obj", "rod.jpg");
        point = new ObjRenderer(context, "float.obj", "float.jpg");
        fish = new ObjRenderer(context, "andy.obj", "andy.png");
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        GLES30.glClearColor(0f,1f,1f,1f);
        mCamera.init();
        fishingRod.init();
        point.init();
        bucket.init();
        fish.init();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0,0, width, height);
        viewportChange = true;
        this.width = width;
        this.height = height;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        myCallBack.preRender();

        GLES30.glDepthMask(false);
        mCamera.draw();
        GLES30.glDepthMask(true);

        fishingRod.draw();
        point.draw();
        bucket.draw();
        fish.draw();
    }

    void onDisplayChanged(){
        viewportChange = true;
        Log.d("MainRenderer 여","onDisplayChanged 여");
    }

    int getTextureID(){  //카메라의 색칠하기 id를 리턴 한다.
        return mCamera == null ? -1 : mCamera.mTextures[0];
    }

    void updateSession(Session session, int rotation){
        if(viewportChange){
            session.setDisplayGeometry(rotation, width, height);
            viewportChange = false;
        }
    }

    void updateProjMatrix(float [] matrix){
        fishingRod.setProjectionMatrix(matrix);
        point.setProjectionMatrix(matrix);
        bucket.setProjectionMatrix(matrix);
        fish.setProjectionMatrix(matrix);
    }

    void updateViewMatrix(float [] matrix){
        fishingRod.setViewMatrix(matrix);
        point.setViewMatrix(matrix);
        bucket.setViewMatrix(matrix);
        fish.setViewMatrix(matrix);
    }
}
