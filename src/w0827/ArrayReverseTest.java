package w0827;

import java.util.Arrays;
import java.util.Collections;

public class ArrayReverseTest {
    public static void main(String[] args) {
        String[] memberArr = {"레이","가을","안유진","이서","리즈"};
        System.out.println("처음배열: "+ Arrays.toString(memberArr));

        Collections.reverse(Arrays.asList(memberArr));

        System.out.println("역순배열: " + Arrays.toString(memberArr));
    }
}
