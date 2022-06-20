package com.example.arfishing;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.display.DisplayManager;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

public class MainActivity extends FragmentActivity {

    Session mSession;
    GLSurfaceView mySurfaceView;
    MainRenderer mRenderer;
    float displayX, displayY;
    boolean mTouched = false, setRod = false, casting = false;
    Button castingBtn;
    SeekBar castingSeekbar;
    TextView timerTextView;
    float[] pointMatrix;
    int btnClickCnt;
    Frame frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hidStatusBarTitleBar();
        setContentView(R.layout.activity_main);

        mySurfaceView = (GLSurfaceView) findViewById(R.id.glSsurfaceview);
        castingBtn = (Button) findViewById(R.id.castingBtn);
        castingSeekbar = (SeekBar) findViewById(R.id.castingSeekbar);
        timerTextView = (TextView) findViewById(R.id.timerTextView);

        // zo--------
        // -----------zo
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


                if (mTouched) {
                    List<HitResult> results = frame.hitTest(displayX, displayY);
                    System.out.println(displayX + ", " + displayY);
                    System.out.println(results.size());
                    for (HitResult hr : results) {
                        Pose pose = hr.getHitPose();

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        mConfig.setAugmentedImageDatabase(imgDB);
    }

    boolean drawImages(Frame frame){
//        System.out.println(frame);
        Collection<AugmentedImage> agImgs = frame.getUpdatedTrackables(AugmentedImage.class);
        boolean res = false;

        for (AugmentedImage img : agImgs) {
            if (img.getTrackingState() == TrackingState.TRACKING) {
                switch (img.getName()) {
                    case "bucket":
                        res = true;
                        break;
                }
            }
        }
        return res;
    }

}