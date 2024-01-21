package com.example.project2_pbudhath;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HandleOrders extends AppCompatActivity {

    private Spinner mySpinner;
    private DatabaseOpenHelper dbHelper;
    private SQLiteDatabase db;

    private TextView order_summary;

    private ArrayList<String[]> orders;

    private SimpleCursorAdapter myAdapter;

    private EditText order_amount_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_orders);

        dbHelper = new DatabaseOpenHelper(this);
        db = dbHelper.getWritableDatabase();

        mySpinner = findViewById(R.id.sauce_spinner);
        order_summary = findViewById(R.id.order_summary);
        order_amount_edit = findViewById(R.id.order_amount_edit);
        order_amount_edit.setInputType(InputType.TYPE_CLASS_NUMBER);

        orders = new ArrayList<>();
        new LoadDB().execute();
    }

    public void onHandleClick(View v){
        String name = ((TextView)mySpinner.getSelectedView().findViewById(R.id.sauce_item))
                .getText().toString();

        if(v.getId() == R.id.add_button){
            String order_amount = order_amount_edit.getText().toString();

            if((order_amount.isEmpty() || Float.parseFloat(order_amount) < 1)){
                Toast.makeText(this, "Please enter a valid number of orders",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            int index = findByName(name);
            if(index != -1) {
                Toast.makeText(this, "Please remove item " +
                        "first if you wish to edit the order", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor c = dbHelper.getByName(db, name);

            c.moveToFirst();
            String cost = c.getString(2);
            String stock = c.getString(3);
            if(Float.parseFloat(order_amount) > Float.parseFloat(stock)){
                Toast.makeText(this, "Cannot order more than stock",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            String[] order = {name, order_amount, cost, stock};

            orders.add(order);
            updateText();
        }

        if(v.getId() == R.id.remove_button){
            int index = findByName(name);
            if(index != -1) {
                orders.remove(findByName(name));
            }
            updateText();
        }

        if(v.getId() == R.id.finish_button){

            for(int i = 0; i < orders.size(); i++){
                String[] order = orders.get(i);
                Cursor c = dbHelper.getByName(db, order[0]);
                c.moveToFirst();
                Float new_stock = Float.parseFloat(c.getString(3)) - Float.parseFloat(order[1]);
                dbHelper.update(db, name, c.getString(2), new_stock.toString(),
                        c.getString(4), c.getString(0));
            }

            Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
            clear();

        }
    }

    private void updateText(){
        String text = "Current Order:\n";
        Double total = 0.0;
        for(int i = 0; i < orders.size(); i++){
            String[] order = orders.get(i);
            total += Float.parseFloat(order[2]) * Float.parseFloat(order[1]);
            text += "  " + order[0] + "(" + order[1] + ") $" + order[2] + "\n";
        }

        if(total != 0.0){
            text += String.format("TOTAL COST: $%.2f", total);
        }

        order_summary.setText(text);

    }

    private void clear(){
        orders = new ArrayList<>();
        order_summary.setText("Current Order:");
    }

    private int findByName(String name){
        for(int i = 0; i < orders.size(); i++){
            if(orders.get(i)[0].equals(name)) {
                return i;
            }
        }
        return -1;
    }

    private final class LoadDB extends AsyncTask<String, Void, Cursor> {
        // runs on the UI thread
        @Override protected void onPostExecute(Cursor data) {
            myAdapter = new SimpleCursorAdapter(getApplicationContext(),
                    R.layout.spinnerlist,
                    data,
                    new String[] { DatabaseOpenHelper.COL_1},
                    new int[] {R.id.sauce_item},0);
            mySpinner.setAdapter(myAdapter);
        }
        // runs on its own thread
        @Override
        protected Cursor doInBackground(String... args) {
            return dbHelper.getAllTasks(db);
        }
    }
}