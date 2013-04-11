package com.newsrss.Feed;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * Created with IntelliJ IDEA.
 * User: BruSD
 * Date: 4/10/13
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class DetailsArticle extends Activity {

    Article currentArticle = null;
    Article nextArticle = null;
    int positionArt;
    TextView titleArticle;
    TextView dateArticle;
    WebView descriptionArticle;
    TextView nextTitle;
    TextView nextDate;
    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    setContentView(R.layout.details_articl);

        titleArticle = (TextView)findViewById(R.id.article_title);
        dateArticle = (TextView)findViewById(R.id.article_date);
        descriptionArticle = (WebView)findViewById(R.id.article_description);

        nextTitle = (TextView)findViewById(R.id.next_a_title);
        nextDate = (TextView)findViewById(R.id.next_a_date);

        Intent startDetailArticle = getIntent();
        positionArt = startDetailArticle.getIntExtra("position", -1);

        currentArticle = DataStorage.getArticleList().get(positionArt);
        //TODO: Задать проверку на последнюю статью!
        if ( positionArt+1 != DataStorage.getArticleList().size()) {
        nextArticle = DataStorage.getArticleList().get(positionArt + 1);
        }
        ShowArticle();
        NextArticle();
    }
    public void ShowArticle(){
        String dateArticleV;
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        dateArticleV = sdf.format(currentArticle.getPubDate());
        titleArticle.setText(currentArticle.getTitle());
        dateArticle.setText(dateArticleV);
        descriptionArticle.loadData("<html><body>" + currentArticle.getDescription() + "</body></html>", "text/html", "UTF-8");
    }
    public void NextArticle(){
         if (nextArticle != null){
             nextTitle.setText(nextArticle.getTitle());
             String dateArticleV;
             SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
             dateArticleV = sdf.format(nextArticle.getPubDate());
             nextDate.setText(dateArticleV);
         }
    }
    public void ShowNextArticle(final View view){
        Intent startDetailArticl = new Intent(DetailsArticle.this, DetailsArticle.class);
        startDetailArticl.putExtra("position", positionArt+1);
        startActivity(startDetailArticl);

    }
}
