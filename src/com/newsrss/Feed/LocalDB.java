package com.newsrss.Feed;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LocalDB {

	private static SQLLiteHelper dbHelper = null;
    private static SQLLiteHelperSearch dbHelperSearches = null;
	private static String[] allColumns = { SQLLiteHelper.COLUMN_guID,
			SQLLiteHelper.COLUMN_Title, SQLLiteHelper.COLUMN_Link,SQLLiteHelper.COLUMN_PubDate,SQLLiteHelper.COLUMN_Description,SQLLiteHelper.COLUMN_NewsType};
    private static String[] allColumnsSearches = { SQLLiteHelperSearch.COLUMN_ID,
            SQLLiteHelperSearch.COLUMN_Search,SQLLiteHelperSearch.COLUMN_SearchDate};
    private static SQLiteDatabase LocalDatabase;
    private static SQLiteDatabase LocalDatabaseSearches;

	
	static public void open() throws SQLException {
        LocalDatabase = dbHelper.getWritableDatabase();
        LocalDatabaseSearches = dbHelperSearches.getWritableDatabase();
	  }

    static public void open(Context context) throws SQLException {
        dbHelper= new SQLLiteHelper(context)    ;
        dbHelperSearches = new SQLLiteHelperSearch(context);
        LocalDatabase = dbHelper.getWritableDatabase();
        LocalDatabaseSearches = dbHelperSearches.getWritableDatabase();
    }

	static public void close() {
		LocalDatabase.close();
        LocalDatabaseSearches.close();
	}
	
	private static Article articleToComment(Cursor cursor) {
	    Article art = null;
	    URL url = null;
		XMLNewsType type = null;
		Date date = null;
			try {
				url = new URL(cursor.getString(2));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			type=XMLNewsType.fromInt(cursor.getInt(5));
			try {
				String str = cursor.getString(3);
				date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(cursor.getString(3));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			art = new Article(cursor.getString(0), cursor.getString(1), url, date, cursor.getString(4), type);
			
			
	    return art;
	  }
	
	static public boolean deleteArticle (int guid)
	{	
	    int deleted = LocalDatabase.delete(SQLLiteHelper.DATABASE_NAME, SQLLiteHelper.COLUMN_guID
	        + " = " + guid, null);
		if (deleted!=0)
		return true;
		else return false;
	}
	
	static public boolean AddArticle (Article art)
	{	
    	 String dateStr = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(art.getPubDate());
		ContentValues values = new ContentValues();
		 int id = Integer.parseInt(art.getGuid());
		values.put(SQLLiteHelper.COLUMN_guID, id);
	    values.put(SQLLiteHelper.COLUMN_Title, art.getTitle());
	    values.put(SQLLiteHelper.COLUMN_Link, art.getLink().toString());
	    values.put(SQLLiteHelper.COLUMN_PubDate, dateStr);
	    values.put(SQLLiteHelper.COLUMN_Description, art.getDescription());
	    values.put(SQLLiteHelper.COLUMN_NewsType, art.getNewsType().getIndex());
	    long added = LocalDatabase.insert(SQLLiteHelper.DATABASE_NAME, null,
	        values);
		if (added==-1)
		return false;
		else return true;
	}

    static public List<Article> GetAllArticles ()
	{	
	    List<Article> AllArticles = new ArrayList<Article>();

	    Cursor cursor = LocalDatabase.query(SQLLiteHelper.DATABASE_NAME,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Article art = articleToComment(cursor);
	      AllArticles.add(art);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return AllArticles;
	}

    static public void AddSearch(String search)
    {
        Date curDate = new Date(System.currentTimeMillis());
        String dateStr = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(curDate);
        ContentValues values = new ContentValues();
        values.put(SQLLiteHelperSearch.COLUMN_Search, search);
        values.put(SQLLiteHelperSearch.COLUMN_SearchDate, dateStr);
        long added = LocalDatabaseSearches.insert(SQLLiteHelperSearch.DATABASE_NAME, null,
                values);
    }

    static public void deleteSearch (int id)
    {
       LocalDatabaseSearches.delete(SQLLiteHelperSearch.DATABASE_NAME, SQLLiteHelperSearch.COLUMN_ID
                + " = " + id, null);
    }

   static public List<Searches> Get10Searches()
    {
        List<Searches> AllSearches = new ArrayList<Searches>();

        int i = 0;
        Cursor cursor = LocalDatabaseSearches.query(SQLLiteHelperSearch.DATABASE_NAME,
                allColumnsSearches, null, null, null, null, null);

        cursor.moveToLast();
        while (!cursor.isBeforeFirst()) {
            Searches search = SearchToComment(cursor);
                AllSearches.add(search);
            cursor.moveToPrevious();
        }
        // Make sure to close the cursor
        cursor.close();
          Collections.sort(AllSearches, sortByDate);
          i=AllSearches.size();
        List<Searches> AllSearchesTemp = new ArrayList<Searches>();
          while ((i>AllSearches.size()-10)&&(i>0))
          {
              AllSearchesTemp.add(AllSearches.get(i-1));
              i--;
          }

            return AllSearchesTemp ;
        }

        static Comparator<Searches> sortByDate = new Comparator<Searches>() {

            public int compare(Searches s1, Searches s2) {
                return s1.GetDate().compareTo(s2.GetDate());
            }
        };

    private static Searches SearchToComment(Cursor cursor) {
        Searches search = null;
        Date date = null;
        try {
            date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(cursor.getString(2));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        search = new Searches(cursor.getInt(0), cursor.getString(1), date);


        return search;
    }

}

