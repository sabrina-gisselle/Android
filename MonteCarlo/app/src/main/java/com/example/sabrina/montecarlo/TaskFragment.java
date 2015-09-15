package com.example.sabrina.montecarlo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TextView;

import java.util.Random;

/**
 * TaskFragment manages a single background task and retains itself across
 * configuration changes.
 */
public class TaskFragment extends Fragment {
  /**
   * Callback interface through which the fragment can report the task's
   * progress and results back to the Activity.
   */
  static interface TaskCallbacks {
    void onPreExecute();
    void onProgressUpdate(Double est);
    void onCancelled();
    void onPostExecute();
  }

  private TaskCallbacks mCallbacks;
  private DummyTask mTask;
  private boolean mRunning;

  /**
   * Hold a reference to the parent Activity so we can report the task's current
   * progress and results. The Android framework will pass us a reference to the
   * newly created Activity after each configuration change.
   */
  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (!(activity instanceof TaskCallbacks)) {
      throw new IllegalStateException("Activity must implement the TaskCallbacks interface.");
    }

    // Hold a reference to the parent Activity so we can report back the task's
    // current progress and results.
    mCallbacks = (TaskCallbacks) activity;
  }

  /**
   * This method is called once when the Fragment is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  /**
   * Note that this method is <em>not</em> called when the Fragment is being
   * retained across Activity instances. It will, however, be called when its
   * parent Activity is being destroyed for good (such as when the user clicks
   * the back button, etc.).
   */
  @Override
  public void onDestroy() {
    super.onDestroy();
    cancel();
  }

  /*****************************/
  /***** TASK FRAGMENT API *****/
  /*****************************/

  /**
   * Start the background task.
   */
  public void start(Long mCycles) {
    if (!mRunning) {
      mTask = new DummyTask();
      mTask.execute(mCycles);
      mRunning = true;
    }
  }

  /**
   * Cancel the background task.
   */
  public void cancel() {
    if (mRunning) {
      mTask.cancel(false);
      mTask = null;
      mRunning = false;
    }
  }

  /**
   * Returns the current state of the background task.
   */
  public boolean isRunning() {
    return mRunning;
  }

  /***************************/
  /***** BACKGROUND TASK *****/
  /***************************/

  /**
   * A dummy task that performs some (dumb) background work and proxies progress
   * updates and results back to the Activity.
   */
  private class DummyTask extends AsyncTask<Long, Double, Void> {

      private long mCountInside = 0, mCountTotal = 0;
      private double mPiEst;

    @Override
    protected void onPreExecute() {
      // Proxy the call to the Activity.
      mCallbacks.onPreExecute();
      mRunning = true;
    }

    /**
     * Note that we do NOT call the callback object's methods directly from the
     * background thread, as this could result in a race condition.
     */
    @Override
    protected Void doInBackground(Long... mCycles) {
      long times = mCycles[0] / 1000;

      for (int i = 0; !isCancelled() && i < times; ++i) {
          processCalc(1000);
      }

      if( !isCancelled() ) {
          processCalc(mCycles[0] % 1000);
      }
      return null;
    }

    private void processCalc(long num){
      for (long j = 0; !isCancelled() && j < num; ++j) {
          findAreaRatio();
      }

      if( !isCancelled() ) {
          mPiEst = findPi();
          publishProgress(mPiEst);
      }
    }
    private void findAreaRatio(){
      Random generator = new Random();
      double randNumX = generator.nextDouble();
      double randNumY = generator.nextDouble();
      if ( (randNumX * randNumX + randNumY * randNumY) <= 1) {
          ++mCountInside;
      }
      ++mCountTotal;
    }

    // A = pi * (r)^2 and we are taking a quarter of this so...
    // 4A = pi * (1)^2
    // 4A = pi
    private double findPi(){
      double area = (double)mCountInside/(double)mCountTotal;
      return 4 * area;
    }

    @Override
    protected void onProgressUpdate(Double... est) {
      // Proxy the call to the Activity.
      mCallbacks.onProgressUpdate(est[0]);
    }

    @Override
    protected void onCancelled() {
      // Proxy the call to the Activity.
      mCallbacks.onCancelled();
      mRunning = false;
    }

    @Override
    protected void onPostExecute(Void ignore) {
      // Proxy the call to the Activity.
      mCallbacks.onPostExecute();
      mRunning = false;
    }
  }

  /************************/
  /***** LOGS & STUFF *****/
  /************************/

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onStart() {
    super.onStart();
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
  }

  @Override
  public void onStop() {
    super.onStop();
  }

}
