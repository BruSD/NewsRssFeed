package com.newsrss.Feed;

import android.app.Activity;
import android.os.Bundle;
import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.SherlockActivity;

public class NewsRssActivity extends SherlockActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        ActionBarSherlock bar = getSherlock();

    }


}
