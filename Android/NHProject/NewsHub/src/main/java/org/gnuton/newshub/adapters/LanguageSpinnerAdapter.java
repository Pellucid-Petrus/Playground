package org.gnuton.newshub.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import org.gnuton.newshub.R;
import org.gnuton.newshub.utils.MyApp;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by gnuton on 8/23/13.
 */
public class LanguageSpinnerAdapter extends ArrayAdapter<String> {
    private final String[] langs;
    public static class ViewHolder{
        public ImageView flag;
    }

    public LanguageSpinnerAdapter(Context context, int textViewResourceId, String[] langs) {
        super(context, textViewResourceId, langs);
        this.langs = langs;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;

        // Create delegate (View + view holder) when needed, or get the holder for the current view to convert.
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.language_spinner_item, null);
            holder = new ViewHolder();

            holder.flag = (ImageView) v.findViewById(R.id.flagImageView);
            v.setTag(holder);
        }
        else
            holder=(ViewHolder)v.getTag();

        // Update the delegate setting data stored in the holder
        final String lang = this.langs[position];
        Log.d("TAG", "Loading image:" + lang);
        if (!lang.isEmpty()) {
            // load image
            try {
                InputStream ims = getContext().getAssets().open("flags/" + this.langs[position]);
                Drawable d = Drawable.createFromStream(ims, null);
                holder.flag.setImageDrawable(d);
            }
            catch(IOException ex) {
                return v;
            }
        }

        // returns the updated delegate
        return v;
    }

    // Returns the png flags file names as array of strings
    public static String[] getFlagNamesArray() {
        Resources res = MyApp.getContext().getResources(); //if you are in an activity
        AssetManager am = res.getAssets();
        String fileList[] = new String[0];
        try {
            fileList = am.list("flags");
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* debugging
        if (fileList != null)
        {
            for ( int i = 0;i<fileList.length;i++)
            {
                Log.d("XXX", fileList[i]);
            }
        }*/
        return fileList;
    }
}
