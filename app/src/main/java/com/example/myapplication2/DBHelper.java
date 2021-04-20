package com.example.myapplication2;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "COLLECTION";
    public static final String TABLE_CONTACTS1 = "directory";
    public static final String TABLE_CONTACTS2 = "data";

    public static final String KEY_ID = "_id";
    public static final String KEY_FOLDER="folder";
    public static final String KEY_FOLDER_ID="folder_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_NOTE = "note";
    public static final String KEY_IMAGE="image";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTACTS1 + "(" + KEY_ID
                + " integer primary key,"+ KEY_FOLDER +" text not null"+")");
        db.execSQL("create table " + TABLE_CONTACTS2 + "(" + KEY_ID
                + " integer primary key," + KEY_TITLE + " text not null," + KEY_NOTE + " text," + KEY_FOLDER_ID + " integer not null,"+KEY_IMAGE+" BLOB" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CONTACTS1);
        db.execSQL("drop table if exists " + TABLE_CONTACTS2);
        onCreate(db);
    }
}
