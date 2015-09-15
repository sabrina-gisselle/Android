package com.example.sabrina.montecarlo;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends FragmentActivity implements TaskFragment.TaskCallbacks {

    private static final String CURRENT_PI_VALUE = "piEstimate";
    private static final String TAG_TASK_FRAGMENT = "task_fragment";

    private TaskFragment mTaskFragment;
    private EditText mEditTextNumCycles;
    private TextView mTextViewPi;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextNumCycles = (EditText) findViewById(R.id.editText);
        mTextViewPi = (TextView) findViewById(R.id.textView_estimate);
        mTextViewPi.setText(getPreferences(MODE_PRIVATE).getString(CURRENT_PI_VALUE, ""));
        mButton = (Button) findViewById(R.id.btn_gostop);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTaskFragment.isRunning()) {
                    mTaskFragment.cancel();
                } else if( mEditTextNumCycles.getText().toString().trim().length() > 0) {
                    final Long cycles = Long.valueOf(String.valueOf(mEditTextNumCycles.getText()));
                    mTaskFragment.start(cycles);
                }
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        mTaskFragment = (TaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        // If the Fragment is non-null, then it is being retained
        // over a configuration change.
        if (mTaskFragment == null) {
            mTaskFragment = new TaskFragment();
            fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }

        if (mTaskFragment.isRunning()) {
            mButton.setText(getString(R.string.stop));
        } else {
            mButton.setText(getString(R.string.go));
        }
    }


    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString(CURRENT_PI_VALUE, mTextViewPi.getText().toString());
        editor.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickClear(View v){
        mTaskFragment.cancel();
        mEditTextNumCycles.setText("");
        mEditTextNumCycles.setEnabled(true);
        mTextViewPi.setText("");
    }

    @Override
    public void onPreExecute() {
        mButton.setText(getString(R.string.stop));
        mEditTextNumCycles.setEnabled(false);
    }

    @Override
    public void onProgressUpdate(Double est) {
        mTextViewPi.setText(Double.toString(est));
    }

    @Override
    public void onCancelled() {
        mButton.setEnabled(true);
        mEditTextNumCycles.setEnabled(true);
        mButton.setText(getString(R.string.go));
    }

    @Override
    public void onPostExecute() {
        mButton.setText(getString(R.string.go));
        mEditTextNumCycles.setEnabled(true);
    }
}
