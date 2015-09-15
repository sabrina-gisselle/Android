package com.matthewddiaz.applicationlearn;

import android.app.Application;

/**
 * Created by matthew on 2/25/15.
 */
public class CounterApplication extends Application {
    private MainActivity currentActivity = null;
    private CounterThread myThread;


    public synchronized void resume(MainActivity update){
        this.currentActivity = update;
        myThread.interrupt();
    }

    public synchronized void pause(MainActivity update){

        this.currentActivity = update;
        myThread.interrupt();
    }

    public synchronized void update(final long seconds){

            Runnable notifyAction = new Runnable(){
            public void run(){
                if(currentActivity != null){
                    currentActivity.updateResult(seconds);
                }
            }
        };
        currentActivity.runOnUiThread(notifyAction);
    }


    public void onCreate(){
        super.onCreate();
        myThread = new CounterThread();
        myThread.start();
    }

    private class CounterThread extends  Thread{
        private long seconds = 0;
        private boolean running = false;

        @Override
        public void run(){

           while(true){

               try{
                   Thread.sleep(running?1000:100000);
               }
               catch(InterruptedException e){
                    running = !running;
               }
               if(interrupted()){
                   running = !running;
               }
               if(running){
                   update(++seconds);
               }

           }
        }


    }
}
