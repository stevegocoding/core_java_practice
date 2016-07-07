package com.stevegocoding.blockingqueue;


import static java.lang.Thread.sleep;

/**
 * Created by magkbdev on 7/6/16.
 */

class Producer implements Runnable {
    private MyBlockingQueue<Integer> sharedQueue;

    public Producer(MyBlockingQueue<Integer> q) {
        this.sharedQueue = q;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; ++i) {
            try {
                System.out.println("Produced: " + i);
                sharedQueue.put(i);
            }
            catch (InterruptedException e) {
                System.out.println(e.toString());
            }
        }
    }
}

class Consumer implements Runnable {

    private MyBlockingQueue<Integer> sharedQueue;

    public Consumer(MyBlockingQueue<Integer> q) {
        this.sharedQueue = q;
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; ++i) {
            try {
                int v = sharedQueue.take();
                System.out.println("Consumed: " + v);
                sleep(1000);
            }
            catch (InterruptedException e) {
                System.out.println(e.toString());
            }
        }
    }
}

public class Runner {

    public static final void main(String[] args) {


        MyBlockingQueue<Integer> q = new MyBlockingQueue<>(4);

        Thread producer1 = new Thread(new Producer(q));
        Thread producer2 = new Thread(new Producer(q));
        Thread producer3 = new Thread(new Producer(q));
        Thread producer4 = new Thread(new Producer(q));

        Thread consumer1 = new Thread(new Consumer(q));
        Thread consumer2 = new Thread(new Consumer(q));

        producer1.start();
        producer2.start();
        producer3.start();
        producer4.start();

        consumer1.start();
        consumer2.start();
    }
}
