package lesson_7.tester;

public interface Tester {
    public void start(Class<?> className);
    public void start(String className) throws ClassNotFoundException;
}
