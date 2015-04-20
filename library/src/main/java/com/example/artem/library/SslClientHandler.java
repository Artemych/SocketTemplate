package com.example.artem.library;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public abstract class SslClientHandler<T extends Message> extends ChannelInboundHandlerAdapter implements SocketExceptionHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final ByteBuf in = (ByteBuf) msg;
        try {
            if (in.isReadable()) {
                SocketNetworkService.INPUT_QUEUE.add(convertByteBuffToMessage(in));
            }
        } catch (Exception e) {
            onException(e);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        onException(cause);
    }

    protected abstract T convertByteBuffToMessage(ByteBuf in);
}
