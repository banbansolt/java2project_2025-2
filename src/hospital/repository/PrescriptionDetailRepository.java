package hospital.repository;

import hospital.domain.PrescriptionDetailVO;
// 🚨 JDBCConnector는 프로젝트 내부에 존재하는 DB 연결 관리 클래스라고 가정합니다.
// import hospital.repository.JDBCConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDetailRepository {

    // --- 1. 핵심 기능 메서드 ---

    /**
     * 새로운 처방 상세 정보를 DB에 배치로 등록합니다.
     * @param detailList 등록할 PrescriptionDetailVO 리스트
     */
    public void insertBatch(List<PrescriptionDetailVO> detailList) {
        Connection con = null;
        // 복합키 (처방전ID, 약품코드) 사용
        String sql = "INSERT INTO \"처방상세\" (\"처방전ID\", \"약품코드\", \"용량\", \"수량\") VALUES (?, ?, ?, ?)";
        PreparedStatement psmt = null;

        try {
            con = JDBCConnector.getConnection();
            con.setAutoCommit(false); // 배치 처리를 위해 auto-commit 비활성화
            psmt = con.prepareStatement(sql);

            for (PrescriptionDetailVO vo : detailList) {
                psmt.setInt(1, vo.getPrescriptionId());
                psmt.setString(2, vo.getDrugCode());
                psmt.setString(3, vo.getDosage());
                psmt.setInt(4, vo.getQuantity());

                psmt.addBatch(); // 배치에 추가
            }

            psmt.executeBatch(); // 배치 실행
            con.commit(); // 커밋

        } catch (SQLException e) {
            rollback(con); // 🚨 헬퍼 메서드 호출
            e.printStackTrace();
        } finally {
            close(con, psmt, null); // 🚨 헬퍼 메서드 호출
        }
    }

    /**
     * 특정 처방전 ID에 해당하는 모든 약품 상세 정보(약품명 포함)를 조회합니다.
     * 이 메서드는 PharmacyFulfillmentView에서 약품 목록을 표시할 때 사용됩니다.
     * * @param prescriptionId 조회할 처방전 ID
     * @return PrescriptionDetailVO 객체의 리스트
     * @throws SQLException DB 접근 오류 발생 시
     */
    public List<PrescriptionDetailVO> selectDetailsByPrescriptionId(int prescriptionId) throws SQLException {
        List<PrescriptionDetailVO> detailList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // SQL: 처방상세(pd)와 약품(d) 테이블을 조인하여 약품명(약품.약품명)을 가져옴
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

                // 처방 상세 정보 설정
                vo.setPrescriptionId(rs.getInt("처방전ID"));
                vo.setDrugCode(rs.getString("약품코드"));
                vo.setDosage(rs.getString("용량"));
                vo.setQuantity(rs.getInt("수량"));

                // 🚨 조인된 약품명 설정
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


    // --- 2. JDBC 자원 관리 헬퍼 메서드 (롤백 오류 해결을 위해 포함) ---

    /** Connection, PreparedStatement, ResultSet을 닫는 정적 메서드 */
    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { System.err.println("ResultSet 닫기 오류: " + e.getMessage()); }
        }
        if (pstmt != null) {
            try { pstmt.close(); } catch (SQLException e) { System.err.println("PreparedStatement 닫기 오류: " + e.getMessage()); }
        }
        if (conn != null) {
            try {
                if (!conn.isClosed()) { conn.close(); }
            } catch (SQLException e) {
                System.err.println("Connection 닫기 오류: " + e.getMessage());
            }
        }
    }

    /** Connection 롤백 정적 메서드 */
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