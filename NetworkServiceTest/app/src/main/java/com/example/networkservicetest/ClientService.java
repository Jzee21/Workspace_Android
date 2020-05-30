package com.example.networkservicetest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientService extends Service {

    private static final String ADDR = "70.12.60.105";
    private static final int PORT = 55000;
    private static final int SIZE = 10240;

    private ExecutorService executor;

    private DatagramSocket ds;
    private DatagramPacket dp;

    public ClientService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String data = intent.getExtras().getString("DATA");
        send(data);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //
    private void initialize() {
        try {
            ds = new DatagramSocket();
            byte[] buffer = new byte[SIZE];
            dp = new DatagramPacket(buffer, SIZE);

            executor = Executors.newFixedThreadPool(4);

            Runnable receiver = new Runnable() {
                @Override
                public void run() {
                    while(true) {
                        try {
                            ds.receive(dp);
                            int dataLen = dp.getLength();
                            String line = new String(dp.getData(), 0, dataLen);
    //						System.out.println("[data (" + dataLen + ")] " + line);
    //                        displayText("[Echo data (" + dataLen + ")] " + line);
                            Intent resultIntent = new Intent(getApplicationContext(),
                                    MainActivity.class);
                            resultIntent.putExtra("RESULT", line);

                            if(line.equals("@EXIT")) {
                                break;
                            }
                        } catch (IOException e) {
    //						e.printStackTrace();
                            break;
                        }
                    }
                    close();
                }
            };
            executor.submit(receiver);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void close() {
        ds.close();
        executor.shutdownNow();
        stopSelf();
    }

    private void send(String text) {
        try {
            byte[] buf = text.getBytes();
            dp = new DatagramPacket(buf, buf.length, InetAddress.getByName(ADDR), PORT);
            ds.send(dp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void send(DatagramPacket dp) {
        try {
            dp = new DatagramPacket(dp.getData(), dp.getLength(), dp.getAddress(), dp.getPort());
            ds.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
