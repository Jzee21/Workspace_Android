package com.multi.androidlectureexample;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Example25_ArduinoLEDActivity extends AppCompatActivity {

    private TextView responseTV;
    private Switch ledBtn;
    private SeekBar pwmBar;
    private boolean pwmBarFlag;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean readyFlag;
    private Thread serverThread;

    class SharedObject {
        private Object MONITOR = new Object();
        private LinkedList<Integer> list = new LinkedList<Integer>();

        SharedObject() {}

        public void put(int data) {
            synchronized (MONITOR) {
                list.add(data);
                MONITOR.notify();
            }
        }

        public int pop() {
            int result = -1;
            synchronized (MONITOR) {
                try {
                    while (list.isEmpty()) {
                        MONITOR.wait();
                    }
                    result = list.removeFirst();
                } catch (Exception e) {
                    Log.i("Arduino", e.toString());
                }
            }
            return result;
        } // pop()
    } // class SharedObject

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example25_arduino_led);

        final SharedObject sender = new SharedObject();
//        final SharedObject receiver = new SharedObject();
        readyFlag = true;


        final Runnable sendRunnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int data = sender.pop();
                    out.println(data);
                    out.flush();
                }
            }
        };

        final Runnable receiveRunnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String message = in.readLine();
                        if(message == null) {
                            serverThread.interrupt();
                        }
//                        responseTV.setText(message);
                        String[] msgData = message.split("/");
                        if(msgData.length > 1) {
                            responseTV.setText(msgData[msgData.length-1]);
                        } else {
                            responseTV.setText(message.replace("/", ""));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }

                }
            }
        };

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket();
//                    socket.connect(new InetSocketAddress("70.12.60.91", 55566));
                    socket.connect(new InetSocketAddress("70.12.227.108", 55566));
                    out = new PrintWriter(socket.getOutputStream());
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    readyFlag = false;
//                    notify();
//                    Log.i("Arduino", "notify");

                    Thread in1 = new Thread(sendRunnable);
                    Thread in2 = new Thread(receiveRunnable);
                    in1.setDaemon(true);
                    in2.setDaemon(true);

                    in1.start();
                    in2.start();

//                    while (true) {
//                        int data = sender.pop();
//                        out.println(data);
//                        out.flush();
//                    }
                } catch (Exception e) {
                    Log.i("Arduino", e.toString());
                }
            } // run()
        }; // Runnable r
        serverThread = new Thread(r);
        serverThread.start();

        //
        responseTV = findViewById(R.id._25_responseTV);

        ledBtn = findViewById(R.id.switch1);
        ledBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!pwmBarFlag) {
                    if(isChecked) {
//                        shared.put(255);
                        pwmBar.setProgress(255);
                    }
                    else {
//                        shared.put(0);
                        pwmBar.setProgress(0);
                    }
                }
            }
        }); // ledBtn.setOnCheckedChangeListener

        pwmBar = findViewById(R.id._25_LedPwmBar);
        pwmBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 0) {
                    ledBtn.setChecked(false);
                } else {
                    ledBtn.setChecked(true);
                }
                sender.put(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pwmBarFlag = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                pwmBarFlag = false;
            }
        }); // pwmBar.setOnSeekBarChangeListener

    } // onCreate()

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(socket != null && !socket.isClosed()) {
            try {
                socket.close();
                out.close();
//                serverThread.interrupt();
            } catch (IOException e) {
                Log.i("Arduino", e.toString());
            }
        }
    } // onDestroy()
}
