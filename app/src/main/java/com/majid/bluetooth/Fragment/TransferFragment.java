package com.majid.bluetooth.Fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.majid.bluetooth.ConnectedThread;
import com.majid.bluetooth.R;
import com.majid.bluetooth.Utils.Constants;

/**
 * A fragment for send and receive data between app and connected device
 */
public class TransferFragment extends Fragment {

    private static BluetoothAdapter bTAdapter;
    private static BluetoothSocket socket;

    private ConnectedThread mConnectedThread;

    Button sendBtn ;
    TextView logView;

    Handler h;
    private StringBuilder sb = new StringBuilder();
    // TODO: Rename and change types of parameters
    public static TransferFragment newInstance(BluetoothAdapter adapter , BluetoothSocket s) {
        TransferFragment fragment = new TransferFragment();
        bTAdapter = adapter;
        socket = s;
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TransferFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("SEND_DATA", "Super called for TransferFragment onCreate\n");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer, container, false);

        sendBtn = (Button) view.findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDate("GET\n" );
            }
        });

        logView = (TextView) view.findViewById(R.id.show_log);

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case Constants.RECIEVE_MESSAGE:													// if receive massage
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);					// create string from bytes array
                        sb.append(strIncom);												// append string
                        int endOfLineIndex = sb.indexOf("\r\n");							// determine the end-of-line
                        if (endOfLineIndex > 0) { 											// if end-of-line,
                            String sbprint = sb.substring(0, endOfLineIndex);				// extract string
                            sb.delete(0, sb.length());										// and clear
                            setLog( sbprint + "\n" );
                        }
                        //Log.d(TAG, "...String:"+ sb.toString() +  "Byte:" + msg.arg1 + "...");
                        break;
                }
            };
        };

        mConnectedThread = new ConnectedThread(socket , h);
        mConnectedThread.start();

        return view;
    }
    public final static char LF  = (char) 0x0A;
    public void sendDate(String message){

        try {
            byte[] msgBuffer = message.getBytes();
            mConnectedThread.write(msgBuffer);
        } catch (Exception e) {
            Log.d("error", "...Error data send: " + e.getMessage() + "...");
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(String message) {


    }

    public void setLog(String str){
        logView.setText( logView.getText() + " " + str );
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
