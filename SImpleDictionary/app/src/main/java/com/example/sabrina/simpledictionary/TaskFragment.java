package com.example.sabrina.simpledictionary;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * TaskFragment manages a single background task and retains itself across
 * configuration changes.
 */
public class TaskFragment extends Fragment {
    DictionaryDAO mDictionaryDAO;

    private boolean mRunning;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDictionaryDAO = new DictionaryDAO(activity);
    }

    /**
     * This method is called once when the Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mRunning = true;
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
    /**
     * Start the background task.
     */
    public void update(final String term, final String definition) {
        if(isRunning()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Cursor cursor = mDictionaryDAO.findTerm(term);
                    if (cursor != null) {
                        if (cursor.getCount() > 0) {
                            mDictionaryDAO.updateDictionaryEntry(term, definition);
                        } else {
                            mDictionaryDAO.createDictionaryEntry(term, definition);
                        }
                    }
                    cursor.close();
                }
            }).start();
        }
    }

    public void delete(final String term) {
        if(isRunning()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mDictionaryDAO.deleteDictionaryEntry(term);
                }
            }).start();
        }
    }

    public String lookup(final String term) {
        final String[] result = {null};
        if(isRunning()) {
            Cursor cursor = mDictionaryDAO.findTerm(term);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    result[0] = cursor.getString(cursor.getColumnIndex(mDictionaryDAO.DICT_DEF));
                }
                cursor.close();
            }
        }
        return result[0];
    }


    /**
     * Cancel the background task.
     */
    public void cancel() {
        if (mRunning) {
            mRunning = false;
        }
    }

    /**
     * Returns the current state of the background task.
     */
    public boolean isRunning() {
        return mRunning;
    }
}
