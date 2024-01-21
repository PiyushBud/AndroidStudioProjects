package com.example.lab5_pbudhath;

import  android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "toDos";
    public static final String COL_NAME = "task";
    private static final String CREATE_CMD = "CREATE TABLE " + TABLE_NAME + "("
            + "_id "+"INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME +" TEXT NOT NULL)";
    public DatabaseOpenHelper(Context context) {
        super(context,"artist_db",null,1);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CMD);
        insert("Lab 3 Piyush Bud\n Fall 2023");
    }

    public void insert(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, task);
        db.insert(TABLE_NAME, null, cv);
        db.close();
    }
    public void delete(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_NAME + "=?", new String[]{task});
        db.close();
    }

    public Cursor getAllTasks(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TABLE_NAME, new String[] {"_id", COL_NAME},
                null, null, null, null, null, null);
    }

    public void clearAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}