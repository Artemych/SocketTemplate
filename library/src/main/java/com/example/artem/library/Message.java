package com.example.artem.library;

import io.netty.buffer.ByteBuf;

public abstract class Message {

    public abstract ByteBuf getByteBuff();

    public void send() {
        SocketNetworkService.OUTPUT_QUEUE.add(this);
    }
}
