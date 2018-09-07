package com.example.yangxiaoyu.myapplication;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by yangxy on 2018/8/30.
 */
public class BarView extends View {
    private int currentNum;// 进度的数量
    private int totalNum;// 总的数量
    private float totalWidth;// 总的宽度
    private float totalHeight;// 总的高度
    private float itemWidth;// 每一个子进度条的宽度
    private float itemHeight;// 每一个子进度条的高度
    private float itemSpace;// 进度条之间的间距
    private float spaceLeft = 0;// 防止画背景时与背景重叠，需要距离左边的边距
    private int itemColor;// 子进度条的颜色
    private int spaceColor;// 进度条空隙之间的颜色
    private float itemRadius;// 进度条的圆角度数
    private float topStart;// 中间进度条开始画的位置距离上面的距离
    private float outRadius;// 外围边框的圆弧半径
    private float innerRadius;//内围边框的圆弧半径
    private Context context;
    private Paint itemPaint;
    private Paint spacePaint;

    public BarView(Context context) {
        this(context, null);
    }

    public BarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints(context, attrs);
    }

    private void initPaints(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BarView);
        itemColor = typedArray.getColor(R.styleable.BarView_itemColor, Color.BLACK);
        spaceColor = typedArray.getColor(R.styleable.BarView_spaceColor, Color.WHITE);
        itemSpace = typedArray.getDimension(R.styleable.BarView_itemSpace, 0);
        spaceLeft = typedArray.getDimension(R.styleable.BarView_spaceLeft, 0);
        itemRadius = typedArray.getDimension(R.styleable.BarView_itemRadius, 0);
        itemHeight = typedArray.getDimension(R.styleable.BarView_itemHeight, 0);
        typedArray.recycle();

        itemPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        spacePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        itemPaint.setStrokeWidth(MainActivity.Companion.dip2px(context,2));
        itemPaint.setColor(itemColor);
        spacePaint.setColor(spaceColor);
        spacePaint.setStrokeWidth(2f);
        spacePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        totalHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.d("yxy", "onMeasure: width = " + totalWidth + "height = " + totalHeight);
        itemWidth = (totalWidth - 2 * spaceLeft - (totalNum - 1) * itemSpace) / totalNum;
        topStart = (totalHeight - itemHeight) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画背景框
        drawBackground(canvas);

        // 画中间的进度条
        drawCurrentBar(canvas);
    }

    private void drawCurrentBar(Canvas canvas) {
        float spaceStart = 0;//每一个间距开始绘制的位置
        float progressStart = 0;//每一个进度条开始绘制的位置

        itemPaint.setColor(itemColor);
        for (int i = 0; i < currentNum; i++) {
            if (i == 0) {
                @SuppressLint("DrawAllocation") RectF rectF = new RectF(spaceLeft, topStart, itemWidth + spaceLeft, topStart + itemHeight);
                canvas.drawRoundRect(rectF, itemRadius, itemRadius, itemPaint);
                @SuppressLint("DrawAllocation") RectF rect = new RectF(spaceLeft + MainActivity.Companion.dip2px(context, 10f), topStart, itemWidth + spaceLeft, topStart + itemHeight);
                canvas.drawRect(rect, itemPaint);
                progressStart = itemWidth + spaceLeft + itemSpace;
            } else if (currentNum == totalNum && i == currentNum - 1) {
                @SuppressLint("DrawAllocation") RectF rectF = new RectF(progressStart, topStart, itemWidth + progressStart, topStart + itemHeight);
                canvas.drawRoundRect(rectF, itemRadius, itemRadius, itemPaint);
                @SuppressLint("DrawAllocation") RectF rect = new RectF(progressStart, topStart, itemWidth + progressStart - MainActivity.Companion.dip2px(context, 10f), topStart + itemHeight);
                canvas.drawRect(rect, itemPaint);
            } else {
                @SuppressLint("DrawAllocation") RectF rectF = new RectF(progressStart, topStart, itemWidth + progressStart, topStart + itemHeight);
                canvas.drawRect(rectF, itemPaint);
                spaceStart = itemWidth + progressStart + itemWidth;
                progressStart = itemWidth + progressStart + itemSpace;
            }
        }

        if (totalNum - currentNum > 1) {
            for (int i = 0; i < totalNum - currentNum - 1; i++) {
                @SuppressLint("DrawAllocation") RectF rectF = new RectF(spaceStart, topStart, spaceStart + itemSpace, topStart + itemHeight);
                canvas.drawRect(rectF, spacePaint);
                spaceStart = spaceStart + itemSpace + itemWidth;
            }
        }
    }

    private void drawBackground(Canvas canvas) {
        //画外面的边框，先画红色的
        itemPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        outRadius = totalHeight/2;
        RectF rectF = new RectF(outRadius,0,totalWidth-outRadius,totalHeight);
        canvas.drawRect(rectF,itemPaint);
        itemPaint.setStrokeWidth(0);
        RectF rectLeft = new RectF(0,0,2*outRadius,totalHeight);
        canvas.drawArc(rectLeft, 90, 180, false,itemPaint); // 绘制左边扇形
        RectF rectRight = new RectF(totalWidth - 2 * outRadius,0,totalWidth,totalHeight);
        canvas.drawArc(rectRight,-90,180,false,itemPaint);// 绘制右边扇形
        //画外面的边框，再画白色的进行覆盖
        itemPaint.setStyle(Paint.Style.FILL);
        innerRadius = outRadius - itemSpace;
        itemPaint.setColor(Color.WHITE);
        RectF rectSmall = new RectF(outRadius - itemSpace,itemSpace,totalWidth-outRadius,totalHeight -itemSpace);
        canvas.drawRect(rectSmall,itemPaint);
        RectF rectSmallLeft = new RectF(itemSpace,itemSpace,2*innerRadius,totalHeight - itemSpace);
        canvas.drawArc(rectSmallLeft, 90, 180, false,itemPaint); // 绘制左边扇形
        RectF rectSmallRight = new RectF(totalWidth - outRadius - innerRadius,itemSpace,totalWidth - itemSpace,totalHeight -itemSpace);
        canvas.drawArc(rectSmallRight,-90,180,false,itemPaint);// 绘制右边扇形
    }

    public void setCurrentNum(int num) {
        this.currentNum = num;
    }

    public void setTotalNum(int num) {
        this.totalNum = num;
    }
}
