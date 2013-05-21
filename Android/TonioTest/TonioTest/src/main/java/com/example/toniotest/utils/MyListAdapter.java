package com.example.toniotest.utils;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.toniotest.R;

import java.util.List;

/**
 * Created by gnuton on 5/21/13.
 */
public class MyListAdapter extends ArrayAdapter<RSSParseTask.Entry> {


    private final List<RSSParseTask.Entry> entries;

    public MyListAdapter(Context context, int textViewResourceId, List<RSSParseTask.Entry> entries) {
        super(context, textViewResourceId, entries);
        this.entries = entries;
        //this.a = context;
    }

    public static class ViewHolder{
        public TextView title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) v.findViewById(R.id.ListItemTitleTextView);

            v.setTag(holder);
        }
        else
            holder=(ViewHolder)v.getTag();

        final RSSParseTask.Entry e = entries.get(position);
        if (e != null) {
            holder.title.setText(e.title);
        }
        return v;
    }
}
