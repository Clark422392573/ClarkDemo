package com.example.clark.clarkdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MomentsActivity extends AppCompatActivity {

    @InjectView(R.id.moments_toolbar_return)
    ImageView mToolbarReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.moments_toolbar_return)
    public void onClick() {
        finish();
    }
}
