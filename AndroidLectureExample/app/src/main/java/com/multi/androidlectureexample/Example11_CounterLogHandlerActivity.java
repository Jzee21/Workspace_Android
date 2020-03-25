package com.multi.androidlectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Example11_CounterLogHandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example11_counterlog_handler);

        final TextView tv = (TextView) findViewById(R.id.sumTv4);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.counterProgress2);

        // Data를 주고받는 역할을 수행할 Handler 객체 생성
        // Handler 객체는 메세지 전달 method와
        // 메세지를 전달받아 로직 처리를 하는 method, 2개로 구성된다
        @SuppressLint("HandlerLeak")        // Memory Leak 방지 @
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                // 메세지를 받을 때 실행 - 화면 처리
                String count = msg.getData().getString("count");
                pb.setProgress(Integer.parseInt(count));
            }
        };

        // Thread에게 Handler를 전달하여 Activity와 통신해야한다
        Button startBtn3 = (Button) findViewById(R.id.startBtn4);
        startBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySumThread runnable = new MySumThread(handler);
                Thread t = new Thread(runnable);
                t.start();
            }
        });
    }
}

class MySumThread implements Runnable {

    private Handler handler;

    MySumThread(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        long sum = 0;
        for (long i=0 ; i<1000000000L ; i++) {
            sum += i;
            if(i % 1000000 == 0) {
                int loop = (int) i/10000000;
                // Message를 만들어 Handler를 이용해 Activity에게 전달
                // Bundle 객체를 먼저 만들어야한다
                Bundle bundle = new Bundle();
                bundle.putString("count", String.valueOf(loop));
                // Bundle을 가질 수 있는 Message 객체 생성
                Message msg = new Message();
                msg.setData(bundle);
                this.handler.sendMessage(msg);
            }
        }
    }
}
