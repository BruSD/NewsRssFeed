package com.newsrss.Feed;

import android.os.AsyncTask;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: MediumMG
 * Date: 09.04.13
 * Time: 23:26
 * To change this template use File | Settings | File Templates.
 */
public class PodcastParser extends AsyncTask<Void, Void, ArrayList<Podcast>> {

    @Override
    public ArrayList<Podcast> doInBackground(Void... params) {
        ArrayList<Podcast> podcastList = new ArrayList<Podcast>();

        // Get url of News type
        URL xmlUrl;
        try {
            xmlUrl = new URL("http://www.accountancyireland.ie/en/RSS-Feeds/Accountancy-Ireland-Podcast/");
        }
        catch (MalformedURLException ex) {
            //  This can't be!!!
            return  podcastList;
        }

        try
        {
            HttpURLConnection conn = (HttpURLConnection) xmlUrl.openConnection();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream stream = conn.getInputStream();

                //DocumentBuilderFactory, DocumentBuilder are used for xml parsing
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

                DocumentBuilder db;
                try { db = dbf.newDocumentBuilder();
                } catch (ParserConfigurationException ex) {
                    //  This can't be!!!
                    return podcastList;
                }

                //using db (Document Builder) parse xml data and assign it to Element
                Document document;
                try {
                    document = db.parse(stream);
                } catch (SAXException ex) {
                    //  This can't be!!!
                    return  podcastList;
                }
                Element element = document.getDocumentElement();

                //take rss nodes to NodeList
                NodeList nodeList = element.getElementsByTagName("item");

                if (nodeList.getLength() > 0)
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Element entry = (Element) nodeList.item(i);

                        Element _guidE = (Element) entry.getElementsByTagName("guid").item(0);
                        Element _titleE = (Element) entry.getElementsByTagName("title").item(0);
                        Element _linkE = (Element) entry.getElementsByTagName("link").item(0);
                        Element _descriptionE = (Element) entry.getElementsByTagName("description").item(0);
                        Element _pubDateE = (Element) entry.getElementsByTagName("pubDate").item(0);

                        String _guid = _guidE.getTextContent();//getFirstChild().getNodeValue();
                        String _title = _titleE.getTextContent();//getFirstChild().getNodeValue();
                        URL _link = new URL(_linkE.getTextContent());//getFirstChild().getNodeValue());
                        String _description = _descriptionE.getTextContent();//getFirstChild().getNodeValue();

                        Date _pubDate;
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'UT'", Locale.ENGLISH);
                            _pubDate = format.parse(_pubDateE.getTextContent());//getFirstChild().getNodeValue());
                        }
                        catch (ParseException e) {
                            _pubDate = null;
                        }

                        //create Podcast and add it to the ArrayList
                        Podcast podcast = new Podcast(_guid, _title, _link, _pubDate, _description);

                        podcastList.add(podcast);
                    } // end of item array
            }
            else {
                // Bad Connection
                // TODO: ConnectionExcepion?
            }
        }
        catch (IOException ex){
            // Bad Connection
            // TODO: ConnectionExcepion?
        }

        return  podcastList;
    }
}
