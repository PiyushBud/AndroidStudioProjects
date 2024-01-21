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

public class UpdateInventory extends AppCompatActivity {

    public static String PARAM_NAME = "PARAM_NAME";
    public static String PARAM_DISC = "PARAM_DESCRIPTION";
    public static String PARAM_COST = "PARAM_COST";
    public static String PARAM_STOCK = "PARAM_STOCK";
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_inventory);
        Intent intent = getIntent();

        edit_cost = findViewById(R.id.update_edit_cost);
        edit_cost.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edit_cost.setHint(intent.getStringExtra(PARAM_COST));

        edit_stock = findViewById(R.id.update_edit_stock);
        edit_stock.setInputType(InputType.TYPE_CLASS_NUMBER);
        edit_stock.setHint(intent.getStringExtra(PARAM_STOCK));

        edit_disc = findViewById(R.id.update_edit_disc);
        edit_disc.setHint(intent.getStringExtra(PARAM_DISC));

        ((TextView)findViewById(R.id.update_name))
                .setText(intent.getStringExtra(PARAM_NAME));

        item_id = intent.getStringExtra(PARAM_ITEM_ID);

    }

    public void onClickUpdate (View v){
        Intent intent = new Intent();

        String disc = edit_disc.getText().toString();
        String cost = edit_cost.getText().toString();
        String stock = edit_stock.getText().toString();

        if(disc.isEmpty()){
            disc = getIntent().getStringExtra(PARAM_DISC);
        }

        if(cost.isEmpty()){
            cost = getIntent().getStringExtra(PARAM_COST);
        }
        else if (Float.parseFloat(cost) < 0 || Float.parseFloat(cost) > 10000) {
            Toast.makeText(this,
                    "Invalid input for cost", Toast.LENGTH_SHORT).show();
            return;
        }

        if(stock.isEmpty()){
            stock = getIntent().getStringExtra(PARAM_STOCK);
        }
        else if (Integer.parseInt(stock) < 0 || Integer.parseInt(stock) > 1000000) {
            Toast.makeText(this,
                    "Invalid input for cost", Toast.LENGTH_SHORT).show();
            return;
        }

        intent.putExtra(RESULT_NAME, getIntent().getStringExtra(PARAM_NAME));
        intent.putExtra(RESULT_DISC, disc);
        intent.putExtra(RESULT_COST, cost);
        intent.putExtra(RESULT_STOCK, stock);
        intent.putExtra(RESULT_ITEM_ID, item_id);
        intent.putExtra(CurrentInventory.RESULT_OPP, CurrentInventory.OPP_UPDATE);
        setResult(Activity.RESULT_OK, intent);
        finish();

    }
}