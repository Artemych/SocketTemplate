package com.example.artem.library;

public abstract class AppReader<T extends Message> extends Thread {

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                runCommandParser((T)SocketNetworkService.INPUT_QUEUE.take());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected abstract void runCommandParser(T message);
}

