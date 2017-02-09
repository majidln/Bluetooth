package com.majid.bluetooth;

/**
 * Created by majid on 2/9/17.
 */
public class ConnectedThread extends Thread {
//
//    private final BluetoothSocket mmSocket;
//    private final InputStream mmInStream;
//    private final OutputStream mmOutStream;
//    public ConnectedThread(BluetoothSocket socket) {
//        mmSocket = socket;
//        InputStream tmpIn = null;
//        OutputStream tmpOut = null;
//        try {
//            tmpIn = socket.getInputStream();
//            tmpOut = socket.getOutputStream();
//        } catch (IOException e) { }
//        mmInStream = tmpIn;
//        mmOutStream = tmpOut;
//    }
//    public void run() {
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
//                break;
//            }
//        }
//    }
//    public void write(byte[] bytes) {
//        try {
//            mmOutStream.write(bytes);
//        } catch (IOException e) { }
//    }
//    public void cancel() {
//        try {
//            mmSocket.close();
//        } catch (IOException e) { }
//    }

}
