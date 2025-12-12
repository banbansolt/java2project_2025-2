package hospital.repository;

import hospital.domain.DoctorVO;
import java.sql.*;
import java.util.ArrayList;

public class DoctorRepository {

    // 1. 모든 의사 정보 조회 (SELECT)
    public ArrayList<DoctorVO> selectAllDoctors() {
        Connection con = JDBCConnector.getConnection();
        ArrayList<DoctorVO> doctorList = new ArrayList<>();
        PreparedStatement psmt = null;
        ResultSet rs = null;

        String sql = "SELECT D.\"면허번호\", D.\"이름\", D.\"연락처\", D.\"부서ID\", B.\"부서명\" " +
                "FROM \"의사\" D, \"부서\" B " +
                "WHERE D.\"부서ID\" = B.\"부서ID\" " +
                "ORDER BY D.\"이름\"";
        try {
            psmt = con.prepareStatement(sql);
            rs = psmt.executeQuery();
            while (rs.next()) {
                DoctorVO vo = new DoctorVO();
                vo.setLicenseNumber(rs.getString("면허번호"));
                vo.setName(rs.getString("이름"));

                // 🚨 ORA-17026 오류 해결: rs.getLong() 사용 (DB의 큰 숫자를 Long으로 받음)
                vo.setPhoneNumber(rs.getLong("연락처"));

                vo.setDeptId(rs.getInt("부서ID"));
                vo.setDeptName(rs.getString("부서명"));
                doctorList.add(vo);
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
        return doctorList;
    }
}