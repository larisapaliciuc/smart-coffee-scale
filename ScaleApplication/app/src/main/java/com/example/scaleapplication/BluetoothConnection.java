package com.example.scaleapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class BluetoothConnection {
    private String TAG = "ScaleBluetoothSerial";
    private BluetoothDevice mDevice;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private boolean stopWorker;
    private int readBufferPosition;
    private byte[] readBuffer;
    private static BluetoothConnection connectionObj;
    public String BlWeight;
    public static ConnectedThread mConnectedThread;
    public static BluetoothSocket btSocket;
    public static  Handler bluetoothIn;
    String deviceHardwareAddress;
    public static  UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    public BluetoothConnection() {
         }

     public static BluetoothConnection getInstance() {
        if(connectionObj == null) {
            connectionObj = new BluetoothConnection();
        }
        return connectionObj;
    }
    public void initiateBluetoothProcess(){
        if(!bluetoothAdapter.isEnabled()){
            bluetoothAdapter.enable();
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().contains("HC-05")) {
                    deviceHardwareAddress = device.getAddress(); // MAC address
                }
            }
        }
        //Get MAC address from DeviceListActivity via intent
        //create device and set the MAC address
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceHardwareAddress);
        try {
            btSocket =device.createRfcommSocketToServiceRecord(BTMODULEUUID);
            btSocket.connect();
            Log.i(TAG, "HC-05 connected");
        } catch (IOException e) {
            Log.i(TAG,"Socket creation failed");
            e.printStackTrace();
        }
        // Establish the Bluetooth socket connection.
        mConnectedThread = new ConnectedThread(btSocket,bluetoothIn);
        mConnectedThread.start();

    }

    public void cancelConnection(){
        mConnectedThread.cancel();
    }

    public void write(String text){
        mConnectedThread.write(text);
    }
    public void setHandler(Handler handler) {
        bluetoothIn = handler;
    }
}
