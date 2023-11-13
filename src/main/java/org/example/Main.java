package org.example;

public class Main {
    public static void main(String[] args) {
        AsyncBlockingQueue<Integer> queue = new AsyncBlockingQueue<>();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                queue.asyncPut(i).join();
                System.out.println("асинхронно добавлено: " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                Integer item = queue.asyncTake().join();
                System.out.println("асинхронно извлечено: " + item);
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    queue.put(i);
                    System.out.println("добавлено: " + i);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Integer item = queue.take();
                    System.out.println("извлечено: " + item);
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();

    }
}