package w0910.multi_return;

import java.util.Scanner;

//3개의 정수 값을 입력 받는 클래스
public class MultiInput {

    public int[] inputScores(){
        int[] scores = new int[3];
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < 3; i++) {
            System.out.print("정수" + (i+1) + " 입력:");
            scores[i] = scanner.nextInt();
        }

        scanner.close();

        return scores;
    }
}