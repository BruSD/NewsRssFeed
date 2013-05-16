package com.newsrss.Feed;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.*;
import android.webkit.WebView;
import android.widget.*;
import com.facebook.Session;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: BruSD
 * Date: 16.04.13
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
 */
public class DetailsJobs extends shaerToSocial implements GestureDetector.OnGestureListener {
    private GestureDetector gd;

    Job currentArticle = null;

    int positionArt;
    TextView titleArticle;
    TextView dateArticle;
    WebView descriptionArticle;
    LinearLayout layoutToAddSharePanel,mainJobsLayout;
    LinearLayout.LayoutParams lParamsOfSharePanel;
    LayoutInflater inflaterSharePanel;
    int sharePanelHeight;
    AnimatorSet setSharePanelShow, setSharePanelHide;
    boolean sharePanelTopIsShow=false;
    ImageButton shareButtonTest,shareTwitter,shareFacebook,shareLinkedin,shareMail;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.details_jobs);
            getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

        layoutToAddSharePanel = (LinearLayout)findViewById(R.id.jobs_layoutToShare);
        mainJobsLayout = (LinearLayout)findViewById(R.id.jobs_mainLayout);
        lParamsOfSharePanel=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        inflaterSharePanel = getLayoutInflater();
        layoutToAddSharePanel.addView(inflaterSharePanel.inflate(R.layout.share_panel_top, null), lParamsOfSharePanel);
        layoutToAddSharePanel.setVisibility(View.GONE);

        titleArticle = (TextView)findViewById(R.id.jobs_title);
        dateArticle = (TextView)findViewById(R.id.jobs_date);
        descriptionArticle = (WebView)findViewById(R.id.jobs_description);
        descriptionArticle.setBackgroundResource(R.drawable.bg_w);

        descriptionArticle.setBackgroundColor(0);



        Intent startDetailArticle = getIntent();
        positionArt = startDetailArticle.getIntExtra("position", -1);

        currentArticle = DataStorage.getJobList().get(positionArt);
        ShowArticle();

        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To change body of implemented methods use File | Settings | File Templates.
                finish();
            }
        });

        shareButtonTest = (ImageButton)findViewById(R.id.jobs_share);
        shareTwitter = (ImageButton) findViewById(R.id.share_panel_bottom_twitter);
        shareFacebook = (ImageButton) findViewById(R.id.share_panel_bottom_facebook);
        shareLinkedin = (ImageButton) findViewById(R.id.share_panel_bottom_linkedin);
        shareMail = (ImageButton) findViewById(R.id.share_panel_bottom_mail);

        ImageButton.OnClickListener ocTwitter = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTwitterClick(currentArticle.getTitle()+" "+ currentArticle.getLink().toString());
            }
        };

        ImageButton.OnClickListener ocFacebook = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickbtnConnectFB(3, positionArt);
            }
        };

        ImageButton.OnClickListener ocLinkedIn = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Share LinkedIn" ,Toast.LENGTH_SHORT).show();
            }
        };

        ImageButton.OnClickListener ocMail = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MailSender.send(DetailsJobs.this, currentArticle);
            }
        };

        final AnimatorSet.AnimatorListener sharePanelShowListener  = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                for (int i = 0; i < mainJobsLayout.getChildCount(); i++) {
                    View child = mainJobsLayout.getChildAt(i);
                    if ((i!=2)&&(i!=3))
                        ObjectAnimator.ofFloat(child, "alpha", 1, 0.5f).setDuration(0).start();
                }
                ObjectAnimator.ofFloat(dateArticle, "alpha", 1, 0.5f ).setDuration(0).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };


        final AnimatorSet.AnimatorListener sharePanelHideListener  = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                for (int i = 0; i < mainJobsLayout.getChildCount(); i++) {
                    View child = mainJobsLayout.getChildAt(i);
                    ObjectAnimator.ofFloat(child, "alpha", 0.5f, 1f ).setDuration(0).start();
                    child.setEnabled(true);
                }
                ObjectAnimator.ofFloat(dateArticle, "alpha", 0.5f, 1f ).setDuration(0).start();
                layoutToAddSharePanel.setVisibility(View.GONE);
                ObjectAnimator.ofFloat(descriptionArticle, "translationY", 0, 0).setDuration(0).start();

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };

        ImageButton.OnClickListener ocShare = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sharePanelTopIsShow)   {
                    layoutToAddSharePanel.setVisibility(View.VISIBLE);
                    //TODO size of layoutSharePanel
                    sharePanelHeight=layoutToAddSharePanel.getHeight();
                    setSharePanelShow = new AnimatorSet();
                    setSharePanelShow.playTogether(
                            ObjectAnimator.ofFloat(descriptionArticle, "translationY", -105, 0 ),
                            ObjectAnimator.ofFloat(layoutToAddSharePanel, "Y", -105, 0)
                    );
                    setSharePanelShow.addListener(sharePanelShowListener);
                    setSharePanelShow.setDuration(200).start();
                    sharePanelTopIsShow=true;
                }
                else  {
                    setSharePanelHide = new AnimatorSet();
                    setSharePanelHide.playTogether(
                            ObjectAnimator.ofFloat(descriptionArticle, "translationY", 0, -105),
                            ObjectAnimator.ofFloat(layoutToAddSharePanel, "Y", 0, -105)
                    );
                    setSharePanelHide.addListener(sharePanelHideListener);
                    setSharePanelHide.setDuration(200).start();
                    sharePanelTopIsShow=false;
                }

            }
        };

        shareTwitter.setOnClickListener(ocTwitter);
        shareFacebook.setOnClickListener(ocFacebook);
        shareLinkedin.setOnClickListener(ocLinkedIn);
        shareMail.setOnClickListener(ocMail);
        shareButtonTest.setOnClickListener(ocShare);



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
        gd = new GestureDetector(DetailsJobs.this);

        gd.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {

                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Toast toast = Toast.makeText(getApplicationContext(),"Double Tap",Toast.LENGTH_SHORT);
                toast.show();
                final Dialog dialog = new Dialog(DetailsJobs.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.setContentView(R.layout.share_panel_bottom);

                ImageButton facebokShareTo = (ImageButton)dialog.findViewById(R.id.share_panel_bottom_facebook);
                facebokShareTo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickbtnConnectFB(3, positionArt);
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
                        MailSender.send(DetailsJobs.this, currentArticle);
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
                if(sharePanelTopIsShow)
                    return true;
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
        descriptionArticle.loadData("<html><body>" + currentArticle.getDescription() + "</body></html>", "text/html; charset=UTF-8", null);
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

}
