package com.multi.androidlectureexample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class Example15sub_LifecycleService extends Service {

    private Thread myThread;
//    private Thread myThread = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            // ex : 1sec 간격으로 1~10 count
//            //      >> log(count)
//            for (int i=0 ; i<10 ; i++) {
//                try {
//                    Thread.sleep(1000);     // 1 sec
//                    Log.i("ServiceExam", "count : " + i);
//                } catch (Exception e) {
//                    Log.i("ServiceExam", "["+i+"] e : " + e.toString());
//                }
//            }// for()
//        }// run()
//    });

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
            myThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // ex : 1sec 간격으로 1~10 count
                    //      >> log(count)
                    for (int i=0 ; i<10 ; i++) {
                        try {
                            Thread.sleep(1000);     // 1 sec
                            /*
                                sleep 하려고 할 때,
                                만약 interrupt가 걸려있다면,
                                Exception을 발생시켜 catch문으로 이동
                                >> loop를 벗어나도록 catch 문에 braek; 작성
                            */
                            Log.i("ServiceExam", "count : " + i);
                        } catch (Exception e) {
                            Log.i("ServiceExam", "["+i+"] e : " + e.toString());
                            break;
                        }
                    }// for()
                }// run()
            });
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
        // stopService()가 호출되면 onDestroy()가 호출된다.
        // 여기에서 Service에 의해 동작중인 모든 Thread를 종료한다.
        if (myThread != null && myThread.isAlive()) {
            // myThread.isAlive() : Thread가 실행중인지 확인
            // myThread.stop();    // now, not suggest stop()  // lot of Exception
            myThread.interrupt();
        }

        super.onDestroy();
        Log.i("ServiceExam", "onDestroy()");
    }

} // end class
