package com.multi.androidlectureexample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class Example15sub_LifecycleService extends Service {

    private Thread myThread = new Thread(new Runnable() {
        @Override
        public void run() {
            // ex : 1sec 간격으로 1~10 count
            //      >> log(count)
            for (int i=0 ; i<10 ; i++) {
                try {
                    Thread.sleep(1000);     // 1 sec
                    Log.i("ServiceExam", "count : " + i);
                } catch (Exception e) {
                    Log.i("ServiceExam", "["+i+"] e : " + e.toString());
                }
            }// for()
        }// run()
    });

    public Example15sub_LifecycleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Service 객체가 생성될 때 실행
        Log.i("ServiceExam", "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 실제 서비스 동작을 수행하는 부분
        // ex : 1sec 간격으로 1~10 count
        //      >> log(count)
        Log.i("ServiceExam", "onStartCommand()");
        try {
            myThread.start();
        } catch (Exception e) {
            Log.i("ServiceExam", "Thread.start() : " + e);
        }
        /*
            second : myThread.start();
            >> e : java.lang.IllegalThreadStateException

            Thread.start()  >>  run()이 종료되면 Thread의 상태가 DEAD 가 된다.
                            >>  재사용 불가?!! ( DEAD 상태에서 다시 실행시킬 방법이 없다 )

            유일하게 다시 실행시키는 방법은 Thread를 다시 생성해서 실행
        */


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // Service 객체가 메모리에서 정리될 때
        super.onDestroy();
        Log.i("ServiceExam", "onDestroy()");
    }

} // end class
