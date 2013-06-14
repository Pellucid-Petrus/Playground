package com.gnuton.newshub;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.gnuton.newshub.types.RSSEntry;
import com.gnuton.newshub.utils.NetworkUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by gnuton on 5/21/13.
 */
public class EntryListAdapter extends ArrayAdapter<RSSEntry> {
    private final List<RSSEntry> entries;

    public EntryListAdapter(Context context, int textViewResourceId, List<RSSEntry> entries) {
        super(context, textViewResourceId, entries);
        this.entries = entries;
    }

    public static class ViewHolder{
        public TextView title;
        public TextView date;
        public TextView url;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;

        // Create delegate (View + view holder) when needed, or get the holder for the current view to convert.
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.entrylist_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) v.findViewById(R.id.ListItemTitleTextView);
            holder.url = (TextView) v.findViewById(R.id.ListItemProviderTextView);
            holder.date = (TextView) v.findViewById(R.id.ListItemDateTextView);

            v.setTag(holder);
        }
        else
            holder=(ViewHolder)v.getTag();

        // Update the delegate setting data stored in the holder
        final RSSEntry e = entries.get(position);
        if (e != null) {
            Spanned titleSpanned = Html.fromHtml(e.title, null, null);
            holder.title.setText(titleSpanned, TextView.BufferType.SPANNABLE);
            holder.title.setTypeface(null, e.isRead ? Typeface.NORMAL : Typeface.BOLD);
            holder.url.setText(NetworkUtils.getDomainName(e.link));
            holder.date.setText(dateToString(e.date));
        }

        // returns the updated delegate
        return v;
    }

    String dateToString(Calendar cal) {
        String strdate = null;

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yy HH:mm");

        if (cal != null) {
            strdate = sdf.format(cal.getTime());
        }
        return strdate;
    }
}
