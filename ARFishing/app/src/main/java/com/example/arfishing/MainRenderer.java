package com.example.arfishing;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.google.ar.core.Session;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainRenderer implements GLSurfaceView.Renderer {

    interface RenderCallBack{
        void preRender();
    }

    int width, height;
    RenderCallBack myCallBack;
    boolean viewportChange = false;
    Context context;

    CameraPreView mCamera;


    //추가된 부분-------------------
    float[] mMVPMatrix = new float[16];
    float [] mViewMatrix = new float[16];
    float [] mProjectionMatrix = new float[16];
    float [] modelMatrix;
    //-----------------------------------

    ObjRenderer fishingRod, point, fish, water;

    boolean drawRod, drawPoint, drawFish, drawWater;

    // 현석
    ArrayList<ObjRenderer> fish_arr;

    MainRenderer(RenderCallBack myCallBack, Context context){
        this.myCallBack = myCallBack;
        this.context = context;
        mCamera = new CameraPreView();
        fishingRod = new ObjRenderer(context, "rod.obj", "rod.jpg");
        point = new ObjRenderer(context, "float.obj", "float.jpg");
        water = new ObjRenderer(context, "Water.obj", "Water.jpg");

        fish_arr = new ArrayList<ObjRenderer>();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        GLES30.glClearColor(0f,1f,1f,1f);
        mCamera.init();
        fishingRod.init();
        point.init();
        water.init();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0,0, width, height);
        viewportChange = true;
        this.width = width;
        this.height = height;

        //추가된 부분-----------------
        float ratio = (float) width * 1 / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 100, 200);

        //----------------------------
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        myCallBack.preRender();
        GLES30.glDepthMask(false);
        mCamera.draw();
        GLES30.glDepthMask(true);


        //추가된 부분-----------------
        Matrix.setLookAtM(
                mViewMatrix,0,
                //카메라위치
                // x,      y,      z
                2,5,100,
                //시선 위치
                // x,      y,      z
                0,0,-2,
                //카메라 윗방향
                0,1,0
        );

        Matrix.setIdentityM(mMVPMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        modelMatrix = new float[16];

        Matrix.setIdentityM(modelMatrix,0);
        Matrix.scaleM(modelMatrix, 0, 0.007f, 0.004f, 0.005f);
//        Matrix.translateM(modelMatrix, 0, 10f, -120f, 0);
        Matrix.translateM(modelMatrix, 0, 10f, -120f, 0);
        Matrix.rotateM(modelMatrix, 0, 10, 1, 0, 0);
//        Matrix.rotateM(modelMatrix, 0, 5, 0, 0, 1);
        fishingRod.setModelMatrix(modelMatrix);
        fishingRod.setViewMatrix(mViewMatrix);
        fishingRod.setProjectionMatrix(mProjectionMatrix);

        //----------------------------

        if(drawRod) {
            fishingRod.draw();
        }

        if(drawPoint) {
            point.draw();
        }
        if(fish != null && drawFish){
            if(!fish.isInit){
                fish.init();
            }
            fish.draw();
        }
        if(drawWater) {
            water.draw();
            System.out.println("물 그렸다");
        }

        // 현석
        for (ObjRenderer fish  : fish_arr) {
            if(!fish.isInit){
                fish.init();

            }
            fish.draw();
        }

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
        if(fish != null) {
            fish.setProjectionMatrix(matrix);
        }
        water.setProjectionMatrix(matrix);

        for (ObjRenderer fish  : fish_arr) {
            fish.setProjectionMatrix(matrix);
        }


    }

    void updateViewMatrix(float [] matrix){
        fishingRod.setViewMatrix(matrix);
        point.setViewMatrix(matrix);
        if(fish != null){
            fish.setViewMatrix(matrix);
        }
        water.setViewMatrix(matrix);

        for (ObjRenderer fish  : fish_arr) {
            fish.setViewMatrix(matrix);
        }

    }

    void makeFishObj(String objName){
        fish = new ObjRenderer(context, objName+".obj", objName+".jpeg");
    }
}
