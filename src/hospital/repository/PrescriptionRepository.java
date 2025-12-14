package hospital.repository;

import hospital.domain.PrescriptionVO;
import hospital.domain.PrescriptionDetailVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionRepository {

    private final PrescriptionDetailRepository prescriptionDetailRepository;

    public PrescriptionRepository(PrescriptionDetailRepository prescriptionDetailRepository) {
        this.prescriptionDetailRepository = prescriptionDetailRepository;
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

    public int issuePrescription(PrescriptionVO pVo, List<PrescriptionDetailVO> dList) throws SQLException {
        int generatedId = 0;
        Connection conn = null;

        try {
            conn = JDBCConnector.getConnection();
            if (conn == null) throw new SQLException("DB 연결에 실패했습니다.");

            conn.setAutoCommit(false);

            generatedId = insertPrescriptionAndGetId(conn, pVo);

            if (generatedId > 0) {
                for (PrescriptionDetailVO detail : dList) {
                    detail.setPrescriptionId(generatedId);
                    prescriptionDetailRepository.insertDetail(conn, detail);
                }
                conn.commit();
            } else {
                rollback(conn);
            }

        } catch (SQLException e) {
            rollback(conn);
            e.printStackTrace();
            throw e;
        } finally {
            close(conn, null, null);
        }
        return generatedId;
    }

    private int insertPrescriptionAndGetId(Connection conn, PrescriptionVO vo) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int generatedId = 0;

        String insertSql = "INSERT INTO \"처방전\" (\"처방전ID\", \"진료ID\", \"약국ID\", \"발행일\", \"이행상태\") "
                + "VALUES (SEQ_처방전_ID.NEXTVAL, ?, ?, ?, '대기')";

        String currentIdSql = "SELECT SEQ_처방전_ID.CURRVAL FROM DUAL";

        try {
            pstmt = conn.prepareStatement(insertSql);
            pstmt.setInt(1, vo.getConsultationId());
            pstmt.setString(2, vo.getPharmacyId());

            java.sql.Timestamp issueDate = new java.sql.Timestamp(vo.getIssueDate().getTime());
            pstmt.setTimestamp(3, issueDate);

            int count = pstmt.executeUpdate();

            if (count > 0) {
                close(null, pstmt, null);
                pstmt = conn.prepareStatement(currentIdSql);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
        } finally {
            close(null, pstmt, rs);
        }
        return generatedId;
    }


    public ArrayList<PrescriptionVO> selectAllPrescriptions() throws SQLException {
        ArrayList<PrescriptionVO> list = new ArrayList<>();

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
                list.add(createPrescriptionVO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(conn, pstmt, rs);
        }
        return list;
    }

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

    public ArrayList<PrescriptionVO> selectPrescriptionsByPatientName(String patientName) throws SQLException {
        ArrayList<PrescriptionVO> list = new ArrayList<>();

        String sql = "SELECT p.*, pt.\"이름\" AS 환자이름 "
                + "FROM \"처방전\" p "
                + "JOIN \"진료\" c ON p.\"진료ID\" = c.\"진료ID\" "
                + "JOIN \"환자\" pt ON c.\"환자정보\" = pt.\"정보\" "
                + "WHERE pt.\"이름\" LIKE ? "
                + "ORDER BY p.\"발행일\" DESC";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCConnector.getConnection();
            if (conn == null) throw new SQLException("DB 연결에 실패했습니다.");

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + patientName + "%");

            rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(createPrescriptionVO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(conn, pstmt, rs);
        }
        return list;
    }


    private PrescriptionVO createPrescriptionVO(ResultSet rs) throws SQLException {
        PrescriptionVO vo = new PrescriptionVO();
        int prescriptionId = rs.getInt("처방전ID");

        vo.setPrescriptionId(prescriptionId);
        vo.setConsultationId(rs.getInt("진료ID"));
        vo.setPharmacyId(rs.getString("약국ID"));
        vo.setIssueDate(rs.getTimestamp("발행일"));
        vo.setFulfillmentStatus(rs.getString("이행상태"));
        vo.setPatientName(rs.getString("환자이름"));

        List<PrescriptionDetailVO> details = prescriptionDetailRepository.selectDetailsByPrescriptionId(prescriptionId);
        vo.setDrugDetails(details);

        return vo;
    }
}