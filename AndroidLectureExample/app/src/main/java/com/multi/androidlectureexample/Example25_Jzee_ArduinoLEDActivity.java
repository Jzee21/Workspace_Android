package com.multi.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Example25_Jzee_ArduinoLEDActivity extends AppCompatActivity {

    private Switch ledBtn;
    private SeekBar pwmBar;
    private boolean pwmBarFlag;

    private ExecutorService executor;
    private LEDState ledState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example25_arduino_led);

        ledState = new LEDState();
        pwmBarFlag = false;

        executor = Executors.newFixedThreadPool(1);
        executor.submit(new EX25_SignalSender(ledState));

        ledBtn = findViewById(R.id.switch1);
        ledBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Log.i("Runnable", "" + ledState.getFlag());
                if(!pwmBarFlag) {
                    if (isChecked) {
                        ledState.setState(true, 255);
                    } else {
                        ledState.setState(true, 0);
                    }
                }
            }
        });

        pwmBar = findViewById(R.id._25_LedPwmBar);
        pwmBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("Runnable", (int)(progress*2.55) + " ");
                if(progress != 0) {
                    ledBtn.setChecked(true);
                } else {
                    ledBtn.setChecked(false);
                }
                ledState.setState(true, (int)(progress*2.55));
                // LED PWM Range : 0 ~ 255 (total 256 step)
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i("Runnable", "onStart");
                pwmBarFlag = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i("Runnable", "onStop");
                pwmBarFlag = false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }

}

class LEDState {
    private boolean flag;
    private int pwm;

    LEDState() {
        this.flag = false;
        this.pwm = 0;
    }

    public void setState(boolean flag, int pwm) {
        this.flag = flag;
        this.pwm = pwm;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean getFlag() {
        return this.flag;
    }

    public int getpwm() {
        return this.pwm;
    }
}

class EX25_SignalSender implements Runnable {
    private final String ADDR = "70.12.60.91";
    private final int PORT = 55566;

    private Socket socket;
    private PrintWriter out;

    private LEDState state;

    EX25_SignalSender(LEDState state) {
        this.state = state;
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
            if(state.getFlag()) {
                Log.i("Runnable", state.getpwm() + " ** Send");
                try {
                    this.out.println(state.getpwm());
                    this.out.flush();
                    state.setFlag(false);
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
