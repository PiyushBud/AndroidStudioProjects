package com.example.lab8_pbudhath;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private AppleView mAppleView;



    /**
     * Called when Activity is first created. Turns off the title bar, sets up
     * the content views, and fires up the SnakeView.
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mAppleView = (AppleView) findViewById(R.id.apples);
        mAppleView.setTextView((TextView) findViewById(R.id.text));


    }

}