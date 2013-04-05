package com.newsrss.Feed;

/**
 * Created with IntelliJ IDEA.
 * User: BruSD
 * Date: 4/5/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */
import java.util.HashMap;


public class NewsItem extends HashMap<String, String> {

    private static final long serialVersionUID = 1L;
    public static final String rssImgNewsPass ="rssimgnewspass";
    public static final String rssNewsTitle = "rssnewstitle";
    public static final String rssNewsDate = "rssnewsdate";

    public NewsItem(String rssimgnewspass, String rssnewstitle, String  rssnewsdate){

        super();
        super.put(rssImgNewsPass, rssimgnewspass);
        super.put(rssNewsTitle, rssnewstitle);
        super.put(rssNewsDate, rssnewsdate);
    }



}