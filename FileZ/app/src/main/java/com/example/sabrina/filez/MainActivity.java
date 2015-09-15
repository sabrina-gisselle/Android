package com.example.sabrina.filez;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        writeFileToInternalStorage();
        ((TextView) findViewById(R.id.textArea)).setText(
                readFileFromInternalStorage()
        );
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

    private void writeFileToInternalStorage(){
        // Here if the file exists it will overwrite it or create the file if it doesn't exist
        PrintWriter writer = null;
        try{
            FileOutputStream fos = openFileOutput("file_data.txt", MODE_WORLD_READABLE);
            writer = new PrintWriter(
                    new BufferedOutputStream(fos));
            writer.println("What's better than a tiger?");
            writer.println("Two Tigers!");
        } catch( Exception e ){
            e.printStackTrace();
        }finally{
            writer.close();
        }
    }

    private CharSequence readFileFromInternalStorage(){
        String eol = System.getProperty("line.separator");

        // don't append all strings because every string is immutable so you are not appending you are creating a different string instead use stringBuilder

        StringBuilder builder = new StringBuilder();
        BufferedReader input = null;
        try{
            input = new BufferedReader(
                        new InputStreamReader(
                            openFileInput("file_data.txt")));
            String line;
            while ( (line = input.readLine()) != null){
                builder.append(line);
                builder.append( eol);
            }
        }catch( Exception e){
            e.getStackTrace();
        }finally{
            if(input != null){
                try{
                    input.close(); // closing the file can cause a exception so we have to add a nested try catch
                } catch( IOException e){
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }
}
