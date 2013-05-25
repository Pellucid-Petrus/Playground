package com.example.toniotest.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by gnuton on 5/19/13.
 * listener must implement OnRequestCompleted.
 */
public class DownloadWebTask extends AsyncTask<String, Void, String>{
    private static final String TAG = "DOWNLOAD_WEB_TASK";

    private static OnRequestCompletedListener listener;
    public DownloadWebTask(Object o) {
        if (o instanceof OnRequestCompletedListener) {
            this.listener = (OnRequestCompletedListener) o;
        } else {
            throw new ClassCastException(o.toString() + " must implement DownloadWebTask.OnRequestCompletedListener");
        }
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        listener.onRequestCompleted(s);
    }

    private String downloadUrl(String url) throws IOException {
        InputStream is = null;
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setReadTimeout(10 * 1000);
            conn.setConnectTimeout(10 * 1000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "Response: " + response);
            is = conn.getInputStream();
            return readText(is);
        } finally {
            if (is != null)
                is.close();
        }
    }

    private String readText(InputStream is) {
        //return IOUtils.toString(is, "UTF-8");
        return new Scanner(is, "UTF-8").useDelimiter("\\A").next();
    }

    public interface OnRequestCompletedListener {
        public void onRequestCompleted(final String buffer);
    }
}
