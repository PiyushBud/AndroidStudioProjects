package com.example.project2_pbudhath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void myOnClick(View v){
        if (v.getId() == R.id.Manage_Button){
            startActivity(new Intent(this, CurrentInventory.class));
        }
        if(v.getId() == R.id.Handle_Button){
            startActivity(new Intent(this, HandleOrder.class));
        }
    }
}