package com.newsrss.Feed;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    private static ArrayList<Article> searchList = new ArrayList<Article>();

    // Articles
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

    public static void addToArticleList(ArrayList<Article> loadingArticles) {
        // TODO: add only new article (unique for guid and newsType)
        articleList.clear();
        articleList.addAll(loadingArticles);

        Collections.sort(articleList);
    }


    // Podcasts
    public static ArrayList<Podcast> getPodcastList() {
        return podcastList;
    }

    public static void addToPodcastList(ArrayList<Podcast> loadingPodcasts) {
        // TODO: add only new podcast (unique for guid)
        podcastList.clear();
        podcastList.addAll(loadingPodcasts);

        Collections.sort(podcastList);
    }

    // Jobs
    public static ArrayList<Job> getJobList() {
        return jobList;
    }

    public static void addToJobList(ArrayList<Job> loadingJobs) {
        // TODO: add only new jobs (unique for guid)
        jobList.clear();
        jobList.addAll(loadingJobs);

        Collections.sort(jobList);
    }

    // Filters
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

    // Images
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

        drawableMap.put(imageURLString, image);

        return image;
    }

    // Search Article
    public static void startSearch(Activity activity, String searchText){
        searchList.clear();
        new SearchParser(activity).execute(searchText);
    }

    public static void updateSearchList(ArrayList<Article> loadingSearchArticles) {
        searchList = loadingSearchArticles;
    }

    public static ArrayList<Article> getSearchList() {
        return searchList;
    }

}
