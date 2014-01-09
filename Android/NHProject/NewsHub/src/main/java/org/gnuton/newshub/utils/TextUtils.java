package org.gnuton.newshub.utils;

import android.os.Build;
import android.text.Html;

import org.jsoup.Jsoup;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class TextUtils {
    public static String removeNonPrintableChars(String s) {
        //s = s.replaceAll("[\\x00-\\x1F]","");
        String rxp = "^\\s*";
        s = s.replaceFirst(rxp,"");
        s = s.replaceAll("[\t\n\r\f]","");
        return s;
    }
    //TODO This class should be removed and part of its code should be in DowlooadTask
    // This tryes to read the XML as UTF and tries to get <?xml ... encoding='UTF-8'>
    public static String getXMLEncoding(byte[] xmlBuffer) {
        String xml;
        InputStream is;
        //final XmlPullParser xpp = Xml.newPullParser();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xml = new String(xmlBuffer, "UTF-8");
            is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            xpp.setInput(is, null);
            while (!"xml".equals(xpp.getName()) && xpp.getEventType() != XmlPullParser.START_TAG) {
                xpp.next();
            }
            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB)
                xpp.next();
            return xpp.getInputEncoding();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //TODO This class should be removed and part of its code should be in DowlooadTask
    public static String getXMLasString(byte[] xmlBuffer, String encoding){
        if (xmlBuffer == null || encoding == null)
            return null;

        try {
            return new String(xmlBuffer, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String htmlToSpannable(String html) {
        return Html.fromHtml(html).toString();
    }

    public static String stripHtml(String html) {
        return Jsoup.parse(html).text();
    }
}
