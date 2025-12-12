package hospital.repository;

import hospital.domain.DepartmentVO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentRepository {

    // 1. 모든 부서 정보 조회 (SELECT)
    public List<DepartmentVO> selectAllDepartments() {
        Connection con = JDBCConnector.getConnection();
        List<DepartmentVO> departmentList = new ArrayList<>();
        PreparedStatement psmt = null;
        ResultSet rs = null;

        String sql = "SELECT \"부서ID\", \"부서명\", \"위치\" FROM \"부서\" ORDER BY \"부서ID\"";

        try {
            psmt = con.prepareStatement(sql);
            rs = psmt.executeQuery();

            while (rs.next()) {
                DepartmentVO vo = new DepartmentVO();
                vo.setDeptId(rs.getInt("부서ID"));
                vo.setDeptName(rs.getString("부서명"));
                vo.setLocation(rs.getString("위치"));
                departmentList.add(vo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (psmt != null) psmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return departmentList;
    }

    // TODO: 부서 등록 (INSERT) 등 CRUD 메서드 추가 가능
}