package com.newsrss.Feed;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
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
import java.text.AttributedCharacterIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: MediumMG
 * Date: 08.04.13
 * Time: 14:25
 * To change this template use File | Settings | File Templates.
 */
public class ArticleParser extends AsyncTask<XMLNewsType, Void, ArrayList<Article>> {

    @Override
    protected ArrayList<Article> doInBackground(XMLNewsType... newsTypes) {
        ArrayList<Article> articleList = new ArrayList<Article>();

        for (int j = 0; j < newsTypes.length; j++) {
            XMLNewsType xmlNewsType = newsTypes[j];
            System.out.println("!!! "+xmlNewsType.toString());

            // Get url of News type
            URL xmlUrl = null;
            try {
                switch(xmlNewsType) {
                    case AuditNAccounting: {
                        xmlUrl = new URL("http://www.charteredaccountants.ie/General/News-and-Events/?output=rss&categories=chartered%20accountants%20ireland,Financial%20Reporting,audit");
                        break;
                    }
                    case Business: {
                        xmlUrl = new URL("http://www.charteredaccountants.ie//General/News-and-Events/?output=rss&categories=Financial%20Reporting,business%20law,corporate%20finance,financial%20services,");
                        break;
                    }
                    case Governance: {
                        xmlUrl = new URL("http://www.charteredaccountants.ie/en/General/News-and-Events/News1/2013/?output=rss&categories=governance,AML%20-NI,AML%20-ROI,Ethics,");
                        break;
                    }
                    case Insolvency: {
                        xmlUrl = new URL("http://www.charteredaccountants.ie/en/General/News-and-Events/News1/2013/?output=rss&categories=insolvency");
                        break;
                    }
                    case Practice: {
                        xmlUrl = new URL("http://www.charteredaccountants.ie/en/General/News-and-Events/News1/2013/?output=rss&categories=licenses,governance,business");
                        break;
                    }
                    case Tax: {
                        xmlUrl = new URL("http://www.charteredaccountants.ie/en/General/News-and-Events/News1/2013/?output=rss&categories=taxation");
                        break;
                    }
                }
            }
            catch (MalformedURLException ex) {
                //  This can't be!!!
                return  articleList;
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
                        return articleList;
                    }

                    //using db (Document Builder) parse xml data and assign it to Element
                    Document document;
                    try {
                    document = db.parse(stream);
                    } catch (SAXException ex) {
                        //  This can't be!!!
                        return  articleList;
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
                            Element _pubDateE = (Element) entry.getElementsByTagName("a10:updated").item(0);
                            Element _imageLinkE = (Element) entry.getElementsByTagName("enclosure").item(0);

                            String _guid = _guidE.getTextContent();//getFirstChild().getNodeValue();
                            String _title = _titleE.getTextContent();//getFirstChild().getNodeValue();
                            URL _link = new URL(_linkE.getTextContent());//getFirstChild().getNodeValue());
                            String _description = _descriptionE.getTextContent();//getFirstChild().getNodeValue();

                            Date _pubDate = null;
                            try
                            {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
                                _pubDate = format.parse(_pubDateE.getTextContent());//getFirstChild().getNodeValue());
                            }
                            catch(ParseException e)
                            { _pubDate = new Date(); }

                            /*
                            Attr attribute = ;
                            InputStream inputStream = (InputStream) new URL(attribute.getValue()).getContent();
                            Drawable image = Drawable.createFromStream(inputStream, "src name");
                             */
                            Drawable image = DataStorage.fetchDrawable(_imageLinkE.getAttributeNode("url").getValue());

                            //create Article and add it to the ArrayList
                            Article article = new Article(_guid, _title, _link, _pubDate, _description, xmlNewsType, image);

                            articleList.add(article);

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

        } // end of xmlNewsType array

        return  articleList;
    }
}
