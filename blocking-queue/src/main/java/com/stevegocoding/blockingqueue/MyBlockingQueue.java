package com.stevegocoding.blockingqueue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by magkbdev on 7/6/16.
 */
public class MyBlockingQueue<T> {

    private static final int DEFAULT_CAPACITY = 128;

    private ReentrantLock lock = new ReentrantLock();

    private Condition notFull = lock.newCondition();

    private Condition notEmpty = lock.newCondition();

    private T[] queue;

    private int capacity;

    private int size;

    private int head;

    private int tail;

    public MyBlockingQueue() {
        this(DEFAULT_CAPACITY);
    }

    public MyBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = (T[]) new Object[this.capacity];
    }

    public void put(T elem) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (size >= capacity)
                notFull.await();

            queue[tail++] = elem;
            tail = tail % capacity;
            ++size;
            notEmpty.signalAll();
        }
        finally {
            lock.unlock();
        }

    }

    public T take() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (size == 0)
                notEmpty.await();

            T value = queue[head];
            queue[head++] = null;
            head = head % capacity;
            --size;
            notFull.signalAll();
            return value;
        } finally {
            lock.unlock();
        }
    }
}
