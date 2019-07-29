package com.example.minh.animenotification.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.minh.animenotification.MyCameraManager;
import com.example.minh.animenotification.R;
import com.example.minh.animenotification.dialog.SpeedDialog;
import com.example.minh.animenotification.myService.NotificationListener;
import com.example.minh.animenotification.myService.ServiceIncomingCall;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SpeedDialog.FlashListener {
    private LinearLayout mLinearPhone, mLinearMesage, mLinearNotify, mLinearApp,
            mLinearIcon, mLinearSpeed, mLinearFlash, mLinearWindow;
    private Switch mSwitchNotify, mSwitchMessage, mSwitchPhone, mSwitchFlash, mSwitchWindow;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean hasCameraFlash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSharePrefence();
        initWidget();
        initSwitch();
        checkDrawOverlayPermission();
        addAutoStartup();
        hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    private void addAutoStartup() {
        try {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.size() > 0) {
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("exc", String.valueOf(e));
        }
    }

    private void checkDrawOverlayPermission() {
        /* check if we already  have permission to draw over other apps */
        if (Build.VERSION.SDK_INT > 22) {
            if (!Settings.canDrawOverlays(this)) {
                /* if not construct intent to request permission */
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                /* request permission via start activity for result */
                startActivityForResult(intent, 2019);
            } else {
//                    startOverView();
            }
        } else {
//                startOverView();
        }
    }

    private void initSwitch() {
        boolean isPhone = sharedPreferences.getBoolean("IS_PHONE", false);
        mSwitchPhone.setChecked(isPhone);
        boolean isMessage = sharedPreferences.getBoolean("IS_MESSAGE", false);
        mSwitchMessage.setChecked(isMessage);
        boolean isNotify = sharedPreferences.getBoolean("IS_NOTIFY", false);
        mSwitchNotify.setChecked(isNotify);
        boolean isFlash = sharedPreferences.getBoolean("IS_FLASH", false);
        mSwitchFlash.setChecked(isFlash);
        boolean isWindow = sharedPreferences.getBoolean("IS_WINDOW", false);
        mSwitchWindow.setChecked(isWindow);
    }

    private void initSharePrefence() {
        sharedPreferences = getSharedPreferences("ANIME_NOTIFY", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void initWidget() {
        Intent intentService = new Intent(MainActivity.this, ServiceIncomingCall.class);
        startService(intentService);
        mSwitchPhone = findViewById(R.id.switch_phone);
        mSwitchPhone.setOnClickListener(this);
        mSwitchMessage = findViewById(R.id.switch_message);
        mSwitchMessage.setOnClickListener(this);
        mSwitchNotify = findViewById(R.id.switch_notifi);
        mSwitchNotify.setOnClickListener(this);
        mLinearPhone = findViewById(R.id.linear_phone);
        mLinearPhone.setOnClickListener(this);
        mLinearMesage = findViewById(R.id.linear_message);
        mLinearMesage.setOnClickListener(this);
        mLinearNotify = findViewById(R.id.linear_notify);
        mLinearNotify.setOnClickListener(this);
        mLinearSpeed = findViewById(R.id.linear_speed);
        mLinearSpeed.setOnClickListener(this);
        mLinearApp = findViewById(R.id.linear_app);
        mLinearApp.setOnClickListener(this);
        mLinearIcon = findViewById(R.id.linear_icon);
        mLinearIcon.setOnClickListener(this);
        mLinearFlash = findViewById(R.id.linear_flash);
        mLinearFlash.setOnClickListener(this);
        mLinearWindow = findViewById(R.id.linear_window);
        mLinearWindow.setOnClickListener(this);
        mSwitchFlash = findViewById(R.id.switch_flash);
        mSwitchFlash.setOnClickListener(this);
        mSwitchWindow = findViewById(R.id.switch_window);
        mSwitchWindow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_message:
                messageSwitchEven();
                break;
            case R.id.linear_notify:
                notifySwitchEven();
                break;
            case R.id.linear_phone:
                phoneSwitchEven();
                break;
            case R.id.switch_message:
                messageSwitchEven();
                break;
            case R.id.switch_notifi:
                notifySwitchEven();
                break;
            case R.id.switch_phone:
                phoneSwitchEven();
                break;
            case R.id.linear_app:
                startActivity(new Intent(MainActivity.this, ChooseAppActivity.class));
                break;
            case R.id.linear_icon:
                startActivity(new Intent(MainActivity.this, ChooseIconActivity.class));
                break;
            case R.id.linear_speed:
                SpeedDialog dialog = new SpeedDialog(this, this);
                dialog.show();
                break;
            case R.id.switch_flash:
                if (mSwitchFlash.isChecked()) setEnableFlash();
                break;
            case R.id.switch_window:
                if (mSwitchWindow.isChecked()) setEnableWindow();
                break;

        }
    }

    private void setEnableFlash() {
        mSwitchWindow.setChecked(false);
        editor.putBoolean("IS_FLASH", true);
        editor.putBoolean("IS_WINDOW", false);
        editor.commit();
    }

    private void setEnableWindow() {
        mSwitchFlash.setChecked(false);
        editor.putBoolean("IS_FLASH", false);
        editor.putBoolean("IS_WINDOW", true);
        editor.commit();
    }

    private void notifySwitchEven() {
        ComponentName cn = new ComponentName(MainActivity.this, NotificationListener.class);
        String flat = Settings.Secure.getString(MainActivity.this.getContentResolver(),
                "enabled_notification_listeners");
        final boolean enabled = flat != null && flat.contains(cn.flattenToString());
        if (enabled == false) {
            mSwitchNotify.setChecked(false);
            startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
        } else {
            boolean isNotify = sharedPreferences.getBoolean("IS_NOTIFY", false);
            if (isNotify == false) {
                editor.putBoolean("IS_NOTIFY", true);
                editor.commit();
                mSwitchNotify.setChecked(true);
            } else {
                editor.putBoolean("IS_NOTIFY", false);
                editor.commit();
                mSwitchNotify.setChecked(false);
            }
        }
    }

    public void messageSwitchEven() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.RECEIVE_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS},
                        22);
            } else {
                boolean isMessage = sharedPreferences.getBoolean("IS_MESSAGE", false);
                if (isMessage == false) {
                    editor.putBoolean("IS_MESSAGE", true);
                    editor.commit();
                    mSwitchMessage.setChecked(true);
                } else {
                    editor.putBoolean("IS_MESSAGE", false);
                    editor.commit();
                    mSwitchMessage.setChecked(false);
                }
            }
        } else {
            boolean isMessage = sharedPreferences.getBoolean("IS_MESSAGE", false);
            if (isMessage == false) {
                editor.putBoolean("IS_MESSAGE", true);
                editor.commit();
                mSwitchMessage.setChecked(true);
            } else {
                editor.putBoolean("IS_MESSAGE", false);
                editor.commit();
                mSwitchMessage.setChecked(false);
            }
        }
    }

    public void phoneSwitchEven() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                        20);
            } else {
                boolean isPhone = sharedPreferences.getBoolean("IS_PHONE", false);
                if (isPhone == false) {
                    editor.putBoolean("IS_PHONE", true);
                    editor.commit();
                    mSwitchPhone.setChecked(true);
                } else {
                    editor.putBoolean("IS_PHONE", false);
                    editor.commit();
                    mSwitchPhone.setChecked(false);
                }
            }
        } else {
            boolean isPhone = sharedPreferences.getBoolean("IS_PHONE", false);
            if (isPhone == false) {
                editor.putBoolean("IS_PHONE", true);
                editor.commit();
                mSwitchPhone.setChecked(true);
            } else {
                editor.putBoolean("IS_PHONE", false);
                editor.commit();
                mSwitchPhone.setChecked(false);
            }
        }
    }

    @Override
    public void tryFlash(final int onTime, final int offTime, int runTime) {
        if (ServiceIncomingCall.isAnimation == false) {
            ServiceIncomingCall.isAnimation = true;
            int ontime = onTime + 50;
            int offtime = offTime + 50;
            final int runtime = (runTime + 2) * 1000;
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage(getString(R.string.flash_dang_hoat_dong));
            dialog.show();
            MyCameraManager cameraManager = new MyCameraManager();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Thread thread = new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                        long startTime = System.currentTimeMillis();
                        while (System.currentTimeMillis() - startTime < runtime) {
                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    String cameraId = cameraManager.getCameraIdList()[0];
                                    cameraManager.setTorchMode(cameraId, true);
                                    Thread.sleep(onTime);
                                    cameraManager.setTorchMode(cameraId, false);
                                    Thread.sleep(offTime);
                                }
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        dialog.dismiss();
                        ServiceIncomingCall.isAnimation = false;
                    }
                });
                thread.start();
            } else {
                dialog.show();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long startTime = System.currentTimeMillis();
                        while (System.currentTimeMillis() - startTime < runtime) {
                            try {
                                ServiceIncomingCall.p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                                ServiceIncomingCall.camera.setParameters(ServiceIncomingCall.p);
                                ServiceIncomingCall.camera.startPreview();
                                Thread.sleep(onTime);
                                ServiceIncomingCall.p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                                ServiceIncomingCall.camera.setParameters(ServiceIncomingCall.p);
                                ServiceIncomingCall.camera.stopPreview();
                                Thread.sleep(offTime);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        dialog.dismiss();
                        ServiceIncomingCall.isAnimation = false;
                    }
                });
                thread.start();
            }
        }
    }
}
