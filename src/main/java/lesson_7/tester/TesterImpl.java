package lesson_7.tester;

import lesson_7.annotations.AfterSuite;
import lesson_7.annotations.BeforeSuite;
import lesson_7.annotations.Test;
import lesson_7.exceptions.TestRuntimeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

public class TesterImpl implements Tester {

    private Class<?> aClass;

    private Method afterSuite;
    private Method beforeSuite;
    private SortedMap<Integer, Method> tests = new TreeMap<>(Collections.reverseOrder());


    @Override
    public void start(Class<?> aClass) {
        System.out.println("Testing started");

        this.aClass = aClass;
        parsingMethods();
        startBeforeSuite();
        startTests();
        startAfterSuite();

        System.out.println("Testing ended");
    }

    @Override
    public void start(String className) throws ClassNotFoundException {
        this.start(Class.forName(className));
    }

    private void startBeforeSuite() {
        if (beforeSuite == null) {
            return;
        }
        System.out.println("Start BeforeSuite");

        try {
            beforeSuite.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private void startTests() {
        for (Method method : tests.values()) {
            System.out.format("Method %s is testing%n", method.toString());
            boolean result = false;
            try {
                result = (Boolean)method.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result) {
                System.out.println("Testing is passing");
            } else {
                System.out.println("Testing is NOT passing");
            }
            System.out.println();
        }
    }

    private void startAfterSuite() {
        if (afterSuite == null) {
            return;
        }
        System.out.println("Start AfterSuite");

        try {
            afterSuite.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parsingMethods() {
        for (Method method : aClass.getDeclaredMethods()) {
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                if (annotation.annotationType().equals(BeforeSuite.class)) {
                    if (beforeSuite != null) {
                        throw new TestRuntimeException("Only one BeforeSuite available");
                    }
                    beforeSuite = method;
                }

                else if (annotation.annotationType().equals(AfterSuite.class)) {
                    if (afterSuite != null) {
                        throw new TestRuntimeException("Only one AfterSuite available");
                    }

                    afterSuite = method;
                }

                else if (annotation.annotationType().equals(Test.class)) {
                    Test a = (Test)annotation;
                    tests.put(a.priority(), method);
                }
            }
        }
    }
}
