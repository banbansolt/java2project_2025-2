package w0827;

import java.util.Arrays;

public class ChangeLengthArray {
    public static void main(String[] args) {
        int[] arr = {10, 20, 30, 40, 50};

        System.out.printf("현재 배열의 길이: %d\n", arr.length);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + "\t");
        }
        System.out.println();

        arr = Arrays.copyOf(arr, arr.length+3);
        System.out.printf("복사 후 배열의 길이: %d\n", arr.length);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + "\t");
        }
    }
}
