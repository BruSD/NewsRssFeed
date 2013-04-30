package com.newsrss.Feed;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created with IntelliJ IDEA.
 * User: AndrewTivodar
 * Date: 16.04.13
 * Time: 15:40
 * To change this template use File | Settings | File Templates.
 */
public class DetailsSettings extends Activity {

    ImageView legalView,rateView,feedbackView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        legalView = (ImageView)findViewById(R.id.settings_legalBG);
        rateView = (ImageView)findViewById(R.id.settings_rateBG);
        feedbackView = (ImageView)findViewById(R.id.settings_feedbackBG);

        ImageView.OnClickListener ocLegal = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startLegalActivity = new Intent(DetailsSettings.this, Legal.class);
                startActivity(startLegalActivity);
            }
        };

        ImageView.OnClickListener ocRate = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Rate this app click
            }
        };
        ImageView.OnClickListener ocFeedback = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Send us feedback click
            }
        };

        legalView.setOnClickListener(ocLegal);
        rateView.setOnClickListener(ocRate);
        feedbackView.setOnClickListener(ocFeedback);

    }
}