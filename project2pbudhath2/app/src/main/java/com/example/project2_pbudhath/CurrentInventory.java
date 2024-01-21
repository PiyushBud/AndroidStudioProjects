package com.example.project2_pbudhath;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CurrentInventory extends AppCompatActivity {

    private ListView myList;

    private SimpleCursorAdapter myAdapter;
    public DatabaseOpenHelper dbHelper;

    public SQLiteDatabase db;

    // For checking if activity return is for update or add.
    public static String RESULT_OPP = "RESULT_OPPERATION";

    public static int OPP_ADD = 1;
    public static int OPP_UPDATE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_inventory);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        myList = findViewById(R.id.Inventory_List);
        dbHelper = MainActivity.dbHelper;

        db = MainActivity.db;


        myList.setOnItemClickListener((parent, view, position, id) -> {
            String disc = ((TextView)view.findViewById(R.id.disc)).getText().toString();
            String name = ((TextView)view.findViewById(R.id.name)).getText().toString();
            Snackbar.make(view,  name + ": " + disc, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Action", null).show();

        });

        myList.setOnItemLongClickListener((parent, view, position, id) -> {
            longClickAlert(view);

            return true;
        });

        new LoadDB().execute();

    }

    private void longClickAlert(View v){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle( "Delete or Update?" )
                .setIcon(R.drawable.ic_launcher_background)
                .setMessage("Do you want to delete item or update item?")
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        deleteAlert(v);
                    }})
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), UpdateInventory.class);

                        String name = ((TextView)v.findViewById(R.id.name)).getText().toString();
                        String disc = ((TextView)v.findViewById(R.id.disc)).getText().toString();
                        String cost = ((TextView)v.findViewById(R.id.cost)).getText().toString();
                        String stock = ((TextView)v.findViewById(R.id.stock)).getText().toString();
                        String item_id = ((TextView)v.findViewById(R.id.id)).getText().toString();

                        intent.putExtra(UpdateInventory.PARAM_NAME, name);
                        intent.putExtra(UpdateInventory.PARAM_DISC, disc);
                        intent.putExtra(UpdateInventory.PARAM_COST, cost);
                        intent.putExtra(UpdateInventory.PARAM_STOCK, stock);
                        intent.putExtra(UpdateInventory.PARAM_ITEM_ID, item_id);

                        someActivityResultLauncher.launch(intent);
                    }
                }).show();
    }

    private void deleteAlert(View v){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle( "Delete Item?" )
                .setIcon(R.drawable.ic_launcher_background)
                .setMessage("Are you sure you wish to delete this item?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }})
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        String name = ((TextView)v.findViewById(R.id.name)).getText().toString();
                        dbHelper.delete(db, name);
                        new UpdateDB().execute();
                    }
                }).show();
    }

    private ActivityResultLauncher<Intent>
            someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String id = data.getStringExtra(UpdateInventory.RESULT_ITEM_ID);
                    String name = data.getStringExtra(UpdateInventory.RESULT_NAME);
                    String cost = data.getStringExtra(UpdateInventory.RESULT_COST);
                    String stock = data.getStringExtra(UpdateInventory.RESULT_STOCK);
                    String disc = data.getStringExtra(UpdateInventory.RESULT_DISC);

                    int opp = data.getIntExtra(RESULT_OPP, -1);

                    if(opp == OPP_ADD){
                        if(dbHelper.insert(db, name, cost, stock, disc)){
                            Toast.makeText(this, "Name already exists in inventory",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    if(opp == OPP_UPDATE){
                        dbHelper.update(db, name, cost, stock, disc, id);
                    }
                    new UpdateDB().execute();
                }

            });


    private final class LoadDB extends AsyncTask<String, Void, Cursor> {
        // runs on the UI thread
        @Override protected void onPostExecute(Cursor data) {
            myAdapter = new SimpleCursorAdapter(getApplicationContext(),
                    R.layout.datalist,
                    data,
                    new String[] { "_id", DatabaseOpenHelper.COL_1, DatabaseOpenHelper.COL_2,
                            DatabaseOpenHelper.COL_3, DatabaseOpenHelper.COL_4},
                    new int[] { R.id.id, R.id.name, R.id.cost, R.id.stock, R.id.disc},0);
            myList.setAdapter(myAdapter);
        }
        // runs on its own thread
        @Override
        protected Cursor doInBackground(String... args) {
            return dbHelper.getAllTasks(db);
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
            return dbHelper.getAllTasks(db);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add){
            Intent intent = new Intent(getApplicationContext(), AddToInventory.class);
            someActivityResultLauncher.launch(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}