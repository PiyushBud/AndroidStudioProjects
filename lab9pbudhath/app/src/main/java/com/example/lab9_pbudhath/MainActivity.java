package com.example.lab9_pbudhath;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {

    final ChargingReciever charging = new ChargingReciever();
    final UnchargingReciever uncharging = new UnchargingReciever();

    public static String CHANNEL_ID = "Charge_chan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckBox check_noti = findViewById(R.id.check_notifications);
        check_noti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 0);
                    createNotificationChannel();

                    IntentFilter infCharg = new IntentFilter("android.intent.action.ACTION_POWER_CONNECTED");
                    IntentFilter infUnchar = new IntentFilter("android.intent.action.ACTION_POWER_DISCONNECTED");

                    registerReceiver(charging, infCharg);
                    registerReceiver(uncharging, infUnchar);
                }
                else {
                    unregisterReceiver(charging);
                    unregisterReceiver(uncharging);
                }
            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification channel";
            String description = "This is a test channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}