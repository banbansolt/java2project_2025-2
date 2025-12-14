package hospital.repository;

import hospital.domain.PrescriptionDetailVO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDetailRepository {

    public void insertDetail(Connection conn, PrescriptionDetailVO vo) throws SQLException {
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO \"처방상세\" (\"처방전ID\", \"약품코드\", \"용량\", \"수량\") VALUES (?, ?, ?, ?)";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, vo.getPrescriptionId());
            pstmt.setString(2, vo.getDrugCode());
            pstmt.setString(3, vo.getDosage());
            pstmt.setInt(4, vo.getQuantity());

            pstmt.executeUpdate();

        } finally {
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { System.err.println("PreparedStatement 닫기 오류: " + e.getMessage()); }
            }
        }
    }


    public List<PrescriptionDetailVO> selectDetailsByPrescriptionId(int prescriptionId) throws SQLException {
        List<PrescriptionDetailVO> detailList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT pd.*, d.\"약품명\" "
                + "FROM \"처방상세\" pd "
                + "JOIN \"약품\" d ON pd.\"약품코드\" = d.\"약품코드\" "
                + "WHERE pd.\"처방전ID\" = ?";

        try {
            conn = JDBCConnector.getConnection();
            if (conn == null) throw new SQLException("DB 연결에 실패했습니다.");

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, prescriptionId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                PrescriptionDetailVO vo = new PrescriptionDetailVO();

                vo.setPrescriptionId(rs.getInt("처방전ID"));
                vo.setDrugCode(rs.getString("약품코드"));
                vo.setDosage(rs.getString("용량"));
                vo.setQuantity(rs.getInt("수량"));
                vo.setDrugName(rs.getString("약품명"));

                detailList.add(vo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(conn, pstmt, rs);
        }
        return detailList;
    }


    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println("ResultSet 닫기 오류: " + e.getMessage()); } }
        if (pstmt != null) { try { pstmt.close(); } catch (SQLException e) { System.err.println("PreparedStatement 닫기 오류: " + e.getMessage()); } }
        if (conn != null) {
            try { if (!conn.isClosed()) { conn.close(); } } catch (SQLException e) { System.err.println("Connection 닫기 오류: " + e.getMessage()); }
        }
    }

    public static void rollback(Connection conn) {
        if (conn != null) { try { conn.rollback(); } catch (SQLException e) { System.err.println("롤백 오류: " + e.getMessage()); } }
    }
}