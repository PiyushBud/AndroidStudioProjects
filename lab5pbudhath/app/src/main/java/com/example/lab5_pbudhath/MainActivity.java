package com.example.lab5_pbudhath;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    EditText elem;
    ListView listView;
    DatabaseOpenHelper dbHelper;

    SimpleCursorAdapter myAdapter;

    LoadDB ldb;

    Cursor cCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.mylist);

        dbHelper = new DatabaseOpenHelper(this);

        // Param1 - context
        // Param2 - layout for the row

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doDone(view, position);
            }
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            alertView("Single Item Deletion",view);

            return true;
        }
        );
        elem = (EditText)findViewById(R.id.input);
        ldb = new LoadDB();
        ldb.execute();
    }


    public void clearEdit(View v) {
        dbHelper.clearAll();
        new UpdateDB().execute();
    }

    public void addElem(View v) {
        String input = elem.getText().toString();
        dbHelper.insert(input);
        Toast.makeText(getApplicationContext(), "Adding " + input, Toast.LENGTH_SHORT).show();
        new UpdateDB().execute();
        elem.setText("");
    }

    public void doDone(View v, int i){
        String text = ((TextView)((LinearLayout) v).getChildAt(0)).getText().toString();
        dbHelper.delete(text);
        if(text.contains("Done:")){
            text = text.substring(5);
        }
        else{
            text = "Done:" + text;
        }

        dbHelper.insert(text);
        new UpdateDB().execute();
    }

    public void deleteDone(View v){
        Cursor c = dbHelper.getAllTasks();
        c.moveToFirst();
        for(int i = 0; i < c.getCount(); i++){
            if (c.getString(1).contains("Done:")){
                dbHelper.delete(c.getString(1));
            }
            c.moveToNext();
        }
        new UpdateDB().execute();
    }


    private void alertView(String message, View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle( message )
                .setIcon(R.drawable.ic_launcher_background)
                .setMessage("Are you sure you want to do this?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }})
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dbHelper.delete(((TextView)view).getText().toString());
                        new UpdateDB().execute();
                    }
                }).show();
    }


    private final class LoadDB extends AsyncTask<String, Void, Cursor> {
        // runs on the UI thread
        @Override protected void onPostExecute(Cursor data) {
            myAdapter = new SimpleCursorAdapter(getApplicationContext(),
                    R.layout.datalist,
                    data,
                    new String[] { "task" },
                    new int[] { R.id.tasks },0);
            cCursor = data;
            listView.setAdapter(myAdapter);

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