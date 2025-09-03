package w0903;

import java.awt.desktop.SystemSleepEvent;
import java.util.Scanner;

public class CalcPlus {
    public static int plus(int num1, int num2) {
        int result = num1 + num2;
        return result;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("프로그램 종료를 원하면 정수1에 -1을 입력하시오");
            System.out.print("정수1입력:");
            int num1 = sc.nextInt();
            if(num1==-1)
                break;
            System.out.print("* 정수2 입력:");
            int num2 = sc.nextInt();

        int sum = plus(num1, num2);
        System.out.printf("%d+%d=%d\n", num1, num2, sum);
 //       sc.close();
        }
    }
}
