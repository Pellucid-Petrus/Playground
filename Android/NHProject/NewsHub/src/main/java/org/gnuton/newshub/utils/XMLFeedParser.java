package org.gnuton.newshub.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import org.gnuton.newshub.BuildConfig;
import org.gnuton.newshub.db.RSSEntryDataSource;
import org.gnuton.newshub.types.RSSEntry;
import org.gnuton.newshub.types.RSSFeed;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

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
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

/**
 * Created by gnuton on 6/6/13.
 * Class that parses XML data
 */
public class XMLFeedParser {
    private static final String TAG=XMLFeedParser.class.getName();
    private static final String xmlNamespace = null; // No namespace
    private final RSSEntryDataSource mEds;

    public XMLFeedParser(RSSEntryDataSource eds) {
        mEds = eds;
    }

    public RSSFeed parseXML(final RSSFeed feed){
        if ( BuildConfig.DEBUG ) {
            Log.d(TAG, "PARSING XML at: " + feed.url);
        }

        if (feed != null && feed.xml == null)
            return feed;

        List<RSSEntry> newEntries = null;
        final Calendar latestNewsPubDate = (feed.entries != null && feed.entries.size() > 0) ? ((RSSEntry)feed.entries.get(0)).date : null;

        try {
            try {
                newEntries = parseRSSBuffer(feed.xml, feed.id);

            } catch (XmlPullParserException e) {
                try {
                    newEntries = parseAtomBuffer(feed.xml, feed.id);
                } catch (XmlPullParserException e1) {
                    try {
                        newEntries =  parseRDFBuffer(feed.xml, feed.id);
                    } catch (XmlPullParserException e2) {
                        parseUnknownBuffer(feed);
                        e2.printStackTrace();
                    }

                }
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Merge
        // Merging new with old entries in the adapter
        final List<RSSEntry> _newEntries = newEntries;
        if (MyApp.mMainActivity != null) {
            MyApp.mMainActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    addNewEntries(_newEntries, feed);
                    if (feed.adapter != null)
                        feed.adapter.notifyDataSetChanged();
                }
            });
        } else {
            addNewEntries(newEntries, feed);
        }

        // Clean
        assert feed != null;
        feed.xml= null;

        return feed;
    }

    /**
     * Useful for debuggin'
     */
    private RSSFeed parseUnknownBuffer(RSSFeed feed) throws IOException {
        final String xml = feed.xml;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xml));

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch(eventType){
                    case XmlPullParser.START_DOCUMENT: System.out.println("Start document"); break;
                    case XmlPullParser.START_TAG: System.out.println("Start tag "+xpp.getName()); break;
                    case XmlPullParser.END_TAG: System.out.println("End tag "+xpp.getName()); break;
                    case XmlPullParser.TEXT: System.out.println("Text "+xpp.getText()); break;
                }
                eventType = xpp.next();
            }
            System.out.println("End document");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return feed;
    }

    /**
     * Adds new Entry to entries list
     */
    private void addNewEntries(final List<RSSEntry> newEntries, final RSSFeed feed){
        final List<RSSEntry> entries = feed.entries;
        for (final RSSEntry newEntry : newEntries){
            if (newEntry == null)
                continue;

            // Get right position for item in the list
            int _pos = 0;
            for (RSSEntry entry : entries){
                //Log.d(TAG, "ENTRY TIME: POS:" + String.valueOf(_pos) + " DATE:" + entry.date.getTimeInMillis());
                if (newEntry.date.compareTo(entry.date) > 0)
                    break;
                else
                    _pos = _pos + 1;
            }
            final int pos = _pos;
            //Log.d(TAG, "The new entry will be placed on" + String.valueOf(_pos) + " its time is:" + newEntry.date.getTimeInMillis() );

            entries.add(pos, newEntry);
        }
    }

    @SuppressWarnings("unchecked")
    private List<RSSEntry> parseAtomBuffer(final String xml, final Integer feedID) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xpp = factory.newPullParser();
        final List<RSSEntry> newEntries = new ArrayList<RSSEntry>();

        // Some checking
        if (xml == null) {
            Log.e(TAG, "XML Buffer is empty. Nothing to parse.");
            return null;
        }

        // let's start to parse!
        xpp.setInput(new StringReader(xml));
        xpp.nextTag();
        xpp.require(XmlPullParser.START_TAG, xmlNamespace, "feed");

        //NOTE: That list is sorted by day.
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG)
                continue;

            if (xpp.getName().equals("entry")) {
                final RSSEntry e = parseAtomEntry(xpp, feedID);
                newEntries.add(e);

            } else {
                skip(xpp);
            }
        }
        return newEntries;
    }

    /**
     * Checks if the current feed support ITunes namespace
     */
    boolean isITunesPodcast(XmlPullParser xpp){
        int depth = xpp.getDepth();
        int nsStart;
        int nsEnd;

        try {
            nsStart = xpp.getNamespaceCount(depth-1);
            nsEnd = xpp.getNamespaceCount(depth);
            for (int i = nsStart; i < nsEnd; i++) {
                String prefix = xpp.getNamespacePrefix(i);
                if (prefix.equals("itunes"))
                    return true;
                //String ns = xpp.getNamespaceUri(i);

            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private List<RSSEntry> parseRSSBuffer(final String xml, final Integer feedID) throws XmlPullParserException, IOException {

        if (xml == null) {
            Log.e(TAG, "XML Buffer is empty");
            return null;
        }

        final List<RSSEntry> newEntries = new ArrayList<RSSEntry>();
        boolean itunesPodcast;

        // Start parsing
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(xml));
        xpp.nextTag();
        xpp.require(XmlPullParser.START_TAG, xmlNamespace, "rss");
        itunesPodcast = isITunesPodcast(xpp);
        xpp.nextTag();
        {
            String name = xpp.getName();
            Log.d(TAG, "NAME " + name);
            xpp.require(XmlPullParser.START_TAG, xmlNamespace, "channel");
        }
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG)
                continue;

            if (xpp.getName().toLowerCase().equals("item")) {
                final RSSEntry e = parseRSSEntry(xpp, feedID, itunesPodcast);
                newEntries.add(e);
            } else {
                skip(xpp);
            }
        }
        return newEntries;
        /*
        if ( BuildConfig.DEBUG ) {
            String latestNewsPubDateStr = "None";

            if (latestNewsPubDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, HH:mm");
                latestNewsPubDateStr = sdf.format(latestNewsPubDate.getTime());
            }
            Log.d(TAG, "RSS BUFFER PARSED: NEW ENTRIES NEWER THAN " + latestNewsPubDateStr);
        }
*/
    }

    @SuppressWarnings("unchecked")
    private List<RSSEntry> parseRDFBuffer(final String xml, final Integer feedID) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xpp = factory.newPullParser();
        final List<RSSEntry> newEntries = new ArrayList<RSSEntry>();

        // Some checking
        if (xml == null) {
            Log.e(TAG, "XML Buffer is empty. Nothing to parse.");
            return null;
        }

        xpp.setInput(new StringReader(xml));
        xpp.nextTag();
        xpp.require(XmlPullParser.START_TAG, xmlNamespace, "RDF");

        //NOTE: That list is sorted by day.
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG)
                continue;

            if (xpp.getName().equals("item")) {
                final RSSEntry e = parseRDFEntry(xpp, feedID);
                newEntries.add(e);
            } else {
                skip(xpp);
            }
        }
        return newEntries;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    private RSSEntry parseAtomEntry(XmlPullParser xpp, int feedID) throws IOException, XmlPullParserException {
        xpp.require(XmlPullParser.START_TAG, xmlNamespace, "entry");
        String title = null;
        String description = null;
        String content = null;
        String link = null;
        Calendar publishedData = null;

        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            } else if (xpp.getEventType() != XmlPullParser.END_TAG && xpp.getName().equals("document")) {
                break;
            }

            String name = xpp.getName();
            Log.d(TAG, "ATOM NAME " + name);

            // Skip media:description and media:title, avoid overwriting  description and title text
            String prefix = xpp.getPrefix();
            if (prefix != null){
                skip(xpp);
                continue;
            }


            if (name.equals("title")) {
                title = TextUtils.stripHtml(readTagText(xpp, "title"));
            } else if (name.equals("content")) {
                content = readTagText(xpp, "content");
            } else if (name.equals("summary")) {
                description = readTagText(xpp, "summary");
            } else if (name.equals("link")) {
                String maybeALike = readAtomLink(xpp);
                if (!maybeALike.equals(""))
                    link = maybeALike;
            } else if (name.equals("updated")) {
                String dateString = readTagText(xpp, "updated");
                try {
                    publishedData = DatatypeFactory.newInstance().newXMLGregorianCalendar(dateString).toGregorianCalendar();
                } catch (DatatypeConfigurationException e) {
                    publishedData = new GregorianCalendar();
                }

                if ( BuildConfig.DEBUG ) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, HH:mm");
                    Log.d(TAG, "PUBDATE: " + sdf.format(publishedData.getTime()));
                }

            } else {
                skip(xpp);
            }
        }

        if (description == null && content != null)
            description= "";
        else if (description != null && (content == null || content.isEmpty()))
            content= description;

        return (RSSEntry) mEds.create(
                new String[] {
                        Integer.toString(feedID),
                        title,
                        description,
                        link,
                        content,
                        String.valueOf(publishedData != null ? publishedData.getTimeInMillis() : 0),
                        String.valueOf(0), // Not read
                        ""
                });
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    private RSSEntry parseRSSEntry(XmlPullParser xpp, int feedID, boolean itunesPodcast) throws IOException, XmlPullParserException {
        // Checking
        xpp.require(XmlPullParser.START_TAG, xmlNamespace, "item");

        // Attributes
        String title = null;
        String description = "";
        String link = null;
        Calendar publishedData = GregorianCalendar.getInstance(); // Avoid crashes if data is not parsed correctly

        //iTunes podcast attributes
        String enclosure_url= null;
        String guid = null;

        // Loop XML attributes
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName().toLowerCase();
            Log.d(TAG, "RSS NAME " + name);

            // Skip media:description and media:title, avoid overwriting  description and title text
            String prefix = xpp.getPrefix();
            if (prefix != null && prefix.equals("media")){
                skip(xpp);
                continue;
            }

            // Fill attributes
            if (name.equals("title")) {
                title = TextUtils.stripHtml(readTagText(xpp, "title"));
            } else if (name.equals("description")) {
                description = readTagText(xpp, "description");
            } else if (name.equals("link")) {
                link = readTagText(xpp, "link");
            } else if (name.equals("pubdate")) {
                String dateString;
                try {
                    dateString = readTagText(xpp, "pubDate");
                } catch (XmlPullParserException e) {
                    dateString = readTagText(xpp, "pubdate");
                }
                String[] formatStrings = {
                        "EEE, dd MMM yyyy HH:mm:ss Z",
                        "dd MMM yyyy HH:mm:ss Z",
                        "MM/dd/yy",
                        "MM/dd/yyyy",
                        "MM/dd/yy HH:mm",
                        "MM/dd/yyyy HH:mm",
                        "MM/dd/yy HH:mm:ss",
                        "MM/dd/yyyy HH:mm:ss"
                };

                for (String formatString : formatStrings) {
                    DateFormat formatter= new SimpleDateFormat(formatString, Locale.ENGLISH);
                    try {
                        publishedData = parseRSSDate(dateString, formatter);
                        if ( BuildConfig.DEBUG ) {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, HH:mm");
                            Log.d(TAG, "PUBDATE: " + sdf.format(publishedData.getTime()));
                        }
                        break;
                    } catch (ParseException e) {
                        //e.printStackTrace();
                    } catch (DatatypeConfigurationException e) {
                        e.printStackTrace();
                    }
                }
            } else if (itunesPodcast){
                if (name.equals("enclosure")){
                    String enclosure_mime = xpp.getAttributeValue(null, "type");
                    if (enclosure_mime.contains("audio"))
                        enclosure_url = xpp.getAttributeValue(null, "url");

                    xpp.nextTag();
                } else if (name.equals("guid")){
                    guid = readTagText(xpp, "guid");
                } else {
                    skip(xpp);
                }
            } else {
                skip(xpp);
            }
        }

        // ITunes guid must replace link
        if (guid != null && guid.contains("http"))
            link = guid;

        return (RSSEntry) mEds.create(
                new String[] {
                        Integer.toString(feedID),
                        title,
                        description,
                        link,
                        null,  //content
                        String.valueOf(publishedData.getTimeInMillis()),
                        String.valueOf(0), // Not read
                        enclosure_url
                });
    }

    private RSSEntry parseRDFEntry(XmlPullParser xpp, int feedID) throws IOException, XmlPullParserException {
        return this.parseRSSEntry(xpp, feedID, false); // RDF cannot be itunes podcasts
    }

    private String readAtomLink(XmlPullParser xpp) throws IOException, XmlPullParserException {
        String link = "";
        xpp.require(XmlPullParser.START_TAG, xmlNamespace, "link");

        link = xpp.getAttributeValue(null, "href");
        /*if (link.isEmpty()){
            String relType = xpp.getAttributeValue(null, "rel");
            if (relType.equals("alternate")){
                link = xpp.getAttributeValue(null, "href");
                Log.d(TAG, "Read url:"+ link);
            }
        }*/
        skip(xpp);
        //link can be self-closing - xpp.require(XmlPullParser.END_TAG, xmlNamespace, "link");
        return link;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    private Calendar parseRSSDate(String dateString, DateFormat formatter) throws ParseException, DatatypeConfigurationException {
        Date date = formatter.parse(dateString);
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        return c;
    }

    private String readTagText(XmlPullParser xpp, String tag) throws IOException, XmlPullParserException {
        xpp.require(XmlPullParser.START_TAG, xmlNamespace, tag);
        String text = "";
        if (xpp.next() == XmlPullParser.TEXT) {
            text = xpp.getText();
            xpp.nextTag();
        }
        xpp.require(XmlPullParser.END_TAG, xmlNamespace, tag);
        text = TextUtils.removeNonPrintableChars(text);
        return text;
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
}
