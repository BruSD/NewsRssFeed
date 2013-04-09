package com.newsrss.Feed;

/**
 * Created with IntelliJ IDEA.
 * User: MediumMG
 * Date: 08.04.13
 * Time: 14:41
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionException extends Exception {

    public ConnectionException() {
        super();
    }

    public ConnectionException(String detailMessage) {
        super(detailMessage);
    }

    public ConnectionException(Throwable throwable) {
        super(throwable);
    }

    public ConnectionException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
