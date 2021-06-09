package com.example.scaleapplication;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ConnectedThread extends Thread{
    private  BluetoothSocket mmSocket;
    private  InputStream mmInStream;
    private  OutputStream mmOutStream;
    public static final int RESPONSE_MESSAGE = 10;
    Handler uih;

    public ConnectedThread(BluetoothSocket socket, Handler uih){
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        this.uih = uih;
        Log.i("[THREAD-CT]","Creating thread");
        try{
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();

        } catch(IOException e) {
            Log.e("[THREAD-CT]","Error:"+ e.getMessage());
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;

        try {
            mmOutStream.flush();
        } catch (IOException e) {
            return;
        }
        Log.i("[THREAD-CT]","IO's obtained");

    }

    public void run(){
        //byte[] buffer = new byte[1024];
        //int bytes;

        Log.i("[THREAD-CT]","Starting thread");
        try {
            while (true) {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(mmInStream));
                    // bytes = mmInStream.read(buffer);
                    String resp = br.readLine();
                    //Transfer these data to the UI thread
                    Message msg = new Message();
                    msg.what = RESPONSE_MESSAGE;
                    msg.obj = resp;
                    uih.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
        catch (Exception e){
            e.getStackTrace();
        }
        Log.i("[THREAD-CT]","While loop ended");
    }

    public void write(String data){
        try{
            Log.i("[THREAD-CT]", "Writing bytes");
            byte[] bytes = data.getBytes();
            mmOutStream.write(bytes);

        }catch(IOException e){
            Log.i("[THREAD-CT]",e.getMessage());
        }

    }
    public void cancel() throws IOException {
        Log.i("[THREAD-CT]","requested to close socket");
        mmSocket.close();
        mmOutStream.close();
            
    }
}
