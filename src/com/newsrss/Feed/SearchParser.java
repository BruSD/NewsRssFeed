package com.newsrss.Feed;

import android.graphics.drawable.Drawable;
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
 * Date: 19.04.13
 * Time: 15:31
 * To change this template use File | Settings | File Templates.
 */
public class SearchParser extends AsyncTask<String, Void, ArrayList<Article>> {
    @Override
    protected ArrayList<Article> doInBackground(String... searchText) {
        ArrayList<Article> result = new ArrayList<Article>();

        try
        {
            // We use only one (first; with index = 0) parameters, because this method can receive only array of input parameters
            // So...
            URL searchURL = new URL("http://search.charteredaccountants.ie/rss/?q="+searchText[0].replaceAll(" ","+"));
            System.out.println("search : "+searchText.toString());

            HttpURLConnection connection = (HttpURLConnection) searchURL.openConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream stream = connection.getInputStream();

                //DocumentBuilderFactory, DocumentBuilder are used for xml parsing
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

                DocumentBuilder db;
                try { db = dbf.newDocumentBuilder();
                } catch (ParserConfigurationException ex) {
                    //  This can't be!!!
                    return result;
                }

                //using db (Document Builder) parse xml data and assign it to Element
                Document document;
                try {
                    document = db.parse(stream);
                } catch (SAXException ex) {
                    //  This can't be!!!
                    return  result;
                }
                Element element = document.getDocumentElement();

                //take rss nodes to NodeList
                NodeList nodeList = element.getElementsByTagName("entry");
                if (nodeList.getLength() > 0)
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Element entry = (Element) nodeList.item(i);

                        Element _guidE = (Element) entry.getElementsByTagName("id").item(0);
                        Element _titleE = (Element) entry.getElementsByTagName("title").item(0);
                        Element _pubDateE = (Element) entry.getElementsByTagName("updated").item(0);
                        Element _linkE = (Element) entry.getElementsByTagName("link").item(0);
                        Element _descriptionE = (Element) entry.getElementsByTagName("content").item(0);

                        String guid = _guidE.getTextContent();
                        String title = _titleE.getTextContent();
                        String description = _descriptionE.getTextContent();
                        URL link = new URL(_linkE.getAttributeNode("href").getValue());

                        Date pubDate = null;
                        try
                        {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.ENGLISH);
                            pubDate = format.parse(_pubDateE.getTextContent());
                        }
                        catch(ParseException e)
                        { pubDate = new Date(); }

                        Drawable image =  null;

                        //create Article and add it to the ArrayList
                        Article article = new Article(guid, title, link, pubDate, description, XMLNewsType.AuditNAccounting, image);

                        result.add(article);
                    } // end of item array
            }
            else {
                // Bad Connection
                // TODO: ConnectionExcepion?
            }
        }
        catch(MalformedURLException e) {
            //  This can't be!!!
        }
        catch(IOException e){
            // Bad Connection
            // TODO: ConnectionExcepion?
        }

        return result;
    }
}
