package mvc_jdbc_test.controller;

import jdbc_test.JDBCConnector;
import mvc_jdbc_test.entity.Customer;
import mvc_jdbc_test.entity.Order;
import mvc_jdbc_test.view.CustomerView;
import mvc_jdbc_test.view.OrdersView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainController {
    public static void main(String[] args) {
        Connection con = JDBCConnector.getConnection();
//        customerListAndView(con);
        orderListAndView(con);

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
            con.close();
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

//        CustomerView를 사용해서 customerList에 저장된 Customer Entity의 정보들을 출력해 보세요.
        CustomerView customerView = new CustomerView();
        customerView.printHead();
        for (Customer customer: customerList){
            customerView.printCustomer(customer);
            System.out.println();
        }
        customerView.printFooter();
    }
}