package com.bridgelabz.todoo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.bridgelabz.todoo.home.view.ToDoActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by bridgeit on 12/4/17.
 */
public class AsyncTaskLoadImage  extends AsyncTask<String, String, Bitmap>
{
    private final static String TAG = "AsyncTaskLoadImage";
    private ToDoActivity toDoActivity;
    public AsyncTaskLoadImage(ToDoActivity toDoActivity)
    {
        this.toDoActivity = toDoActivity;
    }
    @Override
    protected Bitmap doInBackground(String... params)
    {
        Bitmap bitmap = null;
        try
        {
            URL url = new URL(params[0]);
            bitmap = BitmapFactory.decodeStream((InputStream)url.getContent());
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage());
        }
        return bitmap;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
        toDoActivity.setImage(bitmap);
    }
}