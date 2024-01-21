package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private int count1 = 0;
    private int count2 = 0;

    TextView output;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        output = findViewById(R.id.output);
    }

    public void onButtonClick(View v) {
        TextView htv;
        int value;
        if (v.getId() == R.id.button1) {
            count1++;
        }
        if(v.getId() == R.id.button2){
            count2++;
        }
        output.setText("Counter 1: " + count1 + "\nCounter 2: " + count2);
    }

}