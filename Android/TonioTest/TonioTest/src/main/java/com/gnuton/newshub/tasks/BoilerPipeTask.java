package com.gnuton.newshub.tasks;

import android.os.AsyncTask;
import android.util.Log;

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
public class BoilerPipeTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "BOILER PIPE TASK";

    private static OnBoilerplateRemovedListener listener;

    public interface OnBoilerplateRemovedListener {
        public void onBoilerplateRemoved(final String buffer);
    }

    public BoilerPipeTask(Object o) {
        if (o instanceof OnBoilerplateRemovedListener) {
            this.listener = (OnBoilerplateRemovedListener) o;
        } else {
            throw new ClassCastException(o.toString() + " must implement BoilerPipeTask.OnBoilerplateRemovedListener");
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url = null;
        try {
            url = new URL(strings[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "Bad URL");
            return "Error: Malformed URL";
        }
        Log.d(TAG, "Processing " + url);
        //return ArticleExtractor.INSTANCE.getText(url);
        return extractArticle(url);
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
    protected void onPostExecute(String s) {
        listener.onBoilerplateRemoved(s);
    }
}
