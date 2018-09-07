package com.example.yangxiaoyu.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
    }

    public void initStatusBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // 设置根布局的参数,先判断有没有状态栏

            ViewGroup view = findViewById(android.R.id.title);
            if(view == null){
                view = findViewById(android.R.id.content);
            }
            if(view == null){
                return;
            }
            ViewGroup rootView =(ViewGroup) view.getChildAt(0);
            View statusView;
            Drawable rootDrawable = rootView.getBackground();
            if(rootDrawable == null){
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else if(rootDrawable instanceof ColorDrawable){
                if(((ColorDrawable) rootDrawable).getColor() == Color.WHITE || ((ColorDrawable) rootDrawable).getColor() == Color.TRANSPARENT ){
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }
            statusView = createStatusView(this, Color.TRANSPARENT);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            decorView.addView(statusView);
        }
    }

    private static View createStatusView(Activity activity, int color) {
        // 获得状态栏高度
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);

        // 绘制一个和状态栏一样高的矩形
        View statusView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight);
        statusView.setLayoutParams(params);
        statusView.setBackgroundColor(color);
        return statusView;
    }
}
