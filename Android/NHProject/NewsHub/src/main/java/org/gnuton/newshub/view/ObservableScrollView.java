package org.gnuton.newshub.view;

/**
 * Created by gnuton on 11/9/13.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView
{
    private static final String TAG = ObservableScrollView.class.getName();

    public ObservableScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        gestureDetector = new GestureDetector( new SwipeDetector() );
    }

    //'''''''''''''' START STUFF NEEDED FOR PARALLAX EFFECT **************************/
    private ScrollCallbacks mCallbacks;

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mCallbacks != null)
        {
            mCallbacks.onScrollChanged(l, t, oldl, oldt);
        }
    }

    @Override
    public int computeVerticalScrollRange()
    {
        return super.computeVerticalScrollRange();
    }

    public void setCallbacks(ScrollCallbacks listener)
    {
        mCallbacks = listener;
    }

    public static interface ScrollCallbacks
    {
        public void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    /**********************************************************************************/
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;

    static boolean scrollingH = false;

    private class SwipeDetector extends GestureDetector.SimpleOnGestureListener {

        public boolean onFling( MotionEvent e1, MotionEvent e2, float velocityX, float velocityY ) {
            Log.d(TAG, "onFling: " + e1.toString()+e2.toString());
            // Check movement along the Y-axis. If it exceeds SWIPE_MAX_OFF_PATH,
            // then dismiss the swipe.
            if( Math.abs( e1.getY() - e2.getY() ) > SWIPE_MAX_OFF_PATH )
                return false;

            // Swipe from right to left.
            // The swipe needs to exceed a certain distance (SWIPE_MIN_DISTANCE)
            // and a certain velocity (SWIPE_THRESHOLD_VELOCITY).
            if( e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs( velocityX ) > SWIPE_THRESHOLD_VELOCITY ) {
                next();
                return true;
            }

            // Swipe from left to right.
            // The swipe needs to exceed a certain distance (SWIPE_MIN_DISTANCE)
            // and a certain velocity (SWIPE_THRESHOLD_VELOCITY).
            if( e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs( velocityX ) > SWIPE_THRESHOLD_VELOCITY ) {
                previous();
                return true;
            }

            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d(TAG, "onScroll: " + String.valueOf(distanceX)+ " " + String.valueOf(distanceY));
            scrollingH = Math.abs(distanceY) > Math.abs(distanceX);
            return scrollingH;
        }

        @Override
        public boolean onDown(MotionEvent event) {
            //Log.d(TAG,"onDown: " + event.toString());
            scrollingH = true;
            return true;
        }
    }
/*
    @Override
    public boolean dispatchTouchEvent( MotionEvent ev ) {
        // TouchEvent dispatcher.
        Log.d(TAG, "Dispatch Touch event" + String.valueOf(scrollingH));

        if (scrollingH)
            return true;
        else
            return super.dispatchTouchEvent( ev );
        if( gestureDetector != null ) {
            if( ! gestureDetector.onTouchEvent( ev ) ){
                // If the gestureDetector handles the event, a swipe has been
                // executed and no more needs to be done.
                Log.d(TAG, "Dispatch Touch event TRUE");
                return true;
            }
        }
        Log.d(TAG, "Dispatch Touch event FALSE");
        //return super.dispatchTouchEvent( ev );
    }
*/
    @Override
    public boolean onTouchEvent( MotionEvent event ) {
        boolean gd = gestureDetector.onTouchEvent( event );

        boolean aa = super.onTouchEvent(event) && scrollingH;
        Log.d(TAG, "XXXX +>" +String.valueOf(scrollingH) + "<>" + String.valueOf(aa));

        return aa;
    }

    protected  void previous() {
        Log.d(TAG, "PREV");
    };

    protected  void next(){
        Log.d(TAG,"NEXT");
    };


}