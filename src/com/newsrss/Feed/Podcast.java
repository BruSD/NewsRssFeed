package com.newsrss.Feed;

import java.net.URL;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: MediumMG
 * Date: 09.04.13
 * Time: 20:53
 * To change this template use File | Settings | File Templates.
 */
public class Podcast implements Comparable<Podcast> {

    private String guid;
    private String title;
    private URL mp3Link;
    private Date pubDate;
    private String desciption;

    public Podcast(String guid, String title, URL mp3Link, Date pubDate, String description) {
        this.guid = guid;
        this.title = title;
        this.mp3Link = mp3Link;
        this.pubDate = pubDate;
        this.desciption = description;
    }

    public String getGuid() {
        return this.guid;
    }

    public String getTitle() {
        return this.title;
    }

    public URL getMP3Link() {
        return this.mp3Link;
    }

    public Date getPubDate() {
        return this.pubDate;
    }

    public String getDescription() {
        return this.desciption;
    }

    @Override
    public String toString() {
        return String.format("% , % ", this.title, this.mp3Link);
    }

    @Override
    public int compareTo(Podcast another) {
        return another.getPubDate().compareTo(this.pubDate);
    }
}
