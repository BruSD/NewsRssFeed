package com.newsrss.Feed;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;


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

        TextView testTextView = (TextView)findViewById(R.id.test_text);
        Intent startDetailArticl = getIntent();
        int positionArt = startDetailArticl.getIntExtra("position", -1);

        currentArticle = DataStorage.getArticleList().get(positionArt);
        testTextView.setText("Ищи элемент по этому ID:"+ " "+currentArticle.getTitle());

    }
}
