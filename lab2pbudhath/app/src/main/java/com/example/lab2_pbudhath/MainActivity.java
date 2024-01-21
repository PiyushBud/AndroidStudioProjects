package com.example.lab2_pbudhath;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private double bill = 0.0;
    private double service_quality = 0.0;
    private double exc_quality = 0.25, avg_quality = 0.2, bavg_quality = 0.15;
    private int num_people = 1;
    private boolean radio_checked = false;

    public final static String RESULT1 = "com.example.lab2_pbudhath.R1";
    public final static String RESULT2 = "com.example.lab2_pbudhath.R2";
    public final static String RESULT3 = "com.example.lab2_pbudhath.R3";

    public final static String STATE_EXC_QUAL = "exc_quality";
    public final static String STATE_AVG_QUAL = "avg_quality";
    public final static String STATE_BAVG_QUAL = "bavg_quality";
    public final static String STATE_RADIO_CHECK = "radio_checked";
    public final static String STATE_NUM_PEOPLE = "num_people";
    public final static String STATE_SERVICE_QUAL = "service_quality";
    public final static String STATE_BILL = "bill";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null){
            exc_quality = savedInstanceState.getDouble(STATE_EXC_QUAL);
            avg_quality = savedInstanceState.getDouble(STATE_AVG_QUAL);
            bavg_quality = savedInstanceState.getDouble(STATE_BAVG_QUAL);

            service_quality = savedInstanceState.getDouble(STATE_SERVICE_QUAL);
            bill = savedInstanceState.getDouble(STATE_BILL);
            num_people = savedInstanceState.getInt(STATE_NUM_PEOPLE);
            radio_checked = savedInstanceState.getBoolean(STATE_RADIO_CHECK);
            
            updateServiceQuality();
            updateText();
        }
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
                    if(radio_checked){
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
                    if(radio_checked && bill != 0){
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
            radio_checked = false;
            bill = 0.0;
        }

        if(v.getId() == R.id.Update_Button){
            Intent intent = new Intent(this, ServiceSelectActivity.class);
            intent.putExtra(ServiceSelectActivity.PARAM1, exc_quality);
            intent.putExtra(ServiceSelectActivity.PARAM2, avg_quality);
            intent.putExtra(ServiceSelectActivity.PARAM3, bavg_quality);
            someActivityResultLauncher.launch(intent);

        }
    }
    public void onServiceRadioClick(View v){
        boolean check = ((RadioButton)v).isChecked();
        radio_checked = check;
        if(!check){
            service_quality = 0.0;
            return;
        }
        if(v.getId() == R.id.Radio_Exc){
            service_quality = exc_quality;
        }

        if(v.getId() == R.id.Radio_Avg){
            service_quality = avg_quality;
        }

        if(v.getId() == R.id.Radio_Bavg){
            service_quality = bavg_quality;
        }

        if(bill == 0){
            Toast.makeText(getApplicationContext(), "Enter bill amount", Toast.LENGTH_SHORT).show();
        }
        else{
            updateText();
        }
    }

    private void updateText(){
        
        if(!radio_checked){
            return;
        }
        
        if(bill == 0.0){
            return;
        }

        double tip_total =  bill * service_quality;
        double total = bill + tip_total;
        double total_per = total/num_people;

        ((TextView)findViewById(R.id.Tip_Total_Val)).setText(String.format("%.2f%n", tip_total));
        ((TextView)findViewById(R.id.Total_Val)).setText(String.format("%.2f%n", total));
        ((TextView)findViewById(R.id.Total_Per_Val)).setText(String.format("%.2f%n", total_per));
    }

    private void updateServiceQuality(){
        ((TextView)findViewById(R.id.Radio_Exc)).setText(String.format("Excellent (%.2f%%)", exc_quality*100));
        ((TextView)findViewById(R.id.Radio_Avg)).setText(String.format("Average (%.2f%%)", avg_quality*100));
        ((TextView)findViewById(R.id.Radio_Bavg)).setText(String.format("Below Average (%.2f%%)", bavg_quality*100));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putDouble(STATE_EXC_QUAL, exc_quality);
        savedInstanceState.putDouble(STATE_AVG_QUAL, avg_quality);
        savedInstanceState.putDouble(STATE_BAVG_QUAL, bavg_quality);
        savedInstanceState.putDouble(STATE_BILL, bill);
        savedInstanceState.putDouble(STATE_SERVICE_QUAL, service_quality);

        savedInstanceState.putInt(STATE_NUM_PEOPLE, num_people);
        savedInstanceState.putBoolean(STATE_RADIO_CHECK, radio_checked);


        super.onSaveInstanceState(savedInstanceState);
    }
    ActivityResultLauncher<Intent>
            someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        exc_quality = data.getExtras().getDouble(RESULT1);
                        avg_quality = data.getExtras().getDouble(RESULT2);
                        bavg_quality = data.getExtras().getDouble(RESULT3);
                        Toast.makeText(getApplicationContext(), String.format("%f %f %f", exc_quality, avg_quality, bavg_quality), Toast.LENGTH_LONG).show();
                        updateServiceQuality();
                    }
                }
            });

}