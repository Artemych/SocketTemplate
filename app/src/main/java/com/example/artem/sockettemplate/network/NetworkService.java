package com.example.artem.sockettemplate.network;

import android.content.Intent;
import android.os.IBinder;

import com.example.artem.library.AppReader;
import com.example.artem.library.SocketNetworkService;
import com.example.artem.library.SslClientHandler;
import com.example.artem.library.SslSocketClient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class NetworkService extends SocketNetworkService {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected AppReader getAppReader() {
        return new AppReader<AppMessage>() {
            @Override
            protected void runCommandParser(AppMessage message) {

            }
        };
    }

    @Override
    protected SslSocketClient getSocketClient() {
        return SslSocketClient.newBuilder()
                .setHost("62.109.11.16")
                .setPort(5000)
                .setClientHandler(new SslClientHandler<AppMessage>() {
                    @Override
                    public void onException(Throwable e) {

                    }

                    @Override
                    protected AppMessage convertByteBuffToMessage(ByteBuf in) {
                        return null;
                    }
                })
                .setChannelFutureListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) {
                        if (!future.isSuccess()) {
                            future.cause().printStackTrace();
                        }
                    }
                })
                .setSslEngine(null)
                .build();
    }
}
