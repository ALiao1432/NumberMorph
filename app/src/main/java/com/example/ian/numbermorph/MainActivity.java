package com.example.ian.numbermorph;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ConstraintLayout constraintLayout;
    private NumberMorphView[] numberMorphViews = new NumberMorphView[14];
    private Handler handler;
    private Date date;
    private SimpleDateFormat dateFormat;
    private String dateString;
    private boolean isStarted = false;
    private boolean isInitViewPath = false;
    private int[] ids = {
            R.id.y0_MorphView,
            R.id.y1_MorphView,
            R.id.y2_MorphView,
            R.id.y3_MorphView,
            R.id.M0_MorphView,
            R.id.M1_MorphView,
            R.id.d0_MorphView,
            R.id.d1_MorphView,
            R.id.H0_MorphView,
            R.id.H1_MorphView,
            R.id.m0_MorphView,
            R.id.m1_MorphView,
            R.id.s0_MorphView,
            R.id.s1_MorphView
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initClock();

        constraintLayout.setBackgroundColor(Color.parseColor("#212121"));

        startedGetTime();
    }

    @Override
    protected void onResume() {
        super.onResume();

        startedGetTime();
    }

    @Override
    protected void onPause() {
        super.onPause();

        isStarted = false;
    }

    private void initView() {
        constraintLayout = findViewById(R.id.constraintLayout);
        for (int i = 0; i < ids.length; i++) {
            numberMorphViews[i] = findViewById(ids[i]);

            if (i <= 7) {
                numberMorphViews[i].setSize(125, 125);
            } else if (i <= 11) {
                numberMorphViews[i].setSize(200, 200);
            } else {
                numberMorphViews[i].setSize(450, 450);
            }
        }
    }

    private void initViewPath() {
        for (int i = 0; i < ids.length; i++) {
            numberMorphViews[i].setCurrentId(VdConstants.vdNumMap.get(0));
        }
    }

    private void initClock() {
        dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.TAIWAN);
        date = new Date();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 9487:
                        date.setTime(System.currentTimeMillis());
                        dateString = dateFormat.format(date);

                        if (!isInitViewPath) {
                            initViewPath();
                            isInitViewPath = true;
                        } else {
                            for (int i = 0; i < dateString.length(); i++) {
                                int value = Integer.valueOf(dateString.substring(i, i + 1));
                                numberMorphViews[i].performAnimation(VdConstants.vdNumMap.get(value));
                            }
                        }

                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    private void startedGetTime() {
        isStarted = true;
        getTime();
    }

    private void getTime() {
        new Thread(() -> {
            while (isStarted) {
                Message clockMessage = new Message();
                clockMessage.what = 9487;
                handler.sendMessage(clockMessage);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
