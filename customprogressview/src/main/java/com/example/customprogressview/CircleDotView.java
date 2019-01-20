package com.example.customprogressview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Administrator on 2019/1/18.
 */

public class CircleDotView extends View {
    private float mSin_1;
    private Paint mPaint;
    private float width,height;

    public void setWidth(int width) {
        //将外界设置的看作dp为单位来计算
        float w = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources()
                .getDisplayMetrics());
        this.width = w;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public CircleDotView(Context context) {
        super(context);
        this.mPaint = new Paint();
    }

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
    public CircleDotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i("onMeasure","执行了onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        //设置宽高
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST){
        }
        //fill_parent或者精确值
        else if (specMode == MeasureSpec.EXACTLY){
        }else if(specMode == MeasureSpec.UNSPECIFIED){
        }
        return specSize;
    }
    //根据xml的设定获取高度
    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST){
        }
        //fill_parent或者精确值
        else if (specMode == MeasureSpec.EXACTLY){
        }
        return specSize;
    }

    /**
     * Implement this to do your drawing.
     *
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("onMeasure","执行了onDraw");
        super.onDraw(canvas);

        mSin_1 = (float) Math.sin(Math.toRadians(1));
        // 大圆半径
        float outerRadius = (getWidth() < getHeight() ? getWidth() : getHeight()) / 2f;
        //小圆点半径
        float dotRadius = mSin_1 * outerRadius / (1 + mSin_1);
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        // 1 画进度
//        mPaint.setColor(getResources().getColor(R.color.colorPrimary));
        mPaint.setStyle(Paint.Style.FILL);
        int count = 0;
        while (count++ < 50) {
            canvas.drawCircle(centerX, centerY - outerRadius + dotRadius, dotRadius, mPaint);
            canvas.rotate(7.2f, centerX, centerY);
        }
    }
    public void setDotColor(int color){
        mPaint.setColor(color);
        //设置完以后记得更新
        invalidate();
    }
}
