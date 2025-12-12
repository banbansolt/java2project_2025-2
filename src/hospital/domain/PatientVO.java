package hospital.domain;

import java.util.Date;

public class PatientVO {

    // 🚨 residentId 필드 제거
    private String patientId;        // 환자ID (DB '정보' 컬럼과 타입 일치)
    private String patientName;      // 환자 이름 (DB '이름')
    // private String residentId;    // 🚨 제거됨
    private Date birthDate;          // 생년월일 (DB '생년월일')
    private String address;          // 주소 (DB '주소')

    // 기본 생성자
    public PatientVO() {}

    // --- Getter and Setter ---

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    // 🚨 getResidentId/setResidentId 메서드 제거

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // getInfo() 메서드
    public String getInfo() {
        return "[" + this.patientId + "] " + this.patientName;
    }


    @Override
    public String toString() {
        return "PatientVO{" +
                "patientId='" + patientId + '\'' +
                ", patientName='" + patientName + '\'' +
                // "residentId는 DB에 없어 제외합니다." +
                ", birthDate=" + birthDate +
                ", address='" + address + '\'' +
                '}';
    }
}