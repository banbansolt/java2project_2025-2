package w0903;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RockGame {
    public static void main(String[] args) {
        String[] result = new String[10000];
//        A와 B가 선택한 가위, 바위, 보를 저장할 수 있는 변수
        String comA, comB;
//        이긴 횟수 또는 비긴 횟수를 저장하는 변수
        int aWin, bWin, noWin;
        String[] strArr = {"가위", "바위", "보"};

        for (int i = 0; i < result.length; i++) {
            Random random = new Random();
            comA = strArr[random.nextInt(strArr.length)];
            comB = strArr[random.nextInt(strArr.length)];

            if (comA.equals("가위")) {
                if(comB.equals("가위"))
                    result[i] = "없음";
                else if (comB.equals("바위"))
                    result[i] = "B";
                else if (comB.equals("보"))
                    result[i] = "A";
            }else if(comA.equals("바위")){
                if(comB.equals("가위"))
                    result[i] = "A";
                else if (comB.equals("바위"))
                    result[i] = "없음";
                else if (comB.equals("보"))
                    result[i] = "B";
            }else if(comA.equals("보")){
                if(comB.equals("가위"))
                    result[i] = "B";
                else if (comB.equals("바위"))
                    result[i] = "A";
                else if (comB.equals("보"))
                    result[i] = "없음";
            }
        }

        List<String> list = Arrays.asList(result);

        aWin = Collections.frequency(list, "A");
        bWin = Collections.frequency(list, "B");
        noWin = Collections.frequency(list, "없음");

        System.out.printf("%d번 중 Computer A의 승리 횟수: %d번\n",result.length, aWin);
        System.out.printf("%d번 중 Computer B의 승리 횟수: %d번\n",result.length, bWin);
        System.out.printf("%d번 중 Computer 비긴 경기 횟수: %d번\n",result.length, noWin);
    }
}
