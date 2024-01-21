package com.example.project2_pbudhath;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class AddToInventory extends AppCompatActivity {
    public static String PARAM_ITEM_ID = "PARAM_ITEM_ID";

    public static String RESULT_NAME = "RESULT_NAME";
    public static String RESULT_DISC = "RESULT_DISC";
    public static String RESULT_COST = "RESULT_COST";
    public static String RESULT_STOCK = "RESULT_STOCK";
    public static String RESULT_ITEM_ID = "RESULT_ITEM_ID";

    private String item_id;
    private EditText edit_disc;
    private EditText edit_cost;
    private EditText edit_stock;
    private EditText edit_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_inventory);
        Intent intent = getIntent();

        edit_cost = findViewById(R.id.add_edit_cost);
        edit_cost.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        edit_stock = findViewById(R.id.add_edit_stock);
        edit_stock.setInputType(InputType.TYPE_CLASS_NUMBER);

        edit_disc = findViewById(R.id.add_edit_disc);

        edit_name = findViewById((R.id.add_edit_name));

        item_id = intent.getStringExtra(PARAM_ITEM_ID);

    }

    public void onClickAdd (View v){
        Intent intent = new Intent();

        String disc = edit_disc.getText().toString();
        String cost = edit_cost.getText().toString();
        String stock = edit_stock.getText().toString();
        String name = edit_name.getText().toString();

        if(name.isEmpty()){
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(disc.isEmpty()){
            disc = "Spicy Sauce";
        }

        if(cost.isEmpty()){
            cost = "0.0";
        }
        else if (Float.parseFloat(cost) < 0 || Float.parseFloat(cost) > 10000) {
            Toast.makeText(this,
                    "Invalid input for cost", Toast.LENGTH_SHORT).show();
            return;
        }

        if(stock.isEmpty()){
            stock = "0";
        }
        else if (Float.parseFloat(stock) < 0 || Float.parseFloat(stock) > 1000000) {
            Toast.makeText(this,
                    "Invalid input for cost", Toast.LENGTH_SHORT).show();
            return;
        }

        intent.putExtra(RESULT_NAME, name);
        intent.putExtra(RESULT_DISC, disc);
        intent.putExtra(RESULT_COST, cost);
        intent.putExtra(RESULT_STOCK, stock);
        intent.putExtra(RESULT_ITEM_ID, item_id);

        intent.putExtra(CurrentInventory.RESULT_OPP, CurrentInventory.OPP_ADD);

        setResult(Activity.RESULT_OK, intent);
        finish();

    }
}