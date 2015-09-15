package com.example.sabrina.alivecounter;

import android.app.Application;

/**
 * Created by Sabrina on 2/25/2015.
 */
public class CounterApplication extends Application {

    private MainActivity currentActivity = null;
    private long mCount = 0;
    private Thread mThread;

    public synchronized void setUpdatable(MainActivity updatable){
        this.currentActivity = updatable;
        mThread.interrupt();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mThread = new Thread (new Runnable() {
            @Override
            public void run (){
                try{
                    wait(10000);
                } catch (InterruptedException e) {
                    while(true){
                        ++mCount;
                        currentActivity.UpdateSeconds(mCount);
                    }
                }
            }});
        mThread.start();
    }

    public void activityPause(){
        try {
            mThread.wait(1000);
        } catch (InterruptedException e) {
            ++mCount;
            currentActivity.UpdateSeconds(mCount);
        }
    }
}
