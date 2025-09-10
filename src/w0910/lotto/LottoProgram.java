package w0910.lotto;

import java.util.ArrayList;
import java.util.Arrays;

public class LottoProgram {
    public static void main(String[] args) {
        System.out.println("========     Lotto Program     ========");
        ArrayList<Integer> numList = new ArrayList<Integer>();
        int randNum = 0;
        RandomNumber rn = new RandomNumber();

        reFindNum:
        while(true) {
            randNum = rn.randomNum(45);
            for (int num : numList) {
                if(randNum==num)
                    continue reFindNum;
            }

            numList.add(randNum);

            if(numList.size()==6)
                break;
        }

        Object[] numArr = numList.toArray();
        Arrays.sort(numArr);
        System.out.println("======== 이번 주 로또 당첨 번호 ========");
        System.out.println(Arrays.toString(numArr));
    }
}
