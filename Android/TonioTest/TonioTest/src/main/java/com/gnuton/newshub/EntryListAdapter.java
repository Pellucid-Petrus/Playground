package com.gnuton.newshub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.gnuton.newshub.types.RSSEntry;

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

            v.setTag(holder);
        }
        else
            holder=(ViewHolder)v.getTag();

        // Update the delegate setting data stored in the holder
        final RSSEntry e = entries.get(position);
        if (e != null) {
            holder.title.setText(e.title);
        }

        // returns the updated delegate
        return v;
    }

}
