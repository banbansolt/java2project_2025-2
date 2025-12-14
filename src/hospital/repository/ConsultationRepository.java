package hospital.repository;

import hospital.domain.ConsultationVO;
// 🚨 JDBCConnector는 프로젝트에 정의된 DB 연결 유틸리티 클래스라고 가정합니다.
// import util.JDBCConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class ConsultationRepository {

    // 헬퍼 메서드: DB 자원을 안전하게 해제합니다.
    private void closeResources(Connection con, PreparedStatement psmt, ResultSet rs) {
        // ... 기존 로직 유지
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
     * 새로운 진료 기록을 DB에 등록하고, 생성된 진료ID를 반환합니다.
     */
    public int insert(ConsultationVO vo) throws SQLException {
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        int generatedId = 0;

        String insertSql = "INSERT INTO \"진료\" (\"진료ID\", \"환자정보\", \"의사면허번호\", \"진단명\", \"진료일시\") " +
                "VALUES (consultation_seq.NEXTVAL, ?, ?, ?, SYSDATE)";

        String currentIdSql = "SELECT consultation_seq.CURRVAL FROM DUAL";

        try {
            con = JDBCConnector.getConnection();
            if (con == null) throw new SQLException("DB 연결에 실패했습니다.");

            con.setAutoCommit(false);

            psmt = con.prepareStatement(insertSql);
            psmt.setString(1, vo.getPatientInfo());
            psmt.setString(2, vo.getDoctorLicenseNumber());
            psmt.setString(3, vo.getDiagnosisName());

            int count = psmt.executeUpdate();
            closeResources(null, psmt, null);

            if (count > 0) {
                psmt = con.prepareStatement(currentIdSql);
                rs = psmt.executeQuery();

                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
                con.commit();
            } else {
                con.rollback();
            }

        } catch (SQLException e) {
            PrescriptionRepository.rollback(con);
            e.printStackTrace();
            throw e;
        } finally {
            closeResources(con, psmt, rs);
        }
        return generatedId;
    }

    /**
     * 모든 진료 기록 목록을 환자 이름, 의사 이름과 함께 조회합니다.
     */
    public ArrayList<ConsultationVO> selectAllConsultations() throws SQLException {
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        ArrayList<ConsultationVO> consultationList = new ArrayList<>();

        String sql = "SELECT c.\"진료ID\", c.\"환자정보\", p.\"이름\" AS \"환자이름\", " +
                "c.\"의사면허번호\", d.\"이름\" AS \"의사이름\", c.\"진단명\", c.\"진료일시\" " +
                "FROM \"진료\" c " +
                "JOIN \"환자\" p ON c.\"환자정보\" = p.\"정보\" " +
                "JOIN \"의사\" d ON c.\"의사면허번호\" = d.\"면허번호\" " +
                "ORDER BY c.\"진료일시\" DESC, c.\"진료ID\" DESC";

        try {
            con = JDBCConnector.getConnection();
            if (con == null) throw new SQLException("DB 연결에 실패했습니다.");

            psmt = con.prepareStatement(sql);
            rs = psmt.executeQuery();

            while (rs.next()) {
                ConsultationVO vo = new ConsultationVO();

                vo.setConsultationId(rs.getInt("진료ID"));
                vo.setPatientInfo(rs.getString("환자정보"));
                vo.setPatientName(rs.getString("환자이름"));
                vo.setDoctorLicenseNumber(rs.getString("의사면허번호"));
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