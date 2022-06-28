package com.example.arfishing;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.media.SoundPool;
import android.net.Uri;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends FragmentActivity {

    Session mSession;
    GLSurfaceView mySurfaceView;
    MainRenderer mRenderer;
    float displayX, displayY;
    boolean mTouched = false, casting = false, threadIsGoing;
    boolean imageCatched = false;

    String gameMode = "입장";
    String setBait = "떡밥";

    float[] rodMatrix, waterMatrix, waterMatrix2, floatMatrix, fishMatrix;

    Button castingBtn;
    SeekBar castingSeekbar;
    TextView timerTextView, baitName;
    ImageView sandImg, timerImg, baitImg;
    LinearLayout baitImgLayout;
    int btnClickCnt;
    Frame frame;
    Pose pose;

    TextView alert_insikplease;

    // 송찬욱--
    FrameLayout mainFrameLayout;
    FrameLayout subFrameLayout;

    Button btn_showMainFrameLayout;
    Button btn_showMenuFrameLayout;

    Button btn_showInformationFragment;
    Button btn_showInventoryFragment;
    Button btn_showQuestFragment;
    Button btn_showDogamFragment;

    Button btn_aquarium_open;
    Button  btn_AddFish, btn_removeFish, btn_capture;


    Fragment main_fragment;
    Fragment information_fragment;
    Fragment inventory_bait_fragment;
    Fragment inventory_fish_fragment;
    Fragment inventory_interior_fragment;
    Fragment quest_fragment;

    Fragment shop_fragment;

    boolean isShopInit = false;
    Dialog customDialog;

    ImageView iv_gal;
    // -- 송찬욱


    // 현석 --


    Aquarium aquarium;
    CatchFish_GPS catchFish_gps;
    Dogam_Fragment dogamfrag;

    String [] add_fish_arr;
    String [] delete_fish_arr;
    String [] add_interior_arr;
   // String [] delete_interior_arr;
    ArrayList<String> fish_name = new ArrayList<String>(Arrays.asList("베스", "부시리", "물고기 뼈",
            "금붕어", "해파리", "니모", "돌", "삼식", "스폰지밥", "거북이"));
    ArrayList<String> interior_name = new ArrayList<String>(Arrays.asList("조개"));
    ArrayList<Integer> main_dogam = new ArrayList<Integer>();
    ArrayList<String> fish_add_cheak;
    ArrayList<String> fish_delete_cheak = new ArrayList<String>();
    ArrayList<ImageView> interior_arr = new ArrayList<ImageView>();

    Integer [] items;
    Integer [] dogam_null_img= {R.drawable.nullimg, R.drawable.nullimg, R.drawable.nullimg,
            R.drawable.nullimg, R.drawable.nullimg, R.drawable.nullimg, R.drawable.nullimg,
            R.drawable.nullimg, R.drawable.nullimg, R.drawable.nullimg};
    Integer [] dogam_img = {R.drawable.img_bass, R.drawable.img_boosiri
            , R.drawable.img_fishbones, R.drawable.img_goldfish, R.drawable.img_jellyfish
            , R.drawable.img_nimo, R.drawable.img_rock, R.drawable.img_samsik, R.drawable.img_spongebob
            , R.drawable.img_turtle};

    AlertDialog insert_fish_dialog;
    AlertDialog delete_fish_dialog;
    AlertDialog interior_dialog;

    Button btn_add_interior;
    Button btn_delete_interior;
    Fragment dogam_frgment;
    ImageView interior_view_main, aquarium_background;

    boolean interior_start = false;



    // -- 현석

    // 지원 --
    boolean sea = false, gang = false;
    String area = "";

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
        sandImg = (ImageView) findViewById(R.id.sandImg);
        timerImg = (ImageView) findViewById(R.id.timerImg);
        baitImg = (ImageView) findViewById(R.id.baitImg);
        baitImgLayout = (LinearLayout) findViewById(R.id.baitImgLayout);
        baitName = (TextView) findViewById(R.id.baitName);


        alert_insikplease = (TextView) findViewById(R.id.alert_insikplease);

        // 찬욱--
        // ------------연결 시작

        ggiLook();
        iv_gal = (ImageView) findViewById(R.id.iv_gal);
        iv_gal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                playSound();
                return false;
            }
        });

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
        dogam_frgment = new Dogam_Fragment();

        shop_fragment = new ShopFragment();
        quest_fragment = new QuestFragment();
        // TODO 나중연결 후 추가 - 퀘스트, 도감 프래그먼트 추가 요망

        // 온보딩 생성 나중에 살리기
        showOnBoarding();


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

                // 정보 업데이트
                if(getSupportFragmentManager().findFragmentByTag("information") != null) {
                    ((InformationFragment) getSupportFragmentManager().findFragmentByTag("information")).updateDB_InformationFragment();
                }

                btn_showInformationFragment.setBackgroundResource(R.drawable.icon_information_selected);
                btn_showInventoryFragment.setBackgroundResource(R.drawable.icon_inventory_default);
                btn_showQuestFragment.setBackgroundResource(R.drawable.icon_quest_default);
                btn_showDogamFragment.setBackgroundResource(R.drawable.icon_dogam_default);


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

                castingBtn.setVisibility(View.VISIBLE);

//                DBDAO dbDAO = new DBDAO(MainActivity.this);
//                dbDAO.updateMemberDB("nickName","송찬욱",1,1,0);
                if(area.equals("")){
                    changeGameMode("입장");
                } else{
                    changeGameMode("낚시");
                }

                isShopInit = false;
            }
        });

        btn_showInformationFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout_menu, information_fragment,"information")
                        .commit();
                // 정보 업데이트
                if(getSupportFragmentManager().findFragmentByTag("information") != null) {
                    ((InformationFragment) getSupportFragmentManager().findFragmentByTag("information")).updateDB_InformationFragment();
                }

                btn_showInformationFragment.setBackgroundResource(R.drawable.icon_information_selected);
                btn_showInventoryFragment.setBackgroundResource(R.drawable.icon_inventory_default);
                btn_showQuestFragment.setBackgroundResource(R.drawable.icon_quest_default);
                btn_showDogamFragment.setBackgroundResource(R.drawable.icon_dogam_default);


            }
        });

        btn_showInventoryFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout_menu, inventory_fish_fragment,"inventory")
                        .commit();

                btn_showInformationFragment.setBackgroundResource(R.drawable.icon_information_default);
                btn_showInventoryFragment.setBackgroundResource(R.drawable.icon_inventory_selected);
                btn_showQuestFragment.setBackgroundResource(R.drawable.icon_quest_default);
                btn_showDogamFragment.setBackgroundResource(R.drawable.icon_dogam_default);

            }
        });

        btn_showQuestFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout_menu, quest_fragment,"quest") // 수정 필
                        .commit();

                new DBDAO(MainActivity.this).update_quest_complete_DB();

                btn_showInformationFragment.setBackgroundResource(R.drawable.icon_information_default);
                btn_showInventoryFragment.setBackgroundResource(R.drawable.icon_inventory_default);
                btn_showQuestFragment.setBackgroundResource(R.drawable.icon_quest_selected);
                btn_showDogamFragment.setBackgroundResource(R.drawable.icon_dogam_default);
            }
        });

        btn_capture = (Button) findViewById(R.id.btn_capture);
        btn_capture.setOnClickListener(view -> {
            captureBitmap(new BitmapReadyCallbacks()
            {
                @Override
                public void onBitmapReady(Bitmap bitmap)
                {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/ARfishingCapture";

                    File file = new File(path);
                    if (!file.exists())
                    {
                        file.mkdirs();
                        Toast.makeText(MainActivity.this, "폴더 완료", Toast.LENGTH_SHORT).show();
                    }
//                    SimpleDateFormat day = new SimpleDateFormat("yyyyMMddHHmmss");
//                    Date date = new Date();
                    Bitmap captureview = bitmap;
                    String name = "";
                    try
                    {
                        name = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/ARfishingCapture/"+(System.currentTimeMillis()/1000) +".jpeg";
                        FileOutputStream fos = new FileOutputStream(name);

                        captureview.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath()+"/ad" + ".jpeg")));

                        Toast.makeText(MainActivity.this, "캡쳐 완료 - ARfishingCapture)", Toast.LENGTH_SHORT).show();
                        fos.flush();
                        fos.close();

                    } catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
//                    shareImage(name);
                }
            });
        });


        // --찬욱

        // 지원 --
        fishingtv = (TextView)findViewById(R.id.FishingTv);


        // -- 지원

        // 현석 --

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
       // final Geocoder geocoder = new Geocoder(this);

        btn_aquarium_open = (Button) findViewById(R.id.btn_aquarium_open);

        btn_AddFish = (Button) findViewById(R.id.btn_AddFish);
        btn_removeFish = (Button) findViewById(R.id.btn_removeFish);
        btn_add_interior = (Button) findViewById(R.id.btn_add_interior);
        btn_delete_interior = (Button) findViewById(R.id.btn_delete_interior);

        aquarium_background = (ImageView) findViewById(R.id.aquarium_background);



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        aquarium = new Aquarium(this);
        catchFish_gps = new CatchFish_GPS(this);
        dogamfrag = new Dogam_Fragment();

        btn_showDogamFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items = new Integer[10];
                ArrayList <Integer> cheak = new DBDAO(getApplicationContext()).update_dogam_fish_DB();


                for (int i = 0; i < cheak.size() ; i++) {
                    if(cheak.get(i) == 0){
                        items[i] = dogam_null_img[i];
                    } else {
                        items[i] = dogam_img[i];
                    }
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout_menu, dogam_frgment,"dogam").commit();

                btn_showInformationFragment.setBackgroundResource(R.drawable.icon_information_default);
                btn_showInventoryFragment.setBackgroundResource(R.drawable.icon_inventory_default);
                btn_showQuestFragment.setBackgroundResource(R.drawable.icon_quest_default);
                btn_showDogamFragment.setBackgroundResource(R.drawable.icon_dogam_selected);


            }
        });

        btn_aquarium_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_aquarium_open.getText().toString().equals("닫힘") ) {
                    btn_aquarium_open.setText("열림");
                    btn_aquarium_open.setBackgroundResource(R.drawable.btn_aquarium_close);

                    btn_AddFish.setVisibility(View.VISIBLE);
                    btn_removeFish.setVisibility(View.VISIBLE);
                    btn_add_interior.setVisibility(View.VISIBLE);
                    btn_delete_interior.setVisibility(View.VISIBLE);
                    btn_capture.setVisibility(View.VISIBLE);
                } else {
                    btn_aquarium_open.setText("닫힘");
                    btn_aquarium_open.setBackgroundResource(R.drawable.btn_aquarium_open);

                    btn_AddFish.setVisibility(View.INVISIBLE);
                    btn_removeFish.setVisibility(View.INVISIBLE);
                    btn_add_interior.setVisibility(View.INVISIBLE);
                    btn_delete_interior.setVisibility(View.INVISIBLE);
                    btn_capture.setVisibility(View.INVISIBLE);
                }

            }
        });

        // TODO 물고기 추가하기
        btn_AddFish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(fish_add_cheak.size() != 0){

                    add_fish_arr = new String[fish_add_cheak.size()];
                    fish_add_cheak.toArray(add_fish_arr);

                    builder.setItems(add_fish_arr, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        aquarium.insertFish(add_fish_arr[id]);
                        fish_delete_cheak.add(add_fish_arr[id]);
                        Toast.makeText(getApplicationContext(), add_fish_arr[id] + "를 추가했습니다", Toast.LENGTH_SHORT).show();
                    }
                });
                insert_fish_dialog = builder.create();
                insert_fish_dialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "물고기를 먼저 잡아주세요", Toast.LENGTH_SHORT).show();
                }

             //   Log.d("붕",mRenderer.fish_arr.size() + "");

            }
        });

        // TODO 물고기 제거하기
        btn_removeFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fish_delete_cheak.size() != 0){



                delete_fish_arr = new String[fish_delete_cheak.size()];
                fish_delete_cheak.toArray(delete_fish_arr);

                builder.setItems(delete_fish_arr, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {

                        aquarium.deleteFish(id);
                        fish_delete_cheak.remove(id);
                        Toast.makeText(getApplicationContext(), delete_fish_arr[id] + "를 제거했습니다", Toast.LENGTH_SHORT).show();
                    }
                });
                delete_fish_dialog = builder.create();
                delete_fish_dialog.show();
             } else {
                    Toast.makeText(getApplicationContext(), "물고기를 추가해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // TODO 꾸미기 버튼
        btn_add_interior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList <Integer> cheak = new DBDAO(getApplicationContext()).cheak_inventory();
                add_interior_arr = new String[cheak.size()];
                if(add_interior_arr.length != 0){
                for (int i = 0; i < cheak.size() ; i++) {
                    if(cheak.get(i) != 0){
                        add_interior_arr [i] = interior_name.get(i);
                    }
                }

                builder.setItems( add_interior_arr, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        interior_start = true;
                        interior_view_main = new ImageView(getApplicationContext());
                        interior_view_main = aquarium.add_Interior(add_interior_arr[0]);
                  //      Toast.makeText(getApplicationContext(), add[id] + "를 추가했습니다", Toast.LENGTH_SHORT).show();
                    }
                });
                interior_dialog = builder.create();
                interior_dialog.show();
                } else {
                    Toast.makeText(getApplicationContext(),"꾸미기 아이템을 구매해주세요" ,Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_delete_interior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (ImageView iv :interior_arr) {
                    iv.setVisibility(View.GONE);
                }
            }
        });



        //TODO 꾸미기 터치 이벤트
        mySurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (interior_start) {
                    interior_view_main.setX(event.getX());
                    interior_view_main.setY(event.getY());
                    interior_arr.add(interior_view_main);
                    interior_start = false;

                }
                return false;
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

                if (castingBtn.getText().toString().equals("시작")

                ) {
                    if(new DBDAO(MainActivity.this).is_bait_Inventory(setBait)) {
                        casting = false;
                        Toast.makeText(getApplicationContext(), "낚시대를 던져주세요.", Toast.LENGTH_SHORT).show();
                        castingBtn.setText("캐스팅");

                        castingSeekbar.setVisibility(View.VISIBLE);
                        new Thread() {
                            boolean dir = true;

                            @Override
                            public void run() {
                                while (!casting) {
                                    Log.d("캐스팅", "");
                                    if (dir) {
                                        castingSeekbar.incrementProgressBy(1);
                                        if (castingSeekbar.getProgress() == castingSeekbar.getMax()) {
                                            dir = false;
                                        }
                                    } else if (!dir) {
                                        castingSeekbar.incrementProgressBy(-1);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            if (castingSeekbar.getProgress() == castingSeekbar.getMin()) {
                                                dir = true;
                                            }
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


                    } else {
                        Toast.makeText(MainActivity.this,"미끼가 없습니다.", Toast.LENGTH_SHORT).show();

                    }
                } else if (castingBtn.getText().toString().equals("캐스팅")) {
                    //todo 지은) 만약에 프로그래스가 일정 범위 안에 안들어오면 캐스팅 실패 >> 미끼 1개 차감 >>> 시간 남으면 하기 >>> 하고싶지 않아졌다.
                    //todo 찬욱) 낚시 장소에 맞는 미끼가 없으면 캐스팅이 안되어야 함

                    casting = true;

                    mRenderer.drawPoint = true;

                    fishMatrix = floatMatrix.clone();

                    Matrix.translateM(fishMatrix, 0, (float) castingSeekbar.getProgress() / 15, 0, 0);
                    mRenderer.point.setModelMatrix(fishMatrix);


                    Toast.makeText(getApplicationContext(), "캐스팅 완료!", Toast.LENGTH_SHORT);
                    castingBtn.setText("잡기");
                    castingBtn.setEnabled(false);

                    new CatchFish(MainActivity.this).start();

                    btnClickCnt = 0;
                } else if (castingBtn.getText().toString().equals("잡기")) {
                    btnClickCnt++;
                }
            }
        });

        castingBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mRenderer.drawWater = !mRenderer.drawWater;
                return true;
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

                //추가된 부분---------------------------------

                float[] projMatrix2 = new float[16];
                camera.getProjectionMatrix(projMatrix2, 0, 100f, 200f);

                rodMatrix = new float[16];
                floatMatrix = new float[16];

                Matrix.setIdentityM(rodMatrix, 0);
                Matrix.setIdentityM(floatMatrix, 0);

                float[] mBallPoint = calculateInitialBallPoint(mRenderer.width,
                        mRenderer.height,
                        projMatrix, viewMatrix);

                float[] mBallPoint2 = calculateInitialBallPoint2(mRenderer.width,
                        mRenderer.height,
                        projMatrix2, viewMatrix);

                Matrix.translateM(rodMatrix, 0, mBallPoint[0], mBallPoint[1]-0.05f, mBallPoint[2]);
                Matrix.scaleM(rodMatrix, 0, 0.001f, 0.0003f, 0.001f);

                Matrix.translateM(floatMatrix, 0, mBallPoint2[0], mBallPoint2[1], mBallPoint2[2]+10f);

                Matrix.scaleM(floatMatrix, 0, 7f, 7f, 7f);


                float[] point = new float[4];

                Matrix.multiplyMV(point, 0, rodMatrix, 0,
                        new float[]{0f, 0f, 0f, 1f}, 0);

                float[] point2 = new float[4];

                Matrix.multiplyMV(point2, 0, floatMatrix, 0,
                        new float[]{0f, 0f, 0f, 1f}, 0);

                float yy = (float) (180 * Math.atan2(
                        point[0] - point2[0],
                        point[2] - point2[2]
                )
                        / Math.PI);

                Matrix.rotateM(rodMatrix, 0, yy, 0, 1, 0);

                double deltaZ = point[2] - point2[2];
                double deltaY = point[1] - point2[1];

                if (deltaZ < 0) {
                    deltaZ *= -1;
                }

                if (deltaY < 0) {
                }

                float xx = -(float) (180 * Math.atan2(

                        deltaY,
                        deltaZ
                )

                        / Math.PI);

                Matrix.rotateM(rodMatrix, 0, xx, 1, 0, 0);

                mRenderer.fishingRod.setModelMatrix(rodMatrix);




                waterMatrix = floatMatrix.clone();



                Matrix.translateM(waterMatrix, 0, 0, 0f, 6f);
                Matrix.scaleM(waterMatrix, 0, 0.0013f, 0.0013f, 0.0013f);
                Matrix.rotateM(waterMatrix, 0, -20, 1, 0, 0);



                mRenderer.water.setModelMatrix(waterMatrix);

                waterMatrix2 = waterMatrix.clone();

                Matrix.rotateM(waterMatrix2, 0, -70, 1, 0, 0);
                Matrix.translateM(waterMatrix2, 0, 0, 1000f, -5000f);
                mRenderer.water2.setModelMatrix(waterMatrix2);

                Matrix.translateM(floatMatrix, 0, 0, 2.5f, 3.5f);
                Matrix.scaleM(floatMatrix, 0, 0.7f, 0.7f, 0.7f);
                if(!casting) {
                    mRenderer.point.setModelMatrix(floatMatrix);
                }

                //--------------------------------------------

                mRenderer.updateProjMatrix(projMatrix);
                mRenderer.updateViewMatrix(viewMatrix);
            }
        };

        mRenderer = new MainRenderer(mr, this);

        mySurfaceView.setPreserveEGLContextOnPause(true);
        mySurfaceView.setEGLContextClientVersion(3); //버전 3.0 사용

        mySurfaceView.setRenderer(mRenderer);
        mySurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        changeGameMode("입장");
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
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
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
                                gang = false;
                                sea = false;

                                runOnUiThread(new Runnable() {
                                    @Override

                                    //TODO 물고기 체크
                                    public void run() {
                                        aquarium.cheak1 = true;
                                        aquarium_background.setVisibility(View.VISIBLE);
                                        fish_add_cheak = new ArrayList<String>();
                                        fishingtv.setBackgroundResource(R.drawable.alertimg_aquarium);
                                        if(alert_insikplease.getVisibility() != View.GONE) {
                                            alert_insikplease.setVisibility(View.GONE);
                                        }
                                     //   cheak_insert_fish.clear();
                                        ArrayList <Integer> cheak = new DBDAO(getApplicationContext()).update_dogam_fish_DB();
                                        for (int i = 0; i < cheak.size() ; i++) {
                                            if(cheak.get(i) != 0){
                                                fish_add_cheak.add(fish_name.get(i));
                                            }
                                        }
                                 //       Log.d("야야", cheak_insert_fish.size() +  " " );

                                        sea = false;
                                        gang = false;
                                        btn_aquarium_open.setBackgroundResource(R.drawable.btn_aquarium_open);
                                        btn_aquarium_open.setText("닫힘");
                                        btn_aquarium_open.setVisibility(View.VISIBLE);
                                        btn_AddFish.setVisibility(View.INVISIBLE);
                                        btn_removeFish.setVisibility(View.INVISIBLE);
                                        btn_add_interior.setVisibility(View.INVISIBLE);
                                        btn_delete_interior.setVisibility(View.INVISIBLE);
                                        btn_capture.setVisibility(View.INVISIBLE);
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
                                gang = false;
                                area = "바다";

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        aquarium_background.setVisibility(View.INVISIBLE);
                                        aquarium.go_fishing();
                                        soundPool.stop(sound_gang);
                                        soundPool.play(sound_sea, 1, 1, 0,3,1);
                                        fishingtv.setBackgroundResource(R.drawable.alertimg_see);
                                        if(alert_insikplease.getVisibility() != View.GONE) {
                                            alert_insikplease.setVisibility(View.GONE);
                                        }
                                        sandImg.setImageResource(R.drawable.beach);
                                        mainFrameLayout.requestLayout();

                                        btn_aquarium_open.setBackgroundResource(R.drawable.btn_aquarium_open);
                                        btn_aquarium_open.setText("닫힘");
                                        btn_aquarium_open.setVisibility(View.INVISIBLE);
                                        btn_AddFish.setVisibility(View.INVISIBLE);
                                        btn_removeFish.setVisibility(View.INVISIBLE);
                                        btn_add_interior.setVisibility(View.INVISIBLE);
                                        btn_delete_interior.setVisibility(View.INVISIBLE);
                                        btn_capture.setVisibility(View.INVISIBLE);

                                        Toast.makeText(MainActivity.this, "동해 바다에 입장하였습니다.", Toast.LENGTH_SHORT).show();
                                        changeGameMode("낚시");
                                    }
                                });


                                Log.d("실행중1", "실행중1");

                            }

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
                                sea = false;
                                area = "강";
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        aquarium.go_fishing();
                                        soundPool.stop(sound_sea);
                                        soundPool.play(sound_gang, 1, 1, 0,3,1);
                                        fishingtv.setBackgroundResource(R.drawable.alertimg_gang);
                                        if(alert_insikplease.getVisibility() != View.GONE) {
                                            alert_insikplease.setVisibility(View.GONE);
                                        }

                                        sandImg.setImageResource(R.drawable.grass);
                                        mainFrameLayout.requestLayout();

                                        Toast.makeText(MainActivity.this, "낙동강 에 입장하였습니다.", Toast.LENGTH_LONG).show();
                                        changeGameMode("낚시");
                                    }
                                });
                                Log.d("실행중2", "실행중2");

                            }

                        }
                        break;

                }
            }

            if (img.getTrackingState() == TrackingState.TRACKING ) {

                pose =img.getCenterPose();
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
                "",
                "",
                Color.parseColor("#E6F6FE"), R.drawable.title,
                R.drawable.shark
        );
        PaperOnboardingPage scr2 = new PaperOnboardingPage(
                "",
                "",
                Color.parseColor("#A6D6CD"), R.drawable.onboarding_page2,
                R.drawable.shark
        );
        PaperOnboardingPage scr3 = new PaperOnboardingPage(
                "",
                "",
                Color.parseColor("#99C4F7"), R.drawable.onboarding_page3,
                R.drawable.shark
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
                if(new DBDAO(MainActivity.this).selectMemberDB().nickName.equals("default")) {
                    main_fragment = new MainFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.subFrameLayout, main_fragment)
                            .commit();

                } else {

                    MainActivity.this.mainFrameLayout.setVisibility(View.VISIBLE);
                    MainActivity.this.mainFrameLayout.setClickable(true);
                    MainActivity.this.subFrameLayout.setVisibility(View.INVISIBLE);
                    MainActivity.this.subFrameLayout.setClickable(false);

                    MainActivity.this.getSupportFragmentManager()
                            .beginTransaction()
                            .remove(onBoardingFragment).commit();
                }


            }
        });
    }


    void showCustomDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                customDialog = new Dialog(MainActivity.this);
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customDialog.setContentView(R.layout.dialog_custom);
                customDialog.setCancelable(false);
                TextView tv_ok = (TextView) customDialog.findViewById(R.id.tv_ok);
                tv_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                        customDialog.dismiss();
                        changeGameMode("상점");

                        mainFrameLayout.setVisibility(View.INVISIBLE);
                        mainFrameLayout.setClickable(false);
                        subFrameLayout.setVisibility(View.VISIBLE);
                        subFrameLayout.setClickable(true);

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout_menu, shop_fragment,"shop")
                                .commit();


//
                    }
                });
                TextView tv_cancel = (TextView) customDialog.findViewById(R.id.tv_cancel);
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                        customDialog.dismiss();


                        // TODO : 여기가 아니라 상점 나가기 버튼을 누르고 false 해줘야   백 쪽에서 인식 안 됨 찬욱아 꼭 옮겨라2
                        isShopInit = false;
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
            mRenderer.drawWater = false;
            casting = true;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    castingBtn.setText("시작");
                    castingBtn.setVisibility(View.INVISIBLE);
                    castingSeekbar.setVisibility(View.INVISIBLE);
                    sandImg.setVisibility(View.INVISIBLE);
                    baitImgLayout.setVisibility(View.INVISIBLE);
                    mainFrameLayout.requestLayout();
                }
            });
        }else{
            mRenderer.drawRod = true;
            mRenderer.drawWater = true;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    castingBtn.setVisibility(View.VISIBLE);
                    sandImg.setVisibility(View.VISIBLE);
                    baitImgLayout.setVisibility(View.VISIBLE);
                    mainFrameLayout.requestLayout();
                }
            });

            if(area.equals("바다")){

            }
            else if(area.equals("강")){

            }
        }
     }

     //지은 추가------------------------------------------------------------

    //공 최초 위치 설정
    public float[] calculateInitialBallPoint(int width, int height,
                                             float[] projMat, float[] viewMat) {
        return getScreenPoint(width / 2, height / 2, width, height, projMat, viewMat);
    }

    //공 최초 위치 설정
    public float[] calculateInitialBallPoint2(int width, int height,
                                              float[] projMat, float[] viewMat) {
        return getScreenPoint(width / 2, height / 2 - 100f, width, height, projMat, viewMat);
    }


    //평면화
    public float[] getScreenPoint(float x, float y, float w, float h,
                                  float[] projMat, float[] viewMat) {
        float[] position = new float[3];
        float[] direction = new float[3];

        x = x * 2 / w - 1.0f;
        y = (h - y) * 2 / h - 1.0f;

        float[] viewProjMat = new float[16];
        Matrix.multiplyMM(viewProjMat, 0, projMat, 0, viewMat, 0);

        float[] invertedMat = new float[16];
        Matrix.setIdentityM(invertedMat, 0);
        Matrix.invertM(invertedMat, 0, viewProjMat, 0);

        float[] farScreenPoint = new float[]{x, y, 1.0F, 1.0F};
        float[] nearScreenPoint = new float[]{x, y, -1.0F, 1.0F};
        float[] nearPlanePoint = new float[4];
        float[] farPlanePoint = new float[4];

        Matrix.multiplyMV(nearPlanePoint, 0, invertedMat, 0, nearScreenPoint, 0);
        Matrix.multiplyMV(farPlanePoint, 0, invertedMat, 0, farScreenPoint, 0);

        position[0] = nearPlanePoint[0] / nearPlanePoint[3];
        position[1] = nearPlanePoint[1] / nearPlanePoint[3];
        position[2] = nearPlanePoint[2] / nearPlanePoint[3];

        direction[0] = farPlanePoint[0] / farPlanePoint[3] - position[0];
        direction[1] = farPlanePoint[1] / farPlanePoint[3] - position[1];
        direction[2] = farPlanePoint[2] / farPlanePoint[3] - position[2];

        normalize(direction);

        position[0] += (direction[0] * 0.1f);
        position[1] += (direction[1] * 0.1f);
        position[2] += (direction[2] * 0.1f);

        return position;
    }

    // 정규화
    private void normalize(float[] v) {
        double norm = Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
        v[0] /= norm;
        v[1] /= norm;
        v[2] /= norm;
    }

    //---------------------------------------------------------------------


     // 캡쳐와 공유

    Bitmap snapshotBitmap;
    void captureBitmap(final BitmapReadyCallbacks bitmapReadyCallbacks) {
        mySurfaceView.queueEvent(new Runnable()
        {
            @Override
            public void run()
            {
                EGL10 egl = (EGL10) EGLContext.getEGL();
                GL10 gl = (GL10) egl.eglGetCurrentContext().getGL();
                snapshotBitmap = createBitmapFromGLSurface(0, 0, mySurfaceView.getWidth(), mySurfaceView.getHeight(), gl);

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        bitmapReadyCallbacks.onBitmapReady(snapshotBitmap);
                    }
                });

            }
        });
    }

    private Bitmap createBitmapFromGLSurface(int x, int y, int w, int h, GL10 gl) throws OutOfMemoryError {
        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);

        try
        {
            gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2;
            for (int i = 0; i < h; i++)
            {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++)
                {
                    int texturePixel = bitmapBuffer[offset1 + j];
                    int blue = (texturePixel >> 16) & 0xff;
                    int red = (texturePixel << 16) & 0x00ff0000;
                    int pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e)
        {
            return null;
        }


        Bitmap returnBitmap = Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
        Bitmap bitmap = returnBitmap.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas = new Canvas(bitmap);
        Drawable drawable = btn_capture.getBackground();

        canvas.save();
        canvas.translate(btn_capture.getX(),btn_capture.getY());
        drawable.draw(canvas);
        canvas.restore();


        return bitmap;
    }


    void shareImage(String result) {
        String [] aa = {result};
        MediaScannerConnection.scanFile(this, aa, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent.setType("image/jpeg");
                        startActivity(Intent.createChooser(shareIntent,"Share"));


                    }
                }

        );


    }

    int sound_aquarium;
    int sound_casting;
    int sound_galmaegi;
    int sound_gang;
    int sound_padack;
    int sound_ril;
    int sound_sea;
    int sound_spongebob;

    SoundPool soundPool;

    void ggiLook() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0 );

        }

        sound_aquarium = soundPool.load(this, R.raw.sound_aquarium,1);
        sound_casting = soundPool.load(this, R.raw.sound_casting,1);
        sound_galmaegi = soundPool.load(this, R.raw.sound_galmaegi,1);
        sound_gang = soundPool.load(this, R.raw.sound_gang,1);
        sound_padack = soundPool.load(this, R.raw.sound_padack,1);
        sound_ril = soundPool.load(this, R.raw.sound_ril,1);
        sound_sea = soundPool.load(this, R.raw.sound_sea,1);
        sound_spongebob = soundPool.load(this, R.raw.sound_spongebob,1);

    }

    void playSound() {
        soundPool.play(sound_ril, (float)0.1,(float)0.1, 0,3,1);
//        soundPool.stop(sound_galmaegi);
    }
}