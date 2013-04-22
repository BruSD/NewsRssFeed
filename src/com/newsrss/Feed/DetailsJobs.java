package com.newsrss.Feed;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: BruSD
 * Date: 16.04.13
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
 */
public class DetailsJobs extends Activity {
    Job currentArticle = null;

    int positionArt;
    TextView titleArticle;
    TextView dateArticle;
    WebView descriptionArticle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.details_jobs);

        titleArticle = (TextView)findViewById(R.id.jobs_title);
        dateArticle = (TextView)findViewById(R.id.jobs_date);
        descriptionArticle = (WebView)findViewById(R.id.jobs_description);
        descriptionArticle.setBackgroundResource(R.drawable.bg_w);

        descriptionArticle.setBackgroundColor(0);



        Intent startDetailArticle = getIntent();
        positionArt = startDetailArticle.getIntExtra("position", -1);

        currentArticle = DataStorage.getJobList().get(positionArt);
        ShowArticle();

        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To change body of implemented methods use File | Settings | File Templates.
                finish();
            }
        });

    }
    public void ShowArticle(){
        String dateArticleV;
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        dateArticleV = sdf.format(currentArticle.getPubDate());
        titleArticle.setText(currentArticle.getTitle());
        dateArticle.setText(dateArticleV);
        descriptionArticle.loadData("<html><body>" + currentArticle.getDescription() + "</body></html>", "text/html", "UTF-8");
    }

}
