package com.example.toniotest.types;

import java.io.Serializable;

/**
 * Created by gnuton on 5/24/13.
 */

public class RSSEntry implements Serializable{
    public final String title;
    public final String link;
    public final String summary;
    //public final Calendar date;
    //public String content;
    //public Boolean isMarkedAsRead;

    public RSSEntry(String title, String summary, String link) {
        this.title = title;
        this.summary = summary;
        this.link = link;
    }
}
