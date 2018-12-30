package com.example.salmanyousaf.lawyerlocator.Helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

public final class Base64ToBitmap {

    public Bitmap ConvertBase64ToBitmap(String recieved)
    {
        Bitmap bitmap = null;

        //Replacing "/" Sign because it causes problem in Asp.net api request.
        String replaced = recieved.replace(".", "/");

        try {
            byte[] decodedString = Base64.decode(replaced, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
        catch(IllegalArgumentException e){
            Log.e("Base64ToBitmap", "Wrong string for bitmap conversion");
        }

        return bitmap;
    }

}
