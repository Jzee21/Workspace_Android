package com.multi.androidlectureexample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.nio.channels.InterruptedByTimeoutException;

public class Example16sub_DataTransferService extends Service {

    public Example16sub_DataTransferService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Activity에서 데이터 받기
        String data = intent.getExtras().getString("DATA");

        // Activity에 데이터 전달하기
        String resultData = data + "를 받았어요";
        Intent resultIntent = new Intent(getApplicationContext(),
                Example16_ServiceDataTransferActivity.class);
        resultIntent.putExtra("RESULT", resultData);
        /* ! Service에서 Activity를 호출하는 작업
             - 화면이 없는 Service에서 화면이 있는 Activity를 호출할 때는
               Task라는게 필요하다
             - Activity를 새로 생성하는것이 아닌,
               메모리에 존재하는 이전 Activity를 찾아서 실행할 때는
               Flag를 2개 추가해야한다
         */
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(resultIntent);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
