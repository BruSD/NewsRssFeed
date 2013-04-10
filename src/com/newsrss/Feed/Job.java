package com.newsrss.Feed;

import java.net.URL;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: MediumMG
 * Date: 10.04.13
 * Time: 11:29
 * To change this template use File | Settings | File Templates.
 */
public class Job implements Comparable<Job> {

    private String guid;
    private String title;
    private URL link;
    private Date pubDate;
    private String desciption;

    public Job(String guid, String title, URL link, Date pubDate, String description) {
        this.guid = guid;
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.desciption = description;
    }

    public String getGuid() {
        return this.guid;
    }

    public String getTitle() {
        return this.title;
    }

    public URL getLink() {
        return this.link;
    }

    public Date getPubDate() {
        return this.pubDate;
    }

    public String getDescription() {
        return this.desciption;
    }

    @Override
    public String toString() {
        return String.format("% , % ", this.title, this.link);
    }

    @Override
    public int compareTo(Job another) {
        return another.getPubDate().compareTo(this.pubDate);
    }
}
