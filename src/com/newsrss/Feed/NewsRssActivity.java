package com.newsrss.Feed;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.*;
import com.actionbarsherlock.app.SherlockActivity;
import android.view.animation.TranslateAnimation;
import com.slidingmenu.lib.SlidingMenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewsRssActivity extends SherlockActivity {

    boolean cellStatusPosition = false;

    int animationID;


    View menu;
    View app;
    boolean menuOut = false;

    int idLayout;
    SlidingMenu slidingMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.main);



        app = findViewById(R.id.app);


        //app.findViewById(R.id.BtnSlide).setOnClickListener(new ClickListener());



        ListView rssListView = (ListView) findViewById(R.id.rssListView);

        DataStorage.updateArticleList();
        showAricleList();
        rssListView.setOnItemClickListener(new LaunchDetalActiviti());

        slidingMenu = new SlidingMenu(this);
        int w = app.getMeasuredWidth();
        int left = (int) (w*0.2);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setBehindOffset(70);
        slidingMenu.setMenu(R.layout.menu);
    }

    class  LaunchDetalActiviti implements AdapterView.OnItemClickListener{


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //To change body of implemented methods use File | Settings | File Templates.

            System.out.println("item click" + id);

            switch (idLayout)  {
                case 1:

                    Intent startDetailArticl = new Intent(NewsRssActivity.this, DetailsArticle.class);
                    startDetailArticl.putExtra("position", position);
                    startActivity(startDetailArticl);
                    break;
                case 2:
                    Intent startDetailPodcast = new Intent(NewsRssActivity.this,DetailsPodcast.class );
                    startDetailPodcast.putExtra("position", position);
                    startActivity(startDetailPodcast);
                    break;
                case 3:
                    Intent startDetailJobs = new Intent(NewsRssActivity.this, DetailsJobs.class );
                    startDetailJobs.putExtra("position", position);
                    startActivity(startDetailJobs);
                    break;
            }



        }
    }

    // Methods for reload ListView
    public void showAricleList() {
        ListView rssListView = (ListView) findViewById(R.id.rssListView);


        SimpleAdapter adapter = new SimpleAdapter(
                this,
                createArticleList(),
                R.layout.rss_item_layout,
                new String[] { "rssnewstitle", "rssnewsdate", "rssnewsimage"},
                new int [] { R.id.rss_news_title, R.id.rss_news_date, R.id.rss_img_news_pass});
        idLayout = 1;
        adapter.setViewBinder(new CustomViewBinder());
        rssListView.setAdapter(adapter);
    }

    private List<Map<String, ?>> createArticleList() {
        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
            ArrayList<Article> articleList = DataStorage.getArticleList();

            for (Article article : articleList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("rssnewstitle", article.getTitle());
                map.put("rssnewsdate", sdf.format(article.getPubDate()));
                if (article.getNewsImage() == null) {
                    //System.out.println("NULL");
                    map.put("rssnewsimage", getResources().getDrawable(R.drawable.default_news_icon));
                }
                else {
                    //System.out.println("NOT NULL");
                    map.put("rssnewsimage", article.getNewsImage());
                }
                items.add(map);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    public void showPodcastList(){
        ListView rssListView = (ListView) findViewById(R.id.rssListView);

        if(DataStorage.getPodcastList().size() == 0 ){
            DataStorage.updatePodcastList();
        }

        SimpleAdapter adapter = new SimpleAdapter(
                this, createPodcastList(), R.layout.podcast_item_layout,
                new String[] { "rssnewstitle", "rssnewsdate"},
                new int [] { R.id.rss_podcast_title, R.id.rss_podcast_date});
        idLayout = 2;
        rssListView.setAdapter(adapter);

    }

    private List<Map<String, ?>> createPodcastList()   {
        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();
        String dateArticleV;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
            ArrayList<Podcast> podcastList = DataStorage.getPodcastList();

            for(Podcast podcast : podcastList) {
                dateArticleV = sdf.format(podcast.getPubDate());
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("rssnewstitle", podcast.getTitle());
                map.put("rssnewsdate", dateArticleV);
                items.add(map);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return  items;
    }

    public void showJobstList(){
        ListView rssListView = (ListView) findViewById(R.id.rssListView);

        if(DataStorage.getJobList().size() == 0 ){
            DataStorage.updateJobList();
        }

        SimpleAdapter adapter = new SimpleAdapter(
                this, createJobsList()/*list*/, R.layout.podcast_item_layout,
                new String[] { "rssnewstitle", "rssnewsdate"},
                new int [] { R.id.rss_podcast_title, R.id.rss_podcast_date});
        idLayout = 3;
        rssListView.setAdapter(adapter);

    }

    private List<Map<String, ?>> createJobsList()   {
        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();
        String dateArticleV;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
            ArrayList<Job> jobsList = DataStorage.getJobList();

            for(Job job : jobsList) {
                dateArticleV = sdf.format(job.getPubDate());
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("rssnewstitle", job.getTitle());
                map.put("rssnewsdate", dateArticleV);
                items.add(map);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return  items;
    }

    // SideBar Elements Click
    public void startSearchActivityFromSideBar(final View view){
          //TODO: Утановите вызов Активити для поиска

        Toast toast = Toast.makeText(getApplicationContext(),"Запустить Поиск",Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showArticleListFromSideBar(final View view){
        //TODO: Утановите вызов Новости

        Toast toast = Toast.makeText(getApplicationContext(),"Показать все статьи",Toast.LENGTH_SHORT);
        toast.show();


        showAricleList();

    }

    public void filterToAudit(final View view){
        if (idLayout != 1)
            return;

        Drawable image = null;
        if (DataStorage.changeFilterStatus(XMLNewsType.AuditNAccounting)) {
            // category on
            image = getResources().getDrawable(R.drawable.check);
        }
        else {
            //category off
            image = getResources().getDrawable(R.drawable.un_check);
        }
        ImageView imageView = (ImageView) findViewById(R.id.audit_check_img_view);
        imageView.setImageDrawable(image);
        showAricleList();

        Toast toast = Toast.makeText(getApplicationContext(),"Использовать фильтер для Аудита",Toast.LENGTH_SHORT);
        toast.show();

    }

    public void filterToBusiness(final View view){
        if (idLayout != 1)
            return;

        Drawable image = null;
        if (DataStorage.changeFilterStatus(XMLNewsType.Business)) {
            // category on
            image = getResources().getDrawable(R.drawable.check);
        }
        else {
            //category off
            image = getResources().getDrawable(R.drawable.un_check);
        }
        ImageView imageView = (ImageView) findViewById(R.id.business_check_img_view);
        imageView.setImageDrawable(image);
        showAricleList();

        Toast toast = Toast.makeText(getApplicationContext(),"Использовать фильтер для Бизнеса",Toast.LENGTH_SHORT);
        toast.show();
    }

    public void filterToGovernance(final View view){
        if (idLayout != 1)
            return;

        Drawable image = null;
        if (DataStorage.changeFilterStatus(XMLNewsType.Governance)) {
            // category on
            image = getResources().getDrawable(R.drawable.check);
        }
        else {
            //category off
            image = getResources().getDrawable(R.drawable.un_check);
        }
        ImageView imageView = (ImageView) findViewById(R.id.governance_check_img_view);
        imageView.setImageDrawable(image);
        showAricleList();

        Toast toast = Toast.makeText(getApplicationContext(),"Использовать фильтер для Правительства",Toast.LENGTH_SHORT);
        toast.show();
    }

    public void filterToInsolvency(final View view){
        if (idLayout != 1)
            return;

        Drawable image = null;
        if (DataStorage.changeFilterStatus(XMLNewsType.Insolvency)) {
            // category on
            image = getResources().getDrawable(R.drawable.check);
        }
        else {
            //category off
            image = getResources().getDrawable(R.drawable.un_check);
        }
        ImageView imageView = (ImageView) findViewById(R.id.insolvency_check_img_view);
        imageView.setImageDrawable(image);
        showAricleList();

        Toast toast = Toast.makeText(getApplicationContext(),"Использовать фильтер для Insolvency",Toast.LENGTH_SHORT);
        toast.show();
    }

    public void filterToPractice(final View view){
        if (idLayout != 1)
            return;

        Drawable image = null;
        if (DataStorage.changeFilterStatus(XMLNewsType.Practice)) {
            // category on
            image = getResources().getDrawable(R.drawable.check);
        }
        else {
            //category off
            image = getResources().getDrawable(R.drawable.un_check);
        }
        ImageView imageView = (ImageView) findViewById(R.id.practice_check_img_view);
        imageView.setImageDrawable(image);
        showAricleList();

        Toast toast = Toast.makeText(getApplicationContext(),"Использовать фильтер для Practice",Toast.LENGTH_SHORT);
        toast.show();
    }

    public void filterToTax(final View view){
        if (idLayout != 1)
            return;

        Drawable image = null;
        if (DataStorage.changeFilterStatus(XMLNewsType.Tax)) {
            // category on
            image = getResources().getDrawable(R.drawable.check);
        }
        else {
            //category off
            image = getResources().getDrawable(R.drawable.un_check);
        }
        ImageView imageView = (ImageView) findViewById(R.id.tax_check_img_view);
        imageView.setImageDrawable(image);
        showAricleList();

        Toast toast = Toast.makeText(getApplicationContext(),"Использовать фильтер для Tax",Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showToFavoritesFromSideBar(final View view){
        //TODO: Утановите вызов Favorites

        Toast toast = Toast.makeText(getApplicationContext(),"Использовать Favorites",Toast.LENGTH_SHORT);
        toast.show();


    }

    public void showSavedSearchFromSideBar(final View view){
        //TODO: Установите вызов showSavedSearchFromSideBar

        Toast toast = Toast.makeText(getApplicationContext(),"Использовать SavedSearch",Toast.LENGTH_SHORT);
        toast.show();


    }

    public void showPodcastsListFromSideBar(final View view){
        //TODO: Утановите вызов Подкасты

        Toast toast = Toast.makeText(getApplicationContext(),"Показать все Подкасты",Toast.LENGTH_SHORT);
        toast.show();


        showPodcastList();

    }

    public void showJobsListFromSideBar(final View view){
        //TODO: Утановите вызов Jobs

        Toast toast = Toast.makeText(getApplicationContext(),"Показать все Jobs",Toast.LENGTH_SHORT);
        toast.show();


        showJobstList();
    }

    public void showContactFromSideBar(final View view){
        //TODO: Утановите вызов Contact

        slidingMenu.setContent(R.layout.contacts);
        idLayout = 4;
        //Intent startContactsActivity = new Intent(NewsRssActivity.this, Contacts.class);
        //startActivity(startContactsActivity);
    }

    public void showSettingsFromSideBar(final View view){
        //TODO: Утановите вызов SettingsFromSideBar

        slidingMenu.setContent(R.layout.details_settings);
        idLayout = 5;
        //Intent startDetailSetting = new Intent(NewsRssActivity.this, DetailsSettings.class);
        //startActivity(startDetailSetting);
    }

       // Main animation for SideBar
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    class CustomViewBinder implements SimpleAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Object data,String textRepresentation) {
            if((view instanceof ImageView) & (data instanceof Drawable))
            {
                ImageView iv = (ImageView) view;
                Drawable image = (Drawable) data;
                iv.setImageDrawable(image);
                return true;
            }
            return false;
        }

    }

}
