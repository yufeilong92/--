package com.example.administrator.zhbj.Utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * c重写方法，
 * Created by Administrator on 2016/6/27.
 * 修改原有的viewpager的滑动效果
 */
public class NoScrollVirwPager extends ViewPager {

    /**
     * 构造方法
     * @param context
     * @param attrs
     */

    public NoScrollVirwPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**方法
     *
     * @param ev
     * @return 作为dispathonTouchEvent()的条件
     * true 拦截掉事件 不下发子控件就能不能调用onTouchEvent
     * false保持下发
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    /**方法
     ** 设置后不能滑动
     * @param ev
     * @return
     */


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;//把事件不传给 onTouchEvent
    }



}
