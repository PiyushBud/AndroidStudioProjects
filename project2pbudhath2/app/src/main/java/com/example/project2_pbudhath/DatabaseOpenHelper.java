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
    public static final String COL_4 = "disc";


    private static final String CREATE_CMD = "CREATE TABLE " + TABLE_NAME + "("
            + "_id "+"INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_1 + " TEXT NOT NULL, " +
            COL_2 + " TEXT NOT NULL, " + COL_3 + " TEXT NOT NULL, " + COL_4 + " TEXT NOT NULL)";
    public DatabaseOpenHelper(Context context) {
        super(context,"hotsauce_db",null,1);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CMD);
        insert(db, "Cholula", "2.50", "50", "The best hot sauce");
        insert(db, "Franks Red Hot", "2.50", "50", "The american hot sauce");
        insert(db, "Sriracha", "3.50", "25", "The second best hot sauce");
    }

    public boolean insert(SQLiteDatabase db, String name, String cost, String stock, String disc){
        Cursor c = db.query(TABLE_NAME, new String[]{"_id", COL_1, COL_2, COL_3, COL_4},
                "name=?", new String[]{name}, null, null, null);
        if(c.getCount() != 0){
            return true;
        }
        ContentValues cv = new ContentValues();
        cv.put(COL_1, name);
        cv.put(COL_2, cost);
        cv.put(COL_3, stock);
        cv.put(COL_4, disc);
        db.insert(TABLE_NAME, null, cv);
        return false;
    }
    public void delete(SQLiteDatabase db, String name){
        db.delete(TABLE_NAME, COL_1 + "=?", new String[]{name});
    }

    public void update(SQLiteDatabase db, String name, String cost, String stock,
                       String disc, String id){
        ContentValues cv = new ContentValues();
        cv.put(COL_1, name);
        cv.put(COL_2, cost);
        cv.put(COL_3, stock);
        cv.put(COL_4, disc);

        db.update(TABLE_NAME, cv, "name=?", new String[]{name});
    }

    public Cursor getAllTasks(SQLiteDatabase db){
        Cursor c =db.query(TABLE_NAME, new String[] {"_id", COL_1, COL_2, COL_3, COL_4},
                null, null, null, null, null, null);
        return c;
    }

    public Cursor getByName(SQLiteDatabase db, String name){
        return db.query(TABLE_NAME, new String[] {"_id", COL_1, COL_2, COL_3, COL_4},
                "name=?", new String[]{name}, null, null, null);
    }

    public void clearAll(SQLiteDatabase db){
        db.delete(TABLE_NAME, null, null);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

}