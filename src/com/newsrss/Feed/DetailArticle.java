package com.newsrss.Feed;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;
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
public class DetailArticle extends Activity {

    Article currentArticle = null;

    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    setContentView(R.layout.details_articl);

        TextView articleTitleV = (TextView)findViewById(R.id.article_title);
        TextView articleDateV =(TextView)findViewById(R.id.article_date);
        WebView  articleDiscriptionV = (WebView)findViewById(R.id.article_discription);

        Intent startDetailArticl = getIntent();
        int positionArt = startDetailArticl.getIntExtra("position", -1);
        currentArticle = DataStorage.getArticleList().get(positionArt);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.ENGLISH);
        String datetimeString = sdf.format(currentArticle.getPubDate());

        articleTitleV.setText(currentArticle.getTitle());
        articleDateV.setText(datetimeString);
        articleDiscriptionV.loadData("<html><body>"+currentArticle.getDescription()+"</body></html>", "text/html", "UTF-8");

    }
}
