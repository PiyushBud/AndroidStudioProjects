package com.example.project3_pbudhath;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ConnectActivity extends AppCompatActivity {


    private IntentFilter intentFilter;
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private myReceiver receiver;
    private String[] deviceNameList;
    private ArrayList<WifiP2pDevice> deviceList;
    private ListView mylist;

    private WifiP2pManager.PeerListListener myPeerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            deviceList = new ArrayList<>(wifiP2pDeviceList.getDeviceList());
            deviceNameList = new String[deviceList.size()];
            for(int i = 0; i < deviceList.size(); i++){
                deviceNameList[i] = deviceList.get(i).deviceName;
            }
            updateListView();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        receiver = new myReceiver();

        mylist = findViewById(R.id.List_P2P);
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        Button search = findViewById(R.id.Button_Search);
        search.setOnClickListener(view -> {
            connection();
        });

        Button back = findViewById(R.id.Button_Back);
        back.setOnClickListener(view -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void connection(){
        if (ContextCompat.checkSelfPermission(
                ConnectActivity.this, android.Manifest.permission.NEARBY_WIFI_DEVICES) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(
                    Manifest.permission.NEARBY_WIFI_DEVICES);
        }
        manager.discoverPeers(channel, yuh);
        manager.requestPeers(channel, myPeerListListener);
    }

    private void updateListView(){
        mylist.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, deviceNameList));
    }

    public class myReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    Toast.makeText(getApplicationContext(), "ENABLED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "NOT ENABLED", Toast.LENGTH_SHORT).show();
                }
            } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                if (manager != null) {
                    manager.requestPeers(channel, myPeerListListener);
                }
            } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                // Respond to new connection or disconnections
            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                // Respond to this device's wifi state changing
            }
        }
    }

    private WifiP2pManager.ActionListener yuh = new WifiP2pManager.ActionListener() {
        @Override
        public void onSuccess() {
            Toast.makeText(ConnectActivity.this, "successful discovery", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(int reasonCode) {
            switch(reasonCode){
                case WifiP2pManager.P2P_UNSUPPORTED:
                    Toast.makeText(ConnectActivity.this, "FAIL, Unsupported", Toast.LENGTH_SHORT).show();
                    break;
                case WifiP2pManager.ERROR:
                    Toast.makeText(ConnectActivity.this, "FAIL, Error", Toast.LENGTH_SHORT).show();
                    break;
                case WifiP2pManager.BUSY:
                    Toast.makeText(ConnectActivity.this, "FAIL, Busy", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    connection();
                }
            });
}