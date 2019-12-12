package com.example.library.disk;

import android.graphics.Bitmap;

import com.example.library.BitmapRequest;

import java.io.IOException;

public interface BitmapCache {

    /**
     * 存入内存
     * @param request
     * @param bitmap
     */
    void put(BitmapRequest request, Bitmap bitmap);

    /**
     * 读取缓存的图片
     * @param request
     * @return
     */
    Bitmap get(BitmapRequest request) throws IOException;

    /**
     * 清除缓存图片
     * @param request
     */
    void remove(BitmapRequest request) throws IOException;
}
