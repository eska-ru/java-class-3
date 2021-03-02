package lesson_1;

import java.util.ArrayList;
import java.util.Arrays;

/*
    В задании не указано, что именно передаётся в метод, поэтому считаем, что метод принимает массив значений
    и два элемента, которые в этот массив входят.
    Новый массив с поменянными элементами или null, если хотя бы одного элемента нет в массиве или элементы равные.
    Замена производится у последних найденных в массиве элементов.
 */

public class Task1 {


    public static <T> T[] swapElements(T[] array, T first, T second) {
        if (first == second) {
            return null;
        }

        Integer fIndex = null, sIndex = null;
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(first)) {
                fIndex = i;
            }
            if (array[i].equals(second)) {
                sIndex = i;
            }
        }

        if (fIndex == null || sIndex == null) {
            return null;
        }

        T[] result = array;
        result[fIndex] = second;
        result[sIndex] = first;
        return result;
    }

    public static void main(String[] args) {
        System.out.println("String array");
        String first = "First";
        String second = "Second";
        String third = "Third";
        String[] strArray = new String[]{first, second, third};
        System.out.println("Array: " + Arrays.toString(strArray));

        System.out.println("Array after swap: " + Arrays.toString(swapElements(strArray, first, second)));

        System.out.println();
        System.out.println("Integer array");
        Integer firstInt = 1;
        Integer secondInt = 2;
        Integer thirdInt = 3;
        Integer[] intArray = new Integer[]{firstInt, secondInt, thirdInt};
        System.out.println("Array: " + Arrays.toString(intArray));

        System.out.println("Array after swap: " + Arrays.toString(swapElements(intArray, firstInt, secondInt)));
    }

}
