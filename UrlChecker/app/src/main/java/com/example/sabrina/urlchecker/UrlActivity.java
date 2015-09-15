package com.example.sabrina.urlchecker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class UrlActivity extends ActionBarActivity {
    String mSite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            boolean matches = extras.getBoolean("MATCHES");
            String site = extras.getString("SITE");
            mSite = site;
            if (matches) {
                goToast();
            }else{
                dontGoToast();
            }
        }else{
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    public void dontGoToast(){
        String text = getResources().getString(R.string.noGo);
        LayoutInflater inflater = getLayoutInflater();
        View layout =
                inflater.inflate(R.layout.toast_layout,
                        (ViewGroup) findViewById(R.id.toast_layout_root));

        ImageView imageView = (ImageView) layout.findViewById( R.id.image );
        imageView.setImageResource( R.drawable.tiger_stop );

        TextView textView = (TextView) layout.findViewById( R.id.text );
        textView.setText(text);

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.FILL_HORIZONTAL|Gravity.FILL_VERTICAL, 0, 0);
        toast.setView( layout );
        toast.show();
    }

    public void goToast(){
        String text = getResources().getString(R.string.go);
        LayoutInflater inflater = getLayoutInflater();
        View layout =
                inflater.inflate(R.layout.toast_layout,
                        (ViewGroup) findViewById(R.id.toast_layout_root));

        ImageView imageView = (ImageView) layout.findViewById( R.id.image );
        imageView.setImageResource( R.drawable.tiger_go );

        TextView textView = (TextView) layout.findViewById( R.id.text );
        textView.setText(text);

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.FILL_HORIZONTAL|Gravity.FILL_VERTICAL, 0, 0);
        toast.setView( layout );
        toast.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3500); // As I am using LENGTH_LONG in Toast
                    Uri uri = Uri.parse(mSite);
                    Intent intentToSite = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intentToSite);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_url, menu);
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

    public void goSiteHandler(View v){
        Uri uri = Uri.parse(mSite);
        Intent intentToSite = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intentToSite);
    }
}
