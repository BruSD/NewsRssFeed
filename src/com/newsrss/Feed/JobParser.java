package com.newsrss.Feed;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
 * User: misa
 * Date: 10.04.13
 * Time: 12:44
 * To change this template use File | Settings | File Templates.
 */
public class JobParser extends AsyncTask<Void, Void, ArrayList<Job>> {

    private Activity activity;
    private ProgressDialog dialog;

    public JobParser(Activity currentActivity){
        this.activity = currentActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(activity);
        dialog.setMessage("Loading...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<Job> result) {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();

        if (activity instanceof NewsRssActivity) {
            DataStorage.addToJobList(result);
            ((NewsRssActivity) activity).updateListForJobs();
        }
    }

    @Override
    public ArrayList<Job> doInBackground(Void... params) {
        ArrayList<Job> jobList = new ArrayList<Job>();

        // Get url of News type
        URL xmlUrl;
        try {
            xmlUrl = new URL("http://www.charteredaccountants.ie/en/Members/Your-Institute/Careers-and-Recruitment-Service/Vacancy-Listing/?output=rss");
        }
        catch (MalformedURLException ex) {
            //  This can't be!!!
            return  jobList;
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
                    return jobList;
                }

                //using db (Document Builder) parse xml data and assign it to Element
                Document document;
                try {
                    document = db.parse(stream);
                } catch (SAXException ex) {
                    //  This can't be!!!
                    return  jobList;
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

                        String _guid = _guidE.getTextContent();//getFirstChild().getNodeValue();
                        String _title = _titleE.getTextContent();//getFirstChild().getNodeValue();
                        URL _link = new URL(_linkE.getTextContent());//getFirstChild().getNodeValue());
                        String _description = _descriptionE.getTextContent();//getFirstChild().getNodeValue();

                        Date _pubDate = null;
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
                            _pubDate = format.parse(_pubDateE.getTextContent().substring(0,19));
                        }
                        catch(ParseException e) {
                            _pubDate = null;
                        }

                        //create Job and add it to the ArrayList
                        Job job = new Job(_guid, _title, _link, _pubDate, _description);

                        jobList.add(job);
                    } // end of item array
            }
            else {
                // Bad Connection
            }
        }
        catch (IOException ex){
            // Bad Connection
        }

        return  jobList;
    }
}
