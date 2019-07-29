package com.example.minh.animenotification.myService;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.minh.animenotification.AnimeListener;
import com.example.minh.animenotification.MyGroupView;
import com.example.minh.animenotification.R;

public class ServiceIncomingCall extends Service implements AnimeListener {
    private IncomingCallReceiver receiverCall;
    public static boolean isAnimation = false;
    private WindowManager mWindowManager;
    private MyGroupView mViewIcon;
    private WindowManager.LayoutParams layoutParamsIcon;
    public static int ICON_NAME;
    public static int ICON_TYPE;
    public static Camera camera;
    public static Camera.Parameters p;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (camera == null && p == null) {
                camera = Camera.open();
                p = camera.getParameters();
            }
        }
        ICON_NAME = R.drawable.i3;
        ICON_TYPE = 2;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        receiverCall = new IncomingCallReceiver(this);
        IntentFilter filterCall = new IntentFilter("android.intent.action.PHONE_STATE");
        filterCall.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiverCall, filterCall);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void animeAnimation() {
        Log.d("TAGG", "anime anime");
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        createIconView();
        showIcon();
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
        Glide.with(this).asGif().load(ICON_NAME).into(imageView);
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


//        layoutParamsIcon.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        layoutParamsIcon.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.translate);
        imageView.startAnimation(animAlpha);
    }
}
