package com.example.ian.numbermorph;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import study.ian.colorpickerlib.ColorPickerView;
import study.ian.morphviewlib.MorphView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private final MorphView[] morphViews = new MorphView[14];
    private View dialogView;
    private ConstraintLayout constraintLayout;
    private Handler handler;
    private Date date;
    private SimpleDateFormat dateFormat;
    private String dateString;
    private boolean isStarted = false;
    private final int[] ids = {
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
    private View.OnLongClickListener longClickListener = v -> {
        int id = v.getId();

        new AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton(R.string.select_btn, (dialog, which) -> {
                    ColorPickerView colorPickerView = dialogView.findViewById(R.id.colorPickerView);
                    int color = colorPickerView.getSelectColor();
                    if (id == R.id.constraintLayout) {
                        constraintLayout.setBackgroundColor(color);
                    } else {
                        switch (id) {
                            case R.id.y0_MorphView:
                            case R.id.y1_MorphView:
                            case R.id.y2_MorphView:
                            case R.id.y3_MorphView:
                            case R.id.M0_MorphView:
                            case R.id.M1_MorphView:
                            case R.id.d0_MorphView:
                            case R.id.d1_MorphView:
                                for (int j = 0; j <= 7; j++) {
                                    morphViews[j].setPaintColor(color);
                                }
                                break;
                            case R.id.H0_MorphView:
                            case R.id.H1_MorphView:
                            case R.id.m0_MorphView:
                            case R.id.m1_MorphView:
                                for (int j = 8; j <= 11; j++) {
                                    morphViews[j].setPaintColor(color);
                                }
                                break;
                            case R.id.s0_MorphView:
                            case R.id.s1_MorphView:
                                for (int j = 12; j < morphViews.length; j++) {
                                    morphViews[j].setPaintColor(color);
                                }
                                break;
                            default:
                                break;
                        }
                    }

                    ((ViewGroup) dialogView.getParent()).removeView(dialogView);
                })
                .setOnCancelListener(dialog -> ((ViewGroup) dialogView.getParent()).removeView(dialogView))
                .show();
        return false;
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

        dialogView = LayoutInflater.from(this).inflate(R.layout.color_picker_dialog, null);
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
        final String APP_BG_COLOR = "#000000";

        constraintLayout = findViewById(R.id.constraintLayout);
        constraintLayout.setOnLongClickListener(longClickListener);
        constraintLayout.setBackgroundColor(Color.parseColor(APP_BG_COLOR));

        for (int i = 0; i < ids.length; i++) {
            morphViews[i] = findViewById(ids[i]);
            morphViews[i].setCurrentId(VdConstants.VD_NUM_MAP.get(0));
            morphViews[i].setOnLongClickListener(longClickListener);

            if (i <= 7) {
                morphViews[i].setSize(125, 125);
                morphViews[i].setPaintColor("#004d40");
                morphViews[i].setPaintWidth(10);
            } else if (i <= 11) {
                morphViews[i].setSize(200, 200);
                morphViews[i].setPaintColor("#00897b");
                morphViews[i].setPaintWidth(20);
            } else {
                morphViews[i].setSize(450, 450);
                morphViews[i].setPaintColor("#4db6ac");
                morphViews[i].setPaintWidth(30);
            }
        }
    }

    @SuppressLint("HandlerLeak")
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

                        for (int i = 0; i < dateString.length(); i++) {
                            int value = Integer.valueOf(dateString.substring(i, i + 1));
                            morphViews[i].performAnimation(VdConstants.VD_NUM_MAP.get(value));
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
