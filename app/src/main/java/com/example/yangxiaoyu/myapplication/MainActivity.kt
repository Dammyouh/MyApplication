package com.example.yangxiaoyu.myapplication

import android.content.Context
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
    }

    private fun initView() {
        var progressNum = 4
        val totalNum = 4
        val dm = resources.displayMetrics
        val width = dm.widthPixels
        if (progressNum > totalNum) {
            progressNum = totalNum
        }
        barView.setProgressNum(progressNum)
        barView.setItemWidth((width - dip2px(this,4f)* (totalNum - 1) - 2 * dip2px(this,2f)) / totalNum)
        barView.setHasDone(progressNum == totalNum)
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
