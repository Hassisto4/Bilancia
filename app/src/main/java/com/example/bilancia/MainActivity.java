package com.example.bilancia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.createbest.sdk.a16.internal.BleDataReceiver;
import com.createbest.sdk.ble_io.BlePresenter;
import com.createbest.sdk.ble_io.FindDeviceCallback;
import com.createbest.sdk.ble_io.LastChargeCallback;
import com.createbest.sdk.ble_io.StepDataCallback;
import com.createbest.sdk.ble_io.SyncTimeCallback;
import com.createbest.sdk.ble_io.TestCallback;
import com.createbest.sdk.ble_io.WarnCallback;
import com.createbest.sdk.blesdk.BleConnectionObserver;
import com.createbest.sdk.blesdk.OperationMonitor;

public class MainActivity extends AppCompatActivity {

    //prova

    String MAC = "60:64:05:94:60:26";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_STORAGE,
                1
        );

        final driver d1 = new driver(this,MAC);

        d1.connect();

        Button button = findViewById(R.id.button);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                d1.getSomething();

            }
        });



    }

}

