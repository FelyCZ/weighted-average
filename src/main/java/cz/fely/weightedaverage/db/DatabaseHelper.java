package cz.fely.weightedaverage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Marks.db";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE = "CREATE TABLE Marks (_id INTEGER PRIMARY KEY, " +
            "name STRING, mark DOUBLE, weight DOUBLE )";
    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS Marks";
    private static DatabaseHelper mInstance = null;
    private DatabaseHelper mDbHelper = null;
    SQLiteDatabase db = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
}
