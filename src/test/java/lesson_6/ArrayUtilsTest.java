package lesson_6;

import org.junit.*;

public class ArrayUtilsTest {
    private static ArrayUtils arrayUtils;

    @BeforeClass
    public static void initTest() {
        arrayUtils = new ArrayUtils();
        System.out.println("Init ArrayUtils");
    }

    @AfterClass
    public static void destroy() {
        arrayUtils = null;
    }

    @Test
    public void testAfterFour01() {
        Integer[] arr = {1, 2, 4, 4, 2, 3, 4, 1, 7};
        Integer[] result = {1, 7};
        Assert.assertArrayEquals(result, arrayUtils.afterFour(arr));
    }

    @Test
    public void testAfterFour02() {
        Integer[] arr = {1, 2, 4, 4, 2, 3, 4, 1, 4};
        Integer[] result = {};
        Assert.assertArrayEquals(result, arrayUtils.afterFour(arr));
    }

    @Test(expected = RuntimeException.class)
    public void testAfterFour03() {
        Integer[] arr = {};
        arrayUtils.afterFour(arr);
    }

    @Test(expected = RuntimeException.class)
    public void testAfterFour04() {
        Integer[] arr = {1, 2, 2, 3, 1};
        arrayUtils.afterFour(arr);
    }
}
