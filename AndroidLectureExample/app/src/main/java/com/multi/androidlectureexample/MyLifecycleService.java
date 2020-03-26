package com.multi.androidlectureexample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

public class MyLifecycleService extends Service {

    private Thread[] threads;
    private int cnt;

    public MyLifecycleService() {
        this.threads = new Thread[3];
        this.cnt = 0;
    }

    protected Thread getThread() {
        if (this.cnt >= threads.length) {
            Log.i("MyService", "max : 3, now : " + cnt);
            return null;
        } else {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    // ex : 1sec 간격으로 1~10 count
                    //      >> log(count)
                    for (int i=0 ; i<10 ; i++) {
                        try {
                            Thread.sleep(1000);     // 1 sec
                            Log.i("MyService", "count : " + i);
                        } catch (Exception e) {
                            //Log.i("MyService", "["+i+"] e : " + e.toString());
                            break;
                        }
                    }// for()
                }// run()
            });
            Log.i("MyService", "getThread() : " + t + " : created");
            Log.i("MyService", "max : "+threads.length+", now : " + cnt);
//            Log.i("MyService", "getThread() : " + threads);
            threads[this.cnt++] = t;
            return t;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MyService", "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //
        Log.i("MyService", "onStartCommand()");
        Thread t = this.getThread();
        if (t != null) {
            t.start();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //
        Log.i("MyService", "onDestroy()");
        for (Thread t : threads) {
            if (t != null && t.isAlive()) {
                Log.i("MyService", "onDestroy() : " + t + " : interrupted");
//                Log.i("MyService", "onDestroy() : " + threads);
                t.interrupt();
                cnt--;
            }
        }

    }
}
