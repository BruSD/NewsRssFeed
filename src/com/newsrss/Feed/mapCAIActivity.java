package com.newsrss.Feed;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockActivity;

/**
 * Created with IntelliJ IDEA.
 * User: BruSD
 * Date: 30.04.13
 * Time: 16:09
 * To change this template use File | Settings | File Templates.
 */
public class mapCAIActivity extends SherlockActivity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.map_cai);


    }
}
