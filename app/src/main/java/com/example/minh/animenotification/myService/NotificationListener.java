package com.example.minh.animenotification.myService;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.minh.animenotification.MyCameraManager;
import com.example.minh.animenotification.MyGroupView;
import com.example.minh.animenotification.R;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("OverrideAbstract")
public class NotificationListener extends NotificationListenerService {
    private WindowManager mWindowManager;
    private MyGroupView mViewIcon;
    private WindowManager.LayoutParams layoutParamsIcon;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TAGG", "start");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onListenerConnected() {
        Log.d("TAGG", "start1");
        super.onListenerConnected();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        SharedPreferences sharedPreferences = getSharedPreferences("ANIME_NOTIFY", Context.MODE_PRIVATE);
        boolean isNotify = sharedPreferences.getBoolean("IS_NOTIFY", false);
        if (isNotify == true) {
            String package_name = sbn.getPackageName();
            boolean isChoose = sharedPreferences.getBoolean(package_name, false);

            if (isChoose == true && ServiceIncomingCall.isAnimation == false) {
                boolean isFlash = sharedPreferences.getBoolean("IS_FLASH", false);
                boolean isWindow = sharedPreferences.getBoolean("IS_WINDOW", false);
                ServiceIncomingCall.isAnimation = true;
                if (isWindow == true) {
                    mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    createIconView();
                    showIcon();
                } else if (isFlash == true) {
                    int onTime = sharedPreferences.getInt("ON_TIME", 50);
                    int offTime = sharedPreferences.getInt("OFF_TIME", 50);
                    int runTime = sharedPreferences.getInt("RUN_TIME", 2);
                    MyCameraManager cameraManager = new MyCameraManager();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraManager.initCameraFlash(this, runTime * 1000, onTime, offTime);
                    }else cameraManager.initCameraFlashM(runTime * 1000, onTime, offTime);
                }
            }
            super.onNotificationPosted(sbn);
        }
    }

    private void showIcon() {
        mWindowManager.addView(mViewIcon, layoutParamsIcon);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < 10000) {

                }
                mWindowManager.removeView(mViewIcon);
                ServiceIncomingCall.isAnimation = false;
            }
        });
        thread.start();
    }

    private void createIconView() {
        mViewIcon = new MyGroupView(this);
        View view = View.inflate(this, R.layout.icon_layout, mViewIcon);
        ImageView imageView = view.findViewById(R.id.image);
        Glide.with(this).asGif().load(ServiceIncomingCall.ICON_NAME).into(imageView);
        layoutParamsIcon = new WindowManager.LayoutParams();
        layoutParamsIcon.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsIcon.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParamsIcon.gravity = Gravity.TOP;
        layoutParamsIcon.format = PixelFormat.TRANSLUCENT;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParamsIcon.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParamsIcon.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParamsIcon.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.translate);
        imageView.startAnimation(animAlpha);
    }
}
