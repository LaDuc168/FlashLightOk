package com.example.minh.animenotification.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.minh.animenotification.R;
import com.example.minh.animenotification.adapter.MyIconAdapter;
import com.example.minh.animenotification.myService.ServiceIncomingCall;

public class ChooseIconActivity extends AppCompatActivity implements MyIconAdapter.IconListener{
    private MyIconAdapter mAdapter;
    private RecyclerView mRecycleIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_icon);
        mAdapter = new MyIconAdapter(this,this);
        mRecycleIcon = findViewById(R.id.recycle_icon);
        mRecycleIcon.setHasFixedSize(true);
        mRecycleIcon.setLayoutManager(new GridLayoutManager(this, 4));
        mRecycleIcon.setAdapter(mAdapter);
    }

    @Override
    public void chooseIcon(int icon) {
        ServiceIncomingCall.ICON_NAME = icon;
        finish();
    }
}
