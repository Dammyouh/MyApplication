package com.example.yangxiaoyu.myapplication;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yangxy on 2018/8/30.
 */
public class BarView extends View {
    private float itemWidth;// 每一个子进度条的宽度
    private int progressNum;// 进度的数量
    private float itemSpace = 0;//进度条之间的间距
    private boolean hasDone;// 是否100%
    private float spaceLeft = 0;// 防止画背景时与背景重叠，需要距离左边的边距
    private int  itemColor;//子进度条的颜色
    private int spaceColor;//断点之间的颜色
    private float itemRadius;//进度条的圆角度数
    private Context context;
    Paint paint1 = new Paint();
    Paint paint2 = new Paint();
    Paint paint3 = new Paint();

    public BarView(Context context) {
        this(context,null);
    }

    public BarView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public BarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints(context,attrs);
    }

    private void initPaints(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.BarView);
        itemColor = typedArray.getColor(R.styleable.BarView_itemColor,Color.BLACK);
        spaceColor = typedArray.getColor(R.styleable.BarView_spaceColor,Color.WHITE);
        itemSpace = typedArray.getDimension(R.styleable.BarView_itemSpace,0);
        spaceLeft = typedArray.getDimension(R.styleable.BarView_spaceLeft,0);
        itemRadius = typedArray.getDimension(R.styleable.BarView_itemRadius,100);
        itemRadius = MainActivity.Companion.dip2px(context,itemRadius);
        typedArray.recycle();

        paint1.setColor(itemColor);
        paint1.setStrokeWidth(50f);
        paint1.setStyle(Paint.Style.FILL);
        paint2.setColor(spaceColor);
        paint2.setStrokeWidth(50f);
        paint2.setStyle(Paint.Style.FILL);
        paint3.setColor(itemColor);
        paint3.setStrokeWidth( MainActivity.Companion.dip2px(context,7));
        paint3.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float spaceStart = 0;//每一个间距开始绘制的位置
        float progressStart = 0;//每一个进度条开始绘制的位置
        for (int i = 0; i < progressNum; i++) {
            if (i == 0) {
                @SuppressLint("DrawAllocation") RectF rectF = new RectF(spaceLeft, 0, itemWidth + spaceLeft, MainActivity.Companion.dip2px(context,7));
                canvas.drawRoundRect(rectF, itemRadius, itemRadius, paint1);
                canvas.drawLine(MainActivity.Companion.dip2px(context, 10f), 0, itemWidth + spaceLeft, 0, paint1);
                spaceStart = itemWidth + spaceLeft;
                canvas.drawLine(spaceStart, 0, spaceStart + itemSpace, 0, paint2);
                progressStart = spaceStart + itemSpace;
            } else if (hasDone && i == progressNum - 1) {
                @SuppressLint("DrawAllocation") RectF rectF = new RectF(progressStart, 0, itemWidth + progressStart, MainActivity.Companion.dip2px(context,7));
                canvas.drawLine(progressStart,0, itemWidth - MainActivity.Companion.dip2px(context, 15f) + progressStart,0, paint1);
                canvas.drawRoundRect(rectF, itemRadius, itemRadius, paint1);
            } else {
                canvas.drawLine(progressStart, 0, itemWidth + progressStart, 0, paint1);
                spaceStart = itemWidth + progressStart;
                canvas.drawLine(spaceStart, 0, spaceStart + itemSpace, 0, paint2);
                progressStart = spaceStart + itemSpace;
            }

        }
    }

    public void setProgressNum(int num) {
        this.progressNum = num;
    }

    public void setItemWidth(float itemWidth) {
        this.itemWidth = itemWidth;
    }

    public void setHasDone(boolean hasDone) {
        this.hasDone = hasDone;
    }


}
