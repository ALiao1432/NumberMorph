package com.example.ian.numbermorph;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class NumberMorphView extends View {

    private static final String TAG = "XmlLabelParser";

    public static final int W_SIZE = 500;
    public static final int H_SIZE = 500;
    private final SvgData svgData;
    private final Paint paint = new Paint();
    private DataPath path;
    private ValueAnimator pointAnimator;
    private int currentId;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(W_SIZE, H_SIZE);
    }

    @SuppressWarnings("ClickableViewAccessibility")
    NumberMorphView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);

        svgData = new SvgData(context);

        initPaint();
        initAnimator();
        initPath(R.drawable.vd_0);
    }

    private void initPaint() {
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#212121"));
        paint.setStrokeWidth(36);
    }

    private void initAnimator() {
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        final long animationDuration = 250;

        pointAnimator = ValueAnimator.ofFloat(0, 1, 1.05f, 1.025f, 1);
        pointAnimator.setDuration(animationDuration);
        pointAnimator.setInterpolator(linearInterpolator);
        pointAnimator.addUpdateListener(valueAnimator -> {
            path.reset();
            path = svgData.getMorphPath((float) valueAnimator.getAnimatedValue());
            invalidate();
        });
    }

    private void initPath(int id) {
        path = svgData.getPath(id);
        currentId = id;
    }

    public void performAnimation(int toId) {
        svgData.setMorphRes(currentId, toId);
        currentId = toId;
        pointAnimator.start();
    }

    public void setCurrentId(int id) {
        initPath(id);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(path, paint);
    }
}
