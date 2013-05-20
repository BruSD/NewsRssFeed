package com.newsrss.Feed;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLLiteHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME  = "ArtLocalDB";
	private static final int DATABASE_VERSION = 3;
	public static final String COLUMN_guID = "guid";
	public static final String COLUMN_Title = "title";
	public static final String COLUMN_Link = "link";
	public static final String COLUMN_PubDate = "pubDate";
	public static final String COLUMN_Description = "description";
	public static final String COLUMN_NewsType = "newsType";
    public static final String COLUMN_Picture = "picture";

	public static final String ArtTable= "articles.sqlite3";

	
	public static final String DATABASE_CREATE = "create table "
		      + DATABASE_NAME + "(guid TEXT primary key, " +
			    "title TEXT, "+
			    "link TEXT, "+
			    "pubDate TEXT, "+
			    "description TEXT, "+
			    "newstype INTEGER, "+
                "picture BLOB "+
		        ");";
	
	public SQLLiteHelper(Context context) {		
		super(context, ArtTable, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
	    onCreate(db);		
	}


}
