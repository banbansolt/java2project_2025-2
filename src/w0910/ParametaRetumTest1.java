package w0910;

import java.util.Scanner;

public class ParametaRetumTest1 {
    public static void main(String[] args) {
        System.out.println("======2개 정수의 합계를 구해드립니다======: ");
        Scanner sc = new Scanner(System.in);
        System.out.print("정수1 입력: ");
        int num1 = sc.nextInt();
        System.out.print("정수2 입력: ");
        int num2 = sc.nextInt();

        Calc1 calc1 = new Calc1();
        int result = calc1.plus(num1,num2);
        System.out.println("덧셈결과 =" + result);
    }
}
