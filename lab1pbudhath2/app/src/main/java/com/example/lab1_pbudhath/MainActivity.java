package com.example.lab1_pbudhath;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    double bill = 0.0;
    double service_quality = 0.0;
    int num_people = 1;
    boolean radioChecked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText Bill_Edit = findViewById(R.id.Bill_Edit);
        EditText People_Edit = findViewById(R.id.People_Edit);
        TextWatcher twB = new TextWatcher(){
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Bill_Edit.getText().length() != 0) {
                    bill = Float.parseFloat(String.valueOf(Bill_Edit.getText()));
                    if(bill > 10000){
                        Toast.makeText(getApplicationContext(), "Max is $10,000", Toast.LENGTH_SHORT).show();
                        bill = 0;
                        return;
                    }
                    if(bill < 0){
                        Toast.makeText(getApplicationContext(), "Invalid Entry", Toast.LENGTH_SHORT).show();
                        bill = 0;
                        return;
                    }
                    if(radioChecked){
                        updateText();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Select a service quality", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    bill = 0.0;
                    ((TextView)findViewById(R.id.Tip_Total_Val)).setText("");
                    ((TextView)findViewById(R.id.Total_Val)).setText("");
                    ((TextView)findViewById(R.id.Total_Per_Val)).setText("");
                }
            }
        };
        Bill_Edit.addTextChangedListener(twB);
        Bill_Edit.setText("");

        TextWatcher twP = new TextWatcher(){
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (People_Edit.getText().length() != 0) {
                    num_people = Integer.parseInt(String.valueOf(People_Edit.getText()));
                    if(num_people > 10){
                        Toast.makeText(getApplicationContext(), "Max 10 people", Toast.LENGTH_SHORT).show();
                        num_people = 1;
                        return;
                    }
                    if(num_people < 1){
                        Toast.makeText(getApplicationContext(), "Invalid Entry", Toast.LENGTH_SHORT).show();
                        num_people = 1;
                        return;
                    }
                    if(radioChecked && bill != 0){
                        updateText();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Select a service quality", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    num_people = 1;
                    ((TextView)findViewById(R.id.Tip_Total_Val)).setText("");
                    ((TextView)findViewById(R.id.Total_Val)).setText("");
                    ((TextView)findViewById(R.id.Total_Per_Val)).setText("");
                }
            }
        };
        People_Edit.addTextChangedListener(twP);
        People_Edit.setText("");

    }

    public void myOnClick(View v){
        if(v.getId() == R.id.Clear_Button){
            ((EditText)findViewById(R.id.Bill_Edit)).setText("");
            ((EditText)findViewById(R.id.People_Edit)).setText("");
            ((RadioButton)findViewById(R.id.Radio_Exc)).setChecked(false);
            ((RadioButton)findViewById(R.id.Radio_Avg)).setChecked(false);
            ((RadioButton)findViewById(R.id.Radio_Bavg)).setChecked(false);
            ((TextView)findViewById(R.id.Tip_Total_Val)).setText("");
            ((TextView)findViewById(R.id.Total_Val)).setText("");
            ((TextView)findViewById(R.id.Total_Per_Val)).setText("");
        }
    }
    public void onServiceRadioClick(View v){
        boolean check = ((RadioButton)v).isChecked();
        radioChecked = check;
        if(!check){
            service_quality = 0.0;
            return;
        }
        if(v.getId() == R.id.Radio_Exc){
            service_quality = 0.25;
        }

        if(v.getId() == R.id.Radio_Avg){
            service_quality = 0.20;
        }

        if(v.getId() == R.id.Radio_Bavg){
            service_quality = 0.15;
        }

        if(bill == 0){
            Toast.makeText(getApplicationContext(), "Enter bill amount", Toast.LENGTH_SHORT).show();
        }
        else{
            updateText();
        }
    }

    private void updateText(){

        double tip_total =  bill * service_quality;
        double total = bill + tip_total;
        double total_per = total/num_people;

        ((TextView)findViewById(R.id.Tip_Total_Val)).setText(String.format("%.2f%n", tip_total));
        ((TextView)findViewById(R.id.Total_Val)).setText(String.format("%.2f%n", total));
        ((TextView)findViewById(R.id.Total_Per_Val)).setText(String.format("%.2f%n", total_per));
    }
}