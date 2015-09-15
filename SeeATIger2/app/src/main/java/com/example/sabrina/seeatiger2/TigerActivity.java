package com.example.sabrina.seeatiger2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class TigerActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bitmap bmap = BitmapFactory.decodeResource(getResources(), R.drawable.tiger);
        setContentView(new TigerView(this, bmap));
    }
}
