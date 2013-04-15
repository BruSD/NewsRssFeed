package com.newsrss.Feed;

import android.os.AsyncTask;

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

    //private static HashSet<XMLNewsType> filters = new HashSet<XMLNewsType>();
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
}
