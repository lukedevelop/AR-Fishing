package com.example.arfishing;

import android.app.AlertDialog;
import android.opengl.Matrix;
import android.widget.Toast;

public class CatchFish extends Thread{

    MainActivity mainActivity;
    boolean isCaught, isCaptured, intime1, intime2;

    AlertDialog.Builder dlg;

    CatchFish(MainActivity activity){
        mainActivity = activity;

        dlg = new AlertDialog.Builder(mainActivity);
        dlg.setMessage("안디 잡음");
    }

    @Override
    public void run() {
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
        intime1 = true;
        new Thread(){
            int time = 10;
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
        if(isCaught) {
            mainActivity.mRenderer.point.init();
            Matrix.scaleM(mainActivity.pointMatrix, 0, 5f, 5f, 5f);
            mainActivity.mRenderer.fish.setModelMatrix(mainActivity.pointMatrix);

            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainActivity.getApplicationContext(), "물고기가 잡혔습니다. 양동이 이미지를 촬영해주세요!", Toast.LENGTH_SHORT).show();
                }
            });


            //다시 30초 타이머 시작
            //시간이 다 흐르면 타이머 스탑, 고기도 놓침
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
                    System.out.println("1)" + intime2);
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
                            System.out.println("2)" + intime2);
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mainActivity.getApplicationContext(), "안디를 잡았습니다!", Toast.LENGTH_SHORT).show();
                                    dlg.show();
                                    mainActivity.setRod = false;
                                    mainActivity.casting = false;
                                    mainActivity.mRenderer.point.init();
                                    mainActivity.castingBtn.setText("완료");
                                    mainActivity.castingBtn.callOnClick();
                                }
                            });
                        }
                    }
                }
            }.start();

            //시간 흐르는동안 고기 흔들흔들
            while (intime2) {
                Matrix.translateM(mainActivity.pointMatrix, 0, 0.5f, 0, 0);
                mainActivity.mRenderer.fish.setModelMatrix(mainActivity.pointMatrix);
                Matrix.translateM(mainActivity.pointMatrix, 0, -0.5f, 0, 0);
                mainActivity.mRenderer.fish.setModelMatrix(mainActivity.pointMatrix);
            }


            //시간도 다 흐르면 놓침
            if (!intime2 && !isCaptured) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mainActivity.getApplicationContext(), "물고기를 놓쳤습니다!", Toast.LENGTH_SHORT).show();
                        mainActivity.setRod = false;
                        mainActivity.casting = false;
                        mainActivity.mRenderer.point.init();
                        mainActivity.castingBtn.setText("완료");
                        mainActivity.castingBtn.callOnClick();
                    }
                });

                //todo 여기서
            }
        }else if(!intime1 && !isCaptured){
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainActivity.getApplicationContext(), "물고기를 놓쳤습니다!", Toast.LENGTH_SHORT).show();
                    mainActivity.setRod = false;
                    mainActivity.casting = false;
                    mainActivity.mRenderer.point.init();
                    mainActivity.castingBtn.setText("완료");
                    mainActivity.castingBtn.callOnClick();
                }
            });

        }
    }
}