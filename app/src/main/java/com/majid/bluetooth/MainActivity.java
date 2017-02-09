package com.majid.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.majid.bluetooth.Fragment.DeviceListFragment;
/*
Created by majid on 2/9/17.
*/
public class MainActivity extends AppCompatActivity implements DeviceListFragment.OnFragmentInteractionListener {

    private DeviceListFragment mDeviceListFragment;
    BluetoothAdapter btAdapter ;
    public static int REQUEST_BLUETOOTH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        // Phone does not support Bluetooth so let the user know and exit.
        if (btAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        if (!btAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, REQUEST_BLUETOOTH);
        }

        try{
            FragmentManager fragmentManager = getSupportFragmentManager();

            mDeviceListFragment = DeviceListFragment.newInstance(btAdapter);
            fragmentManager.beginTransaction().replace(R.id.container, mDeviceListFragment).commit();

        }catch (Exception e){
            Log.e("Error" , e.getMessage());
        }


    }

    @Override
    public void onFragmentInteraction(String id) {

    }
}
