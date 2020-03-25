package com.multi.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Example08_ANRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example08_anr);

        final TextView sumTv = (TextView) findViewById(R.id.sumTv);

        // Btn1 - startBtn
        // 고의로 많은 연산을 걸었다. (오랜시간 연산)
        // 사용자와의 interaction이 중지된다.
        // Activity가 Block된 것처럼 보인다.(ANR : Application Not Responding)
        // ANR은 반드시 피해야한다.
        // Activity의 최우선 작업은 사용자와의 interaction!
        // Activity에서는 시간이 오래 걸리는 작업을 하면 안된다.
        //      시간이 오래 걸리는 작업
        //      - DB사용, 대용량 file처리...
        // Activity는 Thread로 동작한다. (UI Thread)
        // Logic 처리는 background Thread를 이용해서 처리해야 한다.

        Button startBtn = (Button) findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long sum = 0;
                for (long i=0; i<10000000000L; i++) {
                    sum += i;
                }
                Log.i("SumTest","연산 결과 : "+sum);
            }
        });

        // Btn2 - secondBtn
        // Toast Message 출력
        Button secondBtn = (Button) findViewById(R.id.secondBtn);
        secondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Example08_ANRActivity.this,
                        "Button이 클릭됬어요", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
