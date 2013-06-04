package com.gnuton.newshub.tasks;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.util.Xml;
import com.gnuton.newshub.types.RSSEntry;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by gnuton on 5/19/13.
 */
public class RSSParseTask extends AsyncTask<String, Void, List> {
    private static final String xmlNamespace = null; // No namespace
    private static final String TAG = "RSS_PARSE_TASK" ;

    private static OnParsingCompletedListener listener;

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
        if (xml == null) {
            Log.e(TAG, "XML Buffer is empty");
            return entries;
        }

        Log.d(TAG, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" + xml);
        XmlPullParser xpp = Xml.newPullParser();
        xpp.setInput(new StringReader(xml));
        xpp.nextTag();
        xpp.require(XmlPullParser.START_TAG, xmlNamespace, "rss");
        xpp.nextTag();
        String name = xpp.getName();
        Log.d(TAG, "NAME " + name);
        //xpp.nextTag(); // skip START.DOCUMENT
        xpp.require(XmlPullParser.START_TAG, xmlNamespace, "channel"); // or feed
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG)
                continue;

            if (xpp.getName().equals("item")) {
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

    @TargetApi(Build.VERSION_CODES.FROYO)
    private RSSEntry readEntry(XmlPullParser xpp) throws IOException, XmlPullParserException {
        xpp.require(XmlPullParser.START_TAG, xmlNamespace, "item");
        String title = null;
        String summary = null;
        String link = null;
        XMLGregorianCalendar publishedData = null;

        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();
            Log.d(TAG, "NAME " + name);

            if (name.equals("title")) {
                title = readTagText(xpp, "title");
            } else if (name.equals("summary")) {
                summary = readTagText(xpp, "description");
            } else if (name.equals("link")) {
                link = readTagText(xpp, "link");
            } else if (name.equals("pubDate")) {
                String dateString = readTagText(xpp, "pubDate");
                DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
                try {
                    Date date = formatter.parse(dateString);
                    GregorianCalendar c = new GregorianCalendar();
                    c.setTime(date);
                    publishedData = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (DatatypeConfigurationException e) {
                    e.printStackTrace();
                }
            } else {
                skip(xpp);
            }
        }

        return new RSSEntry(0, title, summary, link, publishedData);
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
        public void onParsingCompleted(final List list);
    }
}
