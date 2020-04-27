package com.multi.androidlectureexample;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Example25_ArduinoLEDActivity extends AppCompatActivity {

    private Switch ledBtn;
    private SeekBar pwmBar;
    private boolean pwmBarFlag;

    private Socket socket;
    private PrintWriter out;

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
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example25_arduino_led);

        final SharedObject shared = new SharedObject();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress("70.12.60.91", 55566));
                    out = new PrintWriter(socket.getOutputStream());

                    while (true) {
                        int data = shared.pop();
                        out.println(data);
                        out.flush();
                    }
                } catch (IOException e) {
                    Log.i("Arduino", e.toString());
                }
            }
        };
        Thread t = new Thread(r);
        t.start();

        ledBtn = findViewById(R.id.switch1);
        ledBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!pwmBarFlag) {
                    if(isChecked) {
                        shared.put(255);
                    }
                    else {
                        shared.put(0);
                    }
                }
            }
        });

        pwmBar = findViewById(R.id._25_LedPwmBar);
        pwmBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                shared.put((int)(progress*2.55));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pwmBarFlag = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                pwmBarFlag = false;
            }
        });

    }

}
