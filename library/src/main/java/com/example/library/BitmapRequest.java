package com.example.library;

import android.content.Context;
import android.widget.ImageView;

import java.lang.ref.SoftReference;

/**
 * 图片请求对象封装类
 */
public class BitmapRequest {

    //请求路径
    private String url;

    private Context context;

    //需要加载图片的控件
    private SoftReference<ImageView> imageView;

    //图片的标识
    private String urlMd5;

    //占位图
    private int resID;

    //回调对象
    private RequestListener requestListener;

    public BitmapRequest(Context context){
        this.context = context;
    }

    //加载图片url
    public BitmapRequest load(String url){
        this.url = url;
        this.urlMd5 = MD5Utils.toMD5(url);
        return this;
    }

    public BitmapRequest loading(int resID){
        this.resID = resID;
        return this;
    }

    public BitmapRequest listener(RequestListener listener){
        this.requestListener = listener;
        return this;
    }

    public void into(ImageView imageView){
        imageView.setTag(this.urlMd5);
        this.imageView = new SoftReference<>(imageView);
        RequestManager.getInstance().addBitmapRequest(this);
    }

    public String getUrl() {
        return url;
    }

    public Context getContext() {
        return context;
    }

    public ImageView getImageView() {
        return imageView.get();
    }

    public String getUrlMd5() {
        return urlMd5;
    }

    public int getResID() {
        return resID;
    }

    public RequestListener getRequestListener() {
        return requestListener;
    }
}
