package org.gnuton.newshub.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import org.gnuton.newshub.utils.MyApp;

/**
 * Created by gnuton on 6/18/13.
 */

public class UninterceptableViewPager extends ViewPager {
    private final CountDownTimer mTimer;

    public UninterceptableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTimer = new CountDownTimer(8000, 8000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if (MyApp.mMainActivity != null) {
                    int next = getCurrentItem()+1;
                    int count = getAdapter().getCount();
                    if ( count <= next)
                        next = 0; // Skip Placeholder
                    setCurrentItem(next, true);
                }
                start();

            }
        }.start();
    }

    /*
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Tell our parent to stop intercepting our events!
        boolean ret = super.onInterceptTouchEvent(ev);
        if (ret) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return ret;
    }*/
}