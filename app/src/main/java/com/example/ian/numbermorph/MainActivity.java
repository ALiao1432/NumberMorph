package com.example.ian.numbermorph;

import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static int currentCnt = 0;
    private ConstraintLayout constraintLayout;
    private NumberMorphView numberMorphView;
    private Date date;
    private SimpleDateFormat yyyyMMddFormat;
    private SimpleDateFormat HHmmssFormat;
    private String yyyyMMddString;
    private String HHmmssString;
    private boolean isStarted = false;

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

        findView();
        initClock();
        initView();

        constraintLayout.setBackgroundColor(Color.parseColor("#1a237e"));

        startedGetTime();
    }

    @Override
    protected void onPause() {
        super.onPause();

        isStarted = false;
    }

    private void findView() {
        constraintLayout = findViewById(R.id.constraintLayout);
        numberMorphView = findViewById(R.id.numberMorphView);
    }

    private void initClock() {
        yyyyMMddFormat = new SimpleDateFormat("yyyyMMdd", Locale.TAIWAN);
        HHmmssFormat = new SimpleDateFormat("HHmmss", Locale.TAIWAN);
        date = new Date();
    }

    private void initView() {
        numberMorphView.setSize(500, 500);
        numberMorphView.setOnClickListener(v -> {
            currentCnt++;
            if (currentCnt == 10) {
                currentCnt = 0;
            }

            numberMorphView.performAnimation(VdConstants.vdNumMap.get(currentCnt));
        });
    }

    private void startedGetTime() {
        isStarted = true;
        getTime();
    }

    private void getTime() {
        new Thread(() -> {
            while (isStarted) {
                date.setTime(System.currentTimeMillis());
                yyyyMMddString = yyyyMMddFormat.format(date);
                HHmmssString = HHmmssFormat.format(date);


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
