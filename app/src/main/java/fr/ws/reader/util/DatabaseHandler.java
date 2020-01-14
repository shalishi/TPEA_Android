package fr.ws.reader.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

import fr.ws.reader.bean.Article;
import fr.ws.reader.bean.Feed;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "READER_DB";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Feed.CREATE_TABLE);
        db.execSQL(Article.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL(Feed.DROP_TABLE);
            db.execSQL(Article.DROP_TABLE);
            onCreate(db);
        }
    }

    // DELETE DB
    public void deleteDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(Feed.DROP_TABLE);
        db.execSQL(Article.DROP_TABLE);
        onCreate(db);
        db.close();
    }



    // Getting article Count
    public int getProductsCount() {
        String countQuery = "SELECT  * FROM " + Article.NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }


    // code to add the new article
    public long saveArticle(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Article.TITLE, article.getTitle());
            values.put(Article.DESCIRPTION, article.getDescription());
            values.put(Article.DATE, article.getPubDate().toString());
            values.put(Article.LINK, article.getLink());
            values.put(Article.IMAGE, article.getImage());
            // Inserting Row
            long id = db.insert(Article.NAME, null, values);
        db.close(); // Closing database connection
        return id;
    }

}
