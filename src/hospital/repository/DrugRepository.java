package hospital.repository;

import hospital.domain.DrugVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DrugRepository {


    private void closeResources(Connection con, PreparedStatement psmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (psmt != null) psmt.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("DB 자원 해제 중 오류 발생: " + e.getMessage());

        }
    }


    public List<DrugVO> selectAllDrugs() throws SQLException {
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        List<DrugVO> drugList = new ArrayList<>();


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



}