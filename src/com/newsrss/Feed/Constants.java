package com.newsrss.Feed;

/**
 * Created with IntelliJ IDEA.
 * User: BruSD
 * Date: 08.05.13
 * Time: 11:38
 * To change this template use File | Settings | File Templates.
 */
public class Constants {
    public static final String CONSUMER_KEY = "rLdUmSbLw38ZPujnbGZM8g";
    public static final String CONSUMER_SECRET= "oVLV1wvDmBimSf69iWs9dUtGmPJOY0VNMTSCBRzH3E";

    public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
    public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
    public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";

    public static final String	OAUTH_CALLBACK_SCHEME	= "x-oauthflow-twitter";
    public static final String	OAUTH_CALLBACK_HOST		= "callback";
    public static final String	OAUTH_CALLBACK_URL		= OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;

}
