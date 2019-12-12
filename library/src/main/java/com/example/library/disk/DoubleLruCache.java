package com.example.library.disk;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.library.BitmapRequest;

import java.io.IOException;

public class DoubleLruCache implements BitmapCache {
    private MemoryCache lruCache;
    private DiskBitmapCache bitmapCache;

    public DoubleLruCache(Context context) throws IOException {
        bitmapCache = DiskBitmapCache.getInstance(context);
        lruCache = MemoryCache.getInstance();
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        bitmapCache.put(request,bitmap);
        lruCache.put(request,bitmap);
    }

    @Override
    public Bitmap get(BitmapRequest request) throws IOException {
        Bitmap bitmap = lruCache.get(request);
        if (bitmap==null){
            bitmap = bitmapCache.get(request);
            lruCache.put(request,bitmap);
        }
        return bitmap;
    }

    @Override
    public void remove(BitmapRequest request) throws IOException {
        lruCache.remove(request);
        bitmapCache.remove(request);
    }
}
