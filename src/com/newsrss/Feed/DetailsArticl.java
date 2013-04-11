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
public class DetailsArticl extends Activity {

    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    setContentView(R.layout.details_articl);

        TextView testTextView = (TextView)findViewById(R.id.test_text);
        Intent startDetailArticl = getIntent();
        long positionArt = startDetailArticl.getLongExtra("position", -1);
        testTextView.setText("Ищи элемент по этому ID:"+ " "+ positionArt);
        int x= 0;

    }
}
