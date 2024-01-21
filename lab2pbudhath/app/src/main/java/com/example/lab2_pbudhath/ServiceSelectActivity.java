package com.example.lab2_pbudhath;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ServiceSelectActivity extends AppCompatActivity {

    public final static String PARAM1 = "com.example.lab2_pbudhath.P1";
    public final static String PARAM2 = "com.example.lab2_pbudhath.P2";
    public final static String PARAM3 = "com.example.lab2_pbudhath.P3";

    private double exc_quality = -1, avg_quality = -1, bavg_quality = -1;
    private double o_exc_quality = 0, o_avg_quality = 0, o_bavg_quality = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_select);
        // Get parameters
        Intent intent = getIntent();
        o_exc_quality = intent.getDoubleExtra(this.PARAM1, 0.25);
        o_avg_quality = intent.getDoubleExtra(this.PARAM2, 0.20);
        o_bavg_quality = intent.getDoubleExtra(this.PARAM3, 0.15);
        EditText Exc_Edit = findViewById(R.id.Exc_Edit);
        EditText Avg_Edit = findViewById(R.id.Avg_Edit);
        EditText Bavg_Edit = findViewById(R.id.Bavg_Edit);

        Exc_Edit.setHint(Double.toString(o_exc_quality*100));
        Avg_Edit.setHint(Double.toString(o_avg_quality*100));
        Bavg_Edit.setHint(Double.toString(o_bavg_quality*100));

        TextWatcher tw = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s == Exc_Edit.getEditableText()) {
                    if (Exc_Edit.getText().length() != 0) {
                        exc_quality = Float.parseFloat(String.valueOf(Exc_Edit.getText()))/100;

                        if(exc_quality < 0){
                            Toast.makeText(getApplicationContext(), "Percentage must be a positive integer", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else if (s == Avg_Edit.getEditableText()) {
                    if (Avg_Edit.getText().length() != 0) {
                        avg_quality = Float.parseFloat(String.valueOf(Avg_Edit.getText()))/100;

                        if(avg_quality < 0){
                            Toast.makeText(getApplicationContext(), "Percentage must be a positive integer",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else if (s == Bavg_Edit.getEditableText()) {
                    if (Bavg_Edit.getText().length() != 0) {
                        bavg_quality = Float.parseFloat(String.valueOf(Bavg_Edit.getText()))/100;

                        if(bavg_quality < 0){
                            Toast.makeText(getApplicationContext(), "Percentage must be a positive integer",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            public void beforeTextChanged (CharSequence s,int start, int count, int after){
            }
            public void onTextChanged (CharSequence s,int start, int before, int count){
            }

        };
        Exc_Edit.addTextChangedListener(tw);
        Avg_Edit.addTextChangedListener(tw);
        Bavg_Edit.addTextChangedListener(tw);

    }

    public void onClick (View v){
        Intent intent = new Intent();
        if (v.getId() == R.id.Keep_Button){
            if(exc_quality == -1){
                intent.putExtra(MainActivity.RESULT1, o_exc_quality);
            }
            else {
                intent.putExtra(MainActivity.RESULT1, exc_quality);
            }
            if(avg_quality == -1){
                intent.putExtra(MainActivity.RESULT2, o_avg_quality);
            }
            else {
                intent.putExtra(MainActivity.RESULT2, avg_quality);
            }
            if(bavg_quality == -1){
                intent.putExtra(MainActivity.RESULT3, o_bavg_quality);
            }
            else {
                intent.putExtra(MainActivity.RESULT3, bavg_quality);
            }

        }
        if (v.getId() == R.id.Cancel_Button){
            intent.putExtra(MainActivity.RESULT1, o_exc_quality);
            intent.putExtra(MainActivity.RESULT2, o_avg_quality);
            intent.putExtra(MainActivity.RESULT3, o_bavg_quality);
        }
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}