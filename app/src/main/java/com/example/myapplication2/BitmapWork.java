package com.example.myapplication2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class BitmapWork {

    public static byte[] getArrayByte(Bitmap bitmap) {
        if(bitmap!=null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] photo = baos.toByteArray();
            return photo;
        }
        else{
            return null;
        }
    }

    public static Bitmap getBitmap(byte[] Image) {
        if (Image!=null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(Image);
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            return theImage;
        }
        else{
            return null;
        }
    }

}
