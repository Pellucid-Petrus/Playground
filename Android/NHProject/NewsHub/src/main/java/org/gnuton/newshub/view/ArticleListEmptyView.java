package org.gnuton.newshub.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import org.gnuton.newshub.R;

/**
 * Created by gnuton on 9/19/13.
 */
public class ArticleListEmptyView extends LinearLayout {
    public static final int ID = R.id.ArticleListEmpty;

    private View mView;
    private View mViewToHide ;

    private final String TAG = "ArticleListEmptyView";

    public ArticleListEmptyView(Context context) {
        super(context);
    }

    public ArticleListEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setId(this.ID);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (layoutInflater != null) {
            // Inflate into this mView
            this.mView = layoutInflater.inflate(R.layout.articlelist_empty, this, true);
        }
    }

    /*@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ArticleListEmptyView(Context context, AttributeSet attrs, int theme) {
        super(context, attrs, theme);
    }*/

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (this.mViewToHide == null )
            return;

        if (visibility == View.VISIBLE) {
            this.mViewToHide.setVisibility(View.GONE);
        } else if (visibility == View.GONE) {
            this.mViewToHide.setVisibility(View.VISIBLE);
        }
    }

    /***
     * Article list grows in size. In order to grow the other mView
     * in the viewgroup has to be set to GONE
     *
     * @param v View which will be managed by this class
     */
    public void setViewToHide(View v) {
        this.mViewToHide = v;
    }
}
