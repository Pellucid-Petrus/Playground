package com.gnuton.newshub.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by gnuton on 6/18/13.
 */

public class ImageAdapter extends PagerAdapter {
    Context mContext;
    public List<Drawable> mImages = new ArrayList<Drawable>();

    public ImageAdapter(Context context){
        this.mContext=context;
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        /*if (mImages.size() == 0)
            return null;
        position = Math.max(position, mImages.size()-1);
*/
        ImageView imageView = new ImageView(mContext);
        //int padding = mContext.getResources().getDimensionPixelSize(0);
        //imageView.setPadding(padding, padding, padding, padding);
        imageView.setBackgroundColor(Color.parseColor("#000000"));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageDrawable(mImages.get(position));
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
