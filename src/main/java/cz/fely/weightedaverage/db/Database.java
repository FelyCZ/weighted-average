package cz.fely.weightedaverage.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import cz.fely.weightedaverage.MainActivity;

public class Database {
    public static final String DB_NAME = "MarksV2.db";
    public static final int DB_VER = 1;
    public static final String TABLE_NAME = "Marks";
    public static final String COlUMN_ID = "_id";
    public static final String COLUMN_SUBJECT_ID = "subject";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MARK = "mark";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_DATE = "date";
    public static final String SQL_TABLE_CREATE = "CREATE TABLE Marks (_id INTEGER PRIMARY KEY, subject INTEGER,name STRING, mark DOUBLE, weight DOUBLE, date STRING )";
    public static final String SQL_TABLE_DELETE = "DROP TABLE IF EXIST Marks";
    private SQLiteOpenHelper helper;
    SQLiteDatabase mDb;

    @SuppressWarnings("InfiniteRecursion")
    static class DbHelper extends SQLiteOpenHelper {
                public DbHelper(Context ctx) {
                    super(ctx, DB_NAME, null, DB_VER);
                }

                @Override
                public void onCreate(SQLiteDatabase db) {
                    db.execSQL(SQL_TABLE_CREATE);
                    Log.i("DB", "Marks Created");
                }

                @Override
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                    db.execSQL(SQL_TABLE_DELETE);
                    onCreate(db);
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
            if(c.isClosed() == false){
                mDb = helper.getReadableDatabase();
                c = (mDb.rawQuery("SELECT sum(mark*weight)/sum(weight) AS average FROM Marks WHERE subject = " + sub, null));
                c.moveToFirst();
            }
        }
        return c;
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