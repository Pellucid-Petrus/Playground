package org.gnuton.droidxperiment.gif;

import java.io.IOException;
import java.io.InputStream;

import org.gnuton.droidxperiment.R;
import org.gnuton.droidxperiment.R.styleable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class GifMovieView extends View {
	Movie mMovie = null;
	long mMoviestart;
	Context mContext = null;
	final String TAG = "GifMovieView";

	public GifMovieView(Context context) {
		super(context);
		mContext = context;
		init(null, 0);
	}

	public GifMovieView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init(attrs, 0);
	}

	public GifMovieView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	public void setGif(String gifFile) {
		Log.d(TAG, "Loading gif: " + gifFile);
		InputStream stream = null;
		try {
			stream = mContext.getAssets().open(gifFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mMovie = Movie.decodeStream(stream);
	}

	@SuppressLint("Recycle")
	private void init(AttributeSet attrs, int defStyle) {
		// Load attributes
		final TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.GifMovieView, defStyle, 0);

		String gifFile = a.getString(R.styleable.GifMovieView_GifFile);
		if (gifFile != null)
			setGif(gifFile);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mMovie == null)
			return;

		canvas.drawColor(Color.GREEN);
		super.onDraw(canvas);

		final long now = SystemClock.uptimeMillis();

		if (mMoviestart == 0)
			mMoviestart = now;

		final int relTime = (int) ((now - mMoviestart) % mMovie.duration());

		// Set frame
		mMovie.setTime(relTime);

		mMovie.draw(canvas, 0, 0);

		// Force view re-draw
		this.invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mMovie != null)
			setMeasuredDimension(mMovie.width(), mMovie.height());
	}
}
