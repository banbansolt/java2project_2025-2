package hospital.repository;

import hospital.domain.PatientVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PatientRepository {

    public int insert(PatientVO vo) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int count = 0;

        String sql = "INSERT INTO \"환자\" "
                + "(\"정보\", \"이름\", \"생년월일\", \"주소\") "
                + "VALUES (?, ?, ?, ?)";

        try {
            conn = JDBCConnector.getConnection();
            if (conn == null) throw new SQLException("DB 연결에 실패했습니다.");

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, vo.getPatientId());
            pstmt.setString(2, vo.getPatientName());

            if (vo.getBirthDate() != null) {
                pstmt.setDate(3, new java.sql.Date(vo.getBirthDate().getTime()));
            } else {
                pstmt.setNull(3, java.sql.Types.DATE);
            }
            pstmt.setString(4, vo.getAddress());

            count = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(conn, pstmt, null);
        }
        return count;
    }



    public ArrayList<PatientVO> select(String searchName) {
        ArrayList<PatientVO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql;

        if (searchName.contains("P")) {
            sql = "SELECT * FROM \"환자\" WHERE \"정보\" LIKE ? ORDER BY \"정보\"";
        } else {

            sql = "SELECT * FROM \"환자\" WHERE \"이름\" LIKE ? ORDER BY \"정보\"";

        }

        try {
            conn = JDBCConnector.getConnection();
            if (conn == null) throw new SQLException("DB 연결에 실패했습니다.");

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + searchName + "%");

            rs = pstmt.executeQuery();

            while (rs.next()) {
                PatientVO vo = new PatientVO();

                vo.setPatientId(rs.getString("정보"));
                vo.setPatientName(rs.getString("이름"));

                vo.setBirthDate(rs.getDate("생년월일"));
                vo.setAddress(rs.getString("주소"));

                list.add(vo);
            }
        } catch (SQLException e) {
            System.err.println("환자 목록 조회 중 DB 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            close(conn, pstmt, rs);
        }
        return list;
    }


    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("ResultSet 닫기 오류: " + e.getMessage());
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("PreparedStatement 닫기 오류: " + e.getMessage());
            }
        }
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Connection 닫기 오류: " + e.getMessage());
            }
        }
    }

    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                System.err.println("롤백 오류: " + e.getMessage());
            }
        }
    }
}