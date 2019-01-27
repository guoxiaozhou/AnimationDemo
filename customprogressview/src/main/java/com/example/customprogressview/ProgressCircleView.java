package com.example.customprogressview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2019/1/19.
 */

public class ProgressCircleView extends FrameLayout {

    private  int textColor;
    private Context context;

    private  int circleColor;
    private  int circleDotWidth;
    private  int circleRectWidth;
    private  int circleNumberRingWidth;
    private  int circleRectColor = 0;
    private  int circleDotColor = 0;
    private  int circleNumberColor = 0;
    private  int circleNumberWidth = 0;
    private CircleDotView customDotview;
    private CircleRectView customRectview;
    private CircleNumberView circleNumberView;



    public ProgressCircleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        this.context = context;

    }
    public ProgressCircleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);



        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.progressCircleView,
                defStyleAttr, 0);
        int n = ta.getIndexCount();
        int size_150dp = pxTodp(150);
        int size_170dp = pxTodp(170);
        int size_4dp = pxTodp(4);


        //若xml没有设置以下属性，则使用默认值
        circleDotWidth = size_170dp;
        circleRectWidth = size_150dp;
        circleNumberWidth = size_150dp;
        circleNumberRingWidth = size_4dp;
        circleDotColor = getResources().getColor(R.color.defaultcolor);
        circleRectColor = getResources().getColor(R.color.defaultcolor);
        circleNumberColor = getResources().getColor(R.color.defaultcolor);
        circleColor = getResources().getColor(R.color.defaultcolor);
        textColor = getResources().getColor(R.color.defaultcolor);

        for(int i=0;i<n;i++){
            int attr = ta.getIndex(i);
            if (attr == R.styleable.progressCircleView_circleNumberWidth) {
                circleNumberWidth = (int) ta.getDimension(attr, size_150dp);

            } else if (attr == R.styleable.progressCircleView_circleRectWidth) {
                circleRectWidth = (int) ta.getDimension(attr, size_150dp);

            } else if (attr == R.styleable.progressCircleView_circleDotWidth) {
                circleDotWidth = (int) ta.getDimension(attr, size_170dp);

            } else if (attr == R.styleable.progressCircleView_circleNumberColor) {
                circleNumberColor = ta.getColor(attr, getResources().getColor(R.color.defaultcolor));

            } else if (attr == R.styleable.progressCircleView_circleDotColor) {
                circleDotColor = ta.getColor(attr, getResources().getColor(R.color.defaultcolor));

            } else if (attr == R.styleable.progressCircleView_circleRectColor) {
                circleRectColor = ta.getColor(attr, getResources().getColor(R.color.defaultcolor));

            }else if (attr == R.styleable.progressCircleView_circleColor) {
                circleColor = ta.getColor(attr, getResources().getColor(R.color.defaultcolor));

            }else if (attr == R.styleable.progressCircleView_textColor) {
                circleColor = ta.getColor(attr, getResources().getColor(R.color.defaultcolor));
            }
        }



        customDotview = new CircleDotView(context,null);
        LayoutParams dotParams = new LayoutParams(circleDotWidth, circleDotWidth);
        dotParams.gravity = Gravity.CENTER;
        customDotview.setLayoutParams(dotParams);
        customDotview.setDotColor(circleDotColor);

        customRectview = new CircleRectView(context,null);
        LayoutParams rectParams = new LayoutParams(circleRectWidth, circleRectWidth);
        rectParams.gravity = Gravity.CENTER;
        customRectview.setLayoutParams(rectParams);
        customRectview.setDotColor(circleRectColor);
        this.addView(customDotview);
        this.addView(customRectview);


        Log.i("onMeasure","测试");
        ObjectAnimator animator = ObjectAnimator.ofFloat(customDotview,"rotation", 0f, 360f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(7000);
        animator.start();

        animator = ObjectAnimator.ofFloat(customRectview,"rotation",360f,0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(8000);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();

        circleNumberView = new CircleNumberView(context,null);
        Log.i("ProgressCircleView","circleNumberWidth:"+circleNumberWidth);
        LayoutParams numberParams = new LayoutParams(circleNumberWidth, circleNumberWidth);
        numberParams.gravity = Gravity.CENTER;
        circleNumberView.setLayoutParams(numberParams);
        circleNumberView.setSecondColor(circleNumberColor);
        circleNumberView.setCircleWidth(circleNumberRingWidth);
        circleNumberView.setFirstColor(circleColor);
        circleNumberView.setTextColor(textColor);
        this.addView(circleNumberView);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measure(widthMeasureSpec);
        int height = measure(heightMeasureSpec);
        //设置宽高
        setMeasuredDimension(width, height);
    }

    private int measure(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int base = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST){
            specSize = base;
            Log.i("onMeasure","AT_MOST");
        }
        //fill_parent或者精确值
        else if (specMode == MeasureSpec.EXACTLY){

            Log.i("onMeasure","EXACTLY");
        }
        return specSize;
    }

    int pxTodp(int px){
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
    }

   public void setProgress(int progress){
        circleNumberView.setProgress(progress,false);
    }

}
