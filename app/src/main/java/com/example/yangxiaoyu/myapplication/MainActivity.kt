package com.example.yangxiaoyu.myapplication

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initStatusBar()
    }

    private fun initStatusBar() {

    }

    private fun initView() {
//        barView.setCurrentNum(2)
//        barView.setTotalNum(6)
//        up_bar_view.setData(6,13)
        barView.setTotalNum(6)
        barView.setCurrentNum(2)
    }

    companion object {
        /**
         * dp值转化为px
         *
         * @param context
         * @param dp
         * @return
         */
        fun dip2px(context: Context?, dp: Float): Float {
            if (context == null) return 0f
            val metrics = getDisplayMetrics(context) ?: return 0f
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics)
        }

        private fun getDisplayMetrics(context: Context): DisplayMetrics? {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            if (wm?.defaultDisplay == null)
                return null
            val display = wm.defaultDisplay
            val metrics = DisplayMetrics()
            display.getMetrics(metrics)
            return metrics
        }
    }


}
