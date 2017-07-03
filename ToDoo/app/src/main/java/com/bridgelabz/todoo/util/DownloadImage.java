package com.bridgelabz.todoo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DownloadImage
{
   public static final String TAG = "DownloadImage";
   public static Bitmap mBitmap;

    public static void downloadImage(String url, final DownloadImageInterface image)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference().child(url);

        final long IMAGE_SIZE = 1024*1024;
        reference.getActiveDownloadTasks();
        reference.getBytes(IMAGE_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>()
        {
            @Override
            public void onSuccess(byte[] bytes)
            {
                //Download Only Single Image
                mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Debug.showLog(TAG,"Downloading Image..");
                image.getImage(mBitmap);
            }
        });

    }
}

