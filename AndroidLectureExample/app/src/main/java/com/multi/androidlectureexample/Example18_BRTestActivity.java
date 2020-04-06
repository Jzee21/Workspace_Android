package com.multi.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;

/*
*   Android 에서는 Broadcast라는 signal이 존재
*   이 신호(signal)은 Android System 자체에서 발생할 수도 있고,
*   사용자 App에서 임의로 발생시킬 수 있다.
*
*   여러개의 일반적으로 정해져있는 Broadcast message를 받고 싶다면
*   Broadcast Receiver를 만들어서 등록해야한다.
*
*   등록하는 방법에는 크게 2가지가 있다
*   1. AndroidManifest.xml 파일에 명시
*   2. 코드 상에서 Receiver를 만들어서 등록할 수 있다
*
*/
public class Example18_BRTestActivity extends AppCompatActivity {

    private BroadcastReceiver bReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example18_br_test);

        // Broadcast Receiver 등록
        Button _18_br_registerBtn = (Button) findViewById(R.id._18_br_registerBtn);
        _18_br_registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Broadcast Receiver 객체를 만들어서
                // IntentFilter와 함께 시스템에 등록

                // 1. IntentFilter 생성
                //  묵시적 Intent 호출을 위한 IntentFilter
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("MY_BROADCAST_SIGNAL");

                // 2. Broadcast Receiver 객체 생성
                //  상단 Feild에 선언
                bReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        // Receiver 가 신호를 받았을 때 수행되는 Method
                        if (intent.getAction().equals("MY_BROADCAST_SIGNAL")) {
                            Toast.makeText(Example18_BRTestActivity.this,
                                    "신호를 수신했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                // 3. Broadcast Receiver를 Filter와 함께 등록
                registerReceiver(bReceiver, intentFilter);

            }
        });

        // Broadcast Receiver 등록 해제
        Button _18_br_unRegisterBtn = (Button) findViewById(R.id._18_br_unRegisterBtn);
        _18_br_unRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unregisterReceiver(bReceiver);
            }
        });

        // Broadcast 발생!
        Button _18_sendBroadcastBtn = (Button) findViewById(R.id._18_sendBroadcastBtn);
        _18_sendBroadcastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("MY_BROADCAST_SIGNAL");
                sendBroadcast(i);
            }
        });

    }
}
