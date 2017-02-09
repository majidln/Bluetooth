package com.majid.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.majid.bluetooth.Fragment.DeviceListFragment;
import com.majid.bluetooth.Interface.OnFragmentInteractionListener;
import com.majid.bluetooth.Model.DeviceItem;

import java.lang.reflect.Method;
import java.util.UUID;

/*
Created by majid on 2/9/17.
*/
public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

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
    public void onFragmentInteraction(DeviceItem device) {
        Toast.makeText(this , device.getDeviceName() ,  Toast.LENGTH_LONG).show();

        BluetoothDevice selectedDevice = btAdapter.getRemoteDevice(device.getAddress());

        UUID uuid = getUIUD();
        ConnectThread connection = new ConnectThread(selectedDevice , MY_UUID);
        connection.start();
        connection.connect();

    }
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    public UUID getUIUD()  {
        UUID res = null;
        try{
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

            Method getUuidsMethod = BluetoothAdapter.class.getDeclaredMethod("getUuids", null);

            ParcelUuid[] uuids = (ParcelUuid[]) getUuidsMethod.invoke(adapter, null);

            if(uuids.length > 0 ){
                res = uuids[0].getUuid();
            }
        }catch (Exception e){
            Log.d("System out", "android_id : ");
        }
        return res;


//        String android_id = Settings.Secure.getString(getApplicationContext()
//                .getContentResolver(), Settings.Secure.ANDROID_ID);
//        Log.i("System out", "android_id : " + android_id);
//
//        final TelephonyManager tm = (TelephonyManager) getBaseContext()
//                .getSystemService(Context.TELEPHONY_SERVICE);
//
//        final String tmDevice, tmSerial, androidId;
//        tmDevice = "" + tm.getDeviceId();
//        Log.i("System out", "tmDevice : " + tmDevice);
//        tmSerial = "" + tm.getSimSerialNumber();
//        Log.i("System out", "tmSerial : " + tmSerial);
//        androidId = ""
//                + android.provider.Settings.Secure.getString(
//                getContentResolver(),
//                android.provider.Settings.Secure.ANDROID_ID);
//
//        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice
//                .hashCode() << 32)
//                | tmSerial.hashCode());
//
//        return deviceUuid;
    }
}
