package com.example.minh.animenotification;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.minh.animenotification.myService.ServiceIncomingCall;

public class MyCameraManager {

    public void initCameraFlash(final Context context, final long runtime, final int onTime, final int offTime) {
        Thread thread = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
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
                ServiceIncomingCall.isAnimation = false;
            }
        });
        thread.start();

    }

    //Android < M
    public void initCameraFlashM(final long runtime, final int onTime, final int offTime) {
        long startTime = System.currentTimeMillis();
        if (ServiceIncomingCall.camera == null && ServiceIncomingCall.p == null) {
            ServiceIncomingCall.camera = Camera.open();
            ServiceIncomingCall.p = ServiceIncomingCall.camera.getParameters();
        }

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
        ServiceIncomingCall.isAnimation = false;
    }
}
