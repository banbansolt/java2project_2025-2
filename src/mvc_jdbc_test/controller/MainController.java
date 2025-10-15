package mvc_jdbc_test.controller;

import jdbc_test.JDBCConnector;
import mvc_jdbc_test.entity.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainController {
    public static void main(String[] args) {
        Connection con = JDBCConnector.getConnection();
        try {
            String sql = "select * from 고객";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ArrayList<Customer> customerList = new ArrayList<Customer>();
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

    }
}