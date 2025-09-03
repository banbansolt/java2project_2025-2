package w0903;

import java.util.Random;

public class UserMethod1 {
    public static void outputDice(String userName) {
        System.out.println(userName+"님, 주사위를 던집니다");
        Random random = new Random();
        int diceNum = random.nextInt(6)+1;
        System.out.println("주사위 던진 결과:"+diceNum);
    }
    public static void main(String[] args) {
        outputDice("리즈");
        outputDice("다니엘");
        outputDice("원영");
        outputDice("유진");

    }
}
