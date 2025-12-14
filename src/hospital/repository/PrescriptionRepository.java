package hospital.repository;

import hospital.domain.PrescriptionVO;
import hospital.domain.PrescriptionDetailVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
// 🚨 [가정] JDBCConnector는 프로젝트에 정의된 DB 연결 유틸리티 클래스입니다.
// import util.JDBCConnector;

public class PrescriptionRepository {

    // --- Repositories (데이터 2차 조회를 위해 필요) ---
    private final PrescriptionDetailRepository prescriptionDetailRepository;

    public PrescriptionRepository() {
        // PrescriptionDetailRepository 초기화
        this.prescriptionDetailRepository = new PrescriptionDetailRepository();
    }

    // --- JDBC 자원 관리 헬퍼 메서드 (기존 유지) ---

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
                // conn이 null이 아니고 닫혀있지 않은 경우에만 닫기 시도
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

    // --- 핵심 기능 메서드 ---

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

        // 1. 처방전 삽입 SQL
        String insertSql = "INSERT INTO \"처방전\" "
                + "(\"처방전ID\", \"진료ID\", \"약국ID\", \"발행일\", \"이행상태\") "
                + "VALUES (SEQ_처방전_ID.NEXTVAL, ?, ?, ?, '발행')";

        // 2. 생성된 시퀀스 ID를 조회하는 SQL
        String currentIdSql = "SELECT SEQ_처방전_ID.CURRVAL FROM DUAL";

        try {
            // 🚨 JDBCConnector.getConnection() 호출 부분은 실제 DB 연결 클래스로 대체해야 합니다.
            conn = JDBCConnector.getConnection();
            if (conn == null) throw new SQLException("DB 연결에 실패했습니다.");

            conn.setAutoCommit(false); // 트랜잭션 시작

            // --- 1단계: INSERT 실행 ---
            pstmt = conn.prepareStatement(insertSql);

            // 바인딩 변수 설정 (총 3개)
            pstmt.setInt(1, vo.getConsultationId());
            // 🚨 약국 ID가 String 타입이므로 setString 사용
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
     * 모든 처방전 목록을 조회하고, 각 처방전에 연결된 약품 상세 정보(Drug Details)를 로드합니다.
     * @return PrescriptionVO 리스트
     * @throws SQLException DB 접근 오류 발생 시
     */
    public ArrayList<PrescriptionVO> selectAllPrescriptions() throws SQLException {
        ArrayList<PrescriptionVO> list = new ArrayList<>();

        // SQL: 환자 이름 조인하여 가져오기
        String sql = "SELECT p.*, pt.\"이름\" AS 환자이름 "
                + "FROM \"처방전\" p "
                + "JOIN \"진료\" c ON p.\"진료ID\" = c.\"진료ID\" "
                + "JOIN \"환자\" pt ON c.\"환자정보\" = pt.\"정보\" "
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
                PrescriptionVO vo = createPrescriptionVO(rs);
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

    // 🚨 [신규 추가] 특정 환자 ID로 처방전 목록을 조회하는 메서드
    /**
     * 특정 환자 ID로 처방전 목록을 조회하고 상세 정보를 로드합니다.
     * @param patientId 검색할 환자 ID
     * @return PrescriptionVO 리스트
     * @throws SQLException DB 접근 오류 발생 시
     */
    public ArrayList<PrescriptionVO> selectPrescriptionsByPatientId(String patientId) throws SQLException {
        ArrayList<PrescriptionVO> list = new ArrayList<>();

        // SQL: 환자 테이블을 조인하여 환자 ID를 조건으로 사용
        String sql = "SELECT p.*, pt.\"이름\" AS 환자이름 "
                + "FROM \"처방전\" p "
                + "JOIN \"진료\" c ON p.\"진료ID\" = c.\"진료ID\" "
                + "JOIN \"환자\" pt ON c.\"환자정보\" = pt.\"정보\" "
                + "WHERE pt.\"정보\" = ? " // 🚨 환자 ID 조건 (pt."정보"는 환자 ID 필드로 가정)
                + "ORDER BY p.\"발행일\" DESC";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCConnector.getConnection();
            if (conn == null) throw new SQLException("DB 연결에 실패했습니다.");

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, patientId); // 환자 ID 바인딩

            rs = pstmt.executeQuery();

            while (rs.next()) {
                PrescriptionVO vo = createPrescriptionVO(rs);
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

    // 🚨 [신규 추가] ResultSet에서 VO 객체를 생성하는 유틸리티 메서드
    /**
     * ResultSet에서 데이터를 읽어 PrescriptionVO 객체를 생성하고 약품 상세 정보를 로드합니다.
     * selectAllPrescriptions와 selectPrescriptionsByPatientId 메서드 간의 코드 중복을 제거합니다.
     */
    private PrescriptionVO createPrescriptionVO(ResultSet rs) throws SQLException {
        PrescriptionVO vo = new PrescriptionVO();
        int prescriptionId = rs.getInt("처방전ID");

        vo.setPrescriptionId(prescriptionId);
        vo.setConsultationId(rs.getInt("진료ID"));
        vo.setPharmacyId(rs.getString("약국ID"));
        vo.setIssueDate(rs.getTimestamp("발행일"));
        vo.setFulfillmentStatus(rs.getString("이행상태"));
        vo.setPatientName(rs.getString("환자이름")); // JOIN으로 가져온 환자 이름 설정

        // 2차 조회: 약품 상세 정보 로드
        List<PrescriptionDetailVO> details = prescriptionDetailRepository.selectDetailsByPrescriptionId(prescriptionId);
        vo.setDrugDetails(details);

        return vo;
    }
}