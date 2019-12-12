package com.example.library;

import android.content.Context;

public class Glide {

    public static BitmapRequest with(Context context){
        return new BitmapRequest(context);
    }
}
