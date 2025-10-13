package jdbc_test;

import java.sql.*;

public class JDBCConnector {
    private static final String DRIVER_PATH = "oracle.jdbc.driver.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/xe";
    private static final String USER_NAME = "c##minn";
    private static final String PASSWORD = "1234";

    private static Connection con;

    public static Connection getConnection() {
        try {
            Class.forName(DRIVER_PATH);
            System.out.println("JDBC Driver Loaded");
            con = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            System.out.println("Connection Done Successfully");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found");
        } catch (SQLException e) {
            System.out.println("SQL Exception");
        }

        return con;
    }
    public static void ResultSetTest() {
    try {
        String sql = "select id, name, publish_name, author from book b, publish p where b.publish_id=p.publish_id";
        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            System.out.print (rs.getString("name") + " || ");
            System.out.println(rs.getString("publish_name"));
            System.out.println();
        }
    } catch(SQLException e){
        throw new RuntimeException(e);
    }
}


    public static void main(String[] args) {
        Connection con = getConnection();
        ResultSetTest();
    }
}
