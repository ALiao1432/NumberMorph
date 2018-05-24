package com.example.ian.numbermorph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

public class NumberMorphView extends View {

    private static final String TAG = "XmlLabelParser";

    private DataPath path;
    private final Paint paint = new Paint();
    private final int wSize = 500;
    private final int hSize = 500;
    private final int vdId = R.drawable.vd_test;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(wSize, hSize);
    }

    NumberMorphView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);

        initPaint();
        initPath(getPathData(context));
    }

    private void initPaint() {
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#212121"));
        paint.setStrokeWidth(15);
    }

    private void initPath(List<String> data) {
        float[] viewports = getViewport(getContext());
        float[] scaleFactors = {
                wSize / viewports[0],
                hSize / viewports[1],
        };

        path = new DataPath(data, scaleFactors);
    }

    private List<String> getPathData(Context context) {
        List<String> pathData;
        XmlLabelParser xmlLabelParser = new XmlLabelParser(context, vdId);
        pathData = xmlLabelParser.getLabelData("path", "pathData");

        return pathData;
    }

    private float[] getViewport(Context context) {
        XmlLabelParser x1 = new XmlLabelParser(context, vdId);
        XmlLabelParser x2 = new XmlLabelParser(context, vdId);

        String[] viewportStrings = {
                x1.getLabelData("vector", "viewportWidth").get(0),
                x2.getLabelData("vector", "viewportHeight").get(0)
        };

        return new float[]{
                Float.valueOf(viewportStrings[0]),
                Float.valueOf(viewportStrings[1])
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(path, paint);
    }
}
