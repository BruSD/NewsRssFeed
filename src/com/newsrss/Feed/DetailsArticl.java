package com.newsrss.Feed;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;


/**
 * Created with IntelliJ IDEA.
 * User: BruSD
 * Date: 4/10/13
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class DetailsArticl extends Activity {

    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    setContentView(R.layout.details_articl);
    }
}
