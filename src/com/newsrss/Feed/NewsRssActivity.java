package com.newsrss.Feed;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.SherlockActivity;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewsRssActivity extends SherlockActivity implements Animation.AnimationListener{
    /**
     * Called when the activity is first created.
     */
    boolean cellStatusPosition = false;

    int animationID;
    View slideLayar1;
    MyCAdapter adapter;
    View menu;
    View app;
    boolean menuOut = false;
    AnimParams animParams = new AnimParams();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.main);


        menu = findViewById(R.id.menu);
        app = findViewById(R.id.app);

        ListView listView = (ListView) app.findViewById(R.id.rssListView);
        app.findViewById(R.id.BtnSlide).setOnClickListener(new ClickListener());


        menu.findViewById(R.id.show_podcast).setOnClickListener(new ShowPodcastOnclickListener());

        ListView rssListView = (ListView) findViewById(R.id.rssListView);
        ArrayList<NewsItem> list = new ArrayList<NewsItem>();

        list.add(new NewsItem("@drawable/ic_launcher", " Этот день is only good news!Today is only good news!Today is only good news!Today is only good news!", " day!"));
        list.add(new NewsItem("@drawable/ic_launcher", "Today is only good news!Today is only good news!Today is only good news!Today is only good news!", "Today!"));
        list.add(new NewsItem("@drawable/ic_launcher", "Today is only  news!Today is only good news!Today is only good news!Today is only good news!", "Today is good day!"));
        list.add(new NewsItem("@drawable/ic_launcher", "Today is only good news!Today is only good news!Today is only good news!Today is only good news!", "Today is good day!"));
        list.add(new NewsItem("@drawable/ic_launcher", "Today is only good news!Today is only good news!Today is only good news!Today is only good news!", "Today is good day!"));
        list.add(new NewsItem("@drawable/ic_launcher", "Today is only good news!Today is only good news!Today is only good news!Today is only good news!", "Today is good day!"));
        list.add(new NewsItem("@drawable/ic_launcher", "Today is only good news!Today is only good news!Today is only good news!Today is only good news!", "Today is good day!"));
        list.add(new NewsItem("@drawable/ic_launcher", "Today is only good news!Today is only good news!Today is only good news!Today is only good news!", "Today is good day!"));

        adapter = new MyCAdapter(
                this, createArticleList() /*list*/, R.layout.rss_item_layout,
                new String[] { "rssnewstitle", "rssnewsdate"},
                new int [] { R.id.rss_news_title, R.id.rss_news_date});
        rssListView.setAdapter(adapter);
    }


    private List<Map<String, ?>> createArticleList()
    {
        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();

        try
        {
            AsyncTask<XMLNewsType, Void, ArrayList<Article>> parser = new ArticleParser().execute(XMLNewsType.AuditNAccounting,
                    XMLNewsType.Business,
                    XMLNewsType.Governance,
                    XMLNewsType.Insolvency,
                    XMLNewsType.Practice,
                    XMLNewsType.Tax);
            ArrayList<Article> articleList = parser.get();

            for (Article article : articleList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("rssnewstitle", article.getTitle());
                map.put("rssnewsdate", article.getPubDate());
                items.add(map);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return items;
    }
    void layoutApp(boolean menuOut) {
        System.out.println("layout [" + animParams.left + "," + animParams.top + "," + animParams.right + ","
                + animParams.bottom + "]");
        app.layout(animParams.left, animParams.top, animParams.right, animParams.bottom);
        //Now that we've set the app.layout property we can clear the animation, flicker avoided :)
        app.clearAnimation();
    }
    public void onAnimationEnd(Animation arg0) {
        // TODO Auto-generated method stub
        System.out.println("onAnimationEnd");
        switch (animationID){
            case (1):  {
                menuOut = !menuOut;
                if (!menuOut) {
                    menu.setVisibility(View.INVISIBLE);
                }
                layoutApp(menuOut);
            }
            break;

        }


    }
    public void onAnimationRepeat(Animation animation) {

        // TODO Auto-generated method stub

    }

    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub
        System.out.println("onAnimationRepeat");

    }
    static class AnimParams{
        int left, right, top, bottom;
        void init (int left, int top, int right, int bottom){
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;

        }
    }


    public void ShowPodcastList(){
        ListView rssListView = (ListView) findViewById(R.id.rssListView);
        ArrayList<NewsItem> list = new ArrayList<NewsItem>();

        list.add(new NewsItem("@drawable/ic_launcher", " 123", " 1!"));
        list.add(new NewsItem("@drawable/ic_launcher", "123", "1!"));
        list.add(new NewsItem("@drawable/ic_launcher", "Today is only  news!Today is only good news!Today is only good news!Today is only good news!", "Today is good day!"));
        list.add(new NewsItem("@drawable/ic_launcher", "Today is only good news!Today is only good news!Today is only good news!Today is only good news!", "Today is good day!"));
        list.add(new NewsItem("@drawable/ic_launcher", "Today is only good news!Today is only good news!Today is only good news!Today is only good news!", "Today is good day!"));
        list.add(new NewsItem("@drawable/ic_launcher", "Today is only good news!Today is only good news!Today is only good news!Today is only good news!", "Today is good day!"));
        list.add(new NewsItem("@drawable/ic_launcher", "Today is only good news!Today is only good news!Today is only good news!Today is only good news!", "Today is good day!"));
        list.add(new NewsItem("@drawable/ic_launcher", "Today is only good news!Today is only good news!Today is only good news!Today is only good news!", "Today is good day!"));

        SimpleAdapter adapter = new SimpleAdapter(
                this, /*createArticleList()*/list, R.layout.podcast_item_layout,
                new String[] { "rssnewstitle", "rssnewsdate"},
                new int [] { R.id.rss_podcast_title, R.id.rss_podcast_date});
        rssListView.setAdapter(adapter);

    }

    class ShowPodcastOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //To change body of implemented methods use File | Settings | File Templates.

            NewsRssActivity me = NewsRssActivity.this;
            Context context = me;
            Animation anim;

            int w = app.getMeasuredWidth();
            int h = app.getMeasuredHeight();
            int left = (int) (app.getMeasuredWidth() * 0.8);



                // anim = AnimationUtils.loadAnimation(context, R.anim.push_left_in_80);
                anim = new TranslateAnimation(left, 0, 0, 0);
                animParams.init(0, 0, w, h);


            animationID =1;
            anim.setDuration(500);

            anim.setAnimationListener(me);

            //Tell the animation to stay as it ended (we are going to set the app.layout first than remove this property)
            anim.setFillAfter(true);
            ShowPodcastList();

            // Only use fillEnabled and fillAfter if we don't call layout ourselves.
            // We need to do the layout ourselves and not use fillEnabled and fillAfter because when the anim is finished
            // although the View appears to have moved, it is actually just a drawing effect and the View hasn't moved.
            // Therefore clicking on the screen where the button appears does not work, but clicking where the View *was* does
            // work.
            // anim.setFillEnabled(true);
            // anim.setFillAfter(true);

            app.startAnimation(anim);
        }
    }




    /*
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }   */
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            NewsRssActivity me = NewsRssActivity.this;
            Context context = me;
            Animation anim;

            int w = app.getMeasuredWidth();
            int h = app.getMeasuredHeight();
            int left = (int) (app.getMeasuredWidth() * 0.8);

            if (!menuOut) {
                // anim = AnimationUtils.loadAnimation(context, R.anim.push_right_out_80);
                anim = new TranslateAnimation(0, left, 0, 0);
                menu.setVisibility(View.VISIBLE);
                animParams.init(left, 0, left + w, h);
            } else {
                // anim = AnimationUtils.loadAnimation(context, R.anim.push_left_in_80);
                anim = new TranslateAnimation(0, -left, 0, 0);
                animParams.init(0, 0, w, h);
            }
            animationID =1;
            anim.setDuration(500);

            anim.setAnimationListener(me);

            //Tell the animation to stay as it ended (we are going to set the app.layout first than remove this property)
            anim.setFillAfter(true);


            // Only use fillEnabled and fillAfter if we don't call layout ourselves.
            // We need to do the layout ourselves and not use fillEnabled and fillAfter because when the anim is finished
            // although the View appears to have moved, it is actually just a drawing effect and the View hasn't moved.
            // Therefore clicking on the screen where the button appears does not work, but clicking where the View *was* does
            // work.
            // anim.setFillEnabled(true);
            // anim.setFillAfter(true);

            app.startAnimation(anim);

        }
    }

    class MyCAdapter extends SimpleAdapter {
        int resurs;
        Context cxt;
        private Context context;
        List<? extends Map<String, ?>> DataList;
        String[] fromL;
        int[] toL;


        public MyCAdapter(Context context, List<? extends Map<String, ?>> data,
                          int _resource, String[] from, int[] to){
            super(context, data, _resource, from, to);
            this.resurs = _resource;
            this.context = context;
            this.DataList = data;
            this.fromL = from;
            this.toL = to;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            View v = convertView;
            System.out.println("Btn 1");

            if (v == null) {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.rss_item_layout, null);
            }

            final View  slideLayar= v.findViewById(R.id.mini_func_lay);
            v.findViewById(R.id.mini_slide).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    System.out.println("Btn 123"+" "+ v.getId());
                    //TODO Auto-generated method stub
                    NewsRssActivity me1 = NewsRssActivity.this;
                    Context context1 = me1;
                    Animation anim1;
                    if (cellStatusPosition == false){
                        anim1 = new TranslateAnimation(0, 50, 0, 0);
                        cellStatusPosition = true;
                    }else {
                        anim1 = new TranslateAnimation(50, 0, 0, 0);
                        cellStatusPosition = false;
                    }



                    animationID = 2;
                    anim1.setDuration(800);
                    anim1.setAnimationListener(me1);
                    anim1.setFillAfter(true);
                    slideLayar.startAnimation(anim1);



                }
            });




            return v;


        }



}
}
