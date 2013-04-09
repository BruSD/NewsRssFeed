package com.newsrss.Feed;

import java.net.URL;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: MediumMG
 * Date: 08.04.13
 * Time: 14:26
 * To change this template use File | Settings | File Templates.
 */
public class Article {

    private String guid;
    private String title;
    private URL link;
    private Date pubDate;
    private String desciption;
    private XMLNewsType newsType;

    public Article(String guid, String title, URL link, Date pubDate, String description, XMLNewsType newsType) {
        this.guid = guid;
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.desciption = description;
        this.newsType = newsType;
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

    public XMLNewsType getNewsType() {
        return this.newsType;
    }

    @Override
    public String toString() {
        return String.format("% , % ", this.title, this.link);
    }
}
