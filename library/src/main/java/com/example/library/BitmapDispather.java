package com.example.library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import com.example.library.disk.DoubleLruCache;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;

public class BitmapDispather extends Thread {

    Handler handler = new Handler(Looper.getMainLooper());
    LinkedBlockingQueue<BitmapRequest> requestQueue;
    private DoubleLruCache doubleLruCache = new DoubleLruCache(MyApplication.instance);

    public BitmapDispather(LinkedBlockingQueue<BitmapRequest> requestQueue) throws IOException {
        this.requestQueue = requestQueue;
    }

    @Override
    public void run() {
        super.run();
        while (!isInterrupted()){
            try {
                BitmapRequest br = requestQueue.take();
                //设置站位图片
                showLoadingImg(br);
                //加载图片
                Bitmap bitmap = findBitmap(br);
                //给图片显示到imageView
                showImageView(br,bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showImageView(final BitmapRequest br, final Bitmap bitmap) {
        if (bitmap!=null&&br.getImageView()!=null&&br.getUrlMd5().equals(br.getImageView().getTag())){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    br.getImageView().setImageBitmap(bitmap);
                    if (br.getRequestListener()!=null){
                        br.getRequestListener().onSuccess(bitmap);
                    }
                }
            });
        }
    }

    private void showLoadingImg(final BitmapRequest br) {
        if (br.getResID()>0&&br.getImageView()!=null)
            handler.post(new Runnable() {
                @Override
                public void run() {
                    br.getImageView().setImageResource(br.getResID());
                }
            });
    }

    private Bitmap findBitmap(BitmapRequest br) throws IOException {
        Bitmap bitmap = null;
        bitmap = doubleLruCache.get(br);
        if (bitmap==null) {
            bitmap = downloadImage(br.getUrl());
            if (bitmap!=null)
                doubleLruCache.put(br,bitmap);
        }

        return bitmap;
    }

    private Bitmap downloadImage(String uri) {
        FileOutputStream fos = null;
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (is!=null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }
}
