package cz.fely.weightedaverage.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

import cz.fely.weightedaverage.activities.MainActivity;

public class Database {
    public static final String DB_NAME = "AppDB.db";
    public static final int DB_VER = 1;
    public static final String TABLE_NAME = "Marks";
    public static final String TABLE2_NAME = "TabList";
    public static final String COlUMN_ID = "_id";
    public static final String COLUMN_SUBJECT_ID = "subject";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MARK = "mark";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TAB = "tab";
    public static final String COLUMN_TITLE = "title";
    public static final String SQL_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS Marks (_id INTEGER PRIMARY KEY, subject INTEGER, name STRING, mark DOUBLE, weight DOUBLE, date STRING )";
    public static final String SQL_TABLE2_CREATE = "CREATE TABLE IF NOT EXISTS TabList ( tab INTEGER PRIMARY KEY, title STRING )";
    public static final String SQL_TABLE_DELETE = "DROP TABLE IF EXISTS Marks";
    public static final String SQL_TABLE2_DELETE = "DROP TABLE IF EXISTS TabList";
    private SQLiteOpenHelper helper;
    SQLiteDatabase mDb;

    public static class DbHelper extends SQLiteOpenHelper {
        public DbHelper(Context ctx) {
            super(ctx, DB_NAME, null, DB_VER);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_TABLE_CREATE);
            db.execSQL(SQL_TABLE2_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            switch (oldVersion){
                case 1:
                    String sql = "";
                 //   db.execSQL(sql);
                    break;
            }
        }
    }

    public Database(Context ctx) {
        helper = new DbHelper(ctx);
    }

    public Cursor getAllEntries(int sub) {
        mDb = helper.getWritableDatabase();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);
        String order = mPrefs.getString("pref_key_general_order", "desc");
        if(order.equals("desc")) {
            return mDb.rawQuery("SELECT * FROM Marks WHERE subject = " + sub + " ORDER BY _id DESC", null);
        }
        else{
            return mDb.rawQuery("SELECT * FROM Marks WHERE subject = " + sub + " ORDER BY _id ASC", null);
        }
    }

    public Cursor getAllTabNTitle(){
        mDb = helper.getWritableDatabase();
        return mDb.rawQuery("SELECT * FROM TabList", null);
    }

    public Cursor getFromNameEntries() {
        mDb = helper.getWritableDatabase();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);
        String order = mPrefs.getString("pref_key_general_order", "desc");
        return mDb.rawQuery("SELECT name FROM Marks", null);
    }

    public Cursor getDate(int sub, long id){
        mDb = helper.getWritableDatabase();
        String subject = String.valueOf(sub);
        Cursor subjectCount = mDb.rawQuery("SELECT date AS day FROM Marks WHERE subject = " + sub, null);
        subjectCount.moveToPosition((int) id);
        /*Cursor c = mDb.rawQuery("SELECT date AS day FROM Marks WHERE _id = " +id+ " AND subject = "+subject, null);
        if (subjectCount != null) {
            subjectCount.moveToFirst();
        }*/
        return subjectCount;
    }

    public Cursor makeAverage(int sub) {
        mDb = helper.getReadableDatabase();
        Cursor c = mDb.rawQuery("SELECT sum(mark*weight)/sum(weight) AS average FROM Marks WHERE subject = " + sub, null);
        if (c != null) {
            if (c.isClosed() == false) {
                mDb = helper.getReadableDatabase();
                c = (mDb.rawQuery("SELECT sum(mark*weight)/sum(weight) AS average FROM Marks WHERE subject = " + sub, null));
                c.moveToFirst();
            }
        }
        return c;
    }

    public void newTabListTable(String[] titles){
        mDb = helper.getWritableDatabase();
        ContentValues values;

        for(int i = 0; i < 15; i++){
            values = new ContentValues();
            values.put(COLUMN_TAB, i);
            values.put(COLUMN_TITLE, titles[i]);
            mDb.insert(TABLE2_NAME, null, values);
        }
    }

    public String getTabTitle(int tab) {
        mDb = helper.getReadableDatabase();
        Cursor cursor = mDb.rawQuery("SELECT title FROM TabList WHERE tab = " + tab, null);
        if(cursor != null){
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
        }
        else return null;
    }

    public void updateTabTitle(int tab, String title){
        mDb = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        mDb.update(TABLE2_NAME, values, "tab = " + tab, null);
    }

    public void addMark(int sub, String name, double mark, double weight, String date) {
        mDb = helper.getWritableDatabase();
        String subject = String.valueOf(sub);
        ContentValues values = new ContentValues();
        values.put(COLUMN_SUBJECT_ID, subject);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_MARK, mark);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_DATE, date);
        mDb.insert(TABLE_NAME, null, values);
    }

    public void deleteMark(long id, int sub) {
        mDb = helper.getWritableDatabase();
        String subject = String.valueOf(sub);
        mDb.delete(TABLE_NAME, "_id=" + id + " AND subject=" + subject, null);
    }

    public void deleteSubject(int sub) {
        mDb = helper.getWritableDatabase();
        mDb.delete(TABLE_NAME, "subject="+sub, null);
    }

    public void updateMark(int sub, String name, double mark, double weight, String date, long id) {
        mDb = helper.getWritableDatabase();
        String subject = String.valueOf(sub);
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_MARK, mark);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_DATE, date);
        mDb.update(TABLE_NAME, values, "_id=" + id + " AND subject=" + subject, null);
    }

    public void updateMark(int sub, String name, double mark, double weight, long id) {
        mDb = helper.getWritableDatabase();
        String subject = String.valueOf(sub);
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_MARK, mark);
        values.put(COLUMN_WEIGHT, weight);
        mDb.update(TABLE_NAME, values, "_id=" + id + " AND subject=" + subject, null);
    }
}