package hospital.domain;

public class DepartmentVO {
    private int deptId;   // 부서ID (부서코드, PK)
    private String deptName; // 부서명
    private String location; // 부서 위치 (예: 외래동 3층)

    // Getters and Setters

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}