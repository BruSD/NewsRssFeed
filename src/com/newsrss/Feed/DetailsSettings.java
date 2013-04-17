package com.newsrss.Feed;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

/**
 * Created with IntelliJ IDEA.
 * User: AndrewTivodar
 * Date: 16.04.13
 * Time: 15:40
 * To change this template use File | Settings | File Templates.
 */
public class DetailsSettings extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.details_settings);
    }
}