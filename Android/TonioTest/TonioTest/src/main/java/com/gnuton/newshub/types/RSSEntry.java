package com.gnuton.newshub.types;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;

/**
 * Created by gnuton on 5/24/13.
 */

public class RSSEntry implements Serializable{
    public final int id;
    public final int feedId;
    public final String title;
    public final String link;
    public final String summary;
    public final XMLGregorianCalendar date;

    public String content = "";
    //public Boolean isMarkedAsRead;

    public RSSEntry(int id, int feedId, String title, String summary, String link, XMLGregorianCalendar date) {
        this.title = title;
        this.feedId = feedId;
        this.summary = summary;
        this.link = link;
        this.id = id;
        this.date = date;
    }
}
