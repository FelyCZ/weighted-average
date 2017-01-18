package cz.fely.weightedaverage.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DatabaseAdapter implements BaseColumns{
    public static final String COLUMN_MARK = "mark";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String TABLE_NAME = "Marks";
    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;

    public DatabaseAdapter(Context context) {
        mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
    }

    public Cursor getAllEntries() {
        return mDb.query(TABLE_NAME,
                new String[]{"_id", COLUMN_NAME, COLUMN_MARK, COLUMN_WEIGHT}, null, null, null, null, null);
    }

    public void addMark(String name, double mark, double weight) {
        ContentValues values = new ContentValues(3);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_MARK, Double.valueOf(mark));
        values.put(COLUMN_WEIGHT, Double.valueOf(weight));
        mDb.insert(TABLE_NAME, null, values);
    }
    public void deleteMark(long id) {
        mDb.delete(TABLE_NAME, "_id=" + id, null);
    }

    public void deleteAll() {
        mDb.delete(TABLE_NAME, null, null);
    }
}