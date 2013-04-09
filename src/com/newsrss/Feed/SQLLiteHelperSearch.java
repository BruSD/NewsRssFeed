package com.newsrss.Feed;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLLiteHelperSearch extends SQLiteOpenHelper {

    public static final String DATABASE_NAME  = "SearchingLocalDB";
    private static final int DATABASE_VERSION = 1;
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_Search = "search";
    public static final String COLUMN_SearchDate = "searchDate";

    public static final String SearchTable= "searches.sqlite3";


    public static final String DATABASE_CREATE = "create table "
            + DATABASE_NAME + "(id INTEGER primary key, " +
            "search TEXT, "+
            "searchDate TEXT "+
            ");";

    public SQLLiteHelperSearch(Context context) {
        super(context, SearchTable, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String str1=DATABASE_CREATE;
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SearchTable);
        onCreate(db);
    }

}
