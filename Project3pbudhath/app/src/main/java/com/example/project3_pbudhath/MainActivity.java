package com.example.project3_pbudhath;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button play = findViewById(R.id.Button_Play);
        Button offline = findViewById(R.id.Button_Offline);
        Button info = findViewById(R.id.Button_Info);

        offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra(GameActivity.GAME_MODE, GameActivity.MODE_OFFLINE);
                startActivity(intent);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ConnectActivity.class));
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("How to use");
                alert.setIcon(R.drawable.ic_launcher_background);
                alert.setMessage("Select Play Offline for offline play. Select Play for online play" +
                        "with Wifi Direct (This does not work at the moment because I cannot acquire" +
                        "2 android devices to develop with and my laptop crashes when running two android" +
                        "emulators at once. Currently, I can discover wifi direct peers)");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert.show();
            }
        });
    }
}