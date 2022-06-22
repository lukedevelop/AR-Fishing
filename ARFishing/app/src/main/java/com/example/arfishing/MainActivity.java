package com.example.arfishing;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
    boolean mTouched = false, casting = false, threadIsGoing;
    boolean imageCatched = false;

    String gameMode = "입장";

    Button castingBtn;
    SeekBar castingSeekbar;
    TextView timerTextView;
    float[] pointMatrix, waterMatrix, fishMatrix;
    int btnClickCnt;
    Frame frame;
    Pose pose;

    CatchFish catchFish;

    // 송찬욱--
    FrameLayout mainFrameLayout;
    FrameLayout subFrameLayout;

    Button btn_showMainFrameLayout;
    Button btn_showMenuFrameLayout;

    Button btn_showInformationFragment;
    Button btn_showInventoryFragment;
    Button btn_showQuestFragment;
    Button btn_showDogamFragment;

    Button  btn_AddFish, btn_removeFish, btn_showAquarium, btn_goFishing;

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

    // 현석 --
    Aquarium aquarium;
    Dogam_Fragment dogamfrag;

    AlertDialog insert_fish_dialog;

    // -- 현석

    // 지원 --
    boolean sea = false, gang = false;
    String area = "";
    ImageView galimg, galimg2, riverimg;
    TextView fishingtv ;

    // -- 지원


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

                changeGameMode("메뉴");
            }
        });

        btn_showMainFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFrameLayout.setVisibility(View.VISIBLE);
                mainFrameLayout.setClickable(true);

                subFrameLayout.setVisibility(View.INVISIBLE);
                subFrameLayout.setClickable(false);

                aquarium.ready = false;
                aquarium.model_arr.clear();
                mRenderer.fish_arr.clear();

                btn_AddFish.setVisibility(View.INVISIBLE);
                btn_removeFish.setVisibility(View.INVISIBLE);


                castingBtn.setVisibility(View.VISIBLE);

//                DBDAO dbDAO = new DBDAO(MainActivity.this);
//                dbDAO.updateMemberDB("nickName","송찬욱",1,1,0);
                if(area.equals("")){
                    changeGameMode("입장");
                } else{
                    changeGameMode("낚시");
                }
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

        // 지원 --
        fishingtv = (TextView)findViewById(R.id.FishingTv);

        galimg = (ImageView)findViewById(R.id.Gal);
        galimg2 = (ImageView)findViewById(R.id.Gal2);
        riverimg = (ImageView)findViewById(R.id.River);
        // -- 지원

        // 현석 --

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        aquarium = new Aquarium(this);
        dogamfrag = new Dogam_Fragment();

        btn_AddFish = (Button) findViewById(R.id.btn_AddFish);
        btn_removeFish = (Button) findViewById(R.id.btn_removeFish);
        btn_showAquarium = (Button) findViewById(R.id.btn_showAquarium);
        btn_goFishing = (Button) findViewById(R.id.btn_goFishing);

        btn_AddFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                builder.setItems(aquarium.items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        aquarium.insertFish(id);
                        Toast.makeText(getApplicationContext(), aquarium.items[id] + "를 추가했습니다", Toast.LENGTH_SHORT).show();
                    }
                });
                insert_fish_dialog = builder.create();
                insert_fish_dialog.show();

                Log.d("붕",mRenderer.fish_arr.size() + "");

            }
        });

        btn_removeFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                aquarium.deleteFish();
            }
        });

        btn_showAquarium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mainFrameLayout.setVisibility(View.VISIBLE);
                mainFrameLayout.setClickable(true);

                subFrameLayout.setVisibility(View.INVISIBLE);
                subFrameLayout.setClickable(false);

                castingBtn.setVisibility(View.INVISIBLE);

                Toast.makeText(MainActivity.this, "수조 이미지를 인식해주세요", Toast.LENGTH_SHORT).show();



            }
        });

        btn_goFishing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aquarium.ready = false;
                aquarium.model_arr.clear();
                mRenderer.fish_arr.clear();

                btn_AddFish.setVisibility(View.INVISIBLE);
                btn_removeFish.setVisibility(View.INVISIBLE);


                castingBtn.setVisibility(View.VISIBLE);
            }
        });



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


                if (castingBtn.getText().toString().equals("시작")) {
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
                                        if (castingSeekbar.getProgress() == 100) {
                                            dir = false;
                                        }
                                    } else if (!dir) {
                                        castingSeekbar.incrementProgressBy(-1);
                                        if (castingSeekbar.getProgress() == 30) {
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
                } else if (castingBtn.getText().toString().equals("캐스팅")) {
                    //todo 지은) 만약에 프로그래스가 일정 범위 안에 안들어오면 캐스팅 실패 >> 미끼 1개 차감 >>> 시간 남으면 하기
                    //todo 찬욱) 낚시 장소에 맞는 미끼가 없으면 캐스팅이 안되어야 함
                    pointMatrix = new float[16];
                    waterMatrix = new float[16];
                    fishMatrix = new float[16];

                    pointMatrix = mRenderer.modelMatrix.clone();
                    waterMatrix = mRenderer.modelMatrix.clone();

                    Matrix.translateM(pointMatrix, 0, 0, 7f, -(float) castingSeekbar.getProgress()*5);
                    Matrix.scaleM(pointMatrix, 0, 10f, 10f, 10f);
                    mRenderer.point.setModelMatrix(pointMatrix);

                    Matrix.scaleM(waterMatrix, 0, 0.007f, 0.007f, 0.007f);
                    Matrix.translateM(waterMatrix, 0, -398f, -14850f,-(float) castingSeekbar.getProgress()*5*120);
                    mRenderer.water.setModelMatrix(waterMatrix);


                    mRenderer.drawPoint = true;
                    mRenderer.drawWater = true;

                    fishMatrix = pointMatrix;

                    casting = true;
                    Toast.makeText(getApplicationContext(), "캐스팅 완료!", Toast.LENGTH_SHORT);
                    castingSeekbar.setVisibility(View.INVISIBLE);
                    castingBtn.setText("잡기");
                    castingBtn.setEnabled(false);


                    catchFish = new CatchFish(MainActivity.this);
                    catchFish.start();
//                    new CatchFish(MainActivity.this).start();

                    btnClickCnt = 0;
                } else if (castingBtn.getText().toString().equals("잡기")) {
                    btnClickCnt++;
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

                if (mTouched) {
                    List<HitResult> results = frame.hitTest(displayX, displayY);
                    for (HitResult hr : results) {

//                        pose = hr.getHitPose();


                    }
                    mTouched = false;
                }

                Camera camera = frame.getCamera();

                float[] viewMatrix = new float[16];
                float[] projMatrix = new float[16];

                camera.getProjectionMatrix(projMatrix, 0, 0.1f, 100f);
                camera.getViewMatrix(viewMatrix, 0);

                drawImages(frame);

                mRenderer.updateProjMatrix(projMatrix);
                mRenderer.updateViewMatrix(viewMatrix);
            }
        };

        mRenderer = new MainRenderer(mr, this);

        mySurfaceView.setPreserveEGLContextOnPause(true);
        mySurfaceView.setEGLContextClientVersion(3); //버전 3.0 사용

        mySurfaceView.setRenderer(mRenderer);
        mySurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        changeGameMode("낚시");
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

            is = getAssets().open("aquarium.jpg");
            bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("aquarium",bitmap);

            is = getAssets().open("river.jpeg");
            bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("river", bitmap);
            is.close();

            is = getAssets().open("sea.jpeg");
            bitmap = BitmapFactory.decodeStream(is);
            imgDB.addImage("sea", bitmap);
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        mConfig.setAugmentedImageDatabase(imgDB);
    }

    void drawImages(Frame frame){
        Collection<AugmentedImage> agImgs = frame.getUpdatedTrackables(AugmentedImage.class);
        for (AugmentedImage img : agImgs) {
            if (img.getTrackingState() == TrackingState.TRACKING &&
                    img.getTrackingMethod() == AugmentedImage.TrackingMethod.FULL_TRACKING) {
                Log.d("야호", "ㅁㅁ");
                img.getName();


                switch (img.getName()) {
                    case "shop_npc":
                        if(!isShopInit) {
                            isShopInit = true;
                            Log.d("야호", "ㅁㅁ");
                            showCustomDialog();
                        }

                        break;

                    case "bucket":
                        if(threadIsGoing) {
                            imageCatched = true;
                        }
                        break;

                    case "aquarium":
                        if(!threadIsGoing) {
                            if (btn_AddFish.getVisibility() == View.INVISIBLE &&
                                    castingBtn.getVisibility() == View.INVISIBLE
                            ) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("야야", "너너");
                                        btn_AddFish.setVisibility(View.VISIBLE);
                                        btn_removeFish.setVisibility(View.VISIBLE);
                                    }
                                });

                            }

                            // 현석
                            aquarium.normalMov();
                            changeGameMode("수조");
                        }
                        break;

                    case "sea":
                        if(!threadIsGoing) {
                            if (!sea) {
                                sea = true;
                                area = "바다";

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        galimg.setVisibility(View.VISIBLE);
                                        galimg2.setVisibility(View.VISIBLE);

                                        riverimg.setVisibility(View.INVISIBLE);
                                        fishingtv.setText("동해바다");

                                        Toast.makeText(MainActivity.this, "동해 바다에 입장하였습니다.", Toast.LENGTH_SHORT).show();
                                        changeGameMode("낚시");
                                    }
                                });


                                Log.d("실행중1", "실행중1");

                            }
                            gang = false;
                        }
                        break;

                    case "river":
                        if(!threadIsGoing) {
//                        Log.d("drawImages2 여", img.getIndex() +img.getName()+
//                                pose.tx()+","+
//                                pose.ty()+","+
//                                pose.tz());

                            if (!gang) {
                                gang = true;
                                area = "강";
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        galimg.setVisibility(View.INVISIBLE);
                                        galimg2.setVisibility(View.INVISIBLE);
                                        riverimg.setVisibility(View.VISIBLE);

                                        fishingtv.setText("낙동강");

                                        Toast.makeText(MainActivity.this, "낙동강 에 입장하였습니다.", Toast.LENGTH_LONG).show();
                                        changeGameMode("낚시");
                                    }
                                });
                                Log.d("실행중2", "실행중2");

                            }
                            sea = false;
                        }
                        break;

                }
            }

            if (img.getTrackingState() == TrackingState.TRACKING ) {
                switch (img.getName()) {
                    case "aquarium":
                        if(!threadIsGoing) {
                            pose = img.getCenterPose();

                            // 현석
                            aquarium.normalMov();
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

     void changeGameMode(String mode){

        gameMode = mode;

        if(!mode.equals("낚시")){
            mRenderer.drawRod = false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    castingBtn.setText("시작");
                }
            });
            castingBtn.setVisibility(View.INVISIBLE);
            castingSeekbar.setVisibility(View.INVISIBLE);

            galimg.setVisibility(View.INVISIBLE);
            galimg2.setVisibility(View.INVISIBLE);
            riverimg.setVisibility(View.INVISIBLE);
        }else{
            mRenderer.drawRod = true;
            castingBtn.setVisibility(View.VISIBLE);

            if(area.equals("바다")){
                galimg.setVisibility(View.VISIBLE);
                galimg2.setVisibility(View.VISIBLE);
            }
            else if(area.equals("강")){
                riverimg.setVisibility(View.VISIBLE);
            }
        }

        System.out.println(gameMode);
     }


}