package lesson_6;

import java.util.Arrays;
import java.util.List;

public class ArrayUtils {
    public Integer[] afterFour(Integer[] arr) {
        if (arr == null) {
            throw new NullPointerException();
        }

        int pos = arr.length-1;
        while (pos >= 0) {
            if (arr[pos] == 4) {
                break;
            }
            --pos;
        }

        if (pos < 0) {
            throw new RuntimeException();
        }

        return Arrays.copyOfRange(arr, pos+1, arr.length);
    }

    public Boolean isOneAndFourIn(Integer[] arr) {
        if (arr == null) {
            throw new NullPointerException();
        }

        List<Integer> list = Arrays.asList(arr);
        boolean one = list.contains(1);
        boolean four = list.contains(4);

        return one && four;
    }
}
