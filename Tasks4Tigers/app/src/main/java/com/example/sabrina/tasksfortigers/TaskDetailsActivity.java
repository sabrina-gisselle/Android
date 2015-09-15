package com.example.sabrina.tasksfortigers;

import android.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sabrina.tasksfortigers.contentprovider.TaskContentProvider;
import com.example.sabrina.tasksfortigers.db.TaskTable;

import org.apache.http.entity.ContentProducer;

import java.net.URI;


public class TaskDetailsActivity extends ActionBarActivity {

    private Spinner mPriority;
    private EditText mSummaryText;
    private EditText mDescriptionText;

    private Uri mTaskUri;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_task_details);

        mPriority = (Spinner) findViewById(R.id.priority);
        mSummaryText =( EditText) findViewById(R.id.todo_edit_summary);
        mDescriptionText =( EditText) findViewById(R.id.todo_edit_description);

        Button confirmButton = (Button) findViewById(R.id.todo_edit_button);
        Bundle extras = getIntent().getExtras();
        mTaskUri = (bundle == null ? null : (Uri) bundle
                .getParcelable(TaskContentProvider.CONTENT_ITEM_TYPE));
        if ( extras != null){
            mTaskUri = extras.getParcelable(TaskContentProvider.CONTENT_ITEM_TYPE);
            fillData( mTaskUri);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mSummaryText.getText().toString())) {
                    makeToast();
                } else {
                    saveState();
                    Log.d("TaskDetailActivity:", "confirm button clicked");
                    setResult(RESULT_OK);
                }
            }
        });
    }


    private void fillData( Uri uri){
        String[] projection = {TaskTable.COLUMN_SUMMARY, TaskTable.COLUMN_DESCRIPTION, TaskTable.COLUMN_PRIORITY};

        Cursor cursor = getContentResolver().query( uri, projection, null, null, null );
        if (cursor != null ){
            cursor.moveToFirst();
            String priority = cursor.getString(
                    cursor.getColumnIndexOrThrow( TaskTable.COLUMN_PRIORITY ));
            for (int i = 0; i < mPriority.getCount(); ++i){
                String s = (String) mPriority.getItemAtPosition( i );
                if( s.equalsIgnoreCase( priority )){
                    mPriority.setSelection( i );
                }
            }
        }
        mSummaryText.setText(cursor.getString( cursor.getColumnIndexOrThrow(TaskTable.COLUMN_SUMMARY)));
        mDescriptionText.setText(cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COLUMN_DESCRIPTION)));
        cursor.close();
    }

    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable( TaskContentProvider.CONTENT_ITEM_TYPE, mTaskUri);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState(){
        String priority = (String) mPriority.getSelectedItem();
        String summary = mSummaryText.getText().toString();
        String description = mDescriptionText.getText().toString();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_details, menu);
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
}
