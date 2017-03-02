package com.majid.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.majid.bluetooth.Fragment.DeviceListFragment;
import com.majid.bluetooth.Fragment.TransferFragment;
import com.majid.bluetooth.Interface.OnFragmentInteractionListener;
import com.majid.bluetooth.Model.DeviceItem;
import com.majid.bluetooth.Utils.Constants;

/*
Created by majid on 2/9/17.
*/
public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private DeviceListFragment mDeviceListFragment;
    private TransferFragment transferFragment;

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
            Log.d("error", "Error: " + e.getMessage() + "...");
        }

        int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

    }

    @Override
    public void onFragmentInteraction(DeviceItem device) {
        Toast.makeText(this , device.getDeviceName() ,  Toast.LENGTH_LONG).show();

        BluetoothDevice selectedDevice = btAdapter.getRemoteDevice(device.getAddress());

        ConnectThread connection = new ConnectThread(selectedDevice , Constants.MY_UUID);
        connection.start();

        if( connection.connect()){
            try{
                FragmentManager fragmentManager = getSupportFragmentManager();

                transferFragment = TransferFragment.newInstance(btAdapter , connection.getbTSocket());
                fragmentManager.beginTransaction().replace(R.id.container, transferFragment).commit();

            }catch (Exception e){
                Log.e("error", "Error: " + e.getMessage() + "...");
            }
        }
    }

    public void displayFragment(int frag){

    }
}
