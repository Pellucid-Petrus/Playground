package com.gnuton.newshub.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.gnuton.newshub.types.RSSEntry;

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
public class BoilerPipeTask extends AsyncTask<RSSEntry, Void, Void> {
    private static final String TAG = "BOILER PIPE TASK";

    private static OnBoilerplateRemovedListener mListener;

    public interface OnBoilerplateRemovedListener {
        public void onBoilerplateRemoved();
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
    protected Void doInBackground(RSSEntry... entries) {

        for (RSSEntry e : entries) {
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
        }
        return null;
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
    protected void onPostExecute(Void v) {
        Log.d(TAG, "Boilerpipe thread terminated");

        if (mListener != null)
            mListener.onBoilerplateRemoved();
    }
}
