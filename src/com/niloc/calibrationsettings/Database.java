package com.niloc.calibrationsettings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database {

    int id = 0;
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "Setting";
    public static final String KEY_RED = "Red";
    public static final String KEY_GREEN = "Green";
    public static final String KEY_BLUE = "Blue";

    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "calibrator.db";
    private static final String DATABASE_TABLE_SETTINGS = "tblCurrentSettings";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE = "CREATE TABLE " + DATABASE_TABLE_SETTINGS 
        + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " 
	+ "Setting TEXT, "
        + "Red INTEGER, "
        + "Green INTEGER,"
        + "Blue INTEGER);";

    private final Context context;

    private static DatabaseHelper DBHelper;
    private static SQLiteDatabase db;


    public Database(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion
                       + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS tblCurrentSettings");
            onCreate(db);
        }
    }

    //---opens the database---
    public Database open() throws SQLException {
	
        db = DBHelper.getWritableDatabase();
        if(getNumEntries() == 0) {
	    	insertRGBPreset("Red", 1000, 0, 0);
			insertRGBPreset("Green", 0, 1000, 0);
			insertRGBPreset("Blue", 0, 0, 1000);
			insertRGBPreset("Custom", 1000, 1000, 1000);
        }
        return this;
    }

    //---closes the database---
    public void close() {
        DBHelper.close();
    }
    
    public static long insertRGBPreset(String name, int red, int green, int blue) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_RED, red);
        initialValues.put(KEY_GREEN, green);
        initialValues.put(KEY_BLUE, blue);
        return db.insert(DATABASE_TABLE_SETTINGS, null, initialValues);
    }

    public long replaceRGBPreset(String name, int red, int green, int blue, int id) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID, id);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_RED, red);
        initialValues.put(KEY_GREEN, green);
        initialValues.put(KEY_BLUE, blue);
        return db.replace(DATABASE_TABLE_SETTINGS, null, initialValues);
    }

    public int getNumEntries() {
        Cursor cursor = db.rawQuery("SELECT COUNT(Setting) FROM tblCurrentSettings", null);
        if(cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return cursor.getInt(0);
    }

    public String getName(int id) {
        Cursor cursor = db.rawQuery("SELECT Setting FROM tblCurrentSettings WHERE _id = " + id, null);
        if(cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return cursor.getString(0);
    }

    public int getRed(int id) {
        Cursor cursor = db.rawQuery("SELECT Red FROM tblCurrentSettings WHERE _id = " + id, null);
        if(cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return cursor.getInt(0);
    }

    public int getGreen(int id) {
        Cursor cursor = db.rawQuery("SELECT Green FROM tblCurrentSettings WHERE _id = " + id, null);
        if(cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return cursor.getInt(0);
    }

    public int getBlue(int id) {
        Cursor cursor = db.rawQuery("SELECT Blue FROM tblCurrentSettings WHERE _id = " + id, null);
        if(cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return cursor.getInt(0);
    }
}
