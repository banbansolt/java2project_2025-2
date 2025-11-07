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

public class mvc_main2 {
    public static void main(String[] args) {
        Connection con = JDBCConnector.getConnection();
        deleteCustomerById(con);
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
            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.out.println("Statement or SQL Error: " + e.getMessage());
        }

        CustomerView customerView = new CustomerView();
        customerView.printHead();
        for (Customer customer : customerList) {
            customerView.printCustomer(customer);
            System.out.println();
        }
        customerView.printFooter();
    }


    public static void deleteCustomerById(Connection con) {
        Scanner sc = new Scanner(System.in);



        System.out.println("\n---  고객 정보 삭제 시작 ---");

        while (true) {


            System.out.print("삭제할 고객의 고객아이디(Customer ID)를 입력하세요: ");
            String customerIdToDelete = sc.nextLine();

            String sql = "DELETE FROM 고객 WHERE 고객아이디 = ?";

            try {
                PreparedStatement pstmt = con.prepareStatement(sql);

                // 💡 2. 입력받은 ID를 SQL 파라미터에 바인딩
                pstmt.setString(1, customerIdToDelete);

                int rowsAffected = pstmt.executeUpdate();
                pstmt.close();

                if (rowsAffected > 0) {
                    System.out.println("고객아이디 **" + customerIdToDelete + "**의 정보가 성공적으로 삭제되었습니다.");
                } else {
                    System.out.println("고객아이디 **" + customerIdToDelete + "**에 해당하는 고객이 없습니다. 삭제할 정보가 없습니다.");
                }

            } catch (SQLException e) {
                System.out.println("Statement or SQL Error: " + e.getMessage());
            }

            System.out.print("프로그램 종료를 원하면 e를 입력:");

            String input = sc.nextLine();

            if (input.equalsIgnoreCase("e")) {
                break;
            }
        }
        System.out.println("프로그램이 종료 되었습니다. !!!");
    }
}