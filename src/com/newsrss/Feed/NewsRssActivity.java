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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewsRssActivity extends SherlockActivity implements Animation.AnimationListener{

    boolean cellStatusPosition = false;

    int animationID;
    View slideLayar1;

    View menu;
    View app;
    boolean menuOut = false;
    AnimParams animParams = new AnimParams();
    int idLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.main);


        menu = findViewById(R.id.menu);
        app = findViewById(R.id.app);


        app.findViewById(R.id.BtnSlide).setOnClickListener(new ClickListener());



        ListView rssListView = (ListView) findViewById(R.id.rssListView);

        DataStorage.updateArticleList();
        showAricleList();
        rssListView.setOnItemClickListener(new LaunchDetalActiviti());

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
        //TODO: Утановите вызов Активити для поиска

        Toast toast = Toast.makeText(getApplicationContext(),"Показать все статьи",Toast.LENGTH_SHORT);
        toast.show();

        backAnimationToSideBar();
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
        //ImageView imageView = (ImageView) findViewById(R.id.audit_check_img_view);
        //imageView.setImageDrawable(image);
        //showAricleList();

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
        ImageView imageView = (ImageView) findViewById(R.id.audit_check_img_view);
        //imageView.setImageDrawable(image);
        //showAricleList();

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
        ImageView imageView = (ImageView) findViewById(R.id.audit_check_img_view);
        //imageView.setImageDrawable(image);
        //showAricleList();

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
        ImageView imageView = (ImageView) findViewById(R.id.audit_check_img_view);
        //imageView.setImageDrawable(image);
        //showAricleList();

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
        ImageView imageView = (ImageView) findViewById(R.id.audit_check_img_view);
        //imageView.setImageDrawable(image);
        //showAricleList();

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
        ImageView imageView = (ImageView) findViewById(R.id.audit_check_img_view);
        //imageView.setImageDrawable(image);
        //showAricleList();

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

        backAnimationToSideBar();
        showPodcastList();

    }

    public void showJobsListFromSideBar(final View view){
        //TODO: Утановите вызов Jobs

        Toast toast = Toast.makeText(getApplicationContext(),"Показать все Jobs",Toast.LENGTH_SHORT);
        toast.show();

        backAnimationToSideBar();
        showJobstList();
    }

    public void showContactFromSideBar(final View view){
        //TODO: Утановите вызов Contact

        Toast toast = Toast.makeText(getApplicationContext(),"Показать все Contact",Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showSettingsFromSideBar(final View view){
        //TODO: Утановите вызов SettingsFromSideBar

        Toast toast = Toast.makeText(getApplicationContext(),"Показать все Settings",Toast.LENGTH_SHORT);
        toast.show();
    }

    // Animation block
    public void backAnimationToSideBar(){
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
        anim.setFillAfter(true);
        app.startAnimation(anim);
    }

    void layoutApp(boolean menuOut) {
        System.out.println("layout [" + animParams.left + "," + animParams.top + "," + animParams.right + ","
                + animParams.bottom + "]");
        app.layout(animParams.left, animParams.top, animParams.right, animParams.bottom);
        //Now that we've set the app.layout property we can clear the animation, flicker avoided :)
        app.clearAnimation();
    }

    public void onAnimationEnd(Animation arg0) {

        System.out.println("onAnimationEnd");
        switch (animationID){
            case (1):  {
                menuOut = !menuOut;
                if (!menuOut) {
                    menu.setVisibility(View.INVISIBLE);
                }
                layoutApp(menuOut);
                break;
            }

        }


    }

    public void onAnimationRepeat(Animation animation) {



    }

    public void onAnimationStart(Animation animation) {

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

    // Main animation for SideBar
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
            animationID = 1;
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

    /*
    class MyCAdapter extends SimpleAdapter {

        private Context context;
        private ListView mListView;
        private List<? extends Map<String, ?>>   data;
        private int resurs;
        private  String[] from;
        private int[] to;


        public MyCAdapter(Context context,
                          List<? extends Map<String, ?>> data,
                          int _resource,
                          String[] from,
                          int[] to){
            super(context, data, _resource, from, to);

            MyCAdapter.this.context = context;
            MyCAdapter.this.data = data;
            MyCAdapter.this.resurs = _resource;
            MyCAdapter.this.from = from;
            MyCAdapter.this.to = to;

        }
        protected class RowViewHolder {
            public TextView mTitle;
            public TextView mDate;
            public ImageView mImageView;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            View v = convertView;
            System.out.println("Btn 1");

            if (v == null) {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.rss_item_layout, null);
            }

            final View  slideLayar= v.findViewById(R.id.mini_func_lay);
            RowViewHolder holder  = new RowViewHolder();
            holder.mImageView = (ImageView)v.findViewById(R.id.rss_img_news_pass);
            holder.mTitle = (TextView)v.findViewById(R.id.rss_news_title);
            holder.mDate = (TextView)v.findViewById(R.id.rss_news_date);

            //Взаимодействие с элементами Списка Картинка Татйтл и Дата

            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //To change body of implemented methods use File | Settings | File Templates.
                    System.out.println("Btn на картинку ");
                }
            });
            v.setTag(holder);
            /*
            v.findViewById(R.id.mini_slide).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    System.out.println("Btn 123"+" "+ v.getId());

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



}        */

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
