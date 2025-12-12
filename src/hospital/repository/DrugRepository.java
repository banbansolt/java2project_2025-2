package hospital.repository;

import hospital.domain.DrugVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DrugRepository {

    // 헬퍼 메서드: DB 자원을 안전하게 해제합니다.
    private void closeResources(Connection con, PreparedStatement psmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (psmt != null) psmt.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("DB 자원 해제 중 오류 발생: " + e.getMessage());
            // e.printStackTrace(); // 운영 환경에서는 로그로 남김
        }
    }

    /**
     * 모든 약품 정보를 DB에서 조회합니다.
     * @return DrugVO 객체의 리스트
     * @throws SQLException DB 접근 오류 발생 시
     */
    public List<DrugVO> selectAllDrugs() throws SQLException {
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        List<DrugVO> drugList = new ArrayList<>();

        // DB 스키마에 따라 큰따옴표 사용 및 컬럼명 지정
        String sql = "SELECT \"약품코드\", \"약품명\", \"제조사\", \"단위가격\" FROM \"약품\" ORDER BY \"약품명\" ASC";

        try {
            con = JDBCConnector.getConnection();
            psmt = con.prepareStatement(sql);
            rs = psmt.executeQuery();

            while (rs.next()) {
                DrugVO vo = new DrugVO();

                vo.setDrugCode(rs.getString("약품코드"));

                // 🚨 수정된 부분 반영: setName() -> setDrugName()
                vo.setDrugName(rs.getString("약품명"));

                vo.setManufacturer(rs.getString("제조사"));
                vo.setUnitPrice(rs.getInt("단위가격"));

                drugList.add(vo);
            }
        } finally {
            closeResources(con, psmt, rs);
        }
        return drugList;
    }

    // 약품 삽입, 수정, 삭제 등 다른 Repository 메서드가 여기에 추가될 수 있습니다.

}