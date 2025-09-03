package w0903;

import java.util.Scanner;

public class Lab10 {

    public static int calc(int num1, int num2, char operator) {
        int result = 0;
        switch(operator) {
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
                result = num1 / num2;
                break;
            default:
                result = 0;
        }

        return result;
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.println("★ 연산기호 입력부분에 소문자 e를 입력하면 프로그램 종료됨");
            System.out.print("계산할 연산기호를 입력(+, -, *, /):");
            char operator = sc.next().charAt(0);

            if (operator == 'e')
                break;

            System.out.print("정수1 입력:");
            int num1 = sc.nextInt();
            System.out.print("정수2 입력:");
            int num2 = sc.nextInt();
            int result = calc(num1, num2, operator);

            System.out.printf("%d %c %d = %d\n", num1, operator, num2, result);
        }

        System.out.println("프로그램 종료");
        sc.close();
    }

}
