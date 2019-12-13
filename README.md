## 源起
* 公司项目用到一个进度条动画，需要自己绘制从0加载到100的情况，而且效果需要很酷炫。最后是采用了lottie动画+一部分自定义View来实现，拆分了出来。过后，觉得其实自己实现这样的一个效果也不难，便开始了以下的尝试。先看下效果

![](https://user-gold-cdn.xitu.io/2019/1/26/1688a8754989d119?w=360&h=640&f=gif&s=190684)

## 分析
1. 分析下：最外层是一个由小圆点顺时针旋转的圆、第二层是直接一个圆、第三层有两层：内层是由矩形逆时针旋转的圆，外层是一个圆环、最后是文字展示。
2. 实现思路是这样：分成多个View来实现，第一层圆点是一个View，绘制完成是使用ObjectAnimator进行旋转；第二层和第三层的外层和文字是一个View，第三层内层矩形是一个View,同样是绘制完成使用ObjectAnimator旋转。（这里为何分成三个View咧，因为涉及到旋转动画，我想实现旋转的时候，是对画布canvas对象进行旋转，而在一个View里只有一个画布canvas对象，如果所有都绘制在一个canvas上，则旋转的时候就都会旋转，实现不了我要的效果。如果读者有更好的解决方案欢迎提出）  

![](https://user-gold-cdn.xitu.io/2019/1/26/1688a95157c599aa?w=306&h=279&f=png&s=31157)

## 开干  
#### 第一部分
1. [进度条动画](https://www.2cto.com/kf/201609/551312.html) 感谢这篇博客作者给我提供了很好的思路，让我举一反三，如果读者第一部分看的很模糊的话，可以结合这篇博客享用
2. 第一层圆点和第三层内层矩形应该怎么绘制？这里实现大同小异放在一起说


![](https://user-gold-cdn.xitu.io/2019/1/27/1688d41737185d79?w=677&h=446&f=png&s=38586)

3. 看到此图不用望而生畏，其实很简单，我们最终在画布上绘制的还是一个一个的圆，蓝色圆是真正要绘制的，空白圆是间隔距离，不需要绘制出来。现在总共是100个蓝色圆，100个空心圆，每个圆占据的角度是360/200=1.8度；绘制完蓝色圆以后，调用canvas.ratate()将画布旋转3.6度到下一个蓝色圆圆心位置开始绘制圆，最终就有100个蓝色圆了。
4. 怎么绘制蓝色圆？需要确定蓝色圆的圆心位置和半径。我们可以在onMeasure()中拿到View的width和height，除以2得到就是圆心坐标（centerX,centerY），R0就是宽或者高/2可以得到的半径，我们要求出r:
> R1 + r = R0  
R1 * sin0.9° = r  
由以上两式可以得出：r = (R0*sin0.9°)/(1+sin0.9°)——R0已知  
小蓝圆的圆心为：(centerX,centerY-R0+r)  

5. 这样就可以绘制出来了，这里把sin0.9°提高到sin1°,让蓝色圆大一点。  
关键代码如下：(为了美观，我将100个蓝色小圆改成了50个，而且每次旋转3.6*2=7.2度，也就是隔开了三个白色小圆的距离，绘制50次，每次转过了7.2度，50 * 7.2=360度，每次都绘制一个蓝色圆，最后是50个小蓝圆)

![](https://user-gold-cdn.xitu.io/2019/1/27/1688d40791431c1e?w=603&h=380&f=png&s=30217)
```
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

        mPaint.setStyle(Paint.Style.FILL);
        int count = 0;
        while (count++ < 50) {
            canvas.drawCircle(centerX, centerY - outerRadius + dotRadius, dotRadius, mPaint);
            canvas.rotate(7.2f, centerX, centerY);
        }
    }
```  
#### 第二部分
1. 绘制矩形形成的圆，原理也是一样的，只不过把绘制小蓝圆变成绘制矩形。绘制矩形需要确定左上角和右下角这两个点的位置就可以了。然后将画布旋转某一角度值继续绘制即可。这里绘制了50次，每次旋转10度，总共是500度>360度，保证大于360度即可，多余的会重复覆盖，但如果小于360度，就会导致绘制残缺。  

![](https://user-gold-cdn.xitu.io/2019/1/27/1688d733c3ee1abd?w=563&h=366&f=png&s=17166)

```
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
```  

#### 第三部分
1. 绘制圆环和圆，绘制文字这几个是放到一个View处理的，因为不用涉及到旋转，所以可以放到一起绘制。这部分是参考了另外一篇博客，有兴趣的读者也可以享用 [自定义进度条](https://blog.csdn.net/anonymousprogrammer/article/details/65634886)
2. 画圆这个比较简单不说，圆环的话，其实就是画圆弧，画圆弧的时候会先确定一个外接矩形，设置画笔类型为描边circlePaint.setStyle(Paint.Style.STROKE);同时不包含圆心，就可以了。具体的可以看下这篇博客  [drawArc画圆弧介绍](https://www.cnblogs.com/tjudzj/p/4387145.html)  ，这里不便展开。
```
private void drawCircle(Canvas canvas, int center, int radius)
    {
        //画一个简单的圆
        firstPaint.setShader(null); // 清除上一次的shader
        firstPaint.setColor(firstColor); // 设置底部圆环的颜色，这里使用第一种颜色
        firstPaint.setStyle(Paint.Style.STROKE); // 设置绘制的圆为空心
        canvas.drawCircle(center, center, radius+40, firstPaint); 
        
        //画一个圆环
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        circlePaint.setShader(null);
        // 绘制颜色渐变圆环
        // shader类是Android在图形变换中非常重要的一个类。Shader在三维软件中我们称之为着色器，其作用是来给图像着色。
        LinearGradient linearGradient = new LinearGradient(circleWidth, circleWidth, getMeasuredWidth()
                - circleWidth, getMeasuredHeight() - circleWidth, colorArray, null, Shader.TileMode.MIRROR);
        circlePaint.setShader(linearGradient);
        //这里注意设置为描边类型
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setShadowLayer(10, 10, 10, Color.RED);
        circlePaint.setColor(secondColor); // 设置圆弧的颜色
        circlePaint.setStrokeCap(Paint.Cap.ROUND); // 把每段圆弧改成圆角的
        // 计算每次画圆弧时扫过的角度，这里计算要注意分母要转为float类型，否则alphaAngle永远为0
        alphaAngle = currentValue * 360.0f / maxValue * 1.0f;
        canvas.drawArc(oval, -90, alphaAngle, false, circlePaint);
    }
```
4. 可以看到绘制圆环的时候,用到alphaAngle，这个是扫过的角度，根据currentValue当前的进度值，得出当前扫过的角度。maxValue是100最大进度值。更新进度的时候会调用以下这个方法进行更新，在invalidate()被调用后，View会进行重绘，回调onDraw()方法，得出新的alphaAngle角度值，绘制出对应进度的圆环。
```
public void setProgress(int progress)
    {
        int percent = progress * maxValue / 100;
        if (percent < 0)
        {
            percent = 0;
        }
        if (percent > 100)
        {
            percent = 100;
        }
        this.currentValue = percent;
        //更新View
        invalidate();
    }
```
5. 绘制文字也是同样的道理，通过setProgress()方法来更新currentValue值。这里绘制文字有个注意的地方：文字的绘制位置居中。drawText()方法的第二三个参数分别是Text文字x,y坐标，这里为什么设置为center和baseline这两个值是有原因的，墙裂推荐这篇博客了解原因：[drawText介绍](https://blog.csdn.net/harvic880925/article/details/50423762)  这部分的主要代码如下：
```
private void drawText(Canvas canvas, int center, int radius)
    {
        float result = (currentValue * 100.0f / maxValue * 1.0f); // 计算进度
        String percent = String.format("%.1f", result) + "%";

        textPaint.setTextAlign(Paint.Align.CENTER); // 设置文字居中，文字的x坐标要注意
        textPaint.setColor(textColor); // 设置文字颜色
        textPaint.setTextSize(40); // 设置要绘制的文字大小
        textPaint.setStrokeWidth(0); // 注意此处一定要重新设置宽度为0,否则绘制的文字会重叠
        Rect bounds = new Rect(); // 文字边框
        textPaint.getTextBounds(percent, 0, percent.length(), bounds); // 获得绘制文字的边界矩形
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt(); // 获取绘制Text时的四条线
        int baseline = center + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom; 
        canvas.drawText(percent, center, baseline, textPaint); // 绘制表示进度的文字
    }
```
## 总结
1. 写到这其实还没完，只是对里面的重点剖析了下，剩下的细节读者可以看下源码，我也封装成了一个依赖库，有需要可以使用。对应的demo使用放到github上，喜欢的可以点个star。  
* 在项目的build.gradle中添加：
```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
* 在module的build.gradle中添加：
```
implementation 'com.github.guoxiaozhou:AnimationDemo:0.5'
```
* 具体使用demo地址：https://github.com/guoxiaozhou/AnimationDemo 项目中包含依赖库源码，有兴趣的可以看看细节，有问题或者错误直接联系我
