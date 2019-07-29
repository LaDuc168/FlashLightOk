package com.example.minh.animenotification.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.minh.animenotification.R;
import com.example.minh.animenotification.adapter.AppAdapter;
import com.example.minh.animenotification.model.App;
import com.example.minh.animenotification.model.GetAppIntalledAsync;

import java.util.ArrayList;

public class ChooseAppActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<App> mApps;
    private AppAdapter mAdapter;
    private ListView mListViewApp;
    private TextView mTextOk;
    private TextView mTextCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_app);
        init();
    }

    private void init() {
        mApps = new ArrayList<>();
        mAdapter = new AppAdapter(this,mApps);
        mListViewApp = findViewById(R.id.list_app);
        mTextOk = findViewById(R.id.text_ok);
        mTextOk.setOnClickListener(this);
        mTextCancel = findViewById(R.id.text_cancel);
        mTextCancel.setOnClickListener(this);
        mListViewApp.setAdapter(mAdapter);
        GetAppIntalledAsync async = new GetAppIntalledAsync(this,mApps,mAdapter);
        async.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.text_ok:
                SharedPreferences sharedPreferences = getSharedPreferences("ANIME_NOTIFY", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                for (int i = 0; i < mApps.size(); i++) {
                    editor.putBoolean(mApps.get(i).getPakageName(), mApps.get(i).getChoose());
                }
                editor.commit();
                finish();
                break;
            case R.id.text_cancel:
                finish();
                break;
        }
    }
}
