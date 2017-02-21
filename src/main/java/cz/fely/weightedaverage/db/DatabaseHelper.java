package cz.fely.weightedaverage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Marks.db";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE = "CREATE TABLE Marks (_id INTEGER PRIMARY KEY," +
            " name STRING, mark DOUBLE, weight DOUBLE )";
    private static final String SQL_CREATE_TABLE2 = "CREATE TABLE Marks2 (_id INTEGER PRIMARY " +
            "KEY,  name STRING, mark DOUBLE, weight DOUBLE )";
    private static final String SQL_CREATE_TABLE3 = "CREATE TABLE Marks3 (_id INTEGER PRIMARY " +
            "KEY, name STRING, mark DOUBLE, weight DOUBLE )";
    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS Marks";
    private static final String SQL_DELETE_TABLE2 = "DROP TABLE IF EXISTS Marks2";
    private static final String SQL_DELETE_TABLE3 = "DROP TABLE IF EXISTS Marks3";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_TABLE2);
        db.execSQL(SQL_CREATE_TABLE3);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        db.execSQL(SQL_DELETE_TABLE2);
        db.execSQL(SQL_DELETE_TABLE3);
        onCreate(db);
    }
}