package com.example.ian.numbermorph;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private XmlLabelParser xmlLabelParser;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xmlLabelParser = new XmlLabelParser(this, R.drawable.vd_6);
        data = xmlLabelParser.getLabelData("path", "pathData");
        Log.d(TAG, "data : " + data);
    }
}
