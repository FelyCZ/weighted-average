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
    public static final String TABLE_NAME2 = "Marks2";
    public static final String TABLE_NAME3 = "Marks3";
    public static final String TABLE_NAME4 = "Marks4";
    public static final String TABLE_NAME5 = "Marks5";
    public static final String TABLE_NAME6 = "Marks6";
    public static SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;

    public DatabaseAdapter (Context context) {
        mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
    }

    public Cursor getAllEntries() {
        return mDb.query(TABLE_NAME,
                new String[]{"_id", COLUMN_NAME, COLUMN_MARK, COLUMN_WEIGHT}, null, null, null, null, null);
    }
    public Cursor getAllEntries2() {
        return mDb.query(TABLE_NAME2,
                new String[]{"_id", COLUMN_NAME, COLUMN_MARK, COLUMN_WEIGHT}, null, null, null, null, null);
    }
    public Cursor getAllEntries3() {
        return mDb.query(TABLE_NAME3,
                new String[]{"_id", COLUMN_NAME, COLUMN_MARK, COLUMN_WEIGHT}, null, null, null, null, null);
    }
    public Cursor getAllEntries4() {
        return mDb.query(TABLE_NAME4,
                new String[]{"_id", COLUMN_NAME, COLUMN_MARK, COLUMN_WEIGHT}, null, null, null, null, null);
    }
    public Cursor getAllEntries5() {
        return mDb.query(TABLE_NAME5,
                new String[]{"_id", COLUMN_NAME, COLUMN_MARK, COLUMN_WEIGHT}, null, null, null, null, null);
    }
    public Cursor getAllEntries6() {
        return mDb.query(TABLE_NAME6,
                new String[]{"_id", COLUMN_NAME, COLUMN_MARK, COLUMN_WEIGHT}, null, null, null, null, null);
    }

    public Cursor averageFromMarks(){
        Cursor cursor = mDb.rawQuery("SELECT sum(mark*weight)/sum(weight) AS " +
                "average FROM Marks;", null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor averageFromMarks2(){
        Cursor cursor = mDb.rawQuery("SELECT sum(mark*weight)/sum(weight) AS " +
                "average FROM Marks2", null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor averageFromMarks3(){
        Cursor cursor = mDb.rawQuery("SELECT sum(mark*weight)/sum(weight) AS " +
                "average FROM Marks3", null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor averageFromMarks4(){
        Cursor cursor = mDb.rawQuery("SELECT sum(mark*weight)/sum(weight) AS " +
                "average FROM Marks4", null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor averageFromMarks5(){
        Cursor cursor = mDb.rawQuery("SELECT sum(mark*weight)/sum(weight) AS " +
                "average FROM Marks5", null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor averageFromMarks6(){
        Cursor cursor = mDb.rawQuery("SELECT sum(mark*weight)/sum(weight) AS " +
                "average FROM Marks6", null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void addMark(String name, double mark, double weight) {
        ContentValues values = new ContentValues(3);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_MARK, mark);
        values.put(COLUMN_WEIGHT, weight);
        mDb.insert(TABLE_NAME, null, values);
    }
    public void addMark2(String name, double mark, double weight) {
        ContentValues values = new ContentValues(3);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_MARK, mark);
        values.put(COLUMN_WEIGHT, weight);
        mDb.insert(TABLE_NAME2, null, values);
    }
    public void addMark3(String name, double mark, double weight) {
        ContentValues values = new ContentValues(3);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_MARK, mark);
        values.put(COLUMN_WEIGHT, weight);
        mDb.insert(TABLE_NAME3, null, values);
    }
    public void addMark4(String name, double mark, double weight) {
        ContentValues values = new ContentValues(3);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_MARK, mark);
        values.put(COLUMN_WEIGHT, weight);
        mDb.insert(TABLE_NAME4, null, values);
    }
    public void addMark5(String name, double mark, double weight) {
        ContentValues values = new ContentValues(3);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_MARK, mark);
        values.put(COLUMN_WEIGHT, weight);
        mDb.insert(TABLE_NAME5, null, values);
    }
    public void addMark6(String name, double mark, double weight) {
        ContentValues values = new ContentValues(3);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_MARK, mark);
        values.put(COLUMN_WEIGHT, weight);
        mDb.insert(TABLE_NAME6, null, values);
    }

    public void deleteMark(long id) {
        mDb.delete(TABLE_NAME, "_id=" + id, null);
    }
    public void deleteMark2(long id) {
        mDb.delete(TABLE_NAME2, "_id=" + id, null);
    }
    public void deleteMark3(long id) {
        mDb.delete(TABLE_NAME3, "_id=" + id, null);
    }
    public void deleteMark4(long id) {
        mDb.delete(TABLE_NAME4, "_id=" + id, null);
    }
    public void deleteMark5(long id) {
        mDb.delete(TABLE_NAME5, "_id=" + id, null);
    }
    public void deleteMark6(long id) {
        mDb.delete(TABLE_NAME6, "_id=" + id, null);
    }

    public void deleteAll() {
        mDb.delete(TABLE_NAME, null, null);
    }
    public void deleteAll2() {
        mDb.delete(TABLE_NAME2, null, null);
    }
    public void deleteAll3() {
        mDb.delete(TABLE_NAME3, null, null);
    }
    public void deleteAll4() {
        mDb.delete(TABLE_NAME4, null, null);
    }
    public void deleteAll5() {
        mDb.delete(TABLE_NAME5, null, null);
    }
    public void deleteAll6() {
        mDb.delete(TABLE_NAME6, null, null);
    }

    public void updateMark(String name, double mark, double weight, long id) {
        ContentValues values = new ContentValues(3);
        values.put("name", name);
        values.put("mark", mark);
        values.put("weight", weight);
        mDb.update(TABLE_NAME, values, "_id=" + id, null);
    }
    public void updateMark2(String name, double mark, double weight, long id) {
        ContentValues values = new ContentValues(3);
        values.put("name", name);
        values.put("mark", mark);
        values.put("weight", weight);
        mDb.update(TABLE_NAME2, values, "_id=" + id, null);
    }
    public void updateMark3(String name, double mark, double weight, long id) {
        ContentValues values = new ContentValues(3);
        values.put("name", name);
        values.put("mark", mark);
        values.put("weight", weight);
        mDb.update(TABLE_NAME3, values, "_id=" + id, null);
    }
    public void updateMark4(String name, double mark, double weight, long id) {
        ContentValues values = new ContentValues(3);
        values.put("name", name);
        values.put("mark", mark);
        values.put("weight", weight);
        mDb.update(TABLE_NAME4, values, "_id=" + id, null);
    }
    public void updateMark5(String name, double mark, double weight, long id) {
        ContentValues values = new ContentValues(3);
        values.put("name", name);
        values.put("mark", mark);
        values.put("weight", weight);
        mDb.update(TABLE_NAME5, values, "_id=" + id, null);
    }
    public void updateMark6(String name, double mark, double weight, long id) {
        ContentValues values = new ContentValues(3);
        values.put("name", name);
        values.put("mark", mark);
        values.put("weight", weight);
        mDb.update(TABLE_NAME6, values, "_id=" + id, null);
    }
}