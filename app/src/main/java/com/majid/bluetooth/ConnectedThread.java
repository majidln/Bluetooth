package com.majid.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.majid.bluetooth.Utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by majid on 2/9/17.
 */
public class ConnectedThread extends Thread {
//
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    private Handler mHandler;

    public ConnectedThread(BluetoothSocket socket , Handler h) {
        mHandler = h;
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e("error", "Error: " + e.getMessage() + "...");
        }
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public byte[] buffer = new byte[1024];
    public int bytes;
    public String str2;

    private static final String TAG = "BluetoothService";

    public void run() {

        // Keep listening to the InputStream while connected
        while (true) {
            try {
                if (mmInStream.available()>0) {

                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    String r = String.valueOf(bytes);
                    String r2 =  new String(buffer) ;

                    // Send the obtained bytes to the UI Activity
                    mHandler.obtainMessage(Constants.RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();
                }
                else SystemClock.sleep(200);


            } catch (IOException e) {
                Log.e(TAG, "disconnected", e);
                //connectionLost();
                // Start the service over to restart listening mode
                //Bluetooth.this.start();
                break;
            }

        }

//        byte[] buffer = new byte[256];
//        int bytes;
//
//        // Keep looping to listen for received messages
//        while (true) {
//            try {
//                bytes = mmInStream.read(buffer);            //read bytes from input buffer
//                String readMessage = new String(buffer, 0, bytes);
//                Log.d("receive" , readMessage);
//                // Send the obtained bytes to the UI Activity via handler
//                mHandler.obtainMessage(Constants.RECIEVE_MESSAGE, bytes, -1, readMessage).sendToTarget();
//            } catch (IOException e) {
//                Log.d("receive", "run ...Error on run: " + e.getMessage() + "...");
//                Log.d("error", "...Error data send: " + e.getMessage() + "...");
//                break;
//            }
//        }

//        byte[] buffer = new byte[1024];
//        int begin = 0;
//        int bytes = 0;
//        while (true) {
//            try {
//                bytes += mmInStream.read(buffer, bytes, buffer.length - bytes);
//                for(int i = begin; i < bytes; i++) {
//                    if(buffer[i] == "#".getBytes()[0]) {
//                        mHandler.obtainMessage(1, begin, i, buffer).sendToTarget();
//                        begin = i + 1;
//                        if(i == bytes - 1) {
//                            bytes = 0;
//                            begin = 0;
//                        }
//                    }
//                }
//            } catch (IOException e) {
//                Log.d("error", "...Error on run: " + e.getMessage() + "...");
//                break;
//            }
//        }
    }
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            Log.e("error", "Error: " + e.getMessage() + "...");
        }
    }
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e("error", "Error: " + e.getMessage() + "...");
        }
    }

}
