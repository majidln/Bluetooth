package com.majid.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by User on 6/3/2015.
 */
public class ConnectThread extends Thread {

    private final BluetoothDevice bTDevice;
    private final BluetoothSocket bTSocket;

    public ConnectThread(BluetoothDevice bTDevice, UUID UUID) {
        BluetoothSocket tmp = null;
        this.bTDevice = bTDevice;

        try {
            tmp = this.bTDevice.createRfcommSocketToServiceRecord(UUID);
            Method m = bTDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
            tmp = (BluetoothSocket) m.invoke(bTDevice, 1);
        }
        catch (Exception e) {
            Log.e("error", "Error: " + e.getMessage() + "...");
            Log.e("CONNECTTHREAD", "Could not start listening for RFCOMM");
        }
        bTSocket = tmp;
    }

    public boolean connect() {

        try {
            bTSocket.connect();
        } catch(IOException e) {
            Log.e("error", "Error: " + e.getMessage() + "...");
            Log.e("CONNECTTHREAD","Could not connect: " + e.toString());
            try {
                bTSocket.close();
            } catch(IOException close) {
                Log.e("error", "Error: " + e.getMessage() + "...");
                Log.e("CONNECTTHREAD", "Could not close connection:" + e.toString());
                return false;
            }
        }
        return true;
    }

    public boolean cancel() {
        try {
            bTSocket.close();
        } catch(IOException e) {
            Log.e("error", "Error: " + e.getMessage() + "...");
            return false;
        }
        return true;
    }

    public BluetoothDevice getbTDevice() {
        return bTDevice;
    }

    public BluetoothSocket getbTSocket() {
        return bTSocket;
    }



}
