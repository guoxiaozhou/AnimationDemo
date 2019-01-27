package com.example.customprogressview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Administrator on 2019/1/19.
 */

public class CircleRectView extends View {
    private Paint mPaint;


    public CircleRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
    }

    public CircleRectView(Context context) {
        super(context);
        mPaint = new Paint();
    }

    /**
     * Implement this to do your drawing.
     *
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setShader(null);
        mPaint.setAntiAlias(true); // 抗锯齿
        mPaint.setDither(true); // 防抖动

        // 半径，这里减去40是将半径缩小40
        float outerRadius = (getWidth() < getHeight() ? getWidth() : getHeight()) / 2f-40;
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        mPaint.setStyle(Paint.Style.FILL);
        int count = 0;
        int des = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
        while (count++ < 50) {
            canvas.drawRect(centerX-3,centerY-outerRadius,centerX+3,centerY-outerRadius+des,
                    mPaint);
            canvas.rotate(10.0f, centerX, centerY);
        }
    }
    public void setDotColor(int color){
        mPaint.setColor(color);
        //设置完以后记得更新
        invalidate();
    }

}
