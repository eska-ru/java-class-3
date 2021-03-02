package lesson_6;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class ArrayUtilsParametersTest {
    private ArrayUtils arrayUtils;

    private Integer[] arr;
    private Boolean res;

    public ArrayUtilsParametersTest(Integer[] arr, Boolean res) {
        this.arr = arr;
        this.res = res;
    }

    @Parameters
    public static Collection paramCollection() {
        return Arrays.asList(new Object[][]{
                {new Integer[]{1,1,1,4}, true},
                {new Integer[]{1,1,1,1}, false},
                {new Integer[]{1}, false},
                {new Integer[]{}, false},
        });
    }

    @Before
    public void initTest() {
        arrayUtils = new ArrayUtils();
        System.out.println("Init ArrayUtils");
    }

    @After
    public void destroy() {
        arrayUtils = null;
    }

    @Test
    public void testIsOneAndFourIn() {
        Assert.assertEquals(res, arrayUtils.isOneAndFourIn(arr));
    }
}
