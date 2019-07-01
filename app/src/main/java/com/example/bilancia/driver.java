package com.example.bilancia;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.createbest.sdk.a16.internal.BleDataReceiver;
import com.createbest.sdk.ble_io.BlePresenter;
import com.createbest.sdk.ble_io.DebugCallback;
import com.createbest.sdk.ble_io.TestCallback;
import com.createbest.sdk.ble_io.WarnCallback;
import com.createbest.sdk.blesdk.BleConnectionObserver;
import com.createbest.sdk.blesdk.OperationMonitor;

public class driver implements BleConnectionObserver {

    BleDataReceiver bleDataReceiver;
    Context myctx;
    String MAC;

    WarnCallback warn = new WarnCallback() {
        @Override
        public void onAlarmClockSetSuccess() {

        }

        @Override
        public void onAlarmclockWarn(long l) {

        }

        @Override
        public void onNeedStopAlarmClock() {

        }

        @Override
        public void onNeedStopMedicineWarn() {

        }

        @Override
        public void onMedicineWarn(int i, int i1, int i2, int i3, int i4, int i5) {

        }

        @Override
        public void onNeedStopLongTimeSitWarn() {

        }

        @Override
        public void onLongTimeSitWarn(int i) {

        }

        @Override
        public void onSetLongTimeSitSuccess() {

        }

        @Override
        public void onSetMedicineSuccess() {

        }

        @Override
        public void onGetBattaryQuantity(boolean b, int i) {

        }
    };

    int connected = 0;
    int connectionFailures = 0;
    BlePresenter mBlePresenter;

    public driver(Context context, String MAC){

        myctx = context;
        this.MAC = MAC;

        OperationMonitor om = new OperationMonitor() {
            @Override
            public void onOperationFailedByStateOFF() {

                Log.v("BandWrapper", "onOperationFailedByStateOFF");

            }

            @Override
            public void onOperationFailedByUnsupportBle() {

                Log.v("BandWrapper", "onOperationFailedByUnsupportBle");

            }

            @Override
            public void onOperationFailedDeviceDisconnected() {

                Log.v("BandWrapper", "onOperationFailedDeviceDisconnected");

            }
        };

        mBlePresenter = new BlePresenter(context, om, warn);
        mBlePresenter.registerBleConnectionObserver(this);
        bleDataReceiver = new BleDataReceiver(mBlePresenter);
        connect();

    }

    public void connect() {
        Log.v("BandWrapper", "Connecting");

        /**
         * Lo scan risolve l'assenza di dispositivo.
         * Rimane da correggere l'errore sulla prima connect.
         * una sorta di wait(Discovery_result)
         */

        try{

            final BluetoothAdapter mBTA = BluetoothAdapter.getDefaultAdapter();
            final BroadcastReceiver rec = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    String action = intent.getAction();
                    if(BluetoothDevice.ACTION_FOUND.equals(action)){

                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        Log.v("Nome dispositivo",device.getName().toString());
                        Log.v("MAC sconosciuti trovati",device.getAddress().toString());

                        if(device.getAddress().equals(MAC)) {
                            mBTA.cancelDiscovery();
                            mBlePresenter.connect(MAC);
                        }

                    }
                }

            };

            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            if(mBTA.isDiscovering()){
                myctx.unregisterReceiver(rec);
            }
            myctx.registerReceiver(rec,filter);
            mBTA.startDiscovery();

            Log.v("Ricerca avviata","ok");


        }catch (Exception e){

            e.printStackTrace();

        }

        //mBlePresenter.connect(address);

    }

    public driver() {
        super();
    }

    @Override
    public void onConnectFaild(BluetoothDevice bluetoothDevice) {

    }

    @Override
    public void onConnectSuccess(BluetoothDevice bluetoothDevice) {

    }

    @Override
    public void onDisconnected(BluetoothDevice bluetoothDevice) {

    }

    public void getSomething(){

        mBlePresenter.setDebugCallback(new DebugCallback() {
            @Override
            public void onReceived(String s) {

                Log.v("Ricevuto",s);

                s.substring(13,s.length()-2);

                Log.v("Pesata","Peso: " + s.substring(13,s.length()-2) + " kg.");

            }
        });


    }
}
