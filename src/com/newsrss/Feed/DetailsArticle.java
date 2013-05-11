package com.newsrss.Feed;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.*;



import java.text.SimpleDateFormat;
import java.util.Date;

import com.facebook.*;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;

/**
 * Created with IntelliJ IDEA.
 * User: BruSD
 * Date: 4/10/13
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class DetailsArticle extends shareToSocial {


    Article currentArticle = null;
    Article nextArticle = null;
    int positionArt;
    TextView titleArticle;
    TextView dateArticle;
    WebView descriptionArticle;
    TextView nextTitle;
    TextView nextDate;


    ImageButton shareButtonTest;

    public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    setContentView(R.layout.details_articl);

        titleArticle = (TextView)findViewById(R.id.article_title);
        dateArticle = (TextView)findViewById(R.id.article_date);
        descriptionArticle = (WebView)findViewById(R.id.article_description);
        descriptionArticle.setBackgroundResource(R.drawable.bg_w);

        descriptionArticle.setBackgroundColor(0);

        nextTitle = (TextView)findViewById(R.id.next_a_title);
        nextDate = (TextView)findViewById(R.id.next_a_date);


        Intent startDetailArticle = getIntent();
        positionArt = startDetailArticle.getIntExtra("position", -1);




        ShowArticle();
        NextArticle();

        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To change body of implemented methods use File | Settings | File Templates.
                finish();
            }
        });

        shareButtonTest = (ImageButton)findViewById(R.id.article_share);
        shareButtonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onClickbtnConnectFB(1, positionArt);
                //onTwitterClick(currentArticle.getTitle()+" "+ currentArticle.getLink().toString());
                createServiseToLinkedIn(1, positionArt);
            }
        });

        //FB
        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);

        }
        ImageButton addToFav = (ImageButton)findViewById(R.id.article_fav);
        addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDB.addArticle(currentArticle);
            }
        });

        // LinkedIn



    }


    public void ShowArticle(){
        currentArticle = DataStorage.getArticleList().get(positionArt);
        String dateArticleV;
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        dateArticleV = sdf.format(currentArticle.getPubDate());
        titleArticle.setText(currentArticle.getTitle());
        dateArticle.setText(dateArticleV);
        descriptionArticle.loadData("<html><body>"+ currentArticle.getDescription()+"</body></html>", "text/html", "UTF-8");
        //
    }
    public void NextArticle(){
        if ( positionArt+1 != DataStorage.getArticleList().size()) {
            nextArticle = DataStorage.getArticleList().get(positionArt + 1);
        }
         if (nextArticle != null){
             nextTitle.setText(nextArticle.getTitle());
             String dateArticleV;
             SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
             dateArticleV = sdf.format(nextArticle.getPubDate());
             nextDate.setText(dateArticleV);
         }
    }
    public void ShowNextArticle(final View view){
        positionArt =positionArt+1;
        ShowArticle();
        NextArticle();
    }

    //Twitter


}
