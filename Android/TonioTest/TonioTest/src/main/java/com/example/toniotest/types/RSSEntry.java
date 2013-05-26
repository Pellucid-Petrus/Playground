package com.example.toniotest.types;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by gnuton on 5/24/13.
 */

public class RSSEntry implements Serializable{
    public final long id;
    public final String title;
    public final String link;
    public final String summary;
    public final XMLGregorianCalendar date;
    public String content = "";
    //public Boolean isMarkedAsRead;

    public RSSEntry(long id, String title, String summary, String link, XMLGregorianCalendar date) {
        this.title = title;
        this.summary = summary;
        this.link = link;
        this.id = id;
        this.date = date;
    }
}
