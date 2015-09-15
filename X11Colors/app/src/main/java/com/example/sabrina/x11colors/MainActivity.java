package com.example.sabrina.x11colors;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private ArrayList<XllColor> mList = null;
    private XllColorAdapter mAdapter = null;
    private int mNextColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView = (ListView) findViewById(R.id.list);

        if (mList == null )
        {
            mList = new ArrayList<XllColor>();
        }
        if(mAdapter == null) {
            mAdapter = new XllColorAdapter(this, mList);
        }
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
//              String item = (String) adapter.getItem(position);
//              Toast.makeText(getApplicationContext(), item + " selected", Toast.LENGTH_LONG).show();
                //final String item = (String) parent.getItemAtPosition( position );

                view.animate().setDuration( 500 ).alpha( 0 ).withEndAction(
                        new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.remove(position);
                                view.setAlpha( 1 );
                            }
                });
                //mList.remove( position );
                mAdapter.notifyDataSetChanged();

            }
        });
    }

public void addColorButtonHandler(View v){
    if(mNextColor < XllColor.COUNT){
        mAdapter.add(XllColor.getColor(mNextColor++));
    }else{
        Toast.makeText( this, "Sorry, no more colors.", Toast.LENGTH_LONG).show();
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
}
