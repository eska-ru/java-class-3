package lesson_1;

import java.util.ArrayList;
import java.util.Arrays;

public class Task2 {
    public static <T> ArrayList<T> toArrayList(T[] array) {
        // Вот так правильнее, но тогда вообще смысл этого метода теряется :)
        // ArrayList<T> list = new ArrayList<>(Arrays.asList(array));
        // Поэтому будем перебором:
        ArrayList<T> list = new ArrayList<>();
        for (T t : array) {
            list.add(t);
        }

        return list;
    }

    public static void main(String[] args) {
        System.out.println("String array");
        String[] strArray = new String[]{"First", "Second", "Third"};
        System.out.println("Array: " + Arrays.toString(strArray));

        ArrayList<String> strList = toArrayList(strArray);
        System.out.println("ArrayList: " + strList);

        System.out.println();
        System.out.println("Integer array");
        Integer[] intArray = new Integer[]{1, 2, 3};
        System.out.println("Array: " + Arrays.toString(intArray));

        ArrayList<Integer> intList = toArrayList(intArray);
        System.out.println("ArrayList: " + intList);
    }
}
