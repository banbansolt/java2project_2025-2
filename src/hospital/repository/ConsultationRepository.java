package hospital.repository;

import hospital.domain.ConsultationVO;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class ConsultationRepository {

    // 헬퍼 메서드: DB 자원을 안전하게 해제합니다.
    private void closeResources(Connection con, PreparedStatement psmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (psmt != null) psmt.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("DB 자원 해제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 새로운 진료 기록을 데이터베이스에 등록합니다.
     * @param vo 등록할 ConsultationVO 객체
     * @throws SQLException DB 오류 발생 시
     */
    public void insert(ConsultationVO vo) throws SQLException {
        Connection con = null;
        PreparedStatement psmt = null;

        // 🚨 수정: 테이블 및 컬럼 이름에 큰따옴표를 사용하고, 컬럼 이름을 "의사면허번호"로 수정
        String sql = "INSERT INTO \"진료\" (\"진료ID\", \"환자정보\", \"의사면허번호\", \"진단명\", \"진료일시\") " +
                "VALUES (consultation_seq.NEXTVAL, ?, ?, ?, SYSDATE)";

        try {
            con = JDBCConnector.getConnection();
            psmt = con.prepareStatement(sql);
            psmt.setString(1, vo.getPatientInfo());
            psmt.setString(2, vo.getDoctorLicenseNumber()); // DB 컬럼: "의사면허번호"
            psmt.setString(3, vo.getDiagnosisName());

            psmt.executeUpdate();

        } finally {
            closeResources(con, psmt, null);
        }
    }

    /**
     * 모든 진료 기록 목록을 환자 이름, 의사 이름과 함께 조회합니다.
     * @return ConsultationVO 목록
     * @throws SQLException DB 오류 발생 시 (Controller에서 처리)
     */
    public ArrayList<ConsultationVO> selectAllConsultations() throws SQLException {
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        ArrayList<ConsultationVO> consultationList = new ArrayList<>();

        // 🚨 핵심 수정: 모든 테이블과 컬럼 이름에 큰따옴표를 사용하고, "의사면허"를 "의사면허번호"로 수정
        String sql = "SELECT c.\"진료ID\", c.\"환자정보\", p.\"이름\" AS \"환자이름\", " +
                "c.\"의사면허번호\", d.\"이름\" AS \"의사이름\", c.\"진단명\", c.\"진료일시\" " + // 🚨 "의사면허번호"로 수정
                "FROM \"진료\" c " +
                "JOIN \"환자\" p ON c.\"환자정보\" = p.\"정보\" " +
                "JOIN \"의사\" d ON c.\"의사면허번호\" = d.\"면허번호\" " + // 🚨 "의사면허번호"로 수정
                "ORDER BY c.\"진료일시\" DESC, c.\"진료ID\" DESC";

        try {
            con = JDBCConnector.getConnection();
            psmt = con.prepareStatement(sql);
            rs = psmt.executeQuery();

            while (rs.next()) {
                ConsultationVO vo = new ConsultationVO();

                vo.setConsultationId(rs.getInt("진료ID"));
                vo.setPatientInfo(rs.getString("환자정보"));
                vo.setPatientName(rs.getString("환자이름"));
                vo.setDoctorLicenseNumber(rs.getString("의사면허번호")); // 🚨 "의사면허번호"로 수정
                vo.setDoctorName(rs.getString("의사이름"));
                vo.setDiagnosisName(rs.getString("진단명"));

                Timestamp ts = rs.getTimestamp("진료일시");
                if (ts != null) {
                    vo.setConsultationDateTime(new Date(ts.getTime()));
                } else {
                    vo.setConsultationDateTime(null);
                }

                consultationList.add(vo);
            }
        } finally {
            closeResources(con, psmt, rs);
        }
        return consultationList;
    }
}