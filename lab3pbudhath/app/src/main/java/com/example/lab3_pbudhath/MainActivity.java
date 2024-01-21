package com.example.lab3_pbudhath;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText elem;
    ListView listView;
    ArrayAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.mylist);

        // Param1 - context
        // Param2 - layout for the row

        myAdapter = new ArrayAdapter<String>(this, R.layout.line);

        listView.setAdapter(myAdapter);

        myAdapter.add("Lab 3 Piyush Bud\n Fall 2023");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doDone(view, position);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                                    alertView("Single Item Deletion",position);

                                                    return true;
                                                }
                                            }
        );
        elem =  (EditText)findViewById(R.id.input);

    }

    public void clearEdit(View v) {
        myAdapter.clear();
    }

    public void addElem(View v) {
        String input = elem.getText().toString();
        myAdapter.add(input);
        Toast.makeText(getApplicationContext(), "Adding " + input, Toast.LENGTH_SHORT).show();
        elem.setText("");

    }

    public void doDone(View v, int i){
        String text = ((TextView) v).getText().toString();
        myAdapter.remove(myAdapter.getItem(i));
        if(text.contains("Done:")){
            myAdapter.insert(text.substring(5), 0);
        }
        else{
            myAdapter.add("Done:" + text);
        }
    }

    public void deleteDone(View v){
        for(int i = 0; i < myAdapter.getCount(); i++){
            if (myAdapter.getItem(i).toString().contains("Done:")){
                myAdapter.remove(myAdapter.getItem(i));
                i--;
            }
        }
    }


    private void alertView(String message, final int position ) {
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
                        myAdapter.remove(myAdapter.getItem(position));
                    }
                }).show();
    }
}