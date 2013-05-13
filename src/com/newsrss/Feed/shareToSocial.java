package com.newsrss.Feed;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.facebook.*;
import com.facebook.widget.WebDialog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;


import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;

import com.google.code.linkedinapi.schema.Person;
import com.newsrss.Feed.TwitterApp.TwDialogListener;
import android.app.AlertDialog;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created with IntelliJ IDEA.
 * User: BruSD
 * Date: 03.05.13
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
public class shareToSocial extends SherlockActivity {


    //LinkedIn
    private LinkedInOAuthService oAuthService;
    private LinkedInApiClientFactory factory;
    private LinkedInRequestToken liToken;
    private LinkedInApiClient client;
    public static final String LINKEDIN_PREF = "GamePrefs";
    Intent i;

    //Twitter Variable
    private TwitterApp mTwitter;
    //private CheckBox mTwitterBtn;

    private static final String twitter_consumer_key = "rLdUmSbLw38ZPujnbGZM8g";
    private static final String twitter_secret_key = "oVLV1wvDmBimSf69iWs9dUtGmPJOY0VNMTSCBRzH3E";




    //FB Variable
    String postName;
    String postURL;
    String postImageURL;
    String postDiscription;

    private Intent intent;

    private  Article currentArticle;
    private  Podcast currentPodcast;
    private Job    currentJob;
    public Session.StatusCallback statusCallback = new SessionStatusCallback();





     public void createServiseToLinkedIn(int listID,  int articleID){
         setDatetoShare(listID, articleID);
         oAuthService = LinkedInOAuthServiceFactory.getInstance().createLinkedInOAuthService(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
        System.out.println("oAuthService : " + oAuthService);

        factory = LinkedInApiClientFactory.newInstance(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);

        liToken = oAuthService.getOAuthRequestToken(Constants.OAUTH_CALLBACK_URL);
        System.out.println("onCreate:linktoURL : " + liToken.getAuthorizationUrl());
        i = new Intent(Intent.ACTION_VIEW, Uri.parse(liToken.getAuthorizationUrl()));
        startActivity(i);


    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        try {
            linkedInImport(intent);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void linkedInImport(Intent intent) {
        String verifier = intent.getData().getQueryParameter("oauth_verifier");
        System.out.println("liToken " + liToken);
        System.out.println("verifier " + verifier);

        LinkedInAccessToken accessToken = oAuthService.getOAuthAccessToken(liToken, verifier);
        SharedPreferences settings = getSharedPreferences(LINKEDIN_PREF, MODE_PRIVATE);
         final SharedPreferences.Editor edit = settings.edit();
         edit.putString(OAuth.OAUTH_TOKEN, accessToken.getToken());
         edit.putString(OAuth.OAUTH_TOKEN_SECRET,
         accessToken.getTokenSecret());
         edit.putString("linkedin_login", "valid");
         edit.commit();

        client = factory.createLinkedInApiClient(accessToken);

        //client.postNetworkUpdate("LinkedIn Android app test");

        Person profile = client.getProfileForCurrentUser(EnumSet.of(ProfileField.ID, ProfileField.FIRST_NAME, ProfileField.LAST_NAME, ProfileField.HEADLINE));

        System.out.println("First Name :: " + profile.getFirstName());
        System.out.println("Last Name :: " + profile.getLastName());
        System.out.println("Head Line :: " + profile.getHeadline());

        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
        consumer.setTokenWithSecret(accessToken.getToken(), accessToken.getTokenSecret());

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost("https://api.linkedin.com/v1/people/~/shares");
        try {
            consumer.sign(post);
            post.setHeader("content-type", "text/XML");
            String myEntity = "<share><comment>"+postName+"</comment>" +
                    "<content>" +
                    "<submitted-url>"+postURL+"</submitted-url>"
                    +"</content>"+
                    "<visibility><code>anyone</code></visibility>" +
                    "</share>";
            post.setEntity(new StringEntity(myEntity));
            org.apache.http.HttpResponse response = httpclient.execute(post);
            // Get the response
            BufferedReader rd = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));
            StringBuffer strBfr = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {

                strBfr.append(line);
            }
            System.out.println("Response is : "+strBfr.toString());
            Toast.makeText(getApplicationContext(), strBfr.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }




    //listIDn = 1 Articl List
    //listIDn = 2 Podcast
    //listIDn = 3 jobs
    public void onClickbtnConnectFB( int listID,  int articleID) {

        onClickLogin();
        setDatetoShare(listID, articleID);
    }
    public void setDatetoShare(int list, int ID){
        switch (list){
            case 1:
                currentArticle = DataStorage.getArticleList().get(ID);

                //SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
                //postName = sdf.format(currentArticle.getPubDate());
                postName = currentArticle.getTitle();
                postURL = currentArticle.getLink().toString();
                postDiscription = currentArticle.getDescription();
                //postImageURL = currentArticle.getNewsImage();
                break;
            case 2:
                currentPodcast = DataStorage.getPodcastList().get(ID);

                //SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
                //postName = sdf.format(currentArticle.getPubDate());
                postName = currentPodcast.getTitle();
                postURL = currentPodcast.getMP3Link().toString();
                postDiscription = currentPodcast.getDescription();
                //postImageURL = currentArticle.getNewsImage();
                break;
            case 3:
                currentJob = DataStorage.getJobList().get(ID);

                //SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
                //postName = sdf.format(currentArticle.getPubDate());
                postName = currentJob.getTitle();
                postURL = currentJob.getLink().toString();
                postDiscription= currentJob.getDescription();
                //postImageURL = currentArticle.getNewsImage();
                break;
        }



    }
    @Override
    public void onStart() {
        super.onStart();
        if(Session.getActiveSession() != null)
            Session.getActiveSession().addCallback(statusCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(Session.getActiveSession() != null)
            Session.getActiveSession().removeCallback(statusCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Session.getActiveSession()!= null)
            Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        if(session != null)
            Session.saveSession(session, outState);
    }

    private void onClickLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            Session.OpenRequest openRequest = new Session.OpenRequest(this).setCallback(statusCallback);

            openRequest.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
            session.openForRead(openRequest);
        } else {
            Session.openActiveSession(this, true, statusCallback);
        }

    }



    public void updateView() {
        Session session = Session.getActiveSession();
        if (session.isOpened()) {

            publishFeedDialog();



        } else {
            onClickLogin();
        }
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            updateView();
        }
    }
    private void publishFeedDialog() {
        Bundle params = new Bundle();
        params.putString("name", postName);
        //params.putString("caption", "Build great social apps and get more installs.");
        //params.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
        params.putString("link", postURL);
        //params.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

        WebDialog feedDialog = (
                new WebDialog.FeedDialogBuilder(this,
                        Session.getActiveSession(),
                        params))
                .setOnCompleteListener(new WebDialog.OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values,
                                           FacebookException error) {
                        if (error == null) {
                            // When the story is posted, echo the success
                            // and the post Id.
                            final String postId = values.getString("post_id");
                            if (postId != null) {
                                   Toast.makeText(getApplicationContext(),
                                    "Posted story: "+postName,
                                   Toast.LENGTH_SHORT).show();
                            } else {
                                // User clicked the Cancel button
                                //Toast.makeText(getApplicationContext().getApplicationContext(),
                                // "Publish cancelled",
                                //   Toast.LENGTH_SHORT).show();
                            }
                        } else if (error instanceof FacebookOperationCanceledException) {
                            // User clicked the "x" button
                            // Toast.makeText(getApplicationContext().getApplicationContext(),
                            //  "Publish cancelled",
                            //     Toast.LENGTH_SHORT).show();
                        } else {
                            // Generic, ex: network error
                            //Toast.makeText(getApplicationContext().getApplicationContext(),
                            //   "Error posting story",
                            //   Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .build();
        feedDialog.show();
    }



    // Twitter Functionality
    public void onTwitterClick(String messageToTwitter) {

        mTwitter 	= new TwitterApp(this, twitter_consumer_key,twitter_secret_key);

        mTwitter.setListener(mTwLoginDialogListener);

        if (mTwitter.hasAccessToken()) {
           // SHARE MSG
           mTwitter.postToTwitter(messageToTwitter);
        } else {


            mTwitter.authorize();
        }
    }

    private final TwDialogListener mTwLoginDialogListener = new TwDialogListener() {
        @Override
        public void onComplete(String value) {
            String username = mTwitter.getUsername();
            username		= (username.equals("")) ? "No Name" : username;



            Toast.makeText(getApplicationContext(), "Connected to Twitter as " + username, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(String value) {


            Toast.makeText(getApplicationContext(), "Twitter connection failed", Toast.LENGTH_LONG).show();
        }
    };




    //LinkedIN

}
