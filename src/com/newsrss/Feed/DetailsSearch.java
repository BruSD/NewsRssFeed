package com.newsrss.Feed;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.*;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.Session;
import com.google.analytics.tracking.android.EasyTracker;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: BruSD
 * Date: 16.04.13
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
 */
public class DetailsSearch extends shaerToSocial implements GestureDetector.OnGestureListener {
    private GestureDetector gd;

    Article currentArticle = null;

    int positionArt;
    TextView titleArticle;
    TextView dateArticle;
    WebView descriptionArticle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.details_search);
        getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

        titleArticle = (TextView)findViewById(R.id.jobs_title);
        dateArticle = (TextView)findViewById(R.id.jobs_date);
        descriptionArticle = (WebView)findViewById(R.id.jobs_description);
        descriptionArticle.setBackgroundResource(R.drawable.bg_w);

        descriptionArticle.setBackgroundColor(0);



        Intent startDetailArticle = getIntent();
        positionArt = startDetailArticle.getIntExtra("position", -1);

        currentArticle = DataStorage.getSearchList().get(positionArt);
        ShowArticle();
        final ImageButton addToFav = (ImageButton)findViewById(R.id.article_fav);

        try {
            LocalDB.open(getApplicationContext());
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (!LocalDB.isArticleFavotites(currentArticle.getGuid())){
            addToFav.setImageDrawable(getResources().getDrawable(R.drawable.articles_star_button));
        }   else {
            addToFav.setImageDrawable(getResources().getDrawable(R.drawable.articles_star_push_button));

        }
        LocalDB.close();

        addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    LocalDB.open(getApplicationContext());
                } catch (SQLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                if (!LocalDB.isArticleFavotites(currentArticle.getGuid())){
                    LocalDB.addArticle(currentArticle);
                    addToFav.setImageDrawable(getResources().getDrawable(R.drawable.articles_star_push_button));
                    Toast toast = Toast.makeText(getApplicationContext(),"Article Added to Favorites " ,Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    LocalDB.deleteArticle(currentArticle.getGuid());
                    addToFav.setImageDrawable(getResources().getDrawable(R.drawable.articles_star_button));
                    Toast toast = Toast.makeText(getApplicationContext(),"Article Remove from Favorites " ,Toast.LENGTH_SHORT);
                    toast.show();
                }
                LocalDB.close();
            }
        });
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To change body of implemented methods use File | Settings | File Templates.
                finish();
            }
        });
        //FB
        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);

        }



        //Double Tap
        gd = new GestureDetector(DetailsSearch.this);

        gd.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {

                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Toast toast = Toast.makeText(getApplicationContext(),"Double Tap",Toast.LENGTH_SHORT);
                toast.show();
                final Dialog dialog = new Dialog(DetailsSearch.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.setContentView(R.layout.share_panel_bottom);

                ImageButton facebokShareTo = (ImageButton)dialog.findViewById(R.id.share_panel_bottom_facebook);
                facebokShareTo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickbtnConnectFB(1, positionArt);
                    }
                });

                ImageButton twitterShareTo = (ImageButton)dialog.findViewById(R.id.share_panel_bottom_twitter);
                twitterShareTo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onTwitterClick(currentArticle.getTitle()+" "+ currentArticle.getLink().toString());
                    }
                });

                ImageButton linkedinShareTo = (ImageButton)dialog.findViewById(R.id.share_panel_bottom_linkedin);
                linkedinShareTo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                ImageButton mailShareTo = (ImageButton)dialog.findViewById(R.id.share_panel_bottom_mail);
                mailShareTo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MailSender.send(DetailsSearch.this, currentArticle);
                    }
                });

                dialog.show();
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {

                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        descriptionArticle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);  //To change body of implemented methods use File | Settings | File Templates.
            }
        });

    }
    public void ShowArticle(){
        String dateArticleV;
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        dateArticleV = sdf.format(currentArticle.getPubDate());
        titleArticle.setText(currentArticle.getTitle());
        dateArticle.setText(dateArticleV);


        String data = "<html><head>" +
                "<style type=\"text/css\">" +
                "body{color:#280016; margin:0 10px; font-family:Helvetica; font-size:15px; line-height:24px; }" +
                //"ul{list-style-type:none; padding-left:1.5em; margin-top:-2.5em;margin-bottom:2em;} " +
                //"ul li{margin-bottom:-2em;text-indent:5px; position:relative;top:0em;margin-top:-.15em;} " +
                //"ul li:before{position:relative; margin-left:-.5em !important; font-size:2em; content:'\\2022'; color:#860945; left:-.15em  !important; top:1.1em;margin-top:2em;} " +
                "img{border:1px ridge #777774;} " +
                "p{margin:10px 0;} " +
                "a{font-weight:bold;text-decoration:none; color:#860945;} " +
                "ol{counter-reset:my-counter;} " +
                "ol li:before{content:counter(my-counter); counter-increment(my-counter); color:#860945;}" +
                "</style>"+
                "</head><body>" + currentArticle.getDescription()+"</body></html>";
        descriptionArticle.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);

        //descriptionArticle.loadData("<html><body>" + currentArticle.getDescription() + "</body></html>", "text/html; charset=UTF-8", null);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return gd.onTouchEvent(event);//return the double tap events
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onShowPress(MotionEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
    @Override
    public void onStart() {
        super.onStart();

        EasyTracker.getInstance().activityStart(this); // Add this method.
    }
    @Override
    public void onStop() {
        super.onStop();

        EasyTracker.getInstance().activityStop(this); // Add this method.
    }

}
