package com.example.library;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestManager {
    private static RequestManager requestManager;

    static {
        try {
            requestManager = new RequestManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //创建阻塞队列
    private LinkedBlockingQueue<BitmapRequest> requestqueue = new LinkedBlockingQueue<>();

    //创建一个线程数组
    private BitmapDispather[]bitmapDispathers;

    public static RequestManager getInstance(){
        return requestManager;
    }

    private RequestManager() throws IOException {
        start();
    }
    //将图片请求添加到队列中
    public void addBitmapRequest(BitmapRequest request){
        if (request==null)
            return;
        if (!requestqueue.contains(request))
            requestqueue.add(request);
    }

    //创建并开始所有的线程
    public void startAllDisPatcher() throws IOException {
        //获取手机支持的单个应用最大线程数
        int threadCount = Runtime.getRuntime().availableProcessors();
        bitmapDispathers = new BitmapDispather[threadCount];
        for (int i = 0; i < threadCount; i++) {
            BitmapDispather bitmapDispather = new BitmapDispather(requestqueue);
            bitmapDispather.start();
            //将每个dispatcher放到数组中，方便统一管理
            bitmapDispathers[i] = bitmapDispather;
        }

    }

    //停止所有线程
    public void stop(){
        if (bitmapDispathers!=null&&bitmapDispathers.length>0){
            for (BitmapDispather bitmapDispather : bitmapDispathers) {
                if (!bitmapDispather.isInterrupted())
                bitmapDispather.interrupt();
            }
        }
    }

    public void start() throws IOException {
        stop();
        startAllDisPatcher();
    }
}
