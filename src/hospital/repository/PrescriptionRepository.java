package hospital.repository;

import hospital.domain.PrescriptionVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrescriptionRepository {

    /**
     * 새로운 처방전 정보를 DB에 삽입하고, 생성된 처방전 ID를 반환합니다.
     * @param vo 삽입할 PrescriptionVO 객체
     * @return 생성된 처방전 ID (int)
     * @throws SQLException DB 접근 오류 발생 시
     */
    public int insert(PrescriptionVO vo) throws SQLException {
        int generatedId = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // 1. 처방전 삽입 SQL: 처방전ID는 시퀀스 사용 (바인딩 변수 3개)
        // 🚨 DB 컬럼명에 맞춰 큰따옴표 사용 (Oracle 한글 컬럼명은 대소문자 구분을 위해 따옴표 사용)
        String insertSql = "INSERT INTO \"처방전\" "
                + "(\"처방전ID\", \"진료ID\", \"약국ID\", \"발행일\", \"이행상태\") "
                + "VALUES (SEQ_처방전_ID.NEXTVAL, ?, ?, ?, '발행')"; // 초기 상태는 '발행'

        // 2. 생성된 시퀀스 ID를 조회하는 SQL
        String currentIdSql = "SELECT SEQ_처방전_ID.CURRVAL FROM DUAL";

        try {
            conn = JDBCConnector.getConnection();
            if (conn == null) throw new SQLException("DB 연결에 실패했습니다.");

            conn.setAutoCommit(false); // 트랜잭션 시작

            // --- 1단계: INSERT 실행 ---
            pstmt = conn.prepareStatement(insertSql);

            // 바인딩 변수 설정 (총 3개)
            pstmt.setInt(1, vo.getConsultationId());
            pstmt.setString(2, vo.getPharmacyId());

            java.sql.Timestamp issueDate = new java.sql.Timestamp(vo.getIssueDate().getTime());
            pstmt.setTimestamp(3, issueDate);

            int count = pstmt.executeUpdate();

            if (count > 0) {
                // --- 2단계: 생성된 ID 조회 ---
                close(null, pstmt, null); // 이전 PreparedStatement 닫기

                pstmt = conn.prepareStatement(currentIdSql);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }

                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (SQLException e) {
            rollback(conn);
            e.printStackTrace();
            throw e;
        } finally {
            close(conn, pstmt, rs);
        }
        return generatedId;
    }


    /**
     * 모든 처방전 목록을 조회합니다. (환자 이름 포함)
     * 🚨 ORA-00904 해결: 환자.정보와 진료.환자정보 컬럼을 사용하여 조인합니다.
     * @return PrescriptionVO 리스트
     * @throws SQLException DB 접근 오류 발생 시
     */
    public ArrayList<PrescriptionVO> selectAllPrescriptions() throws SQLException {
        ArrayList<PrescriptionVO> list = new ArrayList<>();

        // SQL 수정: 환자(pt)와 진료(c) 테이블을 '정보'/'환자정보' 컬럼으로 조인
        String sql = "SELECT p.*, pt.\"이름\" AS 환자이름, pt.\"정보\" AS 환자정보ID "
                + "FROM \"처방전\" p "
                + "JOIN \"진료\" c ON p.\"진료ID\" = c.\"진료ID\" "
                + "JOIN \"환자\" pt ON c.\"환자정보\" = pt.\"정보\" " // 🚨 ORA-00904 해결 지점
                + "ORDER BY p.\"발행일\" DESC";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCConnector.getConnection();
            if (conn == null) throw new SQLException("DB 연결에 실패했습니다.");

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                PrescriptionVO vo = new PrescriptionVO();
                vo.setPrescriptionId(rs.getInt("처방전ID"));
                vo.setConsultationId(rs.getInt("진료ID"));
                vo.setPharmacyId(rs.getString("약국ID"));
                vo.setIssueDate(rs.getTimestamp("발행일"));
                vo.setFulfillmentStatus(rs.getString("이행상태"));

                // 조인된 환자 이름 설정
                vo.setPatientName(rs.getString("환자이름"));

                list.add(vo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(conn, pstmt, rs);
        }
        return list;
    }

    /**
     * 특정 처방전의 조제 상태를 업데이트합니다.
     * @param vo 업데이트할 PrescriptionVO 객체 (ID와 업데이트된 상태 포함)
     * @return 업데이트된 행의 수
     * @throws SQLException DB 접근 오류 발생 시
     */
    public int updateFulfillmentStatus(PrescriptionVO vo) throws SQLException {
        String sql = "UPDATE \"처방전\" SET \"이행상태\" = ? WHERE \"처방전ID\" = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        int count = 0;

        try {
            conn = JDBCConnector.getConnection();
            if (conn == null) throw new SQLException("DB 연결에 실패했습니다.");

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, vo.getFulfillmentStatus());
            pstmt.setInt(2, vo.getPrescriptionId());

            count = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(conn, pstmt, null);
        }
        return count;
    }

    // --- JDBC 자원 관리 헬퍼 메서드 ---

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