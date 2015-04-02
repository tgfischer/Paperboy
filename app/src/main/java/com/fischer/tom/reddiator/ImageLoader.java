package com.fischer.tom.reddiator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.fischer.tom.reddiator.content.RedditCache;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class ImageLoader extends AsyncTask<URI,Integer,Bitmap> {
    private URI imageUri;

    private ImageView imageView;
    private int preferredWidth = 80;
    private int preferredHeight = 80;

    public ImageLoader(URI uri, ImageView imageView, int scaleWidth, int scaleHeight) {
        this.imageUri = uri;
        this.imageView = imageView;
        this.preferredWidth = scaleWidth;
        this.preferredHeight = scaleHeight;
    }

    public Bitmap doInBackground(URI... params) {
        if(imageUri == null) return null;
        String url = imageUri.toString();

        if(url.length() == 0 || !url.contains("redditmedia")) return null;

        HttpGet httpGet = new HttpGet(url);
        DefaultHttpClient client = new DefaultHttpClient();

        try {
            HttpResponse response = client.execute( httpGet );
            InputStream is = new BufferedInputStream( response.getEntity().getContent() );

            Bitmap bitmap = BitmapFactory.decodeStream(is);
            //if (preferredWidth > 0 && preferredHeight > 0 && bitmap.getWidth() > preferredWidth && bitmap.getHeight() > preferredHeight) {
                //return Bitmap.createScaledBitmap(bitmap, preferredWidth, preferredHeight, false);
            //} else {
                return bitmap;
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void onPostExecute( Bitmap drawable ) {
        if (drawable == null) {
            imageView.setImageResource(R.drawable.blank_thumbnail);
        } else {
            imageView.setImageBitmap(drawable);
        }
    }
}