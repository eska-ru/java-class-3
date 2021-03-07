package lesson_7;

import lesson_7.annotations.AfterSuite;
import lesson_7.annotations.BeforeSuite;
import lesson_7.annotations.Test;

public class SimpleMethodsTest {
    private static SimpleMethods simpleMethods;

    @BeforeSuite
    public static void init() {
        System.out.println("Init class");
        simpleMethods = new SimpleMethods();
    }

//    @BeforeSuite
//    public static void init2() {
//        System.out.println("Init class");
//        simpleMethods = new SimpleMethods();
//    }

    @AfterSuite
    public static void destroy() {
        System.out.println("Destroy class");
        simpleMethods = null;
    }

//    @AfterSuite
//    public static void destroy2() {
//        System.out.println("Destroy class");
//        simpleMethods = null;
//    }

    @Test(priority = 5)
    public static Boolean testSum01() {
        int x = 1;
        int y = 2;
        int result = 3;
        return result == simpleMethods.sum(x, y);
    }

    @Test(priority = 2)
    public static Boolean testSum02() {
        int x = 2;
        int y = 2;
        int result = 4;
        return result == simpleMethods.sum(x, y);
    }

    @Test(priority = 8)
    public static Boolean testMulti01() {
        int x = 2;
        int y = 2;
        int result = 5;
        return result == simpleMethods.multi(x, y);
    }

}
