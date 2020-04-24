package com.multi.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Example25_ArduinoLEDActivity extends AppCompatActivity {

    private Switch ledBtn;
    private SeekBar rpmBar;

    private ExecutorService executor;
    private LEDStatus ledStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example25_arduino_led);

        ledStatus = new LEDStatus();

        executor = Executors.newFixedThreadPool(1);
        executor.submit(new EX25_SignalSender(ledStatus));

        ledBtn = findViewById(R.id.switch1);
        ledBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ledStatus.setStatus(true, 255);
//                    pwm = 255;
//                    flag = true;
                } else {
                    ledStatus.setStatus(true, 0);
//                    pwm = 0;
//                    flag = true;
                }
            }
        });

        rpmBar = findViewById(R.id._25_LedRpmBar);
        rpmBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress != 0) {
                    ledBtn.setChecked(true);
                } else {
                    ledBtn.setChecked(false);
                }
                ledStatus.setStatus(true, (int)(progress*2.56));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(seekBar.getProgress() != 0) {
                    ledBtn.setChecked(true);
                } else {
                    ledBtn.setChecked(false);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopRun();
        executor.shutdownNow();
    }

//    void stopRun() {
//        try {
//            if (socket != null && !socket.isClosed()) {
//                socket.close();
//                if (out != null) out.close();
//            }
//        } catch (IOException e) {
//            //
//        }
//    }
}

class LEDStatus {
    private boolean flag;
    private int rpm;

    LEDStatus() {
        this.flag = false;
        this.rpm = 0;
    }

    public void setStatus(boolean flag, int rpm) {
        this.flag = flag;
        this.rpm = rpm;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean getFlag() {
        return this.flag;
    }

    public int getRpm() {
        return this.rpm;
    }
}

class EX25_SignalSender implements Runnable {
    private final String ADDR = "70.12.60.91";
    private final int PORT = 55566;

    private Socket socket;
    private PrintWriter out;

    private LEDStatus status;

    EX25_SignalSender(LEDStatus status) {
        this.status = status;
    }

    @Override
    public void run() {

        try {
            this.socket = new Socket();
            this.socket.connect(new InetSocketAddress(ADDR, PORT));
            this.out = new PrintWriter(this.socket.getOutputStream());
        } catch (Exception e) {
            if (this.socket != null && !this.socket.isClosed()) {
                try {
                    this.socket.close();
                    Log.i("Runnable", "fail - connect socket");
                    Log.i("Runnable", e.toString());
                } catch (IOException ex) {
                    return;
                }
            }
        }

        while(true) {
            if(status.getFlag()) {
                Log.i("Runnable", "" + status.getRpm());
                try {
                    this.out.println(status.getRpm());
                    this.out.flush();
                    status.setFlag(false);
                } catch (Exception e) {
                    if (this.socket != null && !this.socket.isClosed()) {
                        try {
                            this.socket.close();
                            this.out.close();
                            Log.i("Runnable", "fail - send data");
                        } catch (IOException ex) {
                            return;
                        }
                    }
                }
            }
        }
    }
}

    /*class TestRunnable implements Runnable {

        TestRunnable() {
            Toast.makeText(Example25_ArduinoLEDActivity.this, "생성자", Toast.LENGTH_LONG).show();
        }

        @Override
        public void run() {
            for (int i=0 ; i<3 ; i++) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(Example25_ArduinoLEDActivity.this, "run() : " + i, Toast.LENGTH_LONG).show();
            }
        }
    }*/


//    class EX25_SignalSender implements Runnable {
//
//        EX25_SignalSender() {
//            try {
//                socket = new Socket();
//                socket.connect  (new InetSocketAddress(ADDR, PORT));
//                out = new PrintWriter(socket.getOutputStream());
//            } catch (IOException e) {
//                Toast.makeText(Example25_ArduinoLEDActivity.this, "생성자", Toast.LENGTH_LONG).show();
//                stopRun();
//            }
//        }
//
//        @Override
//        public void run() {
//            while (true) {
//                try {
//                    if(flag) {
//                        out.println(pwm);
//                        out.flush();
//                        flag = false;
//                    }
//                } catch (Exception e) {
//                    stopRun();
//                }
//            }
//        } // run
//    } // runnable


