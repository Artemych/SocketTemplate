package com.example.artem.library;

import android.app.Service;
import android.content.Intent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class SocketNetworkService extends Service {

    public static final BlockingQueue<Message> INPUT_QUEUE = new LinkedBlockingDeque<>();
    public static final BlockingQueue<Message> OUTPUT_QUEUE = new LinkedBlockingDeque<>();

    private Thread mNioClient;
    private Thread mAppReader;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startNetworkThreads();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopNetworkThreads();
        super.onDestroy();
    }

    protected void startNetworkThreads() {
        if ((mNioClient != null && !mNioClient.isAlive()) || mNioClient == null) {
            mNioClient = getSocketClient();
            mNioClient.start();
        }

        if ((mAppReader != null && !mAppReader.isAlive()) || mAppReader == null) {
            mAppReader = getAppReader();
            mAppReader.start();
        }
    }

    protected void stopNetworkThreads() {
        if (mNioClient != null) {
            mNioClient.interrupt();
        }

        if (mAppReader != null) {
            mAppReader.interrupt();
        }
    }

    protected abstract AppReader getAppReader();

    protected abstract SslSocketClient getSocketClient();
}
