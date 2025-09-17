package w0917;

import java.util.Scanner;

public class GeneratorPassword {
    public static boolean checkPassword(String password) {
        if(password.length() < 8)
            return false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if(!Character.isLetterOrDigit(c))
                return false;
        }
            return true;
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("비밀번호는 영문 또는 한글만 입력가능합니다.(최소 8글자 이상이어야 함)");
        System.out.print("Input New Password: ");
        String password = sc.nextLine();
        if(checkPassword(password))
            System.out.println("Good~~ 비밀번호가 올바르게 생성되었어요.");
        else
            System.out.println("오류! 비밀번호가 규칙에 맞지 않습니다.");

        sc.close();

    }
}
