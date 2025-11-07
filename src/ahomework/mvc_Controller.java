package ahomework;


import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.Connection;

import jdbc_test.JDBCConnector;

public class mvc_Controller {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice = -1;

        System.out.println("=========================================");
        System.out.println("        ✨ MVC 프로그램 선택 컨트롤러 ✨");
        System.out.println("=========================================");

        while (choice != 0) {
            System.out.println("\n--- 실행할 프로그램 로직을 선택하세요 ---");
            System.out.println("1. mvc_main (고객 정보 수정 로직)");
            System.out.println("2. mvc_main2 (고객 정보 삭제 로직)");
            System.out.println("0. 프로그램 종료");
            System.out.print("선택 입력 (0, 1, 2): ");

            try {
                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        executeMainLogic(1);
                        break;
                    case 2:
                        executeMainLogic(2);
                        break;
                    case 0:
                        System.out.println("\n프로그램을 종료합니다. 감사합니다.");
                        break;
                    default:
                        System.out.println("⚠️ [오류] 잘못된 선택입니다. 0, 1, 2 중 하나를 다시 입력하세요.");
                }
            } catch (InputMismatchException e) {
                System.out.println("⚠️ [오류] 숫자를 입력해 주세요.");
                sc.nextLine();
                choice = -1;
            } catch (Exception e) {
                System.out.println("실행 중 알 수 없는 오류가 발생했습니다: " + e.getMessage());
            }
        }
        sc.close();
    }


    private static void executeMainLogic(int mainNumber) {
        Connection con = null;
        try {
            System.out.println("\n-----------------------------------------");
            // 👉 로직 실행 메시지 출력
            System.out.printf("👉 mvc_main%s 로직 실행 시작...%n", (mainNumber == 1 ? "" : "2"));
            con = JDBCConnector.getConnection();

            if (con == null) {
                System.out.println("❌ DB 연결 실패. 로직을 실행할 수 없습니다.");
                return;
            }

            if (mainNumber == 1) {
                // 💡 수정된 부분: mvc_main 클래스의 updateCustomer 메서드를 호출합니다.
                mvc_main.updateCustomer(con);

            } else if (mainNumber == 2) {
                mvc_main2.deleteCustomerById(con);
            }

            System.out.printf("✅ mvc_main%s 로직 실행 완료%n", (mainNumber == 1 ? "" : "2"));

        } catch (Exception e) {
            System.out.println("실행 중 예외 발생: " + e.getMessage());
        } finally {
            // DB 연결 닫기 로직이 필요하다면 여기에 추가할 수 있습니다.
            // if (con != null) { try { con.close(); } catch (SQLException ignore) {} }
        }
    }
}