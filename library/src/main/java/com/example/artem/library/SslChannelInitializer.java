package com.example.artem.library;

import javax.net.ssl.SSLEngine;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;

public class SslChannelInitializer extends ChannelInitializer<SocketChannel> {

    private SSLEngine mSslEngine;
    private SslClientHandler mSslClientHandler;

    public SslChannelInitializer(SSLEngine sslEngine, SslClientHandler sslClientHandler) {
        mSslEngine = sslEngine;
        mSslClientHandler = sslClientHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast();
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        if (mSslEngine != null) {
            pipeline.addLast(new SslHandler(mSslEngine));
        }
        pipeline.addLast(mSslClientHandler);
    }
}
