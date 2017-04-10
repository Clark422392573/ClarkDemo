package com.example.clark.clarkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Clark on 2017/4/2.
 */
public class WelcomeActivity extends AppCompatActivity {

    @InjectView(R.id.txt_welcome_timer)
    TextView mWelcomeTimer;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.inject(this);
        goToNextPage();
    }

    private void goToNextPage() {
        timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mWelcomeTimer.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                mWelcomeTimer.setText("跳转");
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            }
        }.start();
    }

    @OnClick(R.id.txt_welcome_timer)
    public void onClick() {
        timer.cancel();
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }
}
