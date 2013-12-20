package org.gnuton.newshub.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.gnuton.newshub.R;
import org.gnuton.newshub.utils.MyApp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by gnuton on 6/18/13.
 */

public class ImageAdapter extends PagerAdapter {
    Context mContext;
    private List<Drawable> mImages = new ArrayList<Drawable>();
    private List<ImageView> mImageViews = new ArrayList<ImageView>();

    private static ImageAdapter mInstance;
    private static final String TAG = ImageAdapter.class.getName();

    public static synchronized ImageAdapter getInstance(){
        if (mInstance == null)
            mInstance = new ImageAdapter(MyApp.getContext());

        return mInstance;
    }

    private ImageAdapter(Context context){
        this.mContext=context;

        // Initialize the 4 delegates which will host images
        while (mImageViews.size() < 4) {
            ImageView imageView = new ImageView(mContext);
            imageView.setMinimumHeight(240);
            imageView.setBackgroundColor(Color.parseColor("#000000"));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            this.mImageViews.add(imageView);
        }
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    public void clear(){
        mImages.clear();
        notifyDataSetChanged();

    }
    public void addImage(Drawable image){
        mImages.add(image);
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = mImageViews.get(position % 3);
        imageView.setImageDrawable(mImages.get(position));
        if (imageView.getParent() == null)
            container.addView(imageView, 0);
        //if (position == 0)
        //    scheduleAnimationForView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ImageView imageView = (ImageView) object;
        container.removeView(imageView);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public Drawable getRepresentativeDrawable() {
        int size = 0;

        Drawable rep = null;
        for (Drawable image : mImages){
            int _size = image.getIntrinsicHeight() * image.getIntrinsicWidth();
            Log.d(TAG, "XXX" + String.valueOf(_size));
            if (_size > size){
                rep = image;
                size = _size;
            }
        }

        return rep;
    }

    public Drawable getDefaultDrawable() {
        if (mContext == null)
            return null;

        return mContext.getResources().getDrawable(R.drawable.placeholder);
    }
}
