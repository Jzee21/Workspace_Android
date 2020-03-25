package com.multi.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Example10_CounterLogProgressActivity extends AppCompatActivity {

    private TextView tv;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example10_counterlog_progress);

        tv = (TextView) findViewById(R.id.sumTv3);
        pb = (ProgressBar) findViewById(R.id.counterProgress);

        Button startBtn3 = (Button) findViewById(R.id.startBtn3);
        startBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CounterRunnable runnable = new CounterRunnable();
                Thread t = new Thread(runnable);
                t.start();
            }
        });

    }

    /*  Android UI component(Widget)은 Thread Safe 하지 않다
        Android UI component의 화면제어는
        오직 UI Thread(Activity)에 의해서만 제어되어야 한다

        아래 코드는 비록 실행되지만, 올바르지 않은 코드다
        UI Thread가 아닌 외부 Thread에서 UI Component를 직접 제어할 수 없다(해선 안된다)

        -> Handler를 이용해서 이 문제를 해결한다. (다른 방법도 있긴 하다!)
     */
    // Inner Class for Thread
    class CounterRunnable implements Runnable {
        @Override
        public void run() {
            long sum = 0;
            for (long i=0 ; i<10000000000L ; i++) {
                sum += i;
                if (i % 100000000 == 9) {
                    int loop = (int) i/100000000;
                    pb.setProgress(loop);
                    // Long loop2 = i/100000000;       // autoboxing
                }
            }
            tv.setText("연산결과 : " + sum);
//            Log.i("SumTest", "총 합은 : " + sum);

        }
    }

}
