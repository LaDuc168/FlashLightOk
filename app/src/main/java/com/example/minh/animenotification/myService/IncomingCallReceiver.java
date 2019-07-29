package com.example.minh.animenotification.myService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.minh.animenotification.AnimeListener;
import com.example.minh.animenotification.MyCameraManager;

public class IncomingCallReceiver extends BroadcastReceiver {
    private AnimeListener mListener;

    public IncomingCallReceiver() {
    }

    public IncomingCallReceiver(AnimeListener listener) {
        mListener = listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(final Context context, Intent intent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("ANIME_NOTIFY", Context.MODE_PRIVATE);
        boolean isPhone = sharedPreferences.getBoolean("IS_PHONE", false);
        boolean isMessage = sharedPreferences.getBoolean("IS_MESSAGE", false);

        if (intent.getAction().equals("android.intent.action.PHONE_STATE") && isPhone == true) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String state = extras.getString(TelephonyManager.EXTRA_STATE);
                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING) && ServiceIncomingCall.isAnimation == false) {
                    ServiceIncomingCall.isAnimation = true;
                    boolean isFlash = sharedPreferences.getBoolean("IS_FLASH", false);
                    boolean isWindow = sharedPreferences.getBoolean("IS_WINDOW", false);
                    if (isWindow == true) {
                        mListener.animeAnimation();
                    } else if (isFlash == true) {
                        int onTime = sharedPreferences.getInt("ON_TIME", 50);
                        int offTime = sharedPreferences.getInt("OFF_TIME", 50);
                        int runTime = sharedPreferences.getInt("RUN_TIME", 2);
                        MyCameraManager cameraManager = new MyCameraManager();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            cameraManager.initCameraFlash(context, runTime * 1000, onTime, offTime);
                        }else cameraManager.initCameraFlashM(runTime * 1000, onTime, offTime);
                    }
                }

                if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    ServiceIncomingCall.isAnimation = false;
                    Log.d("TAGG", "stop");
                }

                if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    ServiceIncomingCall.isAnimation = false;
                    Log.d("TAGG", "stop1");
                }
            }
        }

        if (intent.getAction().
                equals("android.provider.Telephony.SMS_RECEIVED") && isMessage == true
                && ServiceIncomingCall.isAnimation == false) {
            ServiceIncomingCall.isAnimation = true;
            boolean isFlash = sharedPreferences.getBoolean("IS_FLASH", false);
            boolean isWindow = sharedPreferences.getBoolean("IS_WINDOW", false);
            Log.d("TAGG",isFlash+": "+isWindow);
            if (isWindow == true) {
                mListener.animeAnimation();
            } else if (isFlash == true) {
                int onTime = sharedPreferences.getInt("ON_TIME", 50);
                int offTime = sharedPreferences.getInt("OFF_TIME", 50);
                int runTime = sharedPreferences.getInt("RUN_TIME", 2);
                MyCameraManager cameraManager = new MyCameraManager();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    cameraManager.initCameraFlash(context, runTime * 1000, onTime, offTime);
                }else cameraManager.initCameraFlashM(runTime * 1000, onTime, offTime);
            }
        }
    }
}
