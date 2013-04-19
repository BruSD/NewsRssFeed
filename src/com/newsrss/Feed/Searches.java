package com.newsrss.Feed;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: AndrewTivodar
 * Date: 08.04.13
 * Time: 17:48
 * To change this template use File | Settings | File Templates.
 */
public class Searches implements Comparable<Searches> {

    private int id;
    private String search;
    private Date searchDate;

    public Searches (int _id, String _search, Date _searchDate)
    {
        id=_id;
        search = _search;
        searchDate = _searchDate;
    }

    public int getId ()
    {
        return this.id;
    }

    public String getSearch ()
    {
        return this.search;
    }

    public Date getDate ()
    {
        return this.searchDate;
    }

    @Override
    public int compareTo(Searches anotherSearches) {
       return  anotherSearches.searchDate.compareTo(this.searchDate);
    }
}
