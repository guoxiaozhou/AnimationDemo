package com.example.customprogressview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2019/1/19.
 */

public class CircleRectView extends View {
    private Paint mPaint;

    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     * <p>
     * <p>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     * @see #View(Context, AttributeSet, int)
     */
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

        // 大圆半径
        float outerRadius = (getWidth() < getHeight() ? getWidth() : getHeight()) / 2f;
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        // 1 画进度
//        mPaint.setColor(getResources().getColor(R.color.colorPrimary));
        mPaint.setStyle(Paint.Style.FILL);
        int count = 0;
        while (count++ < 50) {
            canvas.drawRect(centerX-3,centerY-outerRadius,centerX+3,centerY-outerRadius+6,
                    mPaint);
            canvas.rotate(7.2f, centerX, centerY);
        }
    }
    public void setDotColor(int color){
        mPaint.setColor(color);
        //设置完以后记得更新
        invalidate();
    }

}
