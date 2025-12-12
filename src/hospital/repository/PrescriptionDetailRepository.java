package hospital.repository;

import hospital.domain.PrescriptionDetailVO;
import java.sql.*;
import java.util.List;

public class PrescriptionDetailRepository {

    // 1. 처방 상세 등록 (INSERT) - 여러 개의 약품을 리스트로 한 번에 등록
    public void insertBatch(List<PrescriptionDetailVO> detailList) {
        Connection con = JDBCConnector.getConnection();
        // 복합키 (처방전ID, 약품코드) 사용
        String sql = "INSERT INTO \"처방상세\" (\"처방전ID\", \"약품코드\", \"용량\", \"수량\") VALUES (?, ?, ?, ?)";
        PreparedStatement psmt = null;

        try {
            // 배치 처리를 위해 auto-commit 비활성화
            con.setAutoCommit(false);
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
            try {
                con.rollback(); // 오류 발생 시 롤백
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (psmt != null) psmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("처방상세 insert close 문제 발생");
                e.printStackTrace();
            }
        }
    }

    // TODO: 처방상세 조회 (SELECT) 메서드 추가 예정
}