package com.example.ian.numbermorph;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;

public class NumberMorphView extends View {

    private static final String TAG = "XmlLabelParser";

    private DataPath path;
    private SvgData svgData;
    public static final int W_SIZE = 500;
    public static final int H_SIZE = 500;
    private final int VD_ID = R.drawable.vd_6;
    private final Paint paint = new Paint();
    private ValueAnimator pointAnimator;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(W_SIZE, H_SIZE);
    }

    @SuppressWarnings("ClickableViewAccessibility")
    NumberMorphView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);

        initPaint();
        initAnimator();

        svgData = new SvgData(context);
        svgData.setMorphRes(R.drawable.vd_pause_bar, R.drawable.vd_pause_bar);

        path = svgData.getMorphPath(0f);
    }

    private void initPaint() {
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#212121"));
        paint.setStrokeWidth(15);
    }

    private void initAnimator() {
        OvershootInterpolator overshootInterpolator = new OvershootInterpolator();
        long animationDuration = 170;

        pointAnimator = ValueAnimator.ofFloat(0, 1);
        pointAnimator.setDuration(animationDuration);
        pointAnimator.setInterpolator(overshootInterpolator);
        pointAnimator.addUpdateListener(valueAnimator -> {
//            path = svgData.getMorphPath((float) valueAnimator.getAnimatedValue());
            invalidate();
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(path, paint);
    }
}
