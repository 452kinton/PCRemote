package com.kinton.pcremote;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.kinton.pcremote.enitity.CallBack;
import com.kinton.pcremote.enitity.ClickEvent;
import com.kinton.pcremote.utils.NetworkUtils;
import com.kinton.pcremote.widget.JoyStickView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, ButtonAdapater.ItemClickListener, JoyStickView.JoyStickListener {

    private static float STAND_WIDTH = 1080;
    private static float appDensity;
    private static float appScaleDensity;

    private JoyStickView joyStickView;
    private RecyclerView recyclerView;
    private ImageView ivRight;
    private ImageView ivLeft;
    private ImageView ivScreen;

    private ButtonAdapater adapater;
    private List<ButtonInfo> infos;

    private boolean isConnected = false;

    private boolean needHeartBeat = false;

    private Thread heartBeatsThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initScreen();
        super.onCreate(savedInstanceState);
        setDensity();
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        iniList();
        NetworkUtils.getInstance().init("192.168.0.100:3000/");
        //connectPC();
    }

    private void connectPC() {
        Toast.makeText(MainActivity.this, "连接服务器", Toast.LENGTH_SHORT).show();
        NetworkUtils.getInstance().getWebApiService().checkServer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CallBack>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CallBack callBack) {
                        isConnected = true;
                        needHeartBeat = true;
                        ivScreen.setImageResource(R.mipmap.screen);
                        startHeartBeats();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("error", e.toString());
                        Toast.makeText(MainActivity.this, "网络异常！！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void startHeartBeats() {
        if (heartBeatsThread != null) {
            heartBeatsThread.interrupt();
        }
        heartBeatsThread = new Thread() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted() && needHeartBeat) {
                    NetworkUtils.getInstance().getWebApiService().sendHeartBeats()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<CallBack>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(CallBack callBack) {
                                    if (isConnected == false) {
                                        isConnected = true;
                                        ivScreen.setImageResource(R.mipmap.screen);
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.i("error", e.getMessage());
                                    if (isConnected == true) {
                                        Toast.makeText(MainActivity.this, "网络异常！！", Toast.LENGTH_SHORT).show();
                                        isConnected = false;
                                        ivScreen.setImageResource(R.mipmap.screen_normal);
                                    }
                                }

                                @Override
                                public void onComplete() {
                                }
                            });
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        heartBeatsThread.start();
    }

    private void iniList() {
        infos = new ArrayList<>();

        ButtonInfo buttonInfo = new ButtonInfo();
        buttonInfo.setType(ButtonInfo.TYPE_TEXT);
        buttonInfo.setContent("UP");

        infos.add(buttonInfo);

        adapater = new ButtonAdapater(this, infos, this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapater);
    }

    private void initEvent() {
        ivScreen.setOnTouchListener(this);
        ivRight.setOnTouchListener(this);
        ivLeft.setOnTouchListener(this);

        joyStickView.setJoyStickListener(this);
    }

    private void initView() {
        joyStickView = findViewById(R.id.joyStickView);
        recyclerView = findViewById(R.id.recyclerView);
        ivLeft = findViewById(R.id.iv_left);
        ivRight = findViewById(R.id.iv_right);
        ivScreen = findViewById(R.id.iv_screen);
    }

    @Override
    protected void onDestroy() {
        isConnected = false;
        needHeartBeat = false;
        super.onDestroy();
    }

    private void initScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 全屏显示，隐藏状态栏和导航栏，拉出状态栏和导航栏显示一会儿后消失。
                this.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            } else {
                // 全屏显示，隐藏状态栏
                this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            }
        }
    }


    public void setDensity() {

        final DisplayMetrics appMetrics = getApplication().getResources().getDisplayMetrics();
        if (appDensity == 0) {
            //获取系统本来的像素缩放比和字体缩放比
            appDensity = appMetrics.density;
            appScaleDensity = appMetrics.scaledDensity;
        }

        getApplication().registerComponentCallbacks(new ComponentCallbacks() {
            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                //代表字体大小进行了更改，需要对字体缩放比进行重新赋值
                if (newConfig != null && newConfig.fontScale > 0) {
                    appScaleDensity = getApplication().getResources().getDisplayMetrics().scaledDensity;
                }
            }

            @Override
            public void onLowMemory() {

            }
        });

        //通过实际的屏幕大小来计算新的屏幕像素缩放比
        float targetDensity = appMetrics.widthPixels / 720f;
        float targetScaleDensity = targetDensity * (appScaleDensity / appDensity);
        float targetDensityDpi = targetDensity * 420;

        //将获取到的新的缩放比设置回去
        DisplayMetrics actMetrics = this.getResources().getDisplayMetrics();
        actMetrics.density = targetDensity;
        actMetrics.scaledDensity = targetScaleDensity;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.setScaleX(0.8f);
                v.setScaleY(0.8f);
                if (v.getId() == R.id.iv_left) {
                    ivLeft.setImageResource(R.mipmap.left_corner_press);
                } else if (v.getId() == R.id.iv_right) {
                    ivRight.setImageResource(R.mipmap.right_corner_press);
                }

                break;
            case MotionEvent.ACTION_UP:
                v.setScaleX(1f);
                v.setScaleY(1f);
                if (v.getId() == R.id.iv_left) {
                    ivLeft.setImageResource(R.mipmap.left_corner);
                    if(isConnected){
                        NetworkUtils.getInstance().getWebApiService().sendClickEvent(ClickEvent.left)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                    }
                } else if (v.getId() == R.id.iv_right) {
                    ivRight.setImageResource(R.mipmap.right_corner);
                    if(isConnected){
                        NetworkUtils.getInstance().getWebApiService().sendClickEvent(ClickEvent.RIGHT)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                    }
                } else if (v.getId() == R.id.iv_screen) {
                    if (!isConnected) {
                        connectPC();
                    } else {
                        if (heartBeatsThread != null) {
                            heartBeatsThread.interrupt();
                        }
                        needHeartBeat = false;
                        isConnected = false;
                        ivScreen.setImageResource(R.mipmap.screen_normal);
                        Toast.makeText(MainActivity.this, "断开服务器", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "" + infos.get(position).getContent(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDown() {
        isHandle = true;
        if (isConnected) {
            NetworkUtils.getInstance().getWebApiService().sendDownEvent()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<CallBack>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(CallBack callBack) {
                            new StickThread().start();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i("error", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }

    }

    @Override
    public void onMove(double angle, float radio) {
        // Log.i("angle,radio",angle+","+radio);
    }

    @Override
    public void onUp() {
        isHandle = false;
        if(isConnected){
        NetworkUtils.getInstance().getWebApiService().sendUpEvent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        }
    }

    private boolean isHandle = false;

    private class StickThread extends Thread {

        @Override
        public void run() {
            while (isHandle) {
                if (isConnected) {
                    try {
                        NetworkUtils.getInstance().getWebApiService().sendMoveEvent(joyStickView.getAngle(), joyStickView.getRadio())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<CallBack>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(CallBack callBack) {
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.i("error", e.getMessage());
                                    }

                                    @Override
                                    public void onComplete() {
                                    }
                                });

                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
