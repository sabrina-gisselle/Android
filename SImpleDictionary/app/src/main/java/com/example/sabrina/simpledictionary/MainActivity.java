package com.example.sabrina.simpledictionary;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    private static final String TAG_TASK_FRAGMENT = "task_fragment";

    EditText mtextTerm = null;
    EditText mTextDef = null;
    private TaskFragment mTaskFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mtextTerm = (EditText) findViewById(R.id.editTextTerm);
        mTextDef = (EditText) findViewById(R.id.editTextDef);

        FragmentManager fm = getSupportFragmentManager();
        mTaskFragment = (TaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        // If the Fragment is non-null, then it is being retained
        // over a configuration change.
        if (mTaskFragment == null) {
            mTaskFragment = new TaskFragment();
            fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }

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

    public void updateHandler(View v){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if(!isEmpty(mtextTerm) && !isEmpty(mTextDef)) {
            mTaskFragment.update(mtextTerm.getText().toString(), mTextDef.getText().toString());
            Toast.makeText(this, "Updated/Created", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "The term or def is empty", Toast.LENGTH_SHORT).show();
        }

    }

    public void lookupHandler(View v){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if(!isEmpty(mtextTerm)) {
            String result = mTaskFragment.lookup(mtextTerm.getText().toString());
            if (result != null) {
                mTextDef.setText(result);
            } else {
                mTextDef.setText("");
                Toast.makeText(this, "No such term exists", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Term is Empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteHandler(View v){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        if(!isEmpty(mtextTerm)) {
            mTaskFragment.delete(mtextTerm.getText().toString());

            mtextTerm.setText("");
            mTextDef.setText("");
            Toast.makeText(this, "Was deleted successfully", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Term is Empty", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
