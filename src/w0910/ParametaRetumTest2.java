package w0910;

import java.util.ArrayList;

public class ParametaRetumTest2 {
    public static void main(String[] args) {
       int[] nums = {1,3,5,7,9,11};
       Calc1 calc = new Calc1();
       int result = calc.plus(nums);

       System.out.println("배열에 저장된 정수 값들의 합계:"+result);

        ArrayList<Integer> List = new ArrayList<Integer>();
        List.add(10);
        List.add(22);
        List.add(33);
        List.add(11);

        result = calc.plus(List);

        System.out.println("리스트에 추가된 정수의 합계"+result);
    }

}
