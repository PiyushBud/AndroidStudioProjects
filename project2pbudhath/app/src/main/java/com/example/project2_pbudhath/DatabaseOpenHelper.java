package com.example.project2_pbudhath;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "inventory";
    public static final String COL_1 = "name";
    public static final String COL_2 = "cost";
    public static final String COL_3 = "stock";
    public static final String COL_4 = "desc";

    private static final String CREATE_CMD = "CREATE TABLE " + TABLE_NAME + "("
            + "_id "+"INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_1 + " TEXT NOT NULL, " +
            COL_2 + " TEXT NOT NULL)";
    public DatabaseOpenHelper(Context context) {
        super(context,"hotsauce_db",null,1);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CMD);
        insert("Cholula", "2.50", "50", "The best hot sauce");
        insert("Franks Red Hot", "2.50", "50", "The american hot sauce");
        insert("Sriracha", "3.50", "25", "The second best hot sauce");
    }

    public void insert(String name, String cost, String stock, String disc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_1, name);
        cv.put(COL_2, cost);
        cv.put(COL_3, stock);
        cv.put(COL_4, disc);
        db.insert(TABLE_NAME, null, cv);
        db.close();
    }
    public void delete(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_1 + "=?", new String[]{name});
        db.close();
    }

    public Cursor getAllTasks(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TABLE_NAME, new String[] {"_id", COL_1, COL_2, COL_3, COL_4},
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