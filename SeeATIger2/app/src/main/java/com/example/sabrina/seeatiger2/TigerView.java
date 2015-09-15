package com.example.sabrina.seeatiger2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by Sabrina on 1/14/2015.
 */
public class TigerView extends View {

    Bitmap mTigerBitmap = null; // Lowercase 'm' for data members

    public TigerView(Context context, Bitmap pic) {
        super(context);
        mTigerBitmap = pic;
    }

    @Override
    protected void onDraw(Canvas canvas){
        // Called when the view is drawn
        Paint paint = new Paint();
        paint.setFilterBitmap(true);

        //We'll need to scale the picture
        int halfViewWidth = this.getWidth()/2;
        int halfViewHeight = this.getHeight()/2;
        int halfWidth = mTigerBitmap.getWidth()/2;
        int halfHeight = mTigerBitmap.getHeight()/2;
        //Bad way
        Rect destShape = new Rect(halfViewWidth - halfWidth, halfViewHeight - halfHeight, halfViewWidth + halfWidth, halfViewHeight + halfHeight);
        canvas.drawBitmap(mTigerBitmap, null, destShape, paint);
    }
}
