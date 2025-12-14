package hospital.repository;

import hospital.domain.PrescriptionDetailVO;
// 🚨 JDBCConnector는 프로젝트에 정의된 DB 연결 유틸리티 클래스라고 가정합니다.
// import hospital.repository.JDBCConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDetailRepository {

    // --- 1. 핵심 기능 메서드 ---

    // 🚨 [추가] 트랜잭션 참여용 메서드: PrescriptionRepository.issuePrescription에서 호출됨
    /**
     * 외부 트랜잭션 (Connection)을 받아 처방 상세 내역을 하나 삽입합니다.
     * @param conn 외부 트랜잭션에서 받은 Connection
     * @param vo 삽입할 PrescriptionDetailVO 객체
     * @throws SQLException DB 오류 발생 시
     */
    public void insertDetail(Connection conn, PrescriptionDetailVO vo) throws SQLException {
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO \"처방상세\" (\"처방전ID\", \"약품코드\", \"용량\", \"수량\") VALUES (?, ?, ?, ?)";

        try {
            // 외부 Connection을 사용하며, 커밋/롤백은 외부(PrescriptionRepository)에서 관리
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, vo.getPrescriptionId());
            pstmt.setString(2, vo.getDrugCode());
            pstmt.setString(3, vo.getDosage());
            pstmt.setInt(4, vo.getQuantity());

            pstmt.executeUpdate();

        } finally {
            // Connection은 닫지 않고, PreparedStatement만 닫음
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { System.err.println("PreparedStatement 닫기 오류: " + e.getMessage()); }
            }
        }
    }


    /**
     * 특정 처방전 ID에 해당하는 모든 약품 상세 정보(약품명 포함)를 조회합니다.
     */
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


    // --- 2. JDBC 자원 관리 헬퍼 메서드 (PrescriptionRepository와 동일하게 유지) ---

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