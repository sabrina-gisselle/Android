package com.example.sabrina.click_o_tron;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    static final private String CURRENT_COUNT_KEY = "storedCount";

    private long mCurrentCount = -111;
    private TextView mCountTextRepresentation = null;
    private String mSuffixSingular = null;
    private String mSuffixPlural = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCurrentCount = getPreferences(MODE_PRIVATE ).getLong(CURRENT_COUNT_KEY, 0);

        mCountTextRepresentation = (TextView) findViewById(R.id.countText);
        mSuffixSingular = " " + getResources().getString(R.string.clicksingular);
        mSuffixPlural = " " + getResources().getString(R.string.clickplural);
        updateCounterText();
    }

    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putLong(CURRENT_COUNT_KEY, mCurrentCount);
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

    public void clickButtonHandler( View view){
        ++mCurrentCount;
        updateCounterText();
    }

    public void clearButtonHandler( View view){
        mCurrentCount = 0;
        updateCounterText();
    }

    private void updateCounterText(){
        // reference to the text view
        mCountTextRepresentation.setText(String.valueOf(mCurrentCount) + (mCurrentCount == 1 ? mSuffixSingular : mSuffixPlural));
    }
}
