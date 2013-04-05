package com.newsrss.Feed;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.animation.Animation;
import android.widget.ListView;
import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.SherlockActivity;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;



public class NewsRssActivity extends SherlockActivity implements Animation.AnimationListener{
    /**
     * Called when the activity is first created.
     */
    View menu;
    View app;
    boolean menuOut = false;
    AnimParams animParams = new AnimParams();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.main);


        menu = findViewById(R.id.menu);
        app = findViewById(R.id.app);

        ListView listView = (ListView) app.findViewById(R.id.rssListView);
        app.findViewById(R.id.BtnSlide).setOnClickListener(new ClickListener());

    }
    void layoutApp(boolean menuOut) {
        System.out.println("layout [" + animParams.left + "," + animParams.top + "," + animParams.right + ","
                + animParams.bottom + "]");
        app.layout(animParams.left, animParams.top, animParams.right, animParams.bottom);
        //Now that we've set the app.layout property we can clear the animation, flicker avoided :)
        app.clearAnimation();
    }
    public void onAnimationEnd(Animation arg0) {
        // TODO Auto-generated method stub
        System.out.println("onAnimationEnd");
        menuOut = !menuOut;
        if (!menuOut) {
            menu.setVisibility(View.INVISIBLE);
        }
        layoutApp(menuOut);


    }
    public void onAnimationRepeat(Animation animation) {

        // TODO Auto-generated method stub

    }

    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub
        System.out.println("onAnimationRepeat");

    }
    static class AnimParams{
        int left, right, top, bottom;
        void init (int left, int top, int right, int bottom){
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;

        }
    }
    /*
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }   */
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            NewsRssActivity me = NewsRssActivity.this;
            Context context = me;
            Animation anim;

            int w = app.getMeasuredWidth();
            int h = app.getMeasuredHeight();
            int left = (int) (app.getMeasuredWidth() * 0.8);

            if (!menuOut) {
                // anim = AnimationUtils.loadAnimation(context, R.anim.push_right_out_80);
                anim = new TranslateAnimation(0, left, 0, 0);
                menu.setVisibility(View.VISIBLE);
                animParams.init(left, 0, left + w, h);
            } else {
                // anim = AnimationUtils.loadAnimation(context, R.anim.push_left_in_80);
                anim = new TranslateAnimation(0, -left, 0, 0);
                animParams.init(0, 0, w, h);
            }

            anim.setDuration(500);

            anim.setAnimationListener(me);

            //Tell the animation to stay as it ended (we are going to set the app.layout first than remove this property)
            anim.setFillAfter(true);


            // Only use fillEnabled and fillAfter if we don't call layout ourselves.
            // We need to do the layout ourselves and not use fillEnabled and fillAfter because when the anim is finished
            // although the View appears to have moved, it is actually just a drawing effect and the View hasn't moved.
            // Therefore clicking on the screen where the button appears does not work, but clicking where the View *was* does
            // work.
            // anim.setFillEnabled(true);
            // anim.setFillAfter(true);

            app.startAnimation(anim);
        }
    }



}
