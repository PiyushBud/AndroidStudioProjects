package com.example.project2_pbudhath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static DatabaseOpenHelper dbHelper;

    public static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseOpenHelper(this);

        db = dbHelper.getWritableDatabase();
    }

    public void myOnClick(View v){
        if (v.getId() == R.id.Manage_Button){
            startActivity(new Intent(this, CurrentInventory.class));
        }
        if(v.getId() == R.id.Handle_Button){
            startActivity(new Intent(this, HandleOrders.class));
        }
    }
}