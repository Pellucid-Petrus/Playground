package com.gnuton.newshub.tasks;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.util.Xml;

import com.gnuton.newshub.db.DbHelper;
import com.gnuton.newshub.db.RSSEntryDataSource;
import com.gnuton.newshub.types.RSSEntry;
import com.gnuton.newshub.types.RSSFeed;
import com.gnuton.newshub.utils.MyApp;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.xml.datatype.DatatypeConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by gnuton on 5/19/13.
 */
public class RSSParseTask extends AsyncTask<RSSFeed, Void, RSSFeed> {
    private static final String xmlNamespace = null; // No namespace
    private static final String TAG = "RSS_PARSE_TASK" ;
    private static final RSSEntryDataSource eds = new RSSEntryDataSource(MyApp.getContext());
    private static final int UPDATE_INTERVAL = 30;
    private static final int MILLISECONDS_IN_A_MINUTE = 60000;

    private static OnParsingCompletedListener listener;

    public RSSParseTask(Object o) {
        if (o instanceof OnParsingCompletedListener) {
            this.listener = (OnParsingCompletedListener) o;
        } else {
            throw new ClassCastException(o.toString() + " must implement RSSParseTask.OnParsingCompletedListener");
        }
    }

    @Override
    protected RSSFeed doInBackground(RSSFeed... feeds) {
        try {
            try {
                RSSFeed feed = feeds[0];
                Calendar rightNow = Calendar.getInstance();
                if (feed.lastUpdate == null){
                    // Fetch data from the internet if this is the first time
                    feed.lastUpdate = rightNow;
                    Log.d(TAG, "RSS has never been updated during the app life span. Downloading...");
                    feed.xml = DownloadWebTask.downloadUrl(feed.url);
                    parseRSSBuffer(feed);
                } else {
                    Calendar offset = Calendar.getInstance();
                    offset.add(Calendar.MINUTE, UPDATE_INTERVAL);
                    if (feed.lastUpdate.compareTo(offset) > UPDATE_INTERVAL * MILLISECONDS_IN_A_MINUTE) {
                        Log.d(TAG, "RSS LOOKS OLD. Downloading...");
                        feed.xml = DownloadWebTask.downloadUrl(feed.url);
                        parseRSSBuffer(feed);
                    } else {
                        Log.d(TAG, "RSS that we have looks to be updated.");
                    }
                }
                String selection = DbHelper.ENTRIES_FEEDID + " = " + String.valueOf(feed.id);
                String orderBy = DbHelper.ENTRIES_PUBLISHEDDATE +" DESC";
                feed.entries = eds.getAll(selection,null, null, null, orderBy);
                return feed;
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
    protected void onPostExecute(RSSFeed feed) {
        listener.onParsingCompleted(feed);
    }

    private RSSFeed parseRSSBuffer(RSSFeed feed) throws XmlPullParserException, IOException {
        String xml = feed.xml;
        List entries = new ArrayList();
        if (xml == null) {
            Log.e(TAG, "XML Buffer is empty");
            return feed;
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
                entries.add(readEntry(xpp, feed.id));
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
        feed.entries = entries;
        return feed;
    }

    /**
     *
     * @param xpp
     * @param feedID
     * @return RSSEntry pointing to the entry into the DB
     * @throws IOException
     * @throws XmlPullParserException
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    private RSSEntry readEntry(XmlPullParser xpp, int feedID) throws IOException, XmlPullParserException {
        xpp.require(XmlPullParser.START_TAG, xmlNamespace, "item");
        String title = null;
        String description = null;
        String content = null;
        String link = null;
        Calendar publishedData = null;

        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();
            Log.d(TAG, "NAME " + name);

            if (name.equals("title")) {
                title = readTagText(xpp, "title");
            } else if (name.equals("description")) {
                description = readTagText(xpp, "description");
            } else if (name.equals("link")) {
                link = readTagText(xpp, "link");
            } else if (name.equals("pubDate")) {
                String dateString = readTagText(xpp, "pubDate");
                DateFormat formatter2 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
                DateFormat formatter1 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z");//<pubDate>05 Jun 2013 06:00:00 +0300  </pubDate>
                try {
                    try {
                        publishedData = parseDate(dateString, formatter1);
                    } catch (ParseException e) {
                        try {
                            publishedData = parseDate(dateString, formatter2);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                } catch (DatatypeConfigurationException e) {
                    e.printStackTrace();
                }
            } else {
                skip(xpp);
            }
        }

        return (RSSEntry) eds.create(
                new String[] {
                        Integer.toString(feedID),
                        title,
                        description,
                        link,
                        content,
                        String.valueOf(publishedData.getTimeInMillis())
                });
    }

    //TODO Getting read of XMLGregorianCalendar
    @TargetApi(Build.VERSION_CODES.FROYO)
    private Calendar parseDate(String dateString, DateFormat formatter) throws ParseException, DatatypeConfigurationException {
        Date date = formatter.parse(dateString);
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        return c;
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
        public void onParsingCompleted(final RSSFeed feed);
    }
}
