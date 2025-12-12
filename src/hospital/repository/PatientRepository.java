package hospital.repository;

import hospital.domain.PatientVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PatientRepository {

    /**
     * 새로운 환자 정보를 DB에 삽입합니다.
     * DB 스키마(정보, 이름, 생년월일, 주소)에 맞춰 데이터를 삽입합니다.
     * @param vo 삽입할 PatientVO 객체
     * @return 삽입된 행의 수
     * @throws SQLException DB 접근 오류 발생 시
     */
    public int insert(PatientVO vo) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int count = 0;

        // DB 컬럼명 사용: "정보", "이름", "생년월일", "주소"
        String sql = "INSERT INTO \"환자\" "
                + "(\"정보\", \"이름\", \"생년월일\", \"주소\") "
                + "VALUES (?, ?, ?, ?)";

        try {
            conn = JDBCConnector.getConnection();
            if (conn == null) throw new SQLException("DB 연결에 실패했습니다.");

            pstmt = conn.prepareStatement(sql);

            // 바인딩 변수 설정
            pstmt.setString(1, vo.getPatientId());
            pstmt.setString(2, vo.getPatientName());

            // Date 객체를 java.sql.Date로 변환
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


    /**
     * 환자 이름으로 검색하거나, 검색어가 없으면 모든 환자 목록을 조회합니다.
     * ORA-00904 해결을 위해 ORDER BY "정보" 컬럼을 사용합니다.
     * @param searchName 검색할 환자 이름 (일부만 입력 가능)
     * @return PatientVO 리스트
     */
    public ArrayList<PatientVO> select(String searchName) {
        ArrayList<PatientVO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // SQL: ORDER BY "정보" 컬럼 사용
        String sql = "SELECT * FROM \"환자\" WHERE \"이름\" LIKE ? ORDER BY \"정보\"";

        try {
            conn = JDBCConnector.getConnection();
            if (conn == null) throw new SQLException("DB 연결에 실패했습니다.");

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + searchName + "%");

            rs = pstmt.executeQuery();

            while (rs.next()) {
                PatientVO vo = new PatientVO();

                // DB 컬럼명에 맞춰 VO 설정
                vo.setPatientId(rs.getString("정보")); // DB PK 컬럼: 정보 (String)
                vo.setPatientName(rs.getString("이름")); // DB 컬럼: 이름

                // 🚨 DB에 없는 주민등록번호 조회 로직은 완전히 제외
                // vo.setResidentId(rs.getString("주민등록번호")); // 이 코드는 제거됨

                vo.setBirthDate(rs.getDate("생년월일")); // DB 컬럼: 생년월일
                vo.setAddress(rs.getString("주소"));     // DB 컬럼: 주소

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