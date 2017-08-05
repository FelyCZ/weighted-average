package cz.fely.weightedaverage.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;

import cz.fely.weightedaverage.MainActivity;

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
            Log.i("DB: ", "Table Marks created");
            Log.i("DB: ", "Table TabList created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            switch (oldVersion){
                case 1:
                    String sql = "";
                 //   db.execSQL(sql);
                    break;
                case 2:
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
        Cursor c = mDb.rawQuery("SELECT date AS day FROM Marks WHERE _id = " +id+ " AND subject = "+subject, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
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

    public void reChangeTabInt() {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);

        boolean changed = mPrefs.getBoolean("pref_db_changed_titles", false);

        if (changed == false) {

            mDb = helper.getWritableDatabase();

            String name;
            int sub;
            double mark, weight;
            String date;
            long id;

            ArrayList<Long> ids = new ArrayList<>();
            ArrayList<Long> ids2 = new ArrayList<>();

            ArrayList<Long> ids3 = new ArrayList<>();
            ArrayList<Long> ids4 = new ArrayList<>();

            ArrayList<Long> ids5 = new ArrayList<>();
            ArrayList<Long> ids6 = new ArrayList<>();

            ArrayList<Long> ids7 = new ArrayList<>();
            ArrayList<Long> ids8 = new ArrayList<>();

            Cursor c2 = getAllEntries(2);
            Cursor c3 = getAllEntries(3);

            Cursor c4 = getAllEntries(4);
            Cursor c5 = getAllEntries(5);

            Cursor c9 = getAllEntries(9);
            Cursor c12 = getAllEntries(12);

            Cursor c10 = getAllEntries(10);

            Cursor cursor2, cursor3, cursor4, cursor5, cursor9, cursor10, cursor12;

            ContentValues values;

            if (c2.moveToFirst()) {
                do {
                    ids.add(c2.getLong(c2.getColumnIndex(COlUMN_ID)));
                } while (c2.moveToNext());
            }

            if (c3.moveToFirst()) {
                do {
                    ids2.add(c3.getLong(c3.getColumnIndex(COlUMN_ID)));
                } while (c3.moveToNext());
            }

            for (int i = 0; i < c2.getCount(); i++) {
                cursor2 = mDb.rawQuery("SELECT * FROM Marks WHERE _id = " + ids.get(i), null);
                cursor2.moveToFirst();

                name = cursor2.getString(cursor2.getColumnIndex(COLUMN_NAME));
                mark = cursor2.getDouble(cursor2.getColumnIndex(COLUMN_MARK));
                weight = cursor2.getDouble(cursor2.getColumnIndex(COLUMN_WEIGHT));
                sub = 3;
                date = cursor2.getString(cursor2.getColumnIndex(COLUMN_DATE));
                id = cursor2.getLong(cursor2.getColumnIndex(COlUMN_ID));

                values = new ContentValues();
                values.put(COLUMN_NAME, name);
                values.put(COLUMN_MARK, mark);
                values.put(COLUMN_WEIGHT, weight);
                values.put(COLUMN_DATE, date);
                values.put(COLUMN_SUBJECT_ID, sub);

                mDb.update(TABLE_NAME, values, COlUMN_ID + " = " + ids.get(i), null);
            }
            for (int i = 0; i < c3.getCount(); i++) {
                cursor3 = mDb.rawQuery("SELECT * FROM Marks WHERE _id = " + ids2.get(i), null);
                cursor3.moveToFirst();

                name = cursor3.getString(cursor3.getColumnIndex(COLUMN_NAME));
                mark = cursor3.getDouble(cursor3.getColumnIndex(COLUMN_MARK));
                weight = cursor3.getDouble(cursor3.getColumnIndex(COLUMN_WEIGHT));
                sub = 2;
                date = cursor3.getString(cursor3.getColumnIndex(COLUMN_DATE));
                id = cursor3.getLong(cursor3.getColumnIndex(COlUMN_ID));

                values = new ContentValues();
                values.put(COLUMN_NAME, name);
                values.put(COLUMN_MARK, mark);
                values.put(COLUMN_WEIGHT, weight);
                values.put(COLUMN_DATE, date);
                values.put(COLUMN_SUBJECT_ID, sub);

                mDb.update(TABLE_NAME, values, COlUMN_ID + " = " + ids2.get(i), null);
            }

            if (c4.moveToFirst()) {
                do {
                    ids3.add(c4.getLong(c4.getColumnIndex(COlUMN_ID)));
                } while (c4.moveToNext());
            }

            if (c5.moveToFirst()) {
                do {
                    ids4.add(c5.getLong(c5.getColumnIndex(COlUMN_ID)));
                } while (c5.moveToNext());
            }

            for (int i = 0; i < c4.getCount(); i++) {
                cursor4 = mDb.rawQuery("SELECT * FROM Marks WHERE _id = " + ids3.get(i), null);
                cursor4.moveToFirst();

                name = cursor4.getString(cursor4.getColumnIndex(COLUMN_NAME));
                mark = cursor4.getDouble(cursor4.getColumnIndex(COLUMN_MARK));
                weight = cursor4.getDouble(cursor4.getColumnIndex(COLUMN_WEIGHT));
                sub = 5;
                date = cursor4.getString(cursor4.getColumnIndex(COLUMN_DATE));
                id = cursor4.getLong(cursor4.getColumnIndex(COlUMN_ID));

                values = new ContentValues();
                values.put(COLUMN_NAME, name);
                values.put(COLUMN_MARK, mark);
                values.put(COLUMN_WEIGHT, weight);
                values.put(COLUMN_DATE, date);
                values.put(COLUMN_SUBJECT_ID, sub);

                mDb.update(TABLE_NAME, values, COlUMN_ID + " = " + ids3.get(i), null);
            }
            for (int i = 0; i < c5.getCount(); i++) {
                cursor5 = mDb.rawQuery("SELECT * FROM Marks WHERE _id = " + ids4.get(i), null);
                cursor5.moveToFirst();

                name = cursor5.getString(cursor5.getColumnIndex(COLUMN_NAME));
                mark = cursor5.getDouble(cursor5.getColumnIndex(COLUMN_MARK));
                weight = cursor5.getDouble(cursor5.getColumnIndex(COLUMN_WEIGHT));
                sub = 4;
                date = cursor5.getString(cursor5.getColumnIndex(COLUMN_DATE));
                id = cursor5.getLong(cursor5.getColumnIndex(COlUMN_ID));

                values = new ContentValues();
                values.put(COLUMN_NAME, name);
                values.put(COLUMN_MARK, mark);
                values.put(COLUMN_WEIGHT, weight);
                values.put(COLUMN_DATE, date);
                values.put(COLUMN_SUBJECT_ID, sub);

                mDb.update(TABLE_NAME, values, COlUMN_ID + " = " + ids4.get(i), null);
            }

            if (c9.moveToFirst()) {
                do {
                    ids5.add(c9.getLong(c9.getColumnIndex(COlUMN_ID)));
                } while (c9.moveToNext());
            }

            if (c12.moveToFirst()) {
                do {
                    ids6.add(c12.getLong(c12.getColumnIndex(COlUMN_ID)));
                } while (c12.moveToNext());
            }

            for (int i = 0; i < c9.getCount(); i++) {
                cursor9 = mDb.rawQuery("SELECT * FROM Marks WHERE _id = " + ids5.get(i), null);
                cursor9.moveToFirst();

                name = cursor9.getString(cursor9.getColumnIndex(COLUMN_NAME));
                mark = cursor9.getDouble(cursor9.getColumnIndex(COLUMN_MARK));
                weight = cursor9.getDouble(cursor9.getColumnIndex(COLUMN_WEIGHT));
                sub = 12;
                date = cursor9.getString(cursor9.getColumnIndex(COLUMN_DATE));
                id = cursor9.getLong(cursor9.getColumnIndex(COlUMN_ID));

                values = new ContentValues();
                values.put(COLUMN_NAME, name);
                values.put(COLUMN_MARK, mark);
                values.put(COLUMN_WEIGHT, weight);
                values.put(COLUMN_DATE, date);
                values.put(COLUMN_SUBJECT_ID, sub);

                mDb.update(TABLE_NAME, values, COlUMN_ID + " = " + ids5.get(i), null);
            }
            for (int i = 0; i < c12.getCount(); i++) {
                cursor12 = mDb.rawQuery("SELECT * FROM Marks WHERE _id = " + ids6.get(i), null);
                cursor12.moveToFirst();

                name = cursor12.getString(cursor12.getColumnIndex(COLUMN_NAME));
                mark = cursor12.getDouble(cursor12.getColumnIndex(COLUMN_MARK));
                weight = cursor12.getDouble(cursor12.getColumnIndex(COLUMN_WEIGHT));
                sub = 9;
                date = cursor12.getString(cursor12.getColumnIndex(COLUMN_DATE));
                id = cursor12.getLong(cursor12.getColumnIndex(COlUMN_ID));

                values = new ContentValues();
                values.put(COLUMN_NAME, name);
                values.put(COLUMN_MARK, mark);
                values.put(COLUMN_WEIGHT, weight);
                values.put(COLUMN_DATE, date);
                values.put(COLUMN_SUBJECT_ID, sub);

                mDb.update(TABLE_NAME, values, COlUMN_ID + " = " + ids6.get(i), null);
            }

            c12 = getAllEntries(12);

            if (c12.moveToFirst()) {
                do {
                    ids7.add(c12.getLong(c12.getColumnIndex(COlUMN_ID)));
                } while (c12.moveToNext());
            }

            if (c10.moveToFirst()) {
                do {
                    ids8.add(c10.getLong(c10.getColumnIndex(COlUMN_ID)));
                } while (c10.moveToNext());
            }

            for (int i = 0; i < c12.getCount(); i++) {
                cursor12 = mDb.rawQuery("SELECT * FROM Marks WHERE _id = " + ids7.get(i), null);
                cursor12.moveToFirst();

                name = cursor12.getString(cursor12.getColumnIndex(COLUMN_NAME));
                mark = cursor12.getDouble(cursor12.getColumnIndex(COLUMN_MARK));
                weight = cursor12.getDouble(cursor12.getColumnIndex(COLUMN_WEIGHT));
                sub = 10;
                date = cursor12.getString(cursor12.getColumnIndex(COLUMN_DATE));
                id = cursor12.getLong(cursor12.getColumnIndex(COlUMN_ID));

                values = new ContentValues();
                values.put(COLUMN_NAME, name);
                values.put(COLUMN_MARK, mark);
                values.put(COLUMN_WEIGHT, weight);
                values.put(COLUMN_DATE, date);
                values.put(COLUMN_SUBJECT_ID, sub);

                mDb.update(TABLE_NAME, values, COlUMN_ID + " = " + ids7.get(i), null);
            }
            for (int i = 0; i < c10.getCount(); i++) {
                cursor10 = mDb.rawQuery("SELECT * FROM Marks WHERE _id = " + ids8.get(i), null);
                cursor10.moveToFirst();

                name = cursor10.getString(cursor10.getColumnIndex(COLUMN_NAME));
                mark = cursor10.getDouble(cursor10.getColumnIndex(COLUMN_MARK));
                weight = cursor10.getDouble(cursor10.getColumnIndex(COLUMN_WEIGHT));
                sub = 12;
                date = cursor10.getString(cursor10.getColumnIndex(COLUMN_DATE));
                id = cursor10.getLong(cursor10.getColumnIndex(COlUMN_ID));

                values = new ContentValues();
                values.put(COLUMN_NAME, name);
                values.put(COLUMN_MARK, mark);
                values.put(COLUMN_WEIGHT, weight);
                values.put(COLUMN_DATE, date);
                values.put(COLUMN_SUBJECT_ID, sub);

                mDb.update(TABLE_NAME, values, COlUMN_ID + " = " + ids8.get(i), null);
            }

            mPrefs.edit().putBoolean("pref_db_changed_titles", true).apply();
        }
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