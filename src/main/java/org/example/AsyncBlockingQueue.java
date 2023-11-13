package org.example;

import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

public class AsyncBlockingQueue<T> {
    private final LinkedList<T> queue = new LinkedList<>();

    public CompletableFuture<Void> asyncPut(T element) {
        return CompletableFuture.runAsync(() -> {
            synchronized (queue) {
                queue.add(element);
                queue.notifyAll();
            }
        });
    }

    public CompletableFuture<T> asyncTake() {
        return CompletableFuture.supplyAsync(() -> {
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                T item = queue.remove();
                queue.notifyAll();
                return item;
            }
        });
    }

    public void put(T element) {
        synchronized (queue) {
            queue.add(element);
            queue.notifyAll();
        }
    }

    public T take() throws InterruptedException {
        synchronized (queue) {
            while (queue.isEmpty()) {
                queue.wait();
            }
            T item = queue.remove();
            queue.notifyAll();
            return item;
        }
    }
}