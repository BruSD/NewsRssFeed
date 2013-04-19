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
	    Article result_art = null;
	    URL art_url = null;
		XMLNewsType art_type = null;
		Date art_date = null;
			try {
                art_url = new URL(cursor.getString(2));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            art_type=XMLNewsType.fromInt(cursor.getInt(5));
			try {
				String str = cursor.getString(3);
                art_date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(cursor.getString(3));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			result_art = new Article(cursor.getString(0), cursor.getString(1), art_url, art_date, cursor.getString(4), art_type, null);
			
	    return result_art;
	  }
	
	static public void deleteArticle (int guid){
	    LocalDatabase.delete(SQLLiteHelper.DATABASE_NAME, SQLLiteHelper.COLUMN_guID
	        + " = " + guid, null);
	}
	
	static public void addArticle (Article art){
    	String dateStr = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(art.getPubDate());
		ContentValues article_values = new ContentValues();
		int id = Integer.parseInt(art.getGuid());
        article_values.put(SQLLiteHelper.COLUMN_guID, id);
        article_values.put(SQLLiteHelper.COLUMN_Title, art.getTitle());
        article_values.put(SQLLiteHelper.COLUMN_Link, art.getLink().toString());
        article_values.put(SQLLiteHelper.COLUMN_PubDate, dateStr);
        article_values.put(SQLLiteHelper.COLUMN_Description, art.getDescription());
        article_values.put(SQLLiteHelper.COLUMN_NewsType, art.getNewsType().getIndex());
	    long added = LocalDatabase.insert(SQLLiteHelper.DATABASE_NAME, null,
                article_values);
	}

    static public List<Article> getAllArticles () {
	    List<Article> allArticles = new ArrayList<Article>();
	    Cursor cursor = LocalDatabase.query(SQLLiteHelper.DATABASE_NAME,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Article art = articleToComment(cursor);
	      allArticles.add(art);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return allArticles;
	}

    static public void addSearch(String search){
        Date currentDate = new Date(System.currentTimeMillis());
        String dateStr = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(currentDate);
        ContentValues seatch_values = new ContentValues();
        seatch_values.put(SQLLiteHelperSearch.COLUMN_Search, search);
        seatch_values.put(SQLLiteHelperSearch.COLUMN_SearchDate, dateStr);
        LocalDatabaseSearches.insert(SQLLiteHelperSearch.DATABASE_NAME, null,
                seatch_values);
    }

    static public void deleteSearch (int id){
       LocalDatabaseSearches.delete(SQLLiteHelperSearch.DATABASE_NAME, SQLLiteHelperSearch.COLUMN_ID
                + " = " + id, null);
    }

   static public List<Searches> get10Searches() {
        List<Searches> allSearches = new ArrayList<Searches>();
        Cursor cursor = LocalDatabaseSearches.query(SQLLiteHelperSearch.DATABASE_NAME,
                allColumnsSearches, null, null, null, null, null);
        cursor.moveToLast();
        while (!cursor.isBeforeFirst()) {
            Searches search = searchToComment(cursor);
            allSearches.add(search);
            cursor.moveToPrevious();
        }
        // Make sure to close the cursor
        cursor.close();
        int searchCount = 0;
        Collections.sort(allSearches);
        searchCount=allSearches.size();
        List<Searches> last10Searches = new ArrayList<Searches>();
          while ((searchCount>allSearches.size()-10)&&(searchCount>0)) {
              last10Searches.add(allSearches.get(searchCount-1));
              searchCount--;
          }

            return last10Searches ;
        }


    private static Searches searchToComment(Cursor cursor) {
        Searches search = null;
        Date search_date = null;
        try {
            search_date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(cursor.getString(2));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        search = new Searches(cursor.getInt(0), cursor.getString(1), search_date);
        return search;
    }

}

