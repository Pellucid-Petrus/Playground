package com.example.toniotest.utils;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gnuton on 5/19/13.
 */
public class RSSParseTask extends AsyncTask<String, Void, List> {
    private static final String xmlNamespace = null; // No namespace
    private static final String TAG = "RSS_PARSE_TASK" ;

    private static OnParsingCompletedListener listener;

    public static class Entry {
        public final String title;
        public final String link;
        public final String summary;

        private Entry(String title, String summary, String link) {
            this.title = title;
            this.summary = summary;
            this.link = link;
        }
    }


    public RSSParseTask(Object o) {
        if (o instanceof OnParsingCompletedListener) {
            this.listener = (OnParsingCompletedListener) o;
        } else {
            throw new ClassCastException(o.toString() + " must implement RSSParseTask.OnParsingCompletedListener");
        }
    }

    @Override
    protected List doInBackground(String... strings) {
        try {
            try {
                return parseRSSString(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List list) {
        listener.onParsingCompleted(list);
    }

    private List parseRSSString(String xml) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        XmlPullParser xpp = Xml.newPullParser();
        xpp.setInput(new StringReader(xml));

        xpp.nextTag(); // skip START.DOCUMENT
        xpp.require(XmlPullParser.START_TAG, xmlNamespace, "feed");
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG)
                continue;

            if (xpp.getName().equals("entry")) {
                entries.add(readEntry(xpp));
            } else {
                skip(xpp);
            }
        }

        /*
        XmlPullParser xpp = Xml.newPullParser();
        xpp.setInput(new StringReader(xml));

        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_DOCUMENT) {
                System.out.println("Start document");
            } else if(eventType == XmlPullParser.START_TAG) {
                System.out.println("Start tag "+xpp.getName());
            } else if(eventType == XmlPullParser.END_TAG) {
                System.out.println("End tag "+xpp.getName());
            } else if(eventType == XmlPullParser.TEXT) {
                //System.out.println("Text "+xpp.getText());
            }
            eventType = xpp.next();
        }
        System.out.println("End document");*/
        Log.d(TAG, "PARSED " + entries.size() + " ENTRIES");

        return entries;
    }

    private Entry readEntry(XmlPullParser xpp) throws IOException, XmlPullParserException {
        xpp.require(XmlPullParser.START_TAG, xmlNamespace, "entry");
        String title = null;
        String summary = null;
        String link = null;
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();
            if (name.equals("title")) {
                title = readTagText(xpp, "title");
            } else if (name.equals("summary")) {
                summary = readTagText(xpp, "summary");
            } else if (name.equals("link")) {
                link = readLink(xpp);
            } else {
                skip(xpp);
            }
        }
        return new Entry(title, summary, link);

    }

    private void skip(XmlPullParser xpp) throws XmlPullParserException, IOException {
        if (xpp.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (xpp.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private String readLink(XmlPullParser xpp) throws IOException, XmlPullParserException {
        String link = "";
        xpp.require(XmlPullParser.START_TAG, xmlNamespace, "link");
        String tag = xpp.getName();
        String relType = xpp.getAttributeValue(null, "rel");
        if (tag.equals("link")) {
            if (relType.equals("alternate")){
                link = xpp.getAttributeValue(null, "href");
                xpp.nextTag();
            }
        }
        xpp.require(XmlPullParser.END_TAG, xmlNamespace, "link");
        return link;
    }

    private String readTagText(XmlPullParser xpp, String tag) throws IOException, XmlPullParserException {
        xpp.require(XmlPullParser.START_TAG, xmlNamespace, tag);
        String text = "";
        if (xpp.next() == XmlPullParser.TEXT) {
            text = xpp.getText();
            xpp.nextTag();
        }
        xpp.require(XmlPullParser.END_TAG, xmlNamespace, tag);
        return text;
    }

    public interface OnParsingCompletedListener {
        public void onParsingCompleted(List list);
    }
}
