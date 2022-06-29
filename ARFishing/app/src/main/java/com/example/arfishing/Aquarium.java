package com.example.arfishing;


import android.graphics.Color;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Aquarium {

    boolean cheak_new_fish = false;
    boolean ready = true;
    boolean cheak1 = false;

    Random rand = new Random();

    MainActivity mActivity;
    ObjRenderer mObj;

    ArrayList<float []> model_arr = new ArrayList<float[]> ();
    ArrayList<String> rotate_name = new ArrayList<String>();
    String add_name;
    String interior_name;
    int delete_num ,cnt2 = 0;
    int rotate_val = -10;
    float speed = 0.003f;

    boolean aaa = true;

    Aquarium (MainActivity mActivity) {
        this.mActivity = mActivity;


    }

    public void normalMov () {

        if(cheak1) {




            int a = rand.nextInt(30);

            Log.d("랜덤 숫자 확인 웅", "" + a);

            if (a < 10) {
                speed = 0.002f;
                rotate_val = 10;
            } else if (a < 20) {
                speed = 0.003f;
                rotate_val = -10;
            } else if (a < 25) {
                speed = 0.004f;
                rotate_val = 30;
            } else if (a < 30) {
                speed = 0.005f;
                rotate_val = -30;
            }

            cheak_new_fish = true;


            for (int i = 0; i < model_arr.size(); i++) {
                switch (rotate_name.get(i)) {
                    case "베스":
                        Matrix.translateM(model_arr.get(i), 0, 0, -speed, 0);
                        break;
                    case "부시리":
                        Matrix.translateM(model_arr.get(i), 0, speed, 0, 0);
                        break;
                    case "물고기 뼈":
                        Matrix.translateM(model_arr.get(i), 0, 0, 0, speed);
                        break;
                    case "금붕어":
                        Matrix.translateM(model_arr.get(i), 0, 0, 0, speed);
                        break;
                    case "해파리":
                        Matrix.translateM(model_arr.get(i), 0, 0, 0, speed);
                        break;
                    case "니모":
                        Matrix.translateM(model_arr.get(i), 0, speed, 0, 0);
                        break;
                    case "돌":
                        Matrix.translateM(model_arr.get(i), 0, 0, 0, speed);
                        break;
                    case "삼식":
                        Matrix.translateM(model_arr.get(i), 0, -speed, 0, 0);
                        break;
                    case "스폰지밥":
                        Matrix.translateM(model_arr.get(i), 0, 0, 0, speed);
                        break;
                    case "거북이":
                        Matrix.translateM(model_arr.get(i), 0, 0, 0, speed);
                        break;

                }
            }

            for (ObjRenderer fish : mActivity.mRenderer.fish_arr) {
                fish.setModelMatrix(model_arr.get(cnt2));
                cnt2++;
                Log.d("포즈로 물고기 움직이기 여", "위치 재설정");

            }
            cnt2 = 0;


            if (rotate_name.size() > a && model_arr.size() != 0) {

                switch (rotate_name.get(a)) {
                    case "베스":
                        Matrix.rotateM(model_arr.get(a), 0, rotate_val, 0, 0, 100f);
                        break;
                    case "부시리":
                        Matrix.rotateM(model_arr.get(a), 0, rotate_val, 0, 100, 100);
                        break;
                    case "물고기 뼈":
                        Matrix.rotateM(model_arr.get(a), 0, rotate_val, 0, 50f, 0);
                        break;
                    case "금붕어":
                        Matrix.rotateM(model_arr.get(a), 0, rotate_val, 0, 50f, 0);
                        break;
                    case "해파리":
                        Matrix.rotateM(model_arr.get(a), 0, rotate_val, 0, 50f, 0);
                        break;
                    case "니모":
                        Matrix.rotateM(model_arr.get(a), 0, rotate_val, 0, 50f, 0);
                        break;
                    case "돌":
                        Matrix.rotateM(model_arr.get(a), 0, rotate_val, 0, 50f, 0);
                        break;
                    case "삼식":
                        Matrix.rotateM(model_arr.get(a), 0, rotate_val, 0, 0, 100f);
                        break;
                    case "스폰지밥":
                        Matrix.rotateM(model_arr.get(a), 0, rotate_val, 0, 50f, 0);
                        break;
                    case "거북이":
                        Matrix.rotateM(model_arr.get(a), 0, rotate_val, 0, 50f, 0);
                        break;
                }


            }
        }

    }

    public void insertFish(String add_name){
        this.add_name = add_name;
        float[] modelMatrix = new float[16];


        if(cheak_new_fish && mActivity.mRenderer.fish_arr.size() < 11) {

            switch (add_name){
                case "베스" :
                    mObj  = new ObjRenderer(mActivity, "bass_aquarium.obj", "fish_bass.jpeg");
                    rotate_name.add("베스");
                    break;
                case "부시리" :
                    mObj  = new ObjRenderer(mActivity, "boosiri_aquarium.obj", "fish_boosiri.jpeg");
                    rotate_name.add("부시리");
                    break;
                case "물고기 뼈" :
                    mObj  = new ObjRenderer(mActivity, "fishbones_aquarium.obj", "fish_fishbones.jpeg");
                    rotate_name.add("물고기 뼈");
                    break;
                case "금붕어" :
                    mObj  = new ObjRenderer(mActivity, "goldfish_aquarium.obj", "fish_goldfish.jpeg");
                    rotate_name.add("금붕어");
                    break;
                case "해파리" :
                    mObj  = new ObjRenderer(mActivity, "jellyfish_aquarium.obj", "fish_jellyfish.jpeg");
                    rotate_name.add("해파리");
                    break;
                case "니모" :
                    mObj  = new ObjRenderer(mActivity, "nimo_aquarium.obj", "fish_nimo.jpeg");
                    rotate_name.add("니모");
                    break;
                case "돌" :
                    mObj  = new ObjRenderer(mActivity, "rock_aquarium.obj", "fish_rock.jpeg");
                    rotate_name.add("돌");
                    break;
                case "삼식" :
                    mObj  = new ObjRenderer(mActivity, "samsik_aquarium.obj", "fish_samsik.jpeg");
                    rotate_name.add("삼식");
                    break;
                case "스폰지밥" :
                    mObj  = new ObjRenderer(mActivity, "spongebob_aquarium.obj", "fish_spongebob.jpeg");
                    rotate_name.add("스폰지밥");
                    break;
                case "거북이" :
                    mObj  = new ObjRenderer(mActivity, "turtle_aquarium.obj", "fish_turtle.jpeg");
                    rotate_name.add("거북이");
                    break;
            }

                mActivity.pose.toMatrix(modelMatrix, 0);
                mObj.setModelMatrix(modelMatrix);

                mActivity.mRenderer.fish_arr.add(mObj);
                model_arr.add(modelMatrix);
        }

    }

    public void deleteFish(int delete_num){
        this.delete_num = delete_num;

        if(mActivity.mRenderer.fish_arr.size() <= 1) {
            mActivity.mRenderer.fish_arr.clear();
            model_arr.clear();
            rotate_name.clear();
        } else {
            mActivity.mRenderer.fish_arr.remove(delete_num);
            rotate_name.remove(delete_num);
            model_arr.remove(delete_num);

        }

    }

    ImageView add_Interior(String interior_name){
        this.interior_name = interior_name;

        ImageView interior_view = new ImageView(mActivity);

        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(
                mActivity.mainFrameLayout.getLayoutParams().width,
                mActivity.mainFrameLayout.getLayoutParams().height
        );

        switch (interior_name){
            case "조개" :
                interior_view.setImageResource(R.drawable.interior_zogae);
                param.width = 300;  //이미지 너비
                param.height = 300; // 이미지 높이
                break;
            case "해초" :
                interior_view.setImageResource(R.drawable.interior_watergreen);
                param.width = 300;  //이미지 너비
                param.height = 700; // 이미지 높이
                break;
            case "초록산호" :
                interior_view.setImageResource(R.drawable.interior_san1);
                param.width = 500;  //이미지 너비
                param.height = 500; // 이미지 높이
                break;
            case "분홍산호" :
                interior_view.setImageResource(R.drawable.interior_san2);
                param.width = 500;  //이미지 너비
                param.height = 500; // 이미지 높이
                break;
            case "돌" :
                interior_view.setImageResource(R.drawable.interior_rock);
                param.width = 1000;  //이미지 너비
                param.height = 1000; // 이미지 높이
                break;
            case "물레방아" :
                interior_view.setImageResource(R.drawable.interior_mulre);
                param.width = 700;  //이미지 너비
                param.height = 700; // 이미지 높이
                break;
            case "인어공주" :
                interior_view.setImageResource(R.drawable.interior_mermaid);
                param.width = 1000;  //이미지 너비
                param.height = 1000; // 이미지 높이
                break;
        }


        mActivity.mainFrameLayout.addView(interior_view, param);

        return interior_view;
    }

    public void go_fishing (){
        cheak1 = false;
        mActivity.btn_aquarium_open.setVisibility(View.INVISIBLE);
        mActivity.btn_add_interior.setVisibility(View.INVISIBLE);
        mActivity.btn_delete_interior.setVisibility(View.INVISIBLE);
        mActivity.btn_AddFish.setVisibility(View.INVISIBLE);
        mActivity.btn_removeFish.setVisibility(View.INVISIBLE);
        mActivity.btn_capture.setVisibility(View.INVISIBLE);

        if(model_arr.size() != 0) {
            model_arr.clear();
            mActivity.mRenderer.fish_arr.clear();
            rotate_name.clear();
            mActivity.fish_delete_cheak.clear();
        }

        for (ImageView iv :mActivity.interior_arr) {
            iv.setVisibility(View.GONE);
            ready = false;
        }
    }
}

