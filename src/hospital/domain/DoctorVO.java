package hospital.domain;

public class DoctorVO {
    private String licenseNumber; // 면허번호 (PK)
    private String name;
    private Long phoneNumber; // 🚨 Long 타입으로 통일 (DB NUMBER 오버플로우 해결)
    private int deptId; // 부서ID (FK)
    private String deptName; // 부서명

    // Getters and Setters

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    // 🚨 setPhoneNumber 메서드 매개변수도 Long으로 통일 (컴파일 오류 해결)
    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

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
}