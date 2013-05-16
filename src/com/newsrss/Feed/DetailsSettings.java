package com.newsrss.Feed;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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

        ImageView legalView = (ImageView)findViewById(R.id.settings_legalBG);
        legalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startLegalActivity = new Intent(DetailsSettings.this, Legal.class);
                startActivity(startLegalActivity);
            }
        });

        ImageView feedbackView = (ImageView)findViewById(R.id.settings_feedbackBG);
        feedbackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MailSender.sendFeedback(DetailsSettings.this);
            }
        });

        ImageView rateView = (ImageView)findViewById(R.id.settings_rateBG);
        rateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailsSettings.this, "ocRate", Toast.LENGTH_LONG).show();
            }
        });

    }
}