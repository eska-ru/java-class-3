package lesson_5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Race {
    private ArrayList<Stage> stages;
    private final ReentrantLock locker;
    private final Condition condition;

    private Integer readyCarsCount = 0;

    private Boolean isStarted = false;

    public ArrayList<Stage> getStages() {
        return stages;
    }

    public void addReadyCar() {
        ++readyCarsCount;
        locker.lock();
        condition.signalAll();
        locker.unlock();
    }

    public Boolean isStarted() {
        return isStarted;
    }

    public void startTheRace() {
        isStarted = true;
    }

    public Integer getReadyCarsCount() {
        return readyCarsCount;
    }

    public Race(Stage... stages) {
        this.stages = new ArrayList<>(Arrays.asList(stages));
        locker = new ReentrantLock();
        condition = locker.newCondition();
    }

    public ReentrantLock getLocker() {
        return locker;
    }

    public Condition getCondition() {
        return condition;
    }
}