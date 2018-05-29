package com.example.ian.numbermorph;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private NumberMorphView numberMorphView;
    private static int currentCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        initView();
    }

    private void findView() {
        numberMorphView = findViewById(R.id.numberMorphView);
    }

    private void initView() {
        numberMorphView.setOnClickListener(v -> {
            currentCnt++;
            if (currentCnt == 10) {
                currentCnt = 0;
            }

            numberMorphView.performAnimation(VdConstants.vdNumMap.get(currentCnt));
        });
    }
}
