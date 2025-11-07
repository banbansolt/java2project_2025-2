package ahomework;

import jdbc_test.JDBCConnector;
import mvc_jdbc_test.entity.Customer;
import mvc_jdbc_test.entity.Order;
import mvc_jdbc_test.view.CustomerView;
import mvc_jdbc_test.view.InputCustomerInfoView;
import mvc_jdbc_test.view.OrdersView;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class mvc_main {
    public static void main(String[] args) {
        Connection con = JDBCConnector.getConnection();

        updateCustomer(con);
    }

    public static void orderListAndView(Connection con) {
        ArrayList<Order> orderList = new ArrayList<Order>();
        String sql = "select 주문번호, 고객이름, 고객아이디, 배송지, 수량, 주문일자, 제품명  from 주문, 고객, 제품  where 주문.주문고객=고객.고객아이디 and 주문.주문제품=제품.제품번호";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            Order order = null;
            while (rs.next()) {
                order = new Order();
                order.setOrderNum(rs.getString("주문번호"));
                order.setCustomername(rs.getString("고객이름"));
                order.setCustomerid(rs.getString("고객아이디"));
                order.setShippingAddress(rs.getString("배송지"));
                order.setQuantity(rs.getInt("수량"));
                order.setShippingDate(rs.getDate("주문일자"));
                order.setProductname(rs.getString("제품명"));
                orderList.add(order);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        OrdersView.printHead();
        for (Order order : orderList) {
            OrdersView.printOrders(order);
        }

    }

    public static void customerListAndView(Connection con) {
        ArrayList<Customer> customerList = new ArrayList<Customer>();
        try {
            String sql = "select * from 고객";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            Customer customer = null;

            while (rs.next()) {
                customer = new Customer();
                customer.setCustomerid(rs.getString("고객아이디"));
                customer.setCustomername(rs.getString("고객이름"));
                customer.setAge(rs.getInt("나이"));
                customer.setLevel(rs.getString("등급"));
                customer.setJob(rs.getString("직업"));
                customer.setReward(rs.getInt("적립금"));
                customerList.add(customer);
            }

        } catch (SQLException e) {
            System.out.println("Statement or SQL Error");
        }

        CustomerView customerView = new CustomerView();
        customerView.printHead();
        for (Customer customer: customerList){
            customerView.printCustomer(customer);
            System.out.println();
        }
        customerView.printFooter();
    }


    public static void updateCustomer(Connection con) {
        Scanner sc = new Scanner(System.in);
        InputCustomerInfoView inputCustomer = new InputCustomerInfoView();
        while (true){
            System.out.print("정해진 id 입력후 그 다음은 수정할 단어 작성\n "); // 이 부분은 InputCustomerInfoView의 내부 동작에 따라 달라질 수 있습니다.

            Customer customer = inputCustomer.inputCustomerInfo();
            CustomerView customerView = new CustomerView();
            customerView.printHead();
            customerView.printCustomer(customer);
            customerView.printFooter();

            // 🌟 고객아이디를 조건으로 등급과 적립금을 수정하는 SQL (INSERT -> UPDATE로 수정)
            String sql = "UPDATE 고객 SET 고객이름 = ?, 나이 = ?, 등급 = ?, 직업 = ?, 적립금 = ? WHERE 고객아이디 = ?";

            try {
                PreparedStatement pstmt = con.prepareStatement(sql);
                // 🌟 파라미터 인덱스와 개수도 UPDATE 문에 맞게 수정
                pstmt.setString(1, customer.getCustomername()); // 1. 고객이름 (VARCHAR2)
                pstmt.setInt(2, customer.getAge());             // 2. 나이 (NUMBER)
                pstmt.setString(3, customer.getLevel());        // 3. 등급 (VARCHAR2)
                pstmt.setString(4, customer.getJob());          // 4. 직업 (VARCHAR2)
                pstmt.setInt(5, customer.getReward());          // 적립금
                pstmt.setString(6, customer.getCustomerid());


                int result = pstmt.executeUpdate();

                if (result > 0) {
                    System.out.println("✅ 고객 정보가 성공적으로 수정되었습니다.");
                } else {
                    System.out.println("⚠️ 고객아이디에 해당하는 정보가 없어 수정되지 않았습니다.");
                }

                pstmt.close();
            } catch (SQLException e) {
                System.out.println("Statement or SQL Error: " + e.getMessage());
            }

            System.out.print("프로그램 종료를 원하면 e를 입력:");
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("e")) {
                break;
            }
        }
        System.out.println("\n--- 고객 정보 수정 프로그램이 종료되었습니다. ---");
    }
}

