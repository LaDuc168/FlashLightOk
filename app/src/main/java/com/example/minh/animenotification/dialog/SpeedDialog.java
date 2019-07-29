package com.example.minh.animenotification.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.minh.animenotification.R;

public class SpeedDialog extends Dialog {
    private Context mContext;
    int onTime;
    int offTime;
    int runTime;
    SharedPreferences sharedPreferences;
    FlashListener mListener;


    public SpeedDialog(Context context,FlashListener listener) {
        super(context);
        sharedPreferences = context.getSharedPreferences("ANIME_NOTIFY", Context.MODE_PRIVATE);
        onTime = sharedPreferences.getInt("ON_TIME", 50);
        offTime = sharedPreferences.getInt("OFF_TIME", 50);
        runTime = sharedPreferences.getInt("RUN_TIME", 2);
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_speed);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        final TextView textSpeedOn = findViewById(R.id.text_speed_on);
        final TextView textSpeedOff = findViewById(R.id.text_speed_off);
        final TextView textSpeedRun = findViewById(R.id.text_speed_run);
        final TextView textTry = findViewById(R.id.text_try);
        final TextView textCancel = findViewById(R.id.text_cancel);
        TextView textSave = findViewById(R.id.text_save);
        final SeekBar seekOn = findViewById(R.id.seek_bar_on);
        final SeekBar seekOff = findViewById(R.id.seek_bar_off);
        final SeekBar seekRun = findViewById(R.id.seek_bar_run);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        textSpeedOn.setText(onTime + " ms");
        textSpeedOff.setText(offTime + " ms");
        textSpeedRun.setText(runTime + " s");
        seekOn.setProgress(onTime - 50);
        seekOff.setProgress(offTime - 50);
        seekRun.setProgress(runTime - 2);
        textTry.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
              mListener.tryFlash(seekOn.getProgress(), seekOff.getProgress(), seekRun.getProgress());
            }
        });
        textSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("ON_TIME", seekOn.getProgress() + 50);
                editor.putInt("OFF_TIME", seekOff.getProgress() + 50);
                editor.putInt("RUN_TIME", seekRun.getProgress() + 2);
                editor.commit();
                cancel();
                dismiss();
            }
        });

        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
                dismiss();
            }
        });
        seekOn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textSpeedOn.setText((i + 50) + " ms");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekOff.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textSpeedOff.setText((i + 50) + " ms");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekRun.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textSpeedRun.setText((i + 2) + " s");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        super.onCreate(savedInstanceState);
    }

    public interface FlashListener{
        void tryFlash(int onTime,int offTime,int runTime);
    }
}
