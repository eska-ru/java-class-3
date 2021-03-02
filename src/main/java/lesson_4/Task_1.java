package lesson_4;

import static java.lang.Thread.sleep;

public class Task_1 {
    private static final int COUNT = 5;

    private static final Object LOCK_A = new Object();
    private static final Object LOCK_B = new Object();
    private static final Object LOCK_C = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                synchronized (LOCK_C) {
                    LOCK_C.wait();
                }
                for (int i = 0; i < COUNT; i++) {
                    System.out.println("C");
                    synchronized (LOCK_A) {
                        LOCK_A.notify();
                    }
                    synchronized (LOCK_C) {
                        LOCK_C.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                synchronized (LOCK_B) {
                    LOCK_B.wait();
                }
                for (int i = 0; i < COUNT; i++) {
                    System.out.println("B");
                    synchronized (LOCK_C) {
                        LOCK_C.notify();
                    }
                    synchronized (LOCK_B) {
                        LOCK_B.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            synchronized (LOCK_A) {
                try {
                    for (int i = 0; i < COUNT; i++) {
                        System.out.println("A");
                        synchronized (LOCK_B) {
                            LOCK_B.notify();
                        }
                        synchronized (LOCK_A) {
                            LOCK_A.wait();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            synchronized (LOCK_B) {
                LOCK_B.notifyAll();
            }
            synchronized (LOCK_C) {
                LOCK_C.notifyAll();
            }
        }).start();
    }
}
