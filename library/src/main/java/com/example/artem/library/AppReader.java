package com.example.artem.library;

public abstract class AppReader extends Thread {

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                runCommandParser(SocketNetworkService.INPUT_QUEUE.take());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected abstract void runCommandParser(Message message);
}

