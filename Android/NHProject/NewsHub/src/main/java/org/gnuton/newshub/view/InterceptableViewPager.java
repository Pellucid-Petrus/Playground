package org.gnuton.newshub.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by gnuton on 6/18/13.
 */

public class InterceptableViewPager extends ViewPager {
    private static String TAG = InterceptableViewPager.class.getName();
    public boolean stopScrolling;

    public InterceptableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        // Reset stop scrolling when gesture start.
        if(ev.getAction()==MotionEvent.ACTION_DOWN)
            stopScrolling= false;

        // stop to scroll the viewpager when stopscrolling is true
        else if(ev.getAction()==MotionEvent.ACTION_MOVE && stopScrolling)
            return true;

        return super.dispatchTouchEvent(ev);
    }
}