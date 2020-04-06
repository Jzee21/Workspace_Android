package com.multi.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Example20_BRNotiActivity extends AppCompatActivity {

    private BroadcastReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example20_br_noti);

        // Receiver 등록
        Button _20_registerNotiBtn = (Button) findViewById(R.id._20_registerNotiBtn);
        _20_registerNotiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Broadcast Receiver 등록

                // 1. IntentFilter
                IntentFilter filter = new IntentFilter();
                filter.addAction("MY_NOTI_SIGNAL");

                // 2. Receiver 생성
                br = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        // Notification 띄우기

                        // 1. Notification Manager 생성
                        // (Android System이 가지고있는 maneger를 받아 사용
                        NotificationManager nManager =
                                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

                        // 2. Android 8.0 (Oreo) ~ 약간의 차이 발생
                        //  (8.0 ~ Channel 필수)
                        //  알림의 종류에 따라 구분하여
                        //  중요하지 않은 알림은 소리만 나도록 처리
                        //  중요한 알림은 소리와 진동이 같이 울리도록 처리
                        String channelID = "MY_CHANNEL";
                        String channelName = "MY_CHANNEL_NAME";
                        int importance = NotificationManager.IMPORTANCE_HIGH;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            // Channel 객체 생성
                            NotificationChannel nChannel =
                                    new NotificationChannel(channelID, channelName, importance);
                            // Channel에 설정 추가
                            // 진동
                            nChannel.enableVibration(true);
                            nChannel.setVibrationPattern(new long[]{100,200,100,200});  // millisecond 0.1s
                            // LED
                            nChannel.enableLights(true);
//                            nChannel.setLightColor(int argb);
                            // 잠금화면 알림
                            nChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

                            nManager.createNotificationChannel(nChannel);
                        }

                        // Notification 생성  - Builder 사용
                        NotificationCompat.Builder nBuilder =
                                new NotificationCompat.Builder(
                                        context.getApplicationContext(),
                                        channelID);

                        // Notification 전송을 위한 Intent
                        // Activity 위에 띄워야한다.
                        Intent nIntent = new Intent(getApplicationContext(), Example20_BRNotiActivity.class);
                        nIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        nIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // nIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP)

                        // 중복되지 않는 상수값을 사용하기 위해서
                        int requestID = (int) System.currentTimeMillis();

                        // PendingIntent 생성 후 사용
                        PendingIntent pIntent =
                                PendingIntent.getActivity(getApplicationContext(), requestID,
                                        nIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        // Builder를 이용해서 최종적으로 Notification 생성
                        nBuilder.setContentTitle("Noti제목");
                        nBuilder.setContentText("Noti내용");
                        nBuilder.setDefaults(Notification.DEFAULT_ALL); // 알림 - 기본 사운드, 진동
                        nBuilder.setAutoCancel(true);   // 알림 터치 시 반응 후 삭제
                        // 알림의 기본음으로 설정
                        nBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                        // 알림 아이콘 설정
                        nBuilder.setSmallIcon(android.R.drawable.btn_star);
                        nBuilder.setContentIntent(pIntent);

                        // Notification 화면에 구현
                        nManager.notify(0, nBuilder.build());
                    }
                };

                // 3. Receiver 등록
                registerReceiver(br, filter);

            }
        }); // Receiver 등록

        // Receiver 등록 해제
        Button _20_unRegisterNotiBtn = (Button) findViewById(R.id._20_unRegisterNotiBtn);
        _20_unRegisterNotiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unregisterReceiver(br);
            }
        }); // Receiver 등록 해제

        // Signal 생성
        Button _20_sendSignalBtn = (Button) findViewById(R.id._20_sendSignalBtn);
        _20_sendSignalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("MY_NOTI_SIGNAL");
                sendBroadcast(i);
            }
        });
    }
}
