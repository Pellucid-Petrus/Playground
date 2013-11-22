package org.gnuton.newshub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.gnuton.newshub.R;
import org.gnuton.newshub.types.RSSFeed;
import org.gnuton.newshub.utils.NetworkUtils;
import org.gnuton.newshub.utils.Utils;

import java.util.List;

/**
  * Feed list adapter
  */
public class FeedListAdapter extends ArrayAdapter<RSSFeed> {
    private final List<RSSFeed> feeds;
    final int mStyle;
    final boolean mShortUrl;

    public FeedListAdapter(Context context, int textViewResourceId, List<RSSFeed> feeds, boolean shortUrl, int style) {
        super(context, textViewResourceId, feeds);
        this.feeds = feeds;
        this.mStyle = style;
        this.mShortUrl = shortUrl;
    }

    public FeedListAdapter(Context context, int textViewResourceId, List<RSSFeed> feeds, boolean shortUrl) {
        super(context, textViewResourceId, feeds);
        this.feeds = feeds;
        this.mStyle = -1;
        this.mShortUrl = shortUrl;
    }

    public static class ViewHolder{
        public TextView title;
        public TextView desc;
        public ImageView sidebar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;

        // Create delegate (View + view holder) when needed, or get the holder for the current view to convert.
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.feedlist_item, null);
            holder = new ViewHolder();

            assert v != null;
            holder.title = (TextView) v.findViewById(R.id.FeedListItemTitleTextView);
            if (mStyle != -1 ) {
                Context context = v.getContext();
                if (context != null)
                    holder.title.setTextAppearance(context, mStyle);
            }
            holder.desc = (TextView) v.findViewById(R.id.FeedListItemDescTextView);
            holder.sidebar = (ImageView) v.findViewById(R.id.sidebar);

            v.setTag(holder);
        }
        else
            holder=(ViewHolder)v.getTag();

        // Update the delegate setting data stored in the holder
        final RSSFeed f = feeds.get(position);
        if (f != null) {
            holder.title.setText(f.title);

            String urlDomain;
            if (!mShortUrl)
                urlDomain= NetworkUtils.getMoreDetailedDomainName(f.url);
            else
                urlDomain= NetworkUtils.getDomainName(f.url);


            holder.desc.setText(urlDomain);
            holder.sidebar.setBackgroundColor(Utils.generateColor(f.title));
        }

        // returns the updated delegate
        return v;
    }
}
