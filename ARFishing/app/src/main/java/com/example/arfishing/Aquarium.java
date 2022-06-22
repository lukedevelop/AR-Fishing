package com.example.arfishing;


import android.opengl.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class Aquarium {

    //  ArrayList <ObjRenderer> FISH = new ArrayList<ObjRenderer>();

    final CharSequence [] items = {"돌", "안디", "해파리", "미정", "미정"};

    boolean cheak = true;
    boolean cheak_new_fish = false;
    boolean ready = true;

    Random rand = new Random();

    MainRenderer mRenderer;
    MainActivity mActivity;
    ObjRenderer mObj;

    ArrayList<float []> model_arr = new ArrayList<float[]>();


    float tx =1f , ty = 0f, tz = 1f;
    int id;
    int cnt2 = 0;

    Aquarium (MainActivity mActivity) {
        this.mActivity = mActivity;


    }

    public void normalMov () {

        cheak_new_fish = true;

        //TODO 여기도 고쳐야할 부분임 (일단 고침)
        for (float [] model : model_arr) {
            Matrix.translateM(model,0, 0, 0, 0.01f);

            Log.d("포즈로 물고기 움직이기 여" , "움직임");
        }

        for (ObjRenderer fish:mActivity.mRenderer.fish_arr) {
            fish.setModelMatrix(model_arr.get(cnt2));
            cnt2++;
            Log.d("포즈로 물고기 움직이기 여" , "위치 재설정");

        }
        cnt2 = 0;


        int a = rand.nextInt(50);

        Log.d("랜덤 숫자 확인 웅", "" + a);

        if(model_arr.size() >  a) {
            Matrix.rotateM(model_arr.get(a), 0, 45, 0, 100f, 0);
        }

    }

    public void insertFish(int id){
        this.id = id;
        float[] modelMatrix = new float[16];

        Log.d("아이디 눈", id + "");
        if(cheak_new_fish && mActivity.mRenderer.fish_arr.size() < 11) {

            switch (id){
                case 0 :
                    mObj  = new ObjRenderer(mActivity, "rock2.obj", "rock.png");
                 //   Matrix.scaleM(modelMatrix, 0, 0.001f, 0.001f, 0.001f);
                    break;
                case 1 :
                    mObj  = new ObjRenderer(mActivity, "andy.obj", "andy.png");
                //    Matrix.scaleM(modelMatrix, 0, 0.01f, 0.01f, 0.01f);
                    break;
                case 2 :
                    mObj  = new ObjRenderer(mActivity, "Jelly_Fish2.obj", "Jelly_Fish.jpg");
                //    Matrix.scaleM(modelMatrix, 0, 0.01f, 0.01f, 0.01f);
                    break;
                case 3 :
                    mObj  = new ObjRenderer(mActivity, "andy.obj", "andy.png");
                    break;
                case 4 :
                    mObj  = new ObjRenderer(mActivity, "andy.obj", "andy.png");
                    break;
            }






                mActivity.pose.toMatrix(modelMatrix, 0);
                mObj.setModelMatrix(modelMatrix);

                mActivity.mRenderer.fish_arr.add(mObj);
                model_arr.add(modelMatrix);






        }
    }

    public void deleteFish(){

        if(mActivity.mRenderer.fish_arr.size() <= 1) {
            mActivity.mRenderer.fish_arr.clear();
            model_arr.clear();
        } else {
            mActivity.mRenderer.fish_arr.remove(1);
            model_arr.remove(1);
        }

    }
}

