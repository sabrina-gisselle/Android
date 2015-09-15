package com.example.sabrina.urlchecker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends ActionBarActivity {

    EditText mUrlEditText = null;
    ScannerThread mThread;
    String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUrlEditText = (EditText) findViewById(R.id.urlEditText);
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

    public void enterHandler(View v) throws IOException {
        mThread = new ScannerThread();
        mThread.start();
    }

    private class ScannerThread extends Thread {

        @Override
        public void run(){
            BufferedReader in = null;
            if( !interrupted()){
                try {
                    address = String.valueOf(mUrlEditText.getText());
                    Uri site = Uri.parse(address);
                    Intent intentToSite = new Intent(Intent.ACTION_VIEW, site);
                    PackageManager pm = getPackageManager();
                    List<ResolveInfo> activities = pm.queryIntentActivities(intentToSite, 0);
                    Boolean safe = activities.size() > 0;
                    if (safe) {
                        URL url = new URL(site.toString());
                        in = new BufferedReader(new InputStreamReader(url.openStream()));
                        String pattern = "tiger";
                        String inputLine;
                        Boolean matches = false;
                        while ((inputLine = in.readLine()) != null) {
                            Pattern p = Pattern.compile(pattern);
                            Matcher m = p.matcher(inputLine);
                            matches = m.find();
                            if (matches) {
                                break;
                            }

                        }
                        Intent intent = new Intent(getBaseContext(), UrlActivity.class);
                        intent.putExtra("MATCHES", matches);
                        intent.putExtra("SITE", address);
                        startActivity(intent);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
