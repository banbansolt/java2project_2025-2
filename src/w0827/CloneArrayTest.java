package w0827;

import java.util.Arrays;

public class CloneArrayTest {
    public static void main(String[] args) {
        //복제가 아닌 경우
        String[] orginArr1 = { "김치찌개", "잔치국수", "감자탕", "돈부리" };
        String[] orginArr2 = orginArr1;

        orginArr1[1] = "라구파스타";
        orginArr2[2] = "치즈돈까스";

        System.out.println(Arrays.toString(orginArr1));
        System.out.println(Arrays.toString(orginArr2));
        //복제되는 경우

        String[] orginArr = {"다니엘", "민지", "헤인", "해린"};
        String[] cloneArr = orginArr.clone();

        cloneArr[0] = "장원영";
        cloneArr[2] = "리즈";

        System.out.println(Arrays.toString(orginArr));
        System.out.println(Arrays.toString(cloneArr));
    }
}
