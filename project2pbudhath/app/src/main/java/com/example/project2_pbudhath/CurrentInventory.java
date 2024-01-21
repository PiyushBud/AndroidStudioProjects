package com.example.project2_pbudhath;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class CurrentInventory extends AppCompatActivity {

    ListView myList;

    SimpleCursorAdapter myAdapter;
    Cursor cCursor;
    DatabaseOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_inventory);

        myList = findViewById(R.id.Inventory_List);
        dbHelper = new DatabaseOpenHelper(this);

        new LoadDB().execute();

    }


    private final class LoadDB extends AsyncTask<String, Void, Cursor> {
        // runs on the UI thread
        @Override protected void onPostExecute(Cursor data) {
            myAdapter = new SimpleCursorAdapter(getApplicationContext(),
                    R.layout.datalist,
                    data,
                    new String[] { DatabaseOpenHelper.COL_1, DatabaseOpenHelper.COL_2,
                            DatabaseOpenHelper.COL_3, DatabaseOpenHelper.COL_4},
                    new int[] { R.id.name, R.id.cost, R.id.stock, R.id.disc},0);
            cCursor = data;
            myList.setAdapter(myAdapter);
        }
        // runs on its own thread
        @Override
        protected Cursor doInBackground(String... args) {
            return dbHelper.getAllTasks();
        }
    }
    private final class UpdateDB extends AsyncTask<String, Void, Cursor> {
        // runs on the UI thread
        @Override protected void onPostExecute(Cursor data) {
            myAdapter.swapCursor(data);
        }
        // runs on its own thread
        @Override
        protected Cursor doInBackground(String... args) {
            return dbHelper.getAllTasks();
        }
    }
}