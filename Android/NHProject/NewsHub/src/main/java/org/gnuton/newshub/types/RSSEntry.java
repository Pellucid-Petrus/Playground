package org.gnuton.newshub.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by gnuton on 5/24/13.
 */

public class RSSEntry implements Serializable{
    public final int id;
    public final int feedId;
    public final String title;
    public final String link;
    public final String summary;
    public final Calendar date;

    public String content = null;
    public Boolean isRead = false;

    public List<String> columnsToUpdate = new ArrayList<String>(); //Columns which to save into the DB

    //public Boolean isMarkedAsRead;

    public RSSEntry(int id, int feedId, String title, String summary, String link, Calendar date) {
        this.title = title;
        this.feedId = feedId;
        this.summary = summary;
        this.link = link;
        this.id = id;
        this.date = date;
    }
}
