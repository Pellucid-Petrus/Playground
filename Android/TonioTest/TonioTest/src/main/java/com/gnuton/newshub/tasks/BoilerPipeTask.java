package com.gnuton.newshub.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.gnuton.newshub.db.DbHelper;
import com.gnuton.newshub.db.RSSEntryDataSource;
import com.gnuton.newshub.types.RSSEntry;
import com.gnuton.newshub.utils.MyApp;

import org.xml.sax.SAXException;

import de.l3s.boilerpipe.BoilerpipeExtractor;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.CommonExtractors;
import de.l3s.boilerpipe.sax.HTMLHighlighter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gnuton on 5/22/13.
 */
public class BoilerPipeTask extends AsyncTask<RSSEntry, Void, RSSEntry[]> {
    private static final String TAG = "BOILER PIPE TASK";

    private static OnBoilerplateRemovedListener mListener;
    private static final RSSEntryDataSource eds = new RSSEntryDataSource(MyApp.getContext());

    public interface OnBoilerplateRemovedListener {
        public void onBoilerplateRemoved(RSSEntry[] entries);
    }

    public BoilerPipeTask() {
        mListener = null;
    }

    public BoilerPipeTask(Object o) {
        if (o instanceof OnBoilerplateRemovedListener) {
            this.mListener = (OnBoilerplateRemovedListener) o;
        } else {
            throw new ClassCastException(o.toString() + " must implement BoilerPipeTask.OnBoilerplateRemovedListener");
        }
    }

    @Override
    protected RSSEntry[] doInBackground(RSSEntry... entries) {

        for (RSSEntry e : entries) {
            if (e.content != null || "".equals(e.content)) {
                if (e.content!=null)
                    Log.d(TAG, e.content);
                Log.d(TAG, "Skipping...");
                continue;
            }

            URL url = null;
            try {
                url = new URL(e.link);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
                Log.e(TAG, "Bad URL");
                continue;
            }
            Log.d(TAG, "Processing " + url);
            e.content = extractArticle(url);
            if (e.content != null) {
                e.content = sanitizeArticle(e);
                e.columnsToUpdate.add(DbHelper.ENTRIES_CONTENT);
                eds.update(e);
            }
        }
        return entries;
    }

    private String sanitizeArticle(RSSEntry e) {
        String article = e.content;

        // Sanitize html
        if (article != null) {
            article = article.replaceFirst("<style[^>]*>[^<]*</style>","");
            article = article.replaceFirst(e.title,"");
        }

        return article;
    }

    public String extractArticle(URL url){
        String article= null;
        final BoilerpipeExtractor extractor = CommonExtractors.ARTICLE_EXTRACTOR;
        final HTMLHighlighter hh = HTMLHighlighter.newExtractingInstance();

        try {
            article = hh.process(url, extractor);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BoilerpipeProcessingException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return article;
    }

    @Override
    protected void onPostExecute(RSSEntry[] entries) {
        Log.d(TAG, "Boilerpipe thread terminated");

        if (mListener != null)
            mListener.onBoilerplateRemoved(entries);
    }
}
