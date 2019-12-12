package com.example.library.disk;

import android.graphics.Bitmap;
import android.support.v4.app.NavUtils;
import android.util.LruCache;

import com.example.library.BitmapRequest;

public class MemoryCache implements BitmapCache {

    private LruCache<String,Bitmap> lruCache;
    private static volatile MemoryCache instance;
    private static final byte[] lock = new byte[0];

    public static MemoryCache getInstance(){
        if (instance==null){
            synchronized (lock){
                instance = new MemoryCache();
            }
        }
        return instance;
    }

    private MemoryCache(){
        int maxMemorySize = (int) (Runtime.getRuntime().maxMemory()/16);
        if (maxMemorySize<=0){
            maxMemorySize = 10*1024*1024;
        }
        lruCache = new LruCache<String,Bitmap>(maxMemorySize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        if (bitmap!=null){
            lruCache.put(request.getUrlMd5(),bitmap);
        }
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        return lruCache.get(request.getUrlMd5());
    }

    @Override
    public void remove(BitmapRequest request) {
        lruCache.remove(request.getUrlMd5());
    }
}
