package com.multi.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Example09_CounterLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example09_counterlog);

//        final TextView sumTv = (TextView) findViewById(R.id.sumTv2);

        // 연산 시작 버튼
        Button startBtn2 = (Button) findViewById(R.id.startBtn2);
        startBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thread를 만들어서 실행
                MyRunnable runnable = new MyRunnable();
                Thread t = new Thread(runnable);
                t.start();
            }
        });

        // Toast 생성 버튼
        Button secondBtn2 = (Button) findViewById(R.id.secondBtn2);
        secondBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Example09_CounterLogActivity.this,
                        "Button이 클릭됬어요", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Inner Class for Thread
    class MyRunnable implements Runnable {
        @Override
        public void run() {
            long sum = 0;
            for (long i=0 ; i<10000000000L ; i++) {
                sum += i;
            }
            Log.i("SumTest", "총 합은 : " + sum);
        }
    }

}
