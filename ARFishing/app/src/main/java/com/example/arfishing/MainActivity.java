package com.example.arfishing;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.display.DisplayManager;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.ar.core.ArCoreApk;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Camera;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends FragmentActivity {

    Session mSession;
    GLSurfaceView mySurfaceView;
    MainRenderer mRenderer;
    float displayX, displayY;
    boolean mTouched = false, setRod = false, casting = false;
    boolean imageCatched = false;

    Button castingBtn;
    SeekBar castingSeekbar;
    TextView timerTextView;
    float[] pointMatrix;
    int btnClickCnt;
    Frame frame;

    // 송찬욱--
    FrameLayout mainFrameLayout;
    FrameLayout subFrameLayout;

    Button btn_showMainFrameLayout;
    Button btn_showMenuFrameLayout;

    Button btn_showInformationFragment;
    Button btn_showInventoryFragment;
    Button btn_showQuestFragment;
    Button btn_showDogamFragment;


    Fragment main_fragment;
    Fragment information_fragment;
    Fragment inventory_bait_fragment;
    Fragment inventory_fish_fragment;
    Fragment inventory_interior_fragment;
    Fragment quest_fragment;

    Fragment shop_fragment;

    boolean isShopInit = false;
    Dialog customDialog;
    // -- 송찬욱


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hidStatusBarTitleBar();
        setContentView(R.layout.activity_main);

        mySurfaceView = (GLSurfaceView) findViewById(R.id.glSsurfaceview);
        castingBtn = (Button) findViewById(R.id.castingBtn);
        castingSeekbar = (SeekBar) findViewById(R.id.castingSeekbar);
        timerTextView = (TextView) findViewById(R.id.timerTextView);


        // 찬욱--
        // ------------연결 시작

        mainFrameLayout = (FrameLayout) findViewById(R.id.mainFrameLayout);
        subFrameLayout = (FrameLayout) findViewById(R.id.subFrameLayout);
        btn_showMenuFrameLayout = (Button) findViewById(R.id.btn_showMenuFrameLayout);
        btn_showMainFrameLayout = (Button) findViewById(R.id.btn_showMainFrameLayout);

        btn_showInformationFragment = (Button) findViewById(R.id.btn_showInformationFragment);
        btn_showInventoryFragment = (Button) findViewById(R.id.btn_showInventoryFragment);
        btn_showQuestFragment = (Button) findViewById(R.id.btn_showQuestFragment);
        btn_showDogamFragment = (Button) findViewById(R.id.btn_showDogamFragment);


        information_fragment = new InformationFragment();
        inventory_fish_fragment = new Inventory_fish_Fragment();
        inventory_bait_fragment = new Inventory_bait_Fragment();
        inventory_interior_fragment = new Inventory_interior_Fragment();

        shop_fragment = new ShopFragment();
        quest_fragment = new QuestFragment();
        // TODO 나중연결 후 추가 - 퀘스트, 도감 프래그먼트 추가 요망

        // 온보딩 생성 나중에 살리기
//        showOnBoarding();

        btn_showMenuFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainFrameLayout.setVisibility(View.INVISIBLE);
                mainFrameLayout.setClickable(false);
                subFrameLayout.setVisibility(View.VISIBLE);
                subFrameLayout.setClickable(true);


                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout_menu, information_fragment,"information")
                        .commit();

//                // 정보 업데이트
//                if(getSupportFragmentManager().findFragmentByTag("information") != null) {
//                    ((InformationFragment) getSupportFragmentManager().findFragmentByTag("information")).updateDB_InformationFragment();
//                }
            }
        });

        btn_showMainFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFrameLayout.setVisibility(View.VISIBLE);
                mainFrameLayout.setClickable(true);
                subFrameLayout.setVisibility(View.INVISIBLE);
                subFrameLayout.setClickable(false);

//                DBDAO dbDAO = new DBDAO(MainActivity.this);
//                dbDAO.updateMemberDB("nickName","송찬욱",1,1,0);

            }
        });

        btn_showInformationFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout_menu, information_fragment,"information")
                        .commit();
            }
        });

        btn_showInventoryFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout_menu, inventory_fish_fragment,"inventory")
                        .commit();
            }
        });

        btn_showQuestFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout_menu, quest_fragment,"quest") // 수정 필
                        .commit();
            }
        });
//
//        btn_showDogamFragment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.frameLayout_menu, information_fragment,"dogam")// 수정 필
//                        .commit();
//            }
//        });






        // --찬욱



        // 지은 --
        castingSeekbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        castingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (castingBtn.getText().toString().equals("완료")) {
//                    if(!setBucket){
//                        setBucket = true;
//                        Toast.makeText(getApplicationContext(), "낚시대를 설치해주세요.", Toast.LENGTH_SHORT).show();
//                    }
//                    else if(setBucket && !setRod){
                    if (!setRod) {
                        setRod = true;
                        Toast.makeText(getApplicationContext(), "낚시대를 던져주세요.", Toast.LENGTH_SHORT).show();
                        castingBtn.setText("캐스팅");

                        castingSeekbar.setVisibility(View.VISIBLE);
                        new Thread() {
                            boolean dir = true;

                            @Override
                            public void run() {
                                while (!casting) {
                                    if (dir) {
                                        castingSeekbar.incrementProgressBy(1);
                                        if (castingSeekbar.getProgress() == 60) {
                                            dir = false;
                                        }
                                    } else if (!dir) {
                                        castingSeekbar.incrementProgressBy(-1);
                                        if (castingSeekbar.getProgress() == 0) {
                                            dir = true;
                                        }
                                    }
                                    try {
                                        sleep(10);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }.start();
                    }
                } else if (castingBtn.getText().toString().equals("캐스팅")) {
                    //todo 만약에 프로그래스가 일정 범위 안에 안들어오면 캐스팅 실패 >> 미끼 1개 차감 >>> 시간 남으면 하기
                    Matrix.translateM(pointMatrix, 0, 5f, -5f, -(float) castingSeekbar.getProgress());
                    Matrix.scaleM(pointMatrix, 0, 1.5f, 1.5f, 1.5f);
                    mRenderer.point.setModelMatrix(pointMatrix);
                    casting = true;
                    Toast.makeText(getApplicationContext(), "캐스팅 완료!", Toast.LENGTH_SHORT);
                    castingSeekbar.setVisibility(View.INVISIBLE);
                    castingBtn.setText("잡기");
                    castingBtn.setEnabled(false);

                    new CatchFish(MainActivity.this).start();

                    btnClickCnt = 0;
                } else if (castingBtn.getText().toString().equals("잡기")) {
                    btnClickCnt++;
                    System.out.println(btnClickCnt);
                }
            }
        });


        DisplayManager displayManager = (DisplayManager) getSystemService(DISPLAY_SERVICE);

        if (displayManager != null) {
            displayManager.registerDisplayListener(
                    new DisplayManager.DisplayListener() {
                        @Override
                        public void onDisplayAdded(int displayId) {

                        }

                        @Override
                        public void onDisplayRemoved(int displayId) {

                        }

                        @Override
                        public void onDisplayChanged(int displayId) {
                            synchronized (this) {
                                mRenderer.onDisplayChanged();
                            }
                        }
                    }, null);
        }

        MainRenderer.RenderCallBack mr = new MainRenderer.RenderCallBack() {

            @Override
            public void preRender() {

                if (mRenderer.viewportChange) {
                    Display display = getWindowManager().getDefaultDisplay();

                    mRenderer.updateSession(mSession, display.getRotation());
                }
                mSession.setCameraTextureName(mRenderer.getTextureID());

                frame = null;

                try {
                    frame = mSession.update();
                } catch (CameraNotAvailableException e) {
                    e.printStackTrace();
                }

                mRenderer.mCamera.transformDisplayGeometry(frame);

                Log.d("111111","ㅇㅇ");
                if (mTouched) {
                    List<HitResult> results = frame.hitTest(displayX, displayY);
                    System.out.println(displayX + ", " + displayY);
                    System.out.println(results.size());
                    Log.d("22222",results.size()+"");
                    for (HitResult hr : results) {
                        Pose pose = hr.getHitPose();
                        Log.d("333333",results.size()+"");
//                        if(!setBucket) {
//                            float[] bucketMatrix = new float[16];
//                            pose.toMatrix(bucketMatrix, 0);
//                            Matrix.scaleM(bucketMatrix, 0, 0.007f, 0.007f, 0.007f);
//                            mRenderer.bucket.setModelMatrix(bucketMatrix);
//                            break;
//                        }
//                        else if(setBucket && !setRod) {
                        if (!setRod) {
                            float[] rodMatrix = new float[16];
                            pose.toMatrix(rodMatrix, 0);
                            Matrix.scaleM(rodMatrix, 0, 0.005f, 0.005f, 0.005f);
                            Matrix.rotateM(rodMatrix, 0, -45, 1, 0, 0);
                            mRenderer.fishingRod.setModelMatrix(rodMatrix);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    castingBtn.setEnabled(true);
                                }
                            });

                            pointMatrix = new float[16];
                            pose.toMatrix(pointMatrix, 0);
                            break;
                        }
                    }
                    mTouched = false;
                }

                Camera camera = frame.getCamera();

                float[] viewMatrix = new float[16];
                float[] projMatrix = new float[16];

                camera.getProjectionMatrix(projMatrix, 0, 0.1f, 100f);
                camera.getViewMatrix(viewMatrix, 0);

                drawImages(frame);
//                System.out.println(drawImages(frame));

                mRenderer.updateProjMatrix(projMatrix);
                mRenderer.updateViewMatrix(viewMatrix);
            }
        };

        mRenderer = new MainRenderer(mr, this);

        mySurfaceView.setPreserveEGLContextOnPause(true);
        mySurfaceView.setEGLContextClientVersion(3); //버전 3.0 사용

        mySurfaceView.setRenderer(mRenderer);
        mySurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        Toast.makeText(this, "낚시대를 설치해주세요.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermission();

        if (mSession == null) {

            try {
                switch (ArCoreApk.getInstance().requestInstall(this, true)) {
                    case INSTALLED:
                        mSession = new Session(this);

                        Config config = new Config(mSession);
                        config.setInstantPlacementMode(Config.InstantPlacementMode.LOCAL_Y_UP);
                        mSession.configure(config);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Config mConfig = new Config(mSession);
        setImgDB(mConfig);
        mSession.configure(mConfig);

        try {
            mSession.resume();
        } catch (CameraNotAvailableException e) {
            e.printStackTrace();
        }
        mySurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mySurfaceView.onPause();
        mSession.pause();
    }

    void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    0
            );
        }
    }

    void hidStatusBarTitleBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        displayX = event.getX();
        displayY = event.getY();
        Log.d("555",displayX+","+displayY);
        mTouched = true;

        return true;
    }

    void setImgDB(Config mConfig) {
        AugmentedImageDatabase imgDB = new AugmentedImageDatabase(mSession);

        try {
            InputStream is = getAssets().open("bucket.png");
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            imgDB.addImage("bucket", bitmap);
            is.close();

            is = getAssets().open("shop_npc.jpeg");
            bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("shop_npc",bitmap);
            is.close();

            is = getAssets().open("sea.jpeg");
            bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("sea",bitmap);
            is.close();

            is = getAssets().open("aquarium.jpeg");
            bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("aquarium",bitmap);
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        mConfig.setAugmentedImageDatabase(imgDB);
    }

    void drawImages(Frame frame){
        Collection<AugmentedImage> agImgs = frame.getUpdatedTrackables(AugmentedImage.class);

        for (AugmentedImage img : agImgs) {
            if (img.getTrackingState() == TrackingState.TRACKING) {
                Log.d("야호", "ㅁㅁ");
                switch (img.getName()) {

                    case "shop_npc":
                        if(!isShopInit) {
                            isShopInit = true;
                            Log.d("야호", "ㅁㅁ");
                            showCustomDialog();
                        }

                        break;

                }
            }
        }
    }

    void showOnBoarding() {

        mainFrameLayout.setVisibility(View.INVISIBLE);
        mainFrameLayout.setClickable(false);
        subFrameLayout.setVisibility(View.VISIBLE);
        subFrameLayout.setClickable(true);

        // onboarding 온보딩
        PaperOnboardingPage scr1 = new PaperOnboardingPage(
                "Hotel",
                "All hotel",
                Color.parseColor("#678FB4"), R.drawable.bait_gunsaewoo,
                R.drawable.bait_gunsaewoo
        );
        PaperOnboardingPage scr2 = new PaperOnboardingPage(
                "Hotel",
                "All hotel",
                Color.parseColor("#65B0B4"), R.drawable.bait_kingworm,
                R.drawable.bait_gunsaewoo
        );
        PaperOnboardingPage scr3 = new PaperOnboardingPage(
                "Hotel",
                "All hotel",
                Color.parseColor("#9B90BC"), R.drawable.bait_earthworm,
                R.drawable.bait_gunsaewoo
        );

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(scr1);
        elements.add(scr2);
        elements.add(scr3);

        PaperOnboardingFragment onBoardingFragment = PaperOnboardingFragment.newInstance(elements);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.subFrameLayout, onBoardingFragment)
                .commit();

        onBoardingFragment.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
            @Override
            public void onRightOut() {
                main_fragment = new MainFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.subFrameLayout, main_fragment)
                        .commit();

            }
        });


    }


    void showCustomDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                customDialog = new Dialog(MainActivity.this);
                customDialog.setContentView(R.layout.dialog_custom);
                TextView tv_ok = (TextView) customDialog.findViewById(R.id.tv_ok);
                tv_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                        customDialog.dismiss();

                        mainFrameLayout.setVisibility(View.INVISIBLE);
                        mainFrameLayout.setClickable(false);
                        subFrameLayout.setVisibility(View.VISIBLE);
                        subFrameLayout.setClickable(true);

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout_menu, shop_fragment,"shop")
                                .commit();

                        // TODO : 여기가 아니라 상점 나가기 버튼을 누르고 false 해줘야 백 쪽에서 인식 안 됨 찬욱아 꼭 옮겨라1
//                isShopInit = false;
                    }
                });
                TextView tv_cancel = (TextView) customDialog.findViewById(R.id.tv_cancel);
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                        customDialog.dismiss();


                        // TODO : 여기가 아니라 상점 나가기 버튼을 누르고 false 해줘야   백 쪽에서 인식 안 됨 찬욱아 꼭 옮겨라2
//                isShopInit = false;
                    }
                });

                customDialog.show();


            }
        });

    }


}