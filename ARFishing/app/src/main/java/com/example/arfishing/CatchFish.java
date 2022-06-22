package com.example.arfishing;

import android.app.AlertDialog;
import android.opengl.Matrix;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedHashMap;

public class CatchFish extends Thread{

    MainActivity mainActivity;
    boolean isCaught, isCaptured, intime1, intime2;

    //todo 지은) 일단 임의로 바다로 설정, 이후에 지원님의 변수와 연결 필요
    String area = "", fishName;

    //todo 지은) 일단 임의로 루어로 설정, 이후에 찬욱님의 변수와 연결 필요
    String baitName = "루어";

    FishDTO caughtFish;

    LinkedHashMap<String, String> fishNameObj = new LinkedHashMap<String, String>();

    AlertDialog.Builder dlg;
    View dlgView;
    TextView dlgTv1, dlgTv2, dlgTv3;
    ImageView dlgImg;

    CatchFish(MainActivity activity){
        mainActivity = activity;

        this.area = mainActivity.area;

        dlg = new AlertDialog.Builder(mainActivity);
        dlg.setNegativeButton("확인", null);
        dlgView = (View) View.inflate(mainActivity, R.layout.caught_fish_dlg, null);
        dlgTv1 = (TextView) dlgView.findViewById(R.id.dlgTv1);
        dlgTv2 = (TextView) dlgView.findViewById(R.id.dlgTv2);
        dlgTv3 = (TextView) dlgView.findViewById(R.id.dlgTv3);
        dlgImg = (ImageView) dlgView.findViewById(R.id.dlgImg);

        fishNameObj.put("베스", "fish_bass");
        fishNameObj.put("부시리", "fish_boosiri");
        fishNameObj.put("뼈", "fish_fishbones");
        fishNameObj.put("금붕어", "fish_goldfish");
        fishNameObj.put("해파리", "fish_jellyfish");
        fishNameObj.put("니모", "fish_nimo");
        fishNameObj.put("바위", "fish_rock");
        fishNameObj.put("삼식", "fish_samsik");
        fishNameObj.put("스폰지밥", "fish_spongebob");
        fishNameObj.put("거북이", "fish_turtle");
    }

    @Override
    public void run() {

        mainActivity.threadIsGoing = true;

        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.castingBtn.setEnabled(true);
            }
        });


        //타이머 시작 -> 시간 안에 잡기 버튼을 눌러야함
        //todo 찬욱) 시간 스레드1 >> 이 스레드가 멈추가 전에 버튼을 다다다다 눌러야 하는 것
        intime1 = true;
        new Thread(){
            int time = 5;
            @Override
            public void run() {
                while (intime1 && time >= 0) {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainActivity.timerTextView.setText(time + "");
                        }
                    });
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    time--;
                }
                //시간이 다 지나면 타이머 스탑, 고기도 놓침
                intime1 = false;
                isCaught = false;
            }
        }.start();

        //시간 안에 버튼을 일정 횟수 이상 누르면
        //고기가 잡히고 타이머는 스탑
        //todo 지은) 물고기마다 버튼 횟수도 정하기
        new Thread(){
            @Override
            public void run() {
                while(intime1) {
                    if (mainActivity.btnClickCnt > 2) {
                        isCaught = true;
                        intime1 = false;
                    }
                }
            }
        }.start();

        //고기가 잡히기 전까지 찌 흔들흔들
        while(intime1){
            Matrix.translateM(mainActivity.pointMatrix, 0, 0.5f, 0, 0);
            mainActivity.mRenderer.point.setModelMatrix(mainActivity.pointMatrix);
            Matrix.translateM(mainActivity.pointMatrix, 0, -0.5f, 0, 0);
            mainActivity.mRenderer.point.setModelMatrix(mainActivity.pointMatrix);
        }


        //고기가 잡히면 찌 사라지고 고기로 바뀜
        //todo 찬욱) 물고기 잡히는 부분
        if(isCaught) {

            int ranNum = (int) (Math.random()*100)+1;
            if(ranNum <= 20){
                fishName = area.equals("강") ? "베스" : "부시리";
            } else if(ranNum <= 40){
                fishName = area.equals("강") ? "금붕어" : "해파리";
            } else if(ranNum <= 60){
                fishName = area.equals("강") ? "삼식" : "거북이";
            } else if(ranNum <= 70){
                fishName = "뼈";
            } else if(ranNum <= 80){
                fishName = "니모";
            } else if(ranNum <= 90){
                fishName = "바위";
            } else{
                fishName = "스폰지밥";
            }

            caughtFish = new DBDAO(mainActivity).getFishInfo(fishName);

            mainActivity.mRenderer.drawPoint = false;
            mainActivity.mRenderer.makeFishObj(fishNameObj.get(caughtFish.fish_name));
            Matrix.scaleM(mainActivity.fishMatrix, 0, Float.parseFloat(caughtFish.fish_scale), Float.parseFloat(caughtFish.fish_scale), Float.parseFloat(caughtFish.fish_scale));
            Matrix.rotateM(mainActivity.fishMatrix, 0, Float.parseFloat(caughtFish.fish_rotation.split(",")[0]), 1, 0, 0);
            Matrix.rotateM(mainActivity.fishMatrix, 0, Float.parseFloat(caughtFish.fish_rotation.split(",")[1]), 0, 1, 0);
            Matrix.rotateM(mainActivity.fishMatrix, 0, Float.parseFloat(caughtFish.fish_rotation.split(",")[2]), 0, 1, 0);
            mainActivity.mRenderer.fish.setModelMatrix(mainActivity.fishMatrix);
            mainActivity.mRenderer.drawFish = true;

            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainActivity.getApplicationContext(), "물고기가 잡혔습니다. 양동이 이미지를 촬영해주세요!", Toast.LENGTH_SHORT).show();
                }
            });

            //다시 30초 타이머 시작
            //시간이 다 흐르면 타이머 스탑, 고기도 놓침
            //todo 찬욱) 시간 스레드2 >> 여기가 시간이 늘어나면 고기 잡고 나서 양동이 사진 찍기까지 시간이 널널해짐
            intime2 = true;
            new Thread() {
                int time = 30;

                @Override
                public void run() {
                    intime2 = true;
                    while (intime2 && time >= 0) {
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mainActivity.timerTextView.setText(time + "");
                            }
                        });
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        time--;
                    }
                    intime2 = false;
                    isCaught = false;
                    isCaptured = false;
                }
            }.start();


            //시간 안에 이미지를 찍으면 타이머는 멈추고 고기는 포획됨
            new Thread() {
                @Override
                public void run() {
                    while (intime2) {
                        if(mainActivity.frame != null){
                            mainActivity.drawImages(mainActivity.frame);
                        }
                        if (mainActivity.imageCatched) {
                            isCaptured = true;
                            intime2 = false;
                            mainActivity.imageCatched = false;
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mainActivity.getApplicationContext(), fishName+"을(를) 잡았습니다!", Toast.LENGTH_SHORT).show();

                                    new DBDAO(mainActivity).plusFishInventory(fishName);
                                    new DBDAO(mainActivity).minusBaitInventory(baitName);

                                    dlgTv1.setText(fishName+"을(를) 잡았습니다!");
                                    dlgTv2.setText(fishName);
                                    dlgTv3.setText(caughtFish.fish_explain);
                                    dlgImg.setImageResource(new ShopFragment().choiceFishImg(caughtFish.fish_id));
                                    dlg.setView(dlgView);
                                    dlg.show();
                                    mainActivity.casting = false;
                                    mainActivity.mRenderer.drawPoint = false;
                                    mainActivity.mRenderer.drawFish = false;
                                    mainActivity.castingBtn.setText("완료");
                                    mainActivity.castingBtn.callOnClick();
                                }
                            });
                            mainActivity.threadIsGoing = false;
                            interrupt();
                        }
                    }
                }
            }.start();

            //시간 흐르는동안 고기 흔들흔들
            while (intime2) {
                Matrix.translateM(mainActivity.fishMatrix, 0, 0.5f/Float.parseFloat(caughtFish.fish_scale), 0, 0);
                mainActivity.mRenderer.fish.setModelMatrix(mainActivity.fishMatrix);
                Matrix.translateM(mainActivity.fishMatrix, 0, -0.5f/Float.parseFloat(caughtFish.fish_scale), 0, 0);
                mainActivity.mRenderer.fish.setModelMatrix(mainActivity.fishMatrix);
            }


            //시간도 다 흐르면 놓침
            if (!intime2 && !isCaptured) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mainActivity.getApplicationContext(), "물고기를 놓쳤습니다!", Toast.LENGTH_SHORT).show();
                        mainActivity.casting = false;
                        mainActivity.mRenderer.drawPoint = false;
                        mainActivity.mRenderer.drawFish = false;
                        mainActivity.castingBtn.setText("완료");
                        mainActivity.castingBtn.callOnClick();
                    }
                });
                mainActivity.threadIsGoing = false;
                interrupt();
            }
        }else if(!intime1 && !isCaptured){
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainActivity.getApplicationContext(), "물고기를 놓쳤습니다!", Toast.LENGTH_SHORT).show();
                    mainActivity.casting = false;
                    mainActivity.mRenderer.drawPoint = false;
                    mainActivity.mRenderer.drawFish = false;
                    mainActivity.castingBtn.setText("완료");
                    mainActivity.castingBtn.callOnClick();
                }
            });
            mainActivity.threadIsGoing = false;
            interrupt();
        }
        mainActivity.threadIsGoing = false;
    }
}