package com.example.library.disk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.example.library.BitmapRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DiskBitmapCache implements BitmapCache {

    private DiskLruCache diskLruCache;
    private static volatile DiskBitmapCache instance;
    private String imageCachePath = "Image";
    private static final byte[] lock = new byte[0];
    private int MB = 1024*1024;
    private int maxDiskSize = 50*MB;

    private DiskBitmapCache(Context context) throws IOException {
        File cacheFile = getImageCacheFile(context,imageCachePath);
        if (!cacheFile.exists()){
            cacheFile.mkdirs();
        }
        diskLruCache = DiskLruCache.open(cacheFile,getAppVersion(context),1,maxDiskSize);
    }

    private int getAppVersion(Context context) {
        int appVersionCode = 0;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appVersionCode = (int) packageInfo.getLongVersionCode();
            } else {
                appVersionCode = packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return appVersionCode;

    }

    private File getImageCacheFile(Context context,String imageCachePath) {
        String path;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            path = context.getExternalCacheDir().getPath();
        }else{
            path = context.getCacheDir().getPath();
        }
        return new File(path+File.separator+imageCachePath);
    }

    public static DiskBitmapCache getInstance(Context context) throws IOException {
        if (instance==null){
            synchronized (lock){
                if (instance==null){
                    instance = new DiskBitmapCache(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        DiskLruCache.Editor editor;
        OutputStream outputStream = null;
        try {
            editor = diskLruCache.edit(request.getUrlMd5());
            outputStream = editor.newOutputStream(0);
            if (presetBitmap2Disk(outputStream,bitmap)){
                editor.commit();
            }else{
                editor.abort();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (outputStream!=null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean presetBitmap2Disk(OutputStream outputStream, Bitmap bitmap) throws IOException {
        BufferedOutputStream bufferedOutputStream =null;
        bufferedOutputStream = new BufferedOutputStream(outputStream);
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bufferedOutputStream);
        try {
            bufferedOutputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bufferedOutputStream!=null)
                bufferedOutputStream.close();
        }
        return false;
    }

    @Override
    public Bitmap get(BitmapRequest request) throws IOException {
        InputStream stream = null;
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(request.getUrlMd5());
            if (snapshot!=null){
                stream = snapshot.getInputStream(0);
                return BitmapFactory.decodeStream(stream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (stream!=null)
                stream.close();
        }
        return null;
    }

    @Override
    public void remove(BitmapRequest request) throws IOException {
        diskLruCache.remove(request.getUrlMd5());
    }
}
