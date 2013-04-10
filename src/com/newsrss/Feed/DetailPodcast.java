package com.newsrss.Feed;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: BruSD
 * Date: 4/10/13
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class DetailPodcast extends Activity {

    Podcast currentPodcast = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.details_podcast);

        TextView testTextView = (TextView)findViewById(R.id.test_text);
        Intent startDetailArticl = getIntent();
        int position = startDetailArticl.getIntExtra("position", -1);
        testTextView.setText("Ищи Подкаст с таким ID:"+ " "+ position);
        currentPodcast = DataStorage.getPodcastList().get(position);

    }
}
