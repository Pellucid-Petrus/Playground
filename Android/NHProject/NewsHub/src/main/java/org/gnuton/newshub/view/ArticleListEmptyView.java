package org.gnuton.newshub.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import org.gnuton.newshub.MainActivity;
import org.gnuton.newshub.R;
import org.gnuton.newshub.utils.MyApp;

public class ArticleListEmptyView extends LinearLayout {
    public static final int ID = R.id.ArticleListEmpty;

    private View mViewToHide ;
    LayoutInflater mLayoutInflate;

    private final String TAG = "ArticleListEmptyView";

    public ArticleListEmptyView(Context context) {
        super(context);
        initialize();
    }

    public ArticleListEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize(){
        this.setId(ID);

        Context ctx = getContext();
        assert ctx != null;
        this.mLayoutInflate = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (this.mLayoutInflate != null) {
            // Inflate into this mView
            mLayoutInflate.inflate(R.layout.articlelist_empty, this, true);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            this.setLayoutParams(lp);
        }

        MainActivity mainActivity = MyApp.mMainActivity;
        assert null != mainActivity;
        mViewToHide = mainActivity.findViewById(R.id.articlelist_spacer);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        Log.d(TAG, changedView.toString());

        Log.d(TAG, "VISIBILITY ->" + String.valueOf(visibility));
        super.onVisibilityChanged(changedView, visibility);

        //if (this.mViewToHide == null || changedView != this)
        //    return;
    }

    @Override
    public void setVisibility(int visibility) {
        Log.d(TAG, String.valueOf(getVisibility()) + " ->" + String.valueOf(visibility));
        super.setVisibility(visibility);
        if (this.mViewToHide == null)
            return;

        if (visibility == View.VISIBLE) {
            this.mViewToHide.setVisibility(View.GONE);
        } else {
            this.mViewToHide.setVisibility(View.VISIBLE);
        }
    }
}
