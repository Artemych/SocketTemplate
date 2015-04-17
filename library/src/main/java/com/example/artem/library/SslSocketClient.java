package com.example.artem.library;

import android.text.TextUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import javax.net.ssl.SSLEngine;

public class SslSocketClient extends Thread {

    private String mHost;
    private int mPort;
    private SSLEngine mSslEngine;
    private ChannelFutureListener mChannelFutureListener;
    private SslClientHandler mSslClientHandler;

    private SslSocketClient() {}

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new SslChannelInitializer(mSslEngine, mSslClientHandler));
            Channel ch = b.connect(mHost, mPort).sync().channel();
            ChannelFuture lastWriteFuture = null;

            while (!isInterrupted()) {
                lastWriteFuture = ch.writeAndFlush(SocketNetworkService.OUTPUT_QUEUE.take().getByteBuff());
                if (mChannelFutureListener != null) {
                    lastWriteFuture.addListener(mChannelFutureListener);
                }
            }

            if (lastWriteFuture != null) {
                lastWriteFuture.sync();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static Builder newBuilder() {
        return new SslSocketClient().new Builder();
    }

    public class Builder {
        private Builder() {

        }

        public Builder setHost(String host) {
            mHost = host;
            return this;
        }

        public Builder setPort(int port) {
            mPort = port;
            return this;
        }

        public Builder setSslEngine(SSLEngine sslEngine) {
            mSslEngine = sslEngine;
            return this;
        }

        public Builder setChannelFutureListener(ChannelFutureListener listener) {
            mChannelFutureListener = listener;
            return this;
        }

        public Builder setClientHandler(SslClientHandler sslClientHandler) {
            mSslClientHandler = sslClientHandler;
            return this;
        }

        public SslSocketClient build() {
            if (TextUtils.isEmpty(mHost)) {
                throw new RuntimeException("Host is empty!");
            }

            if (mPort == 0) {
                throw new RuntimeException("Port is zero!");
            }

            if (mSslClientHandler == null) {
                throw new RuntimeException("Client handler can't be null!");
            }

            return SslSocketClient.this;
        }
    }
}
