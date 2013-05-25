package com.example.toniotest.tasks;

import android.os.AsyncTask;
import android.util.Log;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

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
        try {
            return ArticleExtractor.INSTANCE.getText(url);
        } catch (BoilerpipeProcessingException e) {
            e.printStackTrace();
            Log.e(TAG, "Unable to remove a boilerplate");
            return "Error removing boilerplate";
        }

    }

    @Override
    protected void onPostExecute(String s) {
        listener.onBoilerplateRemoved(s);
    }
}
