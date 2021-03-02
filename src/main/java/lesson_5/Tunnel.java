package lesson_5;

import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {
    private final Semaphore SEMAPHORE;

    public Tunnel(Integer capacity) {
        SEMAPHORE = new Semaphore(capacity);
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
    }

    @Override
    public void go(Car c) {
        System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
        try {
            SEMAPHORE.acquire();
            try {

                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(c.getName() + " закончил этап: " + description);
            }
            SEMAPHORE.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}