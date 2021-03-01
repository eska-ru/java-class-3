package lesson_5;

public class MainClass {
    public static final int CARS_COUNT = 4;

    public static void main(String[] args) {
        Race race = new Race(new Road(60), new Tunnel(CARS_COUNT/2), new Road(40));

        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Thread[] carThreads = new Thread[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            carThreads[i] = new Thread(cars[i]);
            carThreads[i].start();
        }

        race.getLocker().lock();
        try {
            while (race.getReadyCarsCount() != CARS_COUNT) {
                race.getCondition().await();
            }
            race.startTheRace();
            race.getCondition().signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            race.getLocker().unlock();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

        for (Thread carThread : carThreads) {
            try {
                carThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}