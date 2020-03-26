package com.multi.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Example15_ServiceLifecycleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example15_service_lifecycle);

        Button startServiceBtn = (Button)findViewById(R.id.startServiceBtn);
        startServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Service Component 시작
                Intent i = new Intent(getApplicationContext(),
                        Example15sub_LifecycleService.class);
                i.putExtra("MSG", "소리없는 아우성!");
                startService(i);
                /*  - Service가 생성되지 않은 경우
                        onCreate() > onStartCommand()
                    - Service가 실행되고 종료되지 않은 경우
                        onStartCommand()
                */
            }
        });

        Button stopServiceBtn = (Button)findViewById(R.id.stopServiceBtn);
        stopServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
