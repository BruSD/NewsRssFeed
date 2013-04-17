package com.newsrss.Feed;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created with IntelliJ IDEA.
 * User: MediumMG
 * Date: 10.04.13
 * Time: 11:28
 * To change this template use File | Settings | File Templates.
 */
public class DataStorage {

    private static Map<String, Drawable> drawableMap = new HashMap<String, Drawable>();
    private static HashSet<XMLNewsType> filters = new HashSet<XMLNewsType>(Arrays.asList(XMLNewsType.AuditNAccounting,
                                                                                        XMLNewsType.Business,
                                                                                        XMLNewsType.Governance,
                                                                                        XMLNewsType.Insolvency,
                                                                                        XMLNewsType.Practice,
                                                                                        XMLNewsType.Tax));
    private static ArrayList<Article> articleList = new ArrayList<Article>();
    private static ArrayList<Podcast> podcastList = new ArrayList<Podcast>();
    private static ArrayList<Job> jobList = new ArrayList<Job>();

    public static ArrayList<Article> getArticleList() {
        ArrayList<Article> result = new ArrayList<Article>();
        HashSet<String> guidSet = new HashSet<String>();

        for (Article article: articleList) {
            if (filters.contains(article.getNewsType())) {
                if (!guidSet.contains(article.getGuid())) {
                    result.add(article);
                    guidSet.add(article.getGuid());
                }
            }
        }

        return result;
        //return  articleList;
    }

    public static ArrayList<Podcast> getPodcastList() {
        return podcastList;
    }

    public static ArrayList<Job> getJobList() {
        return jobList;
    }

    public static boolean changeFilterStatus(XMLNewsType newsType) {
        if (filters.contains(newsType)) {
            filters.remove(newsType);
            return false;
        }
        else {
            filters.add(newsType);
            return true;
        }

    }

    public static void updateArticleList () {
        ArrayList<Article> tempList = new ArrayList<Article>();

        try {
            AsyncTask<XMLNewsType, Void, ArrayList<Article>> articleParser = new ArticleParser().execute(XMLNewsType.AuditNAccounting,
                    XMLNewsType.Business,
                    XMLNewsType.Governance,
                    XMLNewsType.Insolvency,
                    XMLNewsType.Practice,
                    XMLNewsType.Tax);
            tempList = articleParser.get();
        }
        catch (ExecutionException e) {}
        catch (InterruptedException e) {}

        // TODO: add only new article (unique for guid and newsType)
        articleList.clear();
        articleList.addAll(tempList);

        Collections.sort(articleList);
    }

    public static void updatePodcastList() {
        ArrayList<Podcast> tempList = new ArrayList<Podcast>();

        try {
            AsyncTask<Void, Void, ArrayList<Podcast>> podcastParser = new PodcastParser().execute();
            tempList = podcastParser.get();
        }
        catch (ExecutionException e) {}
        catch (InterruptedException e) {}

        // TODO: add only new podcast (unique for guid)
        podcastList.clear();
        podcastList.addAll(tempList);

        Collections.sort(podcastList);
    }

    public static void updateJobList() {
        ArrayList<Job> tempList = new ArrayList<Job>();

        try {
            AsyncTask<Void, Void, ArrayList<Job>> jobParser = new JobParser().execute();
            tempList = jobParser.get();
        }
        catch (ExecutionException e) {}
        catch (InterruptedException e) {}

        // TODO: add only new podcast (unique for guid)
        jobList.clear();
       jobList.addAll(tempList);

        Collections.sort(jobList);
    }

    public static Drawable fetchDrawable(String imageURLString) {
        if  (drawableMap.containsKey(imageURLString)) {
            //System.out.println("EXIST : "+imageURLString);
            return  drawableMap.get(imageURLString);
        }

        Drawable image = null;
        try {
            URL imageURL = new URL(imageURLString);
            HttpURLConnection connection = (HttpURLConnection) imageURL.openConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                image = Drawable.createFromStream(inputStream, "src");
            }
            else {
                image = null;
            }
        }
        catch (MalformedURLException e) {
            image = null;
        }
        catch (IOException e) {
            image = null;
        }

        if (image == null) {
            //System.out.println("BAD : "+imageURLString);
            //image = Drawable.createFromPath("res\\drawable-mdpi\\default_news_icon.png");
            // TODO: set default image from resourse
        }
        else {
            //System.out.println("GOOD : "+imageURLString);
        }
        drawableMap.put(imageURLString, image);

        return image;
    }

}
